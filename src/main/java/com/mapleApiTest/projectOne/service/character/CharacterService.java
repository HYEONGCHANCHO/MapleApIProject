package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.domain.character.*;
import com.mapleApiTest.projectOne.dto.ItemInfo.HatStatInfoDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSetEffectDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSimulationDTO;
import com.mapleApiTest.projectOne.dto.character.request.*;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.item.*;
import com.mapleApiTest.projectOne.repository.character.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CharacterService {

    private final CharactersKeyRepository charactersKeyRepository;
    private final CharactersInfoRepository charactersInfoRepository;
    private final CharactersStatInfoRepository charactersStatInfoRepository;
    private final CharactersItemEquipRepository charactersItemEquipRepository;
    private final CharactersBaseTotalInfoRepository charactersBaseTotalInfoRepository;

    private final WebClient webClient;

    private final RateLimiter rateLimiter = RateLimiter.create(300.0 / 60.0); //분당 300회
    @Value("${external.api.key}")
    private String apiKey;
    @Value("${external.api.url}")
    private String apiUrl;


    public CharacterService(WebClient.Builder builder, CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository, CharactersItemEquipRepository charactersItemEquipRepository, CharactersBaseTotalInfoRepository charactersBaseTotalInfoRepository, @Value("${external.api.key}") String apiKey, @Value("${external.api.url}") String apiUrl) {
        this.webClient = builder.defaultHeader("x-nxopen-api-key", apiKey).baseUrl(apiUrl).build();
        this.charactersKeyRepository = charactersKeyRepository;
        this.charactersInfoRepository = charactersInfoRepository;
        this.charactersStatInfoRepository = charactersStatInfoRepository;
        this.charactersItemEquipRepository = charactersItemEquipRepository;
        this.charactersBaseTotalInfoRepository = charactersBaseTotalInfoRepository;
    }

    @Async("characterThreadPool")
    @Transactional    //캐릭터 고유 ocid 불러오기
    public CompletableFuture<String> getCharacterOcid(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {

            String Url = "/maplestory/v1/id";

            Optional<CharactersKey> charactersKeyOptional = charactersKeyRepository.findByCharactersName(request.getCharactersName());
            if (charactersKeyOptional.isPresent()) {
                CharactersKey charactersKey = charactersKeyOptional.get();
                String ocidValue = charactersKey.getOcid();
                return CompletableFuture.completedFuture(ocidValue);
            } else {
                Mono<String> monoResult = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("character_name", request.getCharactersName()).build()).retrieve().bodyToMono(String.class).flatMap(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        String ocidValue = jsonNode.get("ocid").asText();
                        charactersKeyRepository.save(new CharactersKey(request.getCharactersName(), ocidValue));
                        return Mono.just(ocidValue);
                    } catch (Exception exception) {
                        System.err.println("에러: " + exception.getMessage());
                        return Mono.error(new RuntimeException("캐릭터 정보 추출 실패"));
                    }
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);
                });
                CompletableFuture<String> completableFutureResult = new CompletableFuture<>();
                monoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
                return completableFutureResult;
            }

        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 기본정보 불러오기
    public CompletableFuture<CharactersInfoDTO> getCharactersInfo(GetCharactersInfo request, String apiKey, String ocid) {

        String Url = "/maplestory/v1/character/basic";


        if (rateLimiter.tryAcquire()) {
            Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersName(request.getCharactersName());
            if (charactersInfoOptional.isPresent()) {
                CharactersInfo charactersInfo = charactersInfoOptional.get();
                CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getCharactersName(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersLevel());
                return CompletableFuture.completedFuture(charactersInfoDTO);
            } else {
                Mono<CharactersInfoDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {
                        String world_name = jsonNode.get("world_name").asText();
                        String character_class = jsonNode.get("character_class").asText();
                        int character_level = jsonNode.get("character_level").asInt();
//                        String character_image = jsonNode.get("character_image").asText();
                        CharactersInfo charactersInfo = new CharactersInfo(request.getCharactersName(), character_level, character_class, world_name);
                        charactersInfoRepository.save(charactersInfo);
                        CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getCharactersName(), world_name, character_class, character_level);
                        return Mono.just(charactersInfoDTO);
                    } catch (Exception exception) {

                        System.err.println("에러: " + exception.getMessage());
                        return Mono.error(exception);
                    }
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);
                });
                CompletableFuture<CharactersInfoDTO> completableFutureResult = new CompletableFuture<>();
                MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
                return completableFutureResult;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 스탯 정보 불러오기
    public CompletableFuture<CharactersStatInfoDTO> getCharactersStatInfo(GetCharactersInfo request, String apiKey, String ocid) {

        String Url = "/maplestory/v1/character/stat";


        if (rateLimiter.tryAcquire()) {
            Optional<CharactersStatInfo> charactersStatInfoOptional = charactersStatInfoRepository.findByCharactersName(request.getCharactersName());
            if (charactersStatInfoOptional.isPresent()) {
                CharactersStatInfo charactersStatInfo = charactersStatInfoOptional.get();
                CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getCharactersName(), charactersStatInfo.getDamage(), charactersStatInfo.getBossDamage(), charactersStatInfo.getFinalDamage(), charactersStatInfo.getIgnoreRate(), charactersStatInfo.getCriticalDamage(), charactersStatInfo.getStr(), charactersStatInfo.getDex(), charactersStatInfo.getIntel(), charactersStatInfo.getLuk(), charactersStatInfo.getHp(), charactersStatInfo.getApStr(), charactersStatInfo.getApDex(), charactersStatInfo.getApInt(), charactersStatInfo.getApLuk(), charactersStatInfo.getAttackPower(), charactersStatInfo.getMagicPower(), charactersStatInfo.getCombatPower());

                return CompletableFuture.completedFuture(charactersStatInfoDTO);
            } else {
                Mono<CharactersStatInfoDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {
                        double damage = jsonNode.get("final_stat").get(2).get("stat_value").asDouble();
                        double bossDamage = jsonNode.get("final_stat").get(3).get("stat_value").asDouble();
                        double finalDamage = jsonNode.get("final_stat").get(4).get("stat_value").asDouble();
                        double ignoreRate = jsonNode.get("final_stat").get(5).get("stat_value").asDouble();
                        double criticalDamage = jsonNode.get("final_stat").get(7).get("stat_value").asDouble();
                        int str = jsonNode.get("final_stat").get(16).get("stat_value").asInt();
                        int dex = jsonNode.get("final_stat").get(17).get("stat_value").asInt();
                        int intel = jsonNode.get("final_stat").get(18).get("stat_value").asInt();
                        int luk = jsonNode.get("final_stat").get(19).get("stat_value").asInt();
                        int hp = jsonNode.get("final_stat").get(20).get("stat_value").asInt();
                        int apStr = jsonNode.get("final_stat").get(22).get("stat_value").asInt();
                        int apDex = jsonNode.get("final_stat").get(23).get("stat_value").asInt();
                        int apInt = jsonNode.get("final_stat").get(24).get("stat_value").asInt();
                        int apLuk = jsonNode.get("final_stat").get(25).get("stat_value").asInt();
                        int attackPower = jsonNode.get("final_stat").get(40).get("stat_value").asInt();
                        int magicPower = jsonNode.get("final_stat").get(41).get("stat_value").asInt();
                        int combatPower = jsonNode.get("final_stat").get(42).get("stat_value").asInt();


                        CharactersStatInfo charactersStatInfo = new CharactersStatInfo(request.getCharactersName(), damage, bossDamage, finalDamage, ignoreRate, criticalDamage, str, dex, intel, luk, hp, apStr, apDex, apInt, apLuk, attackPower, magicPower, combatPower);
                        charactersStatInfoRepository.save(charactersStatInfo);
                        CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getCharactersName(), damage, bossDamage, finalDamage, ignoreRate, criticalDamage, str, dex, intel, luk, hp, apStr, apDex, apInt, apLuk, attackPower, magicPower, combatPower);
                        return Mono.just(charactersStatInfoDTO);
                    } catch (Exception exception) {
                        System.err.println("에러: " + exception.getMessage());
                        return Mono.error(exception);
                    }
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);
                });
                CompletableFuture<CharactersStatInfoDTO> completableFutureResult = new CompletableFuture<>();
                MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
                return completableFutureResult;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 착용 장비 전체 저장하기
    public CompletableFuture<CharactersItemEquipDTO> getCharactersItemEquip(GetCharactersInfo request, String apiKey, String ocid) {

        String Url = "/maplestory/v1/character/item-equipment";

        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();

                CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getCharactersName(), charactersItemEquip.getHatInfo(), charactersItemEquip.getTopInfo(), charactersItemEquip.getBottomInfo(), charactersItemEquip.getCapeInfo(), charactersItemEquip.getShoesInfo(), charactersItemEquip.getGlovesInfo(), charactersItemEquip.getShoulderInfo(), charactersItemEquip.getFaceInfo(), charactersItemEquip.getEyeInfo(), charactersItemEquip.getEarInfo(), charactersItemEquip.getPendantOneInfo(), charactersItemEquip.getPendantTwoInfo(), charactersItemEquip.getBeltInfo(), charactersItemEquip.getRingOneInfo(), charactersItemEquip.getRingTwoInfo(), charactersItemEquip.getRingThreeInfo(), charactersItemEquip.getRingFourInfo(), charactersItemEquip.getWeaponInfo(), charactersItemEquip.getSubWeaponInfo(), charactersItemEquip.getEmblemInfo(), charactersItemEquip.getBadgeInfo(), charactersItemEquip.getMedalInfo(), charactersItemEquip.getPoketInfo(), charactersItemEquip.getHeartInfo(), charactersItemEquip.getTitleInfo(), charactersItemEquip.getDragonHat(), charactersItemEquip.getDragonPendant(), charactersItemEquip.getDragonWing(), charactersItemEquip.getDragonTail(), charactersItemEquip.getMechanicEngine(), charactersItemEquip.getMechanicArm(), charactersItemEquip.getMechanicLeg(), charactersItemEquip.getMechanicTran());
                return CompletableFuture.completedFuture(charactersItemEquipDTO);

            } else {
                Mono<CharactersItemEquipDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {

                        String[] equipmentTypes = {"모자", "상의", "하의", "망토", "신발", "장갑", "어깨장식", "얼굴장식", "눈장식", "귀고리", "펜던트", "펜던트2", "벨트", "반지1", "반지2", "반지3", "반지4", "무기", "보조무기", "엠블렘", "뱃지", "훈장", "포켓 아이템", "기계 심장", "드래곤 모자", "드래곤 펜던트", "드래곤 날개장식", "드래곤 꼬리장식", "메카닉 엔진", "메카닉 암", "메카닉 레그", "메카닉 트랜지스터"};

                        String[] equipmentInfo = new String[equipmentTypes.length];

                        JsonNode itemEquipmentNode = jsonNode.get("item_equipment");
                        JsonNode itemEquipmentNodeDragon = jsonNode.get("dragon_equipment");
                        JsonNode itemEquipmentNodeMechanic = jsonNode.get("mechanic_equipment");

                        for (JsonNode equipmentNode : itemEquipmentNode) {
                            // "item_equipment_part" 값 가져오기
                            String equipmentPart = equipmentNode.get("item_equipment_slot").asText();

                            // "equipmentTypes" 배열에서 일치하는 값의 인덱스 찾기
                            int index = Arrays.asList(equipmentTypes).indexOf(equipmentPart);

                            // "equipmentTypes" 배열에 있는 값과 일치하는 경우에만 데이터 할당
                            if (index >= 0) {
                                equipmentInfo[index] = equipmentNode.toString();
                            }
                        }
                        for (JsonNode equipmentNode : itemEquipmentNodeDragon) {
                            // "item_equipment_part" 값 가져오기
                            String equipmentPart = equipmentNode.get("item_equipment_slot").asText();

                            // "equipmentTypes" 배열에서 일치하는 값의 인덱스 찾기
                            int index = Arrays.asList(equipmentTypes).indexOf(equipmentPart);

                            // "equipmentTypes" 배열에 있는 값과 일치하는 경우에만 데이터 할당
                            if (index >= 0) {
                                equipmentInfo[index] = equipmentNode.toString();
                            }
                        }
                        for (JsonNode equipmentNode : itemEquipmentNodeMechanic) {
                            // "item_equipment_part" 값 가져오기
                            String equipmentPart = equipmentNode.get("item_equipment_slot").asText();

                            // "equipmentTypes" 배열에서 일치하는 값의 인덱스 찾기
                            int index = Arrays.asList(equipmentTypes).indexOf(equipmentPart);

                            // "equipmentTypes" 배열에 있는 값과 일치하는 경우에만 데이터 할당
                            if (index >= 0) {
                                equipmentInfo[index] = equipmentNode.toString();
                            }
                        }
                        String hatInfo = equipmentInfo[0];
                        String topInfo = equipmentInfo[1];
                        String bottomInfo = equipmentInfo[2];
                        String capeInfo = equipmentInfo[3];
                        String shoesInfo = equipmentInfo[4];
                        String glovesInfo = equipmentInfo[5];
                        String shoulderInfo = equipmentInfo[6];
                        String faceInfo = equipmentInfo[7];
                        String eyeInfo = equipmentInfo[8];
                        String earInfo = equipmentInfo[9];
                        String pendantOneInfo = equipmentInfo[10];
                        String pendantTwoInfo = equipmentInfo[11];
                        String beltInfo = equipmentInfo[12];
                        String ringOneInfo = equipmentInfo[13];
                        String ringTwoInfo = equipmentInfo[14];
                        String ringThreeInfo = equipmentInfo[15];
                        String ringFourInfo = equipmentInfo[16];
                        String weaponInfo = equipmentInfo[17];
                        String subWeaponInfo = equipmentInfo[18];
                        String emblemInfo = equipmentInfo[19];
                        String badgeInfo = equipmentInfo[20];
                        String medalInfo = equipmentInfo[21];
                        String poketInfo = equipmentInfo[22];
                        String heartInfo = equipmentInfo[23];
                        String titleInfo = jsonNode.get("title").get("title_description").toString();
                        String dragonHat = equipmentInfo[24];
                        String dragonPendant = equipmentInfo[25];
                        String dragonWing = equipmentInfo[26];
                        String dragonTail = equipmentInfo[27];
                        String mechanicEngine = equipmentInfo[28];
                        String mechanicArm = equipmentInfo[29];
                        String mechanicLeg = equipmentInfo[30];
                        String mechanicTran = equipmentInfo[31];


                        CharactersItemEquip charactersItemEquip = new CharactersItemEquip(request.getCharactersName(), hatInfo, topInfo, bottomInfo, capeInfo, shoesInfo, glovesInfo, shoulderInfo, faceInfo, eyeInfo, earInfo, pendantOneInfo, pendantTwoInfo, beltInfo, ringOneInfo, ringTwoInfo, ringThreeInfo, ringFourInfo, weaponInfo, subWeaponInfo, emblemInfo, badgeInfo, medalInfo, poketInfo, heartInfo, titleInfo, dragonHat, dragonPendant, dragonWing, dragonTail, mechanicEngine, mechanicArm, mechanicLeg, mechanicTran);
                        charactersItemEquipRepository.save(charactersItemEquip);
                        CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getCharactersName(), hatInfo, topInfo, bottomInfo, capeInfo, shoesInfo, glovesInfo, shoulderInfo, faceInfo, eyeInfo, earInfo, pendantOneInfo, pendantTwoInfo, beltInfo, ringOneInfo, ringTwoInfo, ringThreeInfo, ringFourInfo, weaponInfo, subWeaponInfo, emblemInfo, badgeInfo, medalInfo, poketInfo, heartInfo, titleInfo, dragonHat, dragonPendant, dragonWing, dragonTail, mechanicEngine, mechanicArm, mechanicLeg, mechanicTran);
                        return Mono.just(charactersItemEquipDTO);
                    } catch (Exception exception) {
                        System.err.println("에러: " + exception.getMessage());
                        return Mono.error(exception);
                    }
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);
                });
                CompletableFuture<CharactersItemEquipDTO> completableFutureResult = new CompletableFuture<>();
                MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
                return completableFutureResult;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 착용 장비 전체 스탯 불러오기
    public CompletableFuture<CharactersItemTotalStatInfoDTO> getCharactersItemTotalInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
            if (charactersItemEquipOptional.isPresent()) {

                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                String InfoTitle = null;
                CharactersItemTotalStatInfoDTO charactersItemTotalStatInfoDTO = new CharactersItemTotalStatInfoDTO();

                List<String> equipmentTypes = List.of("hat", "top", "bottom", "cape", "shoes", "gloves", "shoulder", "face", "eye", "ear", "pendantOne", "pendantTwo", "belt", "ringOne", "ringTwo", "ringThree", "ringFour", "weapon", "subWeapon", "emblem", "badge", "medal", "poket", "heart", "dragonHat", "dragonPendant", "dragonWing", "dragonTail", "mechanicEngine", "mechanicArm", "mechanicLeg", "mechanicTran");

                int titleMainStat = 0;
                int titleSubStat = 0;
                int titleAtMgStat = 0;
                int titleBossDamage = 0;
                int titleDamage = 0;

                ObjectMapper objectMapper = new ObjectMapper();
                InfoTitle = charactersItemEquip.getTitleInfo();
                if (InfoTitle != null) {
                    CharactersItemInfoDTO charactersItemInfoDTO = new CharactersItemInfoDTO();
                    Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersName(request.getCharactersName());
                    CharactersInfo charactersInfo = charactersInfoOptional.get();

                    charactersItemInfoDTO.processTitle(InfoTitle);
                    charactersItemInfoDTO.setBossDamage(charactersItemInfoDTO.getBossDamageTitlePer());
                    charactersItemInfoDTO.setDamage(charactersItemInfoDTO.getDamageTitlePer());
                    charactersItemInfoDTO.setStr(charactersItemInfoDTO.getStrTitleStat());
                    charactersItemInfoDTO.setDex(charactersItemInfoDTO.getDexTitleStat());
                    charactersItemInfoDTO.setIntel(charactersItemInfoDTO.getIntTitleStat());
                    charactersItemInfoDTO.setLuk(charactersItemInfoDTO.getLukTitleStat());
                    charactersItemInfoDTO.setAttactPower(charactersItemInfoDTO.getAtMgTitleStat());
                    charactersItemInfoDTO.setMagicPower(charactersItemInfoDTO.getAtMgTitleStat());
                    charactersItemInfoDTO.setAllStat(charactersItemInfoDTO.getAllStatTitle());
                    charactersItemInfoDTO.setCharactersMainSubStat(charactersInfo.getCharacter_class());

                    titleMainStat = charactersItemInfoDTO.getMainStat();
                    titleSubStat = charactersItemInfoDTO.getSubStat();
                    titleAtMgStat = charactersItemInfoDTO.getAtMgStat();
                    titleBossDamage = charactersItemInfoDTO.getBossDamageTitlePer();
                    titleDamage = charactersItemInfoDTO.getDamageTitlePer();


                }

                int mainStat = titleMainStat;
                int subStat = titleSubStat;
                int mainStatPer = 0;
                int subStatPer = 0;
                int atMgStat = titleAtMgStat;
                int atMgStatPer = 0;
                int bossDamage = titleBossDamage;
                int damage = titleDamage;
                int criticalDamage = 0;
                for (String equipmentType : equipmentTypes) {
                    try {
                        objectMapper = new ObjectMapper();

                        switch (equipmentType) {
                            case "hat":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getHatInfo());
                                break;
                            case "top":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getTopInfo());
                                break;
                            case "bottom":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getBottomInfo());
                                break;
                            case "cape":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getCapeInfo());
                                break;
                            case "shoes":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getShoesInfo());
                                break;
                            case "gloves":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getGlovesInfo());
                                break;
                            case "shoulder":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getShoulderInfo());
                                break;
                            case "face":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getFaceInfo());
                                break;
                            case "eye":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getEyeInfo());
                                break;
                            case "ear":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getEarInfo());
                                break;
                            case "pendantOne":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantOneInfo());
                                break;
                            case "pendantTwo":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantTwoInfo());
                                break;
                            case "belt":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getBeltInfo());
                                break;
                            case "ringOne":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getRingOneInfo());
                                break;
                            case "ringTwo":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getRingTwoInfo());
                                break;
                            case "ringThree":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getRingThreeInfo());
                                break;
                            case "ringFour":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getRingFourInfo());
                                break;
                            case "weapon":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
                                break;
                            case "subWeapon":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getSubWeaponInfo());
                                break;
                            case "emblem":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getEmblemInfo());
                                break;
                            case "badge":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getBadgeInfo());
                                break;
                            case "medal":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getMedalInfo());
                                break;
                            case "poket":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getPoketInfo());
                                break;
                            case "heart":
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getHeartInfo());
                                break;

                            case "dragonHat":
                                if (charactersItemEquip.getDragonHat() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonHat());
                                }
                                break;
                            case "dragonPendant":
                                if (charactersItemEquip.getDragonPendant() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonPendant());
                                }
                                break;
                            case "dragonWing":
                                if (charactersItemEquip.getDragonWing() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonWing());
                                }
                                break;
                            case "dragonTail":
                                if (charactersItemEquip.getDragonTail() != null) {

                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonTail());
                                }
                                break;
                            case "mechanicEngine":
                                if (charactersItemEquip.getMechanicEngine() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicEngine());
                                }
                                break;
                            case "mechanicArm":
                                if (charactersItemEquip.getMechanicArm() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicArm());
                                }
                                break;
                            case "mechanicLeg":
                                if (charactersItemEquip.getMechanicLeg() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicLeg());
                                }
                                break;
                            case "mechanicTran":
                                if (charactersItemEquip.getMechanicTran() != null) {
                                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicTran());
                                }
                                break;

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersName(request.getCharactersName());
                    CharactersInfo charactersInfo = charactersInfoOptional.get();
                    CharactersItemInfoDTO charactersItemInfoDTO = new CharactersItemInfoDTO();


                    charactersItemInfoDTO.setItem_equipment_slot(jsonInfo.get("item_equipment_slot").asText());
                    charactersItemInfoDTO.setItemName(jsonInfo.get("item_name").asText());
                    charactersItemInfoDTO.setItemLevel(jsonInfo.get("item_base_option").get("base_equipment_level").asInt());
                    charactersItemInfoDTO.setStarForce(jsonInfo.get("starforce").asInt());
                    charactersItemInfoDTO.setBossDamage(jsonInfo.get("item_total_option").get("boss_damage").asInt());
                    charactersItemInfoDTO.setDamage(jsonInfo.get("item_total_option").get("damage").asInt());
                    charactersItemInfoDTO.setExcepStr(jsonInfo.get("item_exceptional_option").get("str").asInt());
                    charactersItemInfoDTO.setExcepDex(jsonInfo.get("item_exceptional_option").get("dex").asInt());
                    charactersItemInfoDTO.setExcepInt(jsonInfo.get("item_exceptional_option").get("int").asInt());
                    charactersItemInfoDTO.setExcepLuk(jsonInfo.get("item_exceptional_option").get("luk").asInt());
                    charactersItemInfoDTO.setExcepAtPower(jsonInfo.get("item_exceptional_option").get("attack_power").asInt());
                    charactersItemInfoDTO.setExcepMgPower(jsonInfo.get("item_exceptional_option").get("magic_power").asInt());
                    charactersItemInfoDTO.setStr(jsonInfo.get("item_total_option").get("str").asInt());
                    charactersItemInfoDTO.setDex(jsonInfo.get("item_total_option").get("dex").asInt());
                    charactersItemInfoDTO.setIntel(jsonInfo.get("item_total_option").get("int").asInt());
                    charactersItemInfoDTO.setLuk(jsonInfo.get("item_total_option").get("luk").asInt());
                    charactersItemInfoDTO.setAttactPower(jsonInfo.get("item_total_option").get("attack_power").asInt());
                    charactersItemInfoDTO.setMagicPower(jsonInfo.get("item_total_option").get("magic_power").asInt());
                    charactersItemInfoDTO.setAllStat(jsonInfo.get("item_total_option").get("all_stat").asInt());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_1").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_2").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_3").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_1").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_2").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_3").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processSoul(jsonInfo.get("soul_option").asText());


                    charactersItemInfoDTO.setCharactersMainSubStat(charactersInfo.getCharacter_class());

                    CharactersItemStatInfoDTO charactersItemStatInfoDTO = new CharactersItemStatInfoDTO(charactersItemInfoDTO.getItem_equipment_slot(), charactersItemInfoDTO.getItemName(), charactersItemInfoDTO.getMainStat(), charactersItemInfoDTO.getSubStat(), charactersItemInfoDTO.getMainStatPer(), charactersItemInfoDTO.getSubStatPer(), charactersItemInfoDTO.getAtMgStat(), charactersItemInfoDTO.getPotentialMainStat(), charactersItemInfoDTO.getPotentialSubStat(), charactersItemInfoDTO.getPotentialMainStatPer(), charactersItemInfoDTO.getPotentialSubStatPer(), charactersItemInfoDTO.getPotentialAtMgStat(), charactersItemInfoDTO.getPotentialAtMgPer(), charactersItemInfoDTO.getBossDamage(), charactersItemInfoDTO.getDamage(), charactersItemInfoDTO.getCriticalDamage(), charactersItemInfoDTO.getPotentialBossDamagePer(), charactersItemInfoDTO.getPotentialDamagePer(), charactersItemInfoDTO.getCriticalDamagePotential());

                    System.out.println("Item Equipment Slot: " + charactersItemStatInfoDTO.getItem_equipment_slot());
                    System.out.println("Item Name: " + charactersItemStatInfoDTO.getItemName());
                    System.out.println("Main Stat: " + charactersItemStatInfoDTO.getMainStat());
                    System.out.println("Sub Stat: " + charactersItemStatInfoDTO.getSubStat());
                    System.out.println("Main Stat Percentage: " + charactersItemStatInfoDTO.getMainStatPer());
                    System.out.println("Sub Stat Percentage: " + charactersItemStatInfoDTO.getSubStatPer());
                    System.out.println("Attack/Magic Stat: " + charactersItemStatInfoDTO.getAtMgStat());
                    System.out.println("Potential Main Stat: " + charactersItemStatInfoDTO.getPotentialMainStat());
                    System.out.println("Potential Sub Stat: " + charactersItemStatInfoDTO.getPotentialSubStat());
                    System.out.println("Potential Main Stat Percentage: " + charactersItemStatInfoDTO.getPotentialMainStatPer());
                    System.out.println("Potential Sub Stat Percentage: " + charactersItemStatInfoDTO.getPotentialSubStatPer());
                    System.out.println("Potential Attack/Magic Stat: " + charactersItemStatInfoDTO.getPotentialAtMgStat());
                    System.out.println("Potential Attack/Magic Percentage: " + charactersItemStatInfoDTO.getPotentialAtMgPer());
                    System.out.println("Boss Damage: " + charactersItemStatInfoDTO.getBossDamage());
                    System.out.println("Damage: " + charactersItemStatInfoDTO.getDamage());
                    System.out.println("Critical Damage: " + charactersItemStatInfoDTO.getCriticalDamage());
                    System.out.println("Potential Boss Damage Percentage: " + charactersItemStatInfoDTO.getPotentialBossDamagePer());
                    System.out.println("Potential Damage Percentage: " + charactersItemStatInfoDTO.getPotentialDamagePer());
                    System.out.println("Potential Critical Damage: " + charactersItemStatInfoDTO.getPotentialCriticalDamage());


                    mainStat += charactersItemStatInfoDTO.getMainStat() + charactersItemStatInfoDTO.getPotentialMainStat();
                    subStat += charactersItemStatInfoDTO.getSubStat() + charactersItemStatInfoDTO.getPotentialSubStat();
                    mainStatPer += charactersItemStatInfoDTO.getMainStatPer() + charactersItemStatInfoDTO.getPotentialMainStatPer();
                    subStatPer += charactersItemStatInfoDTO.getSubStatPer() + charactersItemStatInfoDTO.getPotentialSubStatPer();
                    atMgStat += charactersItemStatInfoDTO.getAtMgStat() + charactersItemStatInfoDTO.getPotentialAtMgStat();
                    atMgStatPer += charactersItemStatInfoDTO.getPotentialAtMgPer();
                    bossDamage += charactersItemStatInfoDTO.getBossDamage() + charactersItemStatInfoDTO.getPotentialBossDamagePer();
                    damage += charactersItemStatInfoDTO.getDamage() + charactersItemStatInfoDTO.getPotentialDamagePer();
                    criticalDamage += charactersItemStatInfoDTO.getCriticalDamage() + charactersItemStatInfoDTO.getPotentialCriticalDamage();


                    System.out.println("mainStat : " + mainStat);
                    System.out.println("subStat : " + subStat);
                    System.out.println("mainStatPer : " + mainStatPer);
                    System.out.println("subStatPer : " + subStatPer);
                    System.out.println("atMgStat : " + atMgStat);
                    System.out.println("atMgStatPer : " + atMgStatPer);
                    System.out.println("bossDamage : " + bossDamage);
                    System.out.println("damage : " + damage);
                    System.out.println("criticalDamage : " + criticalDamage);


                    charactersItemTotalStatInfoDTO = new CharactersItemTotalStatInfoDTO(mainStat, subStat, mainStatPer, subStatPer, atMgStat, atMgStatPer, bossDamage, damage, criticalDamage);

                }


                return CompletableFuture.completedFuture(charactersItemTotalStatInfoDTO);


            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }

    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 착용 장비 부위별 스탯 불러오기
    public CompletableFuture<CharactersItemStatInfoDTO> getCharactersItemInfo(GetCharactersInfo request, String equipmentType) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
            if (charactersItemEquipOptional.isPresent()) {

                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                String InfoTitle = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    switch (equipmentType) {
                        case "hat":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getHatInfo());
                            break;
                        case "top":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getTopInfo());
                            break;
                        case "bottom":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getBottomInfo());
                            break;
                        case "cape":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getCapeInfo());
                            break;
                        case "shoes":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getShoesInfo());
                            break;
                        case "gloves":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getGlovesInfo());
                            break;
                        case "shoulder":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getShoulderInfo());
                            break;
                        case "face":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getFaceInfo());
                            break;
                        case "eye":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getEyeInfo());
                            break;
                        case "ear":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getEarInfo());
                            break;
                        case "pendantOne":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantOneInfo());
                            break;
                        case "pendantTwo":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantTwoInfo());
                            break;
                        case "belt":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getBeltInfo());
                            break;
                        case "ringOne":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getRingOneInfo());
                            break;
                        case "ringTwo":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getRingTwoInfo());
                            break;
                        case "ringThree":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getRingThreeInfo());
                            break;
                        case "ringFour":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getRingFourInfo());
                            break;
                        case "weapon":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
                            break;
                        case "subWeapon":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getSubWeaponInfo());
                            break;
                        case "emblem":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getEmblemInfo());
                            break;
                        case "badge":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getBadgeInfo());
                            break;
                        case "medal":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getMedalInfo());
                            break;
                        case "poket":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getPoketInfo());
                            break;
                        case "heart":
                            jsonInfo = objectMapper.readTree(charactersItemEquip.getHeartInfo());
                            break;
                        case "dragonHat":
                            if (charactersItemEquip.getDragonHat() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonHat());
                            }
                            break;
                        case "dragonPendant":
                            if (charactersItemEquip.getDragonPendant() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonPendant());
                            }
                            break;
                        case "dragonWing":
                            if (charactersItemEquip.getDragonWing() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonWing());
                            }
                            break;
                        case "dragonTail":
                            if (charactersItemEquip.getDragonTail() != null) {

                                jsonInfo = objectMapper.readTree(charactersItemEquip.getDragonTail());
                            }
                            break;
                        case "mechanicEngine":
                            if (charactersItemEquip.getMechanicEngine() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicEngine());
                            }
                            break;
                        case "mechanicArm":
                            if (charactersItemEquip.getMechanicArm() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicArm());
                            }
                            break;
                        case "mechanicLeg":
                            if (charactersItemEquip.getMechanicLeg() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicLeg());
                            }
                            break;
                        case "mechanicTran":
                            if (charactersItemEquip.getMechanicTran() != null) {
                                jsonInfo = objectMapper.readTree(charactersItemEquip.getMechanicTran());
                            }
                            break;
                        case "title":
                            if (charactersItemEquip.getTitleInfo() != null) {
                                InfoTitle = charactersItemEquip.getTitleInfo();
                            }
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersName(request.getCharactersName());
                CharactersInfo charactersInfo = charactersInfoOptional.get();
                CharactersItemInfoDTO charactersItemInfoDTO = new CharactersItemInfoDTO();


                if (jsonInfo != null) {
                    charactersItemInfoDTO.setItem_equipment_slot(jsonInfo.get("item_equipment_slot").asText());
                    charactersItemInfoDTO.setItemName(jsonInfo.get("item_name").asText());
                    charactersItemInfoDTO.setItemLevel(jsonInfo.get("item_base_option").get("base_equipment_level").asInt());
                    charactersItemInfoDTO.setStarForce(jsonInfo.get("starforce").asInt());
                    charactersItemInfoDTO.setBossDamage(jsonInfo.get("item_total_option").get("boss_damage").asInt());
                    charactersItemInfoDTO.setDamage(jsonInfo.get("item_total_option").get("damage").asInt());
                    charactersItemInfoDTO.setExcepStr(jsonInfo.get("item_exceptional_option").get("str").asInt());
                    charactersItemInfoDTO.setExcepDex(jsonInfo.get("item_exceptional_option").get("dex").asInt());
                    charactersItemInfoDTO.setExcepInt(jsonInfo.get("item_exceptional_option").get("int").asInt());
                    charactersItemInfoDTO.setExcepLuk(jsonInfo.get("item_exceptional_option").get("luk").asInt());
                    charactersItemInfoDTO.setExcepAtPower(jsonInfo.get("item_exceptional_option").get("attack_power").asInt());
                    charactersItemInfoDTO.setExcepMgPower(jsonInfo.get("item_exceptional_option").get("magic_power").asInt());
                    charactersItemInfoDTO.setStr(jsonInfo.get("item_total_option").get("str").asInt());
                    charactersItemInfoDTO.setDex(jsonInfo.get("item_total_option").get("dex").asInt());
                    charactersItemInfoDTO.setIntel(jsonInfo.get("item_total_option").get("int").asInt());
                    charactersItemInfoDTO.setLuk(jsonInfo.get("item_total_option").get("luk").asInt());
                    charactersItemInfoDTO.setAttactPower(jsonInfo.get("item_total_option").get("attack_power").asInt());
                    charactersItemInfoDTO.setMagicPower(jsonInfo.get("item_total_option").get("magic_power").asInt());
                    charactersItemInfoDTO.setAllStat(jsonInfo.get("item_total_option").get("all_stat").asInt());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_1").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_2").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("potential_option_3").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_1").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_2").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processPotential(jsonInfo.get("additional_potential_option_3").asText(), charactersInfo.getCharactersLevel());
                    charactersItemInfoDTO.processSoul(jsonInfo.get("soul_option").asText());

                    charactersItemInfoDTO.setCharactersMainSubStat(charactersInfo.getCharacter_class());

                }
                if (jsonInfo == null && InfoTitle != null) {
                    charactersItemInfoDTO.processTitle(InfoTitle);
                    charactersItemInfoDTO.setCharactersMainSubStat(charactersInfo.getCharacter_class());
                }


                CharactersItemStatInfoDTO charactersItemStatInfoDTO = new CharactersItemStatInfoDTO(charactersItemInfoDTO.getItem_equipment_slot(), charactersItemInfoDTO.getItemName(), charactersItemInfoDTO.getMainStat(), charactersItemInfoDTO.getSubStat(), charactersItemInfoDTO.getMainStatPer(), charactersItemInfoDTO.getSubStatPer(), charactersItemInfoDTO.getAtMgStat(), charactersItemInfoDTO.getPotentialMainStat(), charactersItemInfoDTO.getPotentialSubStat(), charactersItemInfoDTO.getPotentialMainStatPer(), charactersItemInfoDTO.getPotentialSubStatPer(), charactersItemInfoDTO.getPotentialAtMgStat(), charactersItemInfoDTO.getPotentialAtMgPer(), charactersItemInfoDTO.getBossDamage() + charactersItemInfoDTO.getBossDamageTitlePer(), charactersItemInfoDTO.getDamage(), charactersItemInfoDTO.getCriticalDamage(), charactersItemInfoDTO.getPotentialBossDamagePer(), charactersItemInfoDTO.getPotentialDamagePer(), charactersItemInfoDTO.getCriticalDamagePotential());


                return CompletableFuture.completedFuture(charactersItemStatInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional  //캐릭터 세트효과 적용중인거 불러오기
    public CompletableFuture<CharactersSetEffectInfoDTO> getCharactersSetInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/set-effect";

            Mono<CharactersSetEffectInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int absolSetCount = 0;
                    int arcaneSetCount = 0;
                    int bossAcSetCount = 0;
                    int cvelSetCount = 0; //칠흑
                    int lucidAcSetCount = 0; //여명
                    int lomienSetCount = 0; //루타
                    int eternalSetCount = 0; //에테
                    int mystarSetCount = 0; //에테
                    for (JsonNode setEffectNode : jsonNode.get("set_effect")) {
                        String setName = setEffectNode.get("set_name").asText();
                        char firstLetter = setName.charAt(0);
                        switch (firstLetter) {
                            case '앱':
                                absolSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '아':
                                arcaneSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '보':
                                bossAcSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '칠':
                                cvelSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '여':
                                lucidAcSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '에':
                                eternalSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '루':
                                lomienSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            case '마':
                                mystarSetCount = setEffectNode.get("total_set_count").asInt();
                                break;
                            default:
                                break;
                        }


                    }
                    CharactersSetEffectInfoDTO charactersSetEffectInfoDTO = new CharactersSetEffectInfoDTO(charactersName, ocid, absolSetCount, arcaneSetCount, bossAcSetCount, cvelSetCount, lucidAcSetCount, eternalSetCount, lomienSetCount, mystarSetCount);

                    System.out.println("앱솔세트 :" + absolSetCount);
                    System.out.println("아케인세트 :" + arcaneSetCount);
                    System.out.println("보장세트 :" + bossAcSetCount);
                    System.out.println("칠흙세트 :" + cvelSetCount);
                    System.out.println("여명세트 :" + lucidAcSetCount);
                    System.out.println("에테세트 :" + eternalSetCount);
                    System.out.println("루타세트 :" + lomienSetCount);
                    System.out.println("마이세트 :" + mystarSetCount);

                    return Mono.just(charactersSetEffectInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
//                return Mono.empty(); // 반환값이 없는 경우에는 empty로 처리
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersSetEffectInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional //캐릭터 세트효과 적용중인 스탯 불러오기
    public ItemSetEffectDTO getCharactersSetStatInfo(CharactersSetEffectInfoDTO charactersSetEffectInfoDTO) {

        ItemSetEffectDTO itemSetEffectDTO = new ItemSetEffectDTO();
        itemSetEffectDTO.setItemSetEffect(charactersSetEffectInfoDTO.getAbsolSetCount(), charactersSetEffectInfoDTO.getArcaneSetCount(), charactersSetEffectInfoDTO.getBossAcSetCount(), charactersSetEffectInfoDTO.getCvelSetCount(), charactersSetEffectInfoDTO.getLucidAcSetCount(), charactersSetEffectInfoDTO.getLomienSetCount(), charactersSetEffectInfoDTO.getEternalSetCount(), charactersSetEffectInfoDTO.getMystarSetCount());
        System.out.println("세트 allstat : " + itemSetEffectDTO.getAllStat());
        System.out.println("세트 공격력 : " + itemSetEffectDTO.getAtMgPower());
        System.out.println("세트 보공 : " + itemSetEffectDTO.getDamage()); //보스데미지
        System.out.println("세트 크뎀 : " + itemSetEffectDTO.getCriticalDamage());

        return itemSetEffectDTO;

    }

    @Async("characterThreadPool")
    @Transactional   //유니온 아티팩트
    public CompletableFuture<CharactersArtiInfoDTO> getCharactersArtiInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/user/union-artifact";

            Mono<CharactersArtiInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int artiAllStat = 0;
                    int artiAtMgPower = 0;
                    Double artiDamage = 0.0;
                    Double artiBossDamage = 0.0;
                    Double artiCriticalDamage = 0.0;

                    int artiAllStatOneLevel = 15;
                    int artiAtMgPowerOneLevel = 3;
                    Double artiDamageOneLevel = 1.5;
                    Double artiBossDamageOneLevel = 1.5;
                    Double artiCriticalDamageOneLevel = 0.4;


                    System.out.println("jsonNode.get(\"union_artifact_effect\")" + jsonNode.get("union_artifact_effect"));

                    for (JsonNode artiNode : jsonNode.get("union_artifact_effect")) {
                        String artiName = artiNode.get("name").asText();
                        int artiLevel = artiNode.get("level").asInt();
                        char firstLetter = artiName.charAt(0);
                        switch (firstLetter) {
                            case '올':
                                artiAllStat = artiLevel * artiAllStatOneLevel;
                                break;
                            case '공':
                                artiAtMgPower = artiLevel * artiAtMgPowerOneLevel;
                                break;
                            case '데':
                                artiDamage = artiLevel * artiDamageOneLevel;
                                break;
                            case '보':
                                artiBossDamage = artiLevel * artiBossDamageOneLevel;
                                break;
                            case '크':
                                if (artiName.charAt(5) == '데') {
                                    artiCriticalDamage = artiLevel * artiCriticalDamageOneLevel;
                                }
                                break;
                            default:
                                break;
                        }


                    }
                    CharactersArtiInfoDTO charactersArtiInfoDTO = new CharactersArtiInfoDTO(charactersName, artiAllStat, artiAtMgPower, artiDamage, artiBossDamage, artiCriticalDamage);

                    System.out.println("artiAllStat :" + artiAllStat);
                    System.out.println("artiAtMgPower :" + artiAtMgPower);
                    System.out.println("artiDamage :" + artiDamage);
                    System.out.println("artiBossDamage :" + artiBossDamage);
                    System.out.println("artiCriticalDamage" + artiCriticalDamage);

                    return Mono.just(charactersArtiInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersArtiInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional   //유니온 공격대, 유니온 점령
    public CompletableFuture<CharactersUnionInfoDTO> getCharactersUnionInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/user/union-raider";

            Mono<CharactersUnionInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int UnionRaiderStr = 0;
                    int UnionRaiderDex = 0;
                    int UnionRaiderInt = 0;
                    int UnionRaiderLuk = 0;
                    int UnionRaiderAtMgPower = 0;
                    Double UnionRaiderCriticalDamage = 0.0;
                    Double UnionRaiderBossDamage = 0.0;

                    String unionRaiderStat = jsonNode.get("union_raider_stat").toString();
                    System.out.println("union_raider_stat :" + unionRaiderStat);

                    String[] unionRaiderParts = unionRaiderStat.split("\"");


                    for (String part : unionRaiderParts) {
                        if (part.length() > 2) {
                            // 부분에서 숫자 추출
                            String[] tokens = part.split("\\s+");
                            int value = 0; // 숫자를 저장할 변수 초기화
                            double doubleValue = 0.0;

                            System.out.println("part :" + part);

                            for (String token : tokens) {
                                if (token.matches("\\d+%")) { // 숫자 뒤에 %가 있는 경우
                                    // %를 제거하고 숫자만 추출하여 정수로 변환
                                    value = Integer.parseInt(token.replaceAll("%", ""));
                                    System.out.println("value :" + value);
                                } else if (token.matches("\\d+\\.\\d+%")) { // 소수점이 포함된 숫자인 경우
                                    doubleValue = Double.parseDouble(token.replaceAll("%", ""));
                                    System.out.println("double value: " + doubleValue);
                                } else if (token.matches("\\d+")) { // 그냥 숫자인 경우
                                    value = Integer.parseInt(token);
                                    System.out.println("value :" + value);
                                }
                            }
                            // 추출된 숫자에 따라 적절한 변수에 값을 누적하여 저장
                            if (part.contains("공격력") || part.contains("마력")) {
                                System.out.println("UnionRaiderAtMgPower :" + UnionRaiderAtMgPower);
                                UnionRaiderAtMgPower += value;
                                System.out.println("UnionRaiderAtMgPower :" + UnionRaiderAtMgPower);

                            } else if (part.contains("STR") && part.contains("DEX") && part.contains("LUK")) {

                                System.out.println("UnionRaiderStr :" + UnionRaiderStr);
                                System.out.println("UnionRaiderDex :" + UnionRaiderDex);
                                System.out.println("UnionRaiderLuk :" + UnionRaiderLuk);

                                UnionRaiderStr += value;
                                UnionRaiderDex += value;
                                UnionRaiderLuk += value;

                                System.out.println("UnionRaiderStr :" + UnionRaiderStr);
                                System.out.println("UnionRaiderDex :" + UnionRaiderDex);
                                System.out.println("UnionRaiderLuk :" + UnionRaiderLuk);

                            } else if (part.contains("STR")) {
                                UnionRaiderStr += value;
                            } else if (part.contains("DEX")) {
                                UnionRaiderDex += value;
                            } else if (part.contains("LUK")) {
                                UnionRaiderLuk += value;
                            } else if (part.contains("INT")) {
                                UnionRaiderInt += value;
                            } else if (part.contains("크리티컬 데미지")) {
                                UnionRaiderCriticalDamage += value + doubleValue;
                            } else if (part.contains("보스 몬스터 공격 시 데미지")) {
                                UnionRaiderBossDamage += value + doubleValue;
                            }
                        }
                    }

                    // union_occupied_stat
                    int UnionOccupiedStr = 0;
                    int UnionOccupiedDex = 0;
                    int UnionOccupiedInt = 0;
                    int UnionOccupiedLuk = 0;
                    int UnionOccupiedAtMgPower = 0;
                    Double UnionOccupiedCriticalDamage = 0.0;
                    Double UnionOccupiedBossDamage = 0.0;

                    String union_occupied_stat = jsonNode.get("union_occupied_stat").toString();
                    String[] unionOccupiedParts = union_occupied_stat.split("\"");
                    System.out.println("union_occupied_stat :" + union_occupied_stat);
                    for (String part : unionOccupiedParts) {

                        if (part.length() > 2) {
                            // 부분에서 숫자 추출
                            String[] tokens = part.split("\\s+");
                            int value = 0; // 숫자를 저장할 변수 초기화
                            double doubleValue = 0.0;

                            System.out.println("part :" + part);

                            for (String token : tokens) {
                                if (token.matches("\\d+%")) { // 숫자 뒤에 %가 있는 경우
                                    // %를 제거하고 숫자만 추출하여 정수로 변환
                                    value = Integer.parseInt(token.replaceAll("%", ""));
                                    System.out.println("value :" + value);
                                } else if (token.matches("\\d+\\.\\d+%")) { // 소수점이 포함된 숫자인 경우
                                    doubleValue = Double.parseDouble(token.replaceAll("%", ""));
                                    System.out.println("double value: " + doubleValue);
                                } else if (token.matches("\\d+")) { // 그냥 숫자인 경우
                                    value = Integer.parseInt(token);
                                    System.out.println("value :" + value);
                                }
                            }

                            // 추출된 숫자에 따라 적절한 변수에 값을 누적하여 저장
                            if (part.contains("공격력") || part.contains("마력")) {
                                System.out.println("UnionOccupiedAtMgPower :" + UnionOccupiedAtMgPower);
                                UnionOccupiedAtMgPower += value;
                                System.out.println("UnionOccupiedAtMgPower :" + UnionOccupiedAtMgPower);
                            } else if (part.contains("STR") && part.contains("DEX") && part.contains("LUK")) {
                                UnionOccupiedStr += value;
                                UnionOccupiedDex += value;
                                UnionOccupiedLuk += value;
                            } else if (part.contains("STR")) {
                                UnionOccupiedStr += value;
                            } else if (part.contains("DEX")) {
                                UnionOccupiedDex += value;
                            } else if (part.contains("LUK")) {
                                UnionOccupiedLuk += value;
                            } else if (part.contains("INT")) {
                                UnionOccupiedInt += value;
                            } else if (part.contains("크리티컬 데미지")) {
                                UnionOccupiedCriticalDamage += value + doubleValue;
                            } else if (part.contains("보스 몬스터 공격 시 데미지")) {
                                UnionOccupiedBossDamage += value + doubleValue;
                            }

                        }
                    }


                    CharactersUnionInfoDTO charactersUnionInfoDTO = new CharactersUnionInfoDTO(charactersName, UnionRaiderStr, UnionRaiderDex, UnionRaiderInt, UnionRaiderLuk, UnionRaiderAtMgPower, UnionRaiderCriticalDamage, UnionRaiderBossDamage, UnionOccupiedStr, UnionOccupiedDex, UnionOccupiedInt, UnionOccupiedLuk, UnionOccupiedAtMgPower, UnionOccupiedCriticalDamage, UnionOccupiedBossDamage);

                    System.out.println("UnionRaiderStr :" + UnionRaiderStr);
                    System.out.println("UnionRaiderDex :" + UnionRaiderDex);
                    System.out.println("UnionRaiderInt :" + UnionRaiderInt);
                    System.out.println("UnionRaiderLuk :" + UnionRaiderLuk);
                    System.out.println("UnionRaiderAtMgPower" + UnionRaiderAtMgPower);
                    System.out.println("UnionRaiderCriticalDamage" + UnionRaiderCriticalDamage);
                    System.out.println("UnionRaiderBossDamage" + UnionRaiderBossDamage);
                    System.out.println("UnionOccupiedStr" + UnionOccupiedStr);
                    System.out.println("UnionOccupiedDex" + UnionOccupiedDex);
                    System.out.println("UnionOccupiedInt" + UnionOccupiedInt);
                    System.out.println("UnionOccupiedLuk" + UnionOccupiedLuk);
                    System.out.println("UnionOccupiedAtMgPower" + UnionOccupiedAtMgPower);
                    System.out.println("UnionOccupiedCriticalDamage" + UnionOccupiedCriticalDamage);
                    System.out.println("UnionOccupiedBossDamage" + UnionOccupiedBossDamage);

                    return Mono.just(charactersUnionInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersUnionInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional       //하이퍼스탯
    public CompletableFuture<CharactersHyperStatInfoDTO> getCharactersHyperStatInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/hyper-stat";

            Mono<CharactersHyperStatInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int HyperStatStr = 0;
                    int HyperStatDex = 0;
                    int HyperStatInt = 0;
                    int HyperStatLuk = 0;
                    int HyperStatAtMgPower = 0;
                    Double HyperStatCriticalDamage = 0.0;
                    Double HyperStatDamage = 0.0;
                    Double HyperStatBossDamage = 0.0;

                    JsonNode useHyperStatPreset = null;
                    if (jsonNode.get("use_preset_no").asInt() == 1) {
                        useHyperStatPreset = jsonNode.get("hyper_stat_preset_1");
                        System.out.println("use_preset_no :" + jsonNode.get("use_preset_no"));

                    } else if (jsonNode.get("use_preset_no").asInt() == 2) {
                        useHyperStatPreset = jsonNode.get("hyper_stat_preset_2");
                        System.out.println("use_preset_no :" + jsonNode.get("use_preset_no"));

                    } else if (jsonNode.get("use_preset_no").asInt() == 3) {
                        useHyperStatPreset = jsonNode.get("hyper_stat_preset_3");
                        System.out.println("use_preset_no :" + jsonNode.get("use_preset_no"));

                    }

                    for (JsonNode hyperStatNode : useHyperStatPreset) {
                        String stat_increase = hyperStatNode.get("stat_increase").toString();
                        String[] tokens = stat_increase.split("\\s+");
                        int value = 0; // 숫자를 저장할 변수 초기화
                        double doubleValue = 0.0;

                        System.out.println("stat_increase :" + stat_increase);

                        for (String token : tokens) {
                            if (token.matches("\\d+%")) { // 숫자 뒤에 %가 있는 경우
                                // %를 제거하고 숫자만 추출하여 정수로 변환
                                value = Integer.parseInt(token.replaceAll("%", ""));
                                System.out.println("value :" + value);
                            } else if (token.matches("\\d+\\.\\d+%")) { // 소수점이 포함된 숫자인 경우
                                doubleValue = Double.parseDouble(token.replaceAll("%", ""));
                                System.out.println("double value: " + doubleValue);
                            } else if (token.matches("\\d+")) { // 그냥 숫자인 경우
                                value = Integer.parseInt(token);
                                System.out.println("value :" + value);
                            }
                        }

                        // 추출된 숫자에 따라 적절한 변수에 값을 누적하여 저장
                        if (stat_increase.contains("공격력") || stat_increase.contains("마력")) {
                            System.out.println("HyperStatAtMgPower :" + HyperStatAtMgPower);
                            HyperStatAtMgPower += value;
                            System.out.println("HyperStatAtMgPower :" + HyperStatAtMgPower);
                        } else if (stat_increase.contains("힘")) {
                            HyperStatStr += value;
                        } else if (stat_increase.contains("민첩성")) {
                            HyperStatDex += value;
                        } else if (stat_increase.contains("운")) {
                            HyperStatLuk += value;
                        } else if (stat_increase.contains("지력")) {
                            HyperStatInt += value;
                        } else if (stat_increase.contains("크리티컬 데미지")) {
                            HyperStatCriticalDamage += value + doubleValue;
                        } else if (stat_increase.contains("보스 몬스터 공격 시 데미지")) {
                            HyperStatBossDamage += value + doubleValue;
                        } else if (stat_increase.contains("데미지")) {
                            HyperStatDamage += value + doubleValue;
                        }

                    }

                    System.out.println("HyperStatAtMgPower :" + HyperStatAtMgPower);
                    System.out.println("HyperStatStr :" + HyperStatStr);
                    System.out.println("HyperStatDex :" + HyperStatDex);
                    System.out.println("HyperStatLuk :" + HyperStatLuk);
                    System.out.println("HyperStatInt" + HyperStatInt);
                    System.out.println("HyperStatCriticalDamage" + HyperStatCriticalDamage);
                    System.out.println("HyperStatBossDamage" + HyperStatBossDamage);
                    System.out.println("HyperStatDamage" + HyperStatDamage);
                    System.out.println("HyperStatInt" + HyperStatInt);
                    System.out.println("HyperStatInt" + HyperStatInt);

                    CharactersHyperStatInfoDTO charactersHyperStatInfoDTO = new CharactersHyperStatInfoDTO(charactersName, HyperStatStr, HyperStatDex, HyperStatInt, HyperStatLuk, HyperStatAtMgPower, HyperStatCriticalDamage, HyperStatDamage, HyperStatBossDamage);


                    return Mono.just(charactersHyperStatInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersHyperStatInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }


    @Async("characterThreadPool")
    @Transactional       //어빌리티
    public CompletableFuture<CharactersAbilityInfoDTO> getCharactersAbilityInfo(String charactersName, String ocid, CharactersInfoDTO charactersInfoDTO, CharactersStatInfoDTO charactersStatInfoDTO) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/ability";

            Mono<CharactersAbilityInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int abilityStr = 0;
                    int abilityDex = 0;
                    int abilityInt = 0;
                    int abilityLuk = 0;
                    int abilityStrPer = 0;
                    int abilityDexPer = 0;
                    int abilityIntPer = 0;
                    int abilityLukPer = 0;
                    int abilityAtMgPower = 0;
                    Double abilityBossDamage = 0.0;
                    int charactersLevel = charactersInfoDTO.getCharacter_level();
                    int apStr = charactersStatInfoDTO.getApStr();
                    int apDex = charactersStatInfoDTO.getApDex();
                    int apInt = charactersStatInfoDTO.getApInt();
                    int apLuk = charactersStatInfoDTO.getApLuk();

                    for (JsonNode abilityNode : jsonNode.get("ability_info")) {
                        String stat_increase = abilityNode.get("ability_value").toString();
                        int value = 0; // 숫자를 저장할 변수 초기화
                        int levelAtMgValue = 0;
                        double doubleValue = 0.0;

                        String[] parts = stat_increase.split(",");

                        System.out.println("stat_increase: " + stat_increase);

                        for (String part : parts) {
                            String[] tokens = part.split("\\s+");
                            for (String token : tokens) {
                                if (token.matches("\\d+레벨")) { //
                                    levelAtMgValue = Integer.parseInt(token.replaceAll("레벨", ""));
                                } else if (token.matches("\\d+%")) { // 숫자 뒤에 %가 있는 경우
                                    // %를 제거하고 숫자만 추출하여 정수로 변환
                                    value = Integer.parseInt(token.replaceAll("%", ""));
                                    System.out.println("value :" + value);
                                } else if (token.matches("\\d+\\.\\d+%")) { // 소수점이 포함된 숫자인 경우
                                    doubleValue = Double.parseDouble(token.replaceAll("%", ""));
                                    System.out.println("double value: " + doubleValue);
                                } else if (token.matches("\\d+")) { // 그냥 숫자인 경우
                                    value = Integer.parseInt(token);
                                    System.out.println("value :" + value);
                                }
                            }

                            // 추출된 숫자에 따라 적절한 변수에 값을 누적하여 저장
                            if (stat_increase.contains("레벨마다 공격력") || stat_increase.contains("레벨마다 마력")) {
                                System.out.println("abilityAtMgPower :" + abilityAtMgPower);
                                abilityAtMgPower += charactersLevel / levelAtMgValue;
                                System.out.println("abilityAtMgPower :" + abilityAtMgPower);
                            } else if (stat_increase.contains("공격력") || stat_increase.contains("마력")) {
                                abilityAtMgPower += value;
                            } else if (stat_increase.contains("STR")) {
                                abilityStr += value;
                            } else if (stat_increase.contains("DEX")) {
                                abilityDex += value;
                            } else if (stat_increase.contains("LUK")) {
                                abilityLuk += value;
                            } else if (stat_increase.contains("INT")) {
                                abilityInt += value;
                            } else if (stat_increase.contains("AP를 직접 투자한 STR의")) {
                                abilityDexPer += value;
                            } else if (stat_increase.contains("AP를 직접 투자한 DEX의")) {
                                abilityStrPer += value;
                            } else if (stat_increase.contains("AP를 직접 투자한 INT의")) {
                                abilityLukPer += value;
                            } else if (stat_increase.contains("AP를 직접 투자한 LUK의")) {
                                abilityIntPer += value;
                            } else if (stat_increase.contains("모든 능력치")) {
                                abilityStr += value;
                                abilityDex += value;
                                abilityLuk += value;
                                abilityInt += value;
                            } else if (stat_increase.contains("보스 몬스터 공격 시 데미지")) {
                                abilityBossDamage += value + doubleValue;
                            }
                        }
                    }
                    System.out.println("abilityStr :" + abilityStr);
                    System.out.println("abilityDex :" + abilityDex);
                    System.out.println("abilityLuk :" + abilityLuk);
                    System.out.println("abilityInt" + abilityInt);
                    System.out.println("abilityStrPer" + abilityStrPer);
                    System.out.println("abilityDexPer" + abilityDexPer);
                    System.out.println("abilityIntPer" + abilityIntPer);
                    System.out.println("abilityLukPer" + abilityLukPer);
                    System.out.println("abilityAtMgPower" + abilityAtMgPower);
                    System.out.println("abilityBossDamage" + abilityBossDamage);

                    CharactersAbilityInfoDTO charactersAbilityInfoDTO = new CharactersAbilityInfoDTO(charactersName, abilityStr, abilityDex, abilityInt, abilityLuk, abilityStrPer, abilityDexPer, abilityIntPer, abilityLukPer, abilityAtMgPower, abilityBossDamage);

                    return Mono.just(charactersAbilityInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersAbilityInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }


    @Async("characterThreadPool")
    @Transactional       //심볼
    public CompletableFuture<CharactersSimbolInfoDTO> getCharactersSimbolInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/symbol-equipment";

            Mono<CharactersSimbolInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int simbolStr = 0;
                    int simbolDex = 0;
                    int simbolInt = 0;
                    int simbolLuk = 0;


                    for (JsonNode symbolNode : jsonNode.get("symbol")) {
                        simbolStr += symbolNode.get("symbol_str").asInt();
                        simbolDex += symbolNode.get("symbol_dex").asInt();
                        simbolInt += symbolNode.get("symbol_int").asInt();
                        simbolLuk += symbolNode.get("symbol_luk").asInt();
                    }

                    System.out.println("simbolStr :" + simbolStr);
                    System.out.println("simbolDex :" + simbolDex);
                    System.out.println("simbolInt :" + simbolInt);
                    System.out.println("simbolLuk :" + simbolLuk);

                    CharactersSimbolInfoDTO charactersSimbolInfoDTO = new CharactersSimbolInfoDTO(charactersName, simbolStr, simbolDex, simbolInt, simbolLuk);

                    return Mono.just(charactersSimbolInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersSimbolInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional       //펫장비
    public CompletableFuture<CharactersPetEquipInfoDTO> getCharactersPetEquipInfo(String charactersName, String ocid) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/pet-equipment";

            Mono<CharactersPetEquipInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {
                    int petAt = 0;
                    int petMg = 0;


                    for (JsonNode symbolNode : jsonNode.get("pet_1_equipment").get("item_option")) {

                        if (symbolNode.get("option_type").toString().contains("공격력")) {
                            petAt += symbolNode.get("option_value").asInt();
                        } else if (symbolNode.get("option_type").toString().contains("마력")) {
                            petMg += symbolNode.get("option_value").asInt();
                        }
                    }
                    for (JsonNode symbolNode : jsonNode.get("pet_2_equipment").get("item_option")) {

                        if (symbolNode.get("option_type").toString().contains("공격력")) {
                            petAt += symbolNode.get("option_value").asInt();
                        } else if (symbolNode.get("option_type").toString().contains("마력")) {
                            petMg += symbolNode.get("option_value").asInt();
                        }
                    }
                    for (JsonNode symbolNode : jsonNode.get("pet_3_equipment").get("item_option")) {

                        if (symbolNode.get("option_type").toString().contains("공격력")) {
                            petAt += symbolNode.get("option_value").asInt();
                        } else if (symbolNode.get("option_type").toString().contains("마력")) {
                            petMg += symbolNode.get("option_value").asInt();
                        }
                    }

                    System.out.println("petAt :" + petAt);
                    System.out.println("petMg :" + petMg);

                    CharactersPetEquipInfoDTO charactersPetEquipInfoDTO = new CharactersPetEquipInfoDTO(charactersName, petAt, petMg);

                    return Mono.just(charactersPetEquipInfoDTO);

                } catch (Exception exception) {
                    System.err.println("에러: " + exception.getMessage());
                    return Mono.error(exception);
                }
            }).onErrorResume(exception -> {
                System.err.println("에러: " + exception.getMessage());
                exception.printStackTrace(); // 추가된 부분
                return Mono.error(exception);
            });
            CompletableFuture<CharactersPetEquipInfoDTO> completableFutureResult = new CompletableFuture<>();
            MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
            return completableFutureResult;
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    //헥사스탯
    //5차 스킬
    @Async("characterThreadPool")
    @Transactional       //어빌리티
    public CompletableFuture<CharactersSkillStatInfoDTO> getCharactersSkillStatInfo(String charactersName, String ocid, int nthSkill) {
        if (rateLimiter.tryAcquire()) {
            String Url = "/maplestory/v1/character/skill";
            Mono<CharactersSkillStatInfoDTO> MonoResult
                    = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("character_skill_grade", nthSkill).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                try {

                    int skillStatAllStat = 0;
                    int skillStatAtMgPower = 0;
                    boolean isFree = false;
                    if (nthSkill == 0) {
                        int jungBless = 0;
                        int yujeBless = 0;
                        for (JsonNode nthSkillNode : jsonNode.get("character_skill")) {
                            String skillName = nthSkillNode.get("skill_name").toString();
                            if (skillName.contains("정령의 축복")) {
                                jungBless = nthSkillNode.get("skill_level").asInt();
                            }
                            if (skillName.contains("여제의 축복")) {
                                yujeBless = nthSkillNode.get("skill_level").asInt();
                            }
                            if (jungBless < yujeBless) {
                                skillStatAtMgPower += yujeBless;
                            } else {
                                skillStatAtMgPower += jungBless;
                            }

                            if (skillName.contains("Lv.1")) {
                                skillStatAtMgPower += 8;
                            }

                            if (skillName.contains("Lv.2")) {
                                skillStatAtMgPower += 10;
                            }
                            if (skillName.contains("Lv.3")) {
                                skillStatAtMgPower += 12;
                            }
                            if (skillName.contains("파괴의 얄다바오트")) {
                                isFree=true;
                            }
                        }
                    }

                    if (nthSkill == 5) {
                        for (JsonNode nthSkillNode : jsonNode.get("character_skill")) {
                            String skillName = nthSkillNode.get("skill_name").toString();
                            if (skillName.contains("로프 커넥트")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += skillLevel;
                            } else if (skillName.contains("블링크")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAtMgPower += skillLevel;
                            } else if (skillName.contains("쓸만한 미스틱 도어")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += (skillLevel - 1) % 5 + 1;
                            } else if (skillName.contains("쓸만한 샤프 아이즈")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += (skillLevel - 1) % 5 + 1;
                            } else if (skillName.contains("쓸만한 미스틱 도어")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += (skillLevel - 1) % 5 + 1;
                            } else if (skillName.contains("쓸만한 윈드 부스터")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += (skillLevel - 1) % 5 + 1;
                            } else if (skillName.contains("쓸만한 하이퍼 바디")) {
                                int skillLevel = nthSkillNode.get("skill_level").asInt();
                                skillStatAllStat += (skillLevel - 1) % 5 + 1;
                            }
                        }
                    }

                System.out.println("skillStatAllStat :" + skillStatAllStat);
                System.out.println("skillStatAtMgPower :" + skillStatAtMgPower);
                System.out.println("isFree :" + isFree);


                    CharactersSkillStatInfoDTO charactersSkillStatInfoDTO = new CharactersSkillStatInfoDTO(charactersName,skillStatAllStat,skillStatAtMgPower, isFree);

                return Mono.just(charactersSkillStatInfoDTO);

            } catch(Exception exception){
                System.err.println("에러: " + exception.getMessage());
                return Mono.error(exception);
            }
        }).onErrorResume(exception -> {
            System.err.println("에러: " + exception.getMessage());
            exception.printStackTrace(); // 추가된 부분
            return Mono.error(exception);
        });
        CompletableFuture<CharactersSkillStatInfoDTO> completableFutureResult = new CompletableFuture<>();
        MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
        return completableFutureResult;
    } else

    {
        return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
    }

}
////////////////////////////////////////////////////////////////////////////////////////////////
//아래는 잠시 보류//

    @Async("characterThreadPool")
    @Transactional
    public String getCharactersCombat(CharactersBaseTotalInfoDTO request
    ) {

        int addAllStat = request.getAddAllStat();
        Double addBossDamage = request.getAddBossDamage();
        int addAtMgPower = request.getAddAtMgPower();
        int petAtMgPower = request.getPetAtMgPower();
        int mainStatNonPer = request.getMainStatNonPer();
        int subStatNonPer = request.getSubStatNonPer();
        int mainStatBase = request.getMainStatBase() - request.getMainStatSkill() + addAllStat;
        int mainStatPerBase = request.getMainStatPerBase() - request.getMainStatPerSkill();
        int subStatBase = request.getSubStatBase() - request.getSubStatSkill() + addAllStat;
        int subStatPerBase = request.getSubStatPerBase() - request.getSubStatPerSkill();
        int atMgPowerBase = request.getAtMgPowerBase() - request.getAtMgPowerSkill() + addAtMgPower + petAtMgPower + 30;
        int atMgPowerPerBase = request.getAtMgPowerPerBase() - request.getAtMgPowerPerSkill();
        Double criticalDamageBase = request.getCriticalDamageBase() - request.getCriticalDamageSkill();
        Double DamageBase = request.getDamageBase() - request.getDamageSkill();
        Double BossDamageBase = request.getBossDamageBase() - request.getBossDamageSkill();
        ////전투력 실험용

        mainStatBase = 2813 + 40 + 150 + 10 + 39;
        mainStatNonPer = 25010;
        mainStatPerBase = 370;
        subStatBase = 1898 + 25 + 150 + 10 + 39;
        subStatNonPer = 560;
        subStatPerBase = 144;
        atMgPowerBase = 45 + 1808 + 35 + 21 + 30 + 5 + 129;
        atMgPowerPerBase = 99;
        criticalDamageBase = 16.0 + 25 + 13 + 4;
        DamageBase = 4.0 + 39 + 15;
        BossDamageBase = 3.0 + 158 + 5 + 15;


        /////

        BigDecimal criticalDamageBaseBD = BigDecimal.valueOf(criticalDamageBase);

        criticalDamageBaseBD = criticalDamageBaseBD.setScale(2, RoundingMode.HALF_UP);


        Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
        if (charactersItemEquipOptional.isPresent()) {
            CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
            JsonNode jsonInfo = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
            } catch (Exception e) {
                e.printStackTrace();

            }
            int weaponAddStat = 0;
            int weaponAtMgStat = 0;
            int weaponAtPowerAdd = jsonInfo.get("item_add_option").get("attack_power").asInt();
            int weaponMgPowerAdd = jsonInfo.get("item_add_option").get("magic_power").asInt();
            int weaponStarforce = jsonInfo.get("starforce").asInt();
            int weaponAtPower = jsonInfo.get("item_total_option").get("attack_power").asInt();
            int weaponMgPower = jsonInfo.get("item_total_option").get("magic_power").asInt();
            String weaponName = jsonInfo.get("item_name").asText();
            String weaponSort = jsonInfo.get("item_equipment_part").asText();
            if (weaponAtPowerAdd != 0) {
                weaponAddStat = weaponAtPowerAdd;
            } else if (weaponMgPowerAdd != 0) {
                weaponAddStat = weaponMgPowerAdd;
            }
            if (weaponAtPower != 0) {
                weaponAtMgStat = weaponAtPower;
            } else if (weaponMgPower != 0) {
                weaponAtMgStat = weaponMgPower;
            }

            int papnirBowAtPower[] = {241, 246, 251, 257, 263, 269, 275, 281, 287, 293, 299, 305, 312, 319, 326, 333, 341, 350, 359, 369, 380, 392, 405};
            int absolBowAtPower[] = {273, 279, 285, 291, 297, 303, 310, 317, 324, 331, 338, 345, 352, 360, 368, 376, 385, 394, 404, 415, 427, 440, 454};
            int arcaneBowAtPower[] = {357, 365, 373, 381, 389, 397, 405, 414, 423, 432, 441, 450, 460, 470, 480, 490, 503, 516, 530, 544, 559, 575, 592};
            int papnirBowAddAtPower[] = {66, 52, 39, 29, 20};
            int absolBowAddAtPower[] = {99, 77, 59, 43, 29};
            int arcaneBowAddAtPower[] = {170, 133, 101, 73, 50};
            int jenesisBowAddAtPower[] = {170, 133, 101, 73, 50};
            int weaponAddGrade = 0;
            int jenesisBowAtPower = 318;

            if (weaponName.charAt(0) == '제') {

                if (weaponSort.equals("아대")) {
                    if (weaponAddStat == 106) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 83) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 63) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 46) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 4;
                    }

                } else if (weaponSort.equals("건")) {
                    if (weaponAtPower == 154) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 120) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 91) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 66) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 45) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("너클") || weaponSort.equals("소울슈터") || weaponSort.equals("에너지소드") || weaponSort.equals("건틀렛 리볼버")) {
                    if (weaponAddStat == 157) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 123) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 93) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 68) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 46) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("폴암")) {
                    if (weaponAddStat == 187) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 146) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 111) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 81) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 55) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채") || weaponSort.equals("차크람")) {
                    if (weaponAddStat == 196) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 153) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 116) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 84) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 58) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("한손검") || weaponSort.equals("한손도끼") || weaponSort.equals("한손둔기") || weaponSort.equals("석궁") || weaponSort.equals("케인")) {
                    if (weaponAtPower == 201) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 157) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 119) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 87) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 59) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("두손검") || weaponSort.equals("데스페라도") || weaponSort.equals("튜너") || weaponSort.equals("두손도끼") || weaponSort.equals("두손둔기") || weaponSort.equals("창")) {
                    if (weaponAddStat == 210) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 163) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 124) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 90) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 62) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("핸드캐논")) {
                    if (weaponAddStat == 215) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 167) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 127) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 92) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 63) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("완드") || weaponSort.equals("샤아닝로드") || weaponSort.equals("ESP리미터") || weaponSort.equals("매직 건틀렛")) {
                    if (weaponAddStat == 246) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 192) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 146) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 106) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 72) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("스태프")) {
                    if (weaponAddStat == 250) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 195) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 148) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 108) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 74) {
                        weaponAddGrade = 4;
                    }
                }
                atMgPowerBase = atMgPowerBase - weaponAtMgStat + jenesisBowAtPower + jenesisBowAddAtPower[weaponAddGrade];

            } else if (weaponName.charAt(0) == '아') {

                if (weaponSort.equals("아대")) {
                    if (weaponAddStat == 92) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 72) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 55) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 40) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 27) {
                        weaponAddGrade = 4;
                    }

                } else if (weaponSort.equals("건")) {
                    if (weaponAtPower == 133) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 104) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 79) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 58) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 39) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("너클") || weaponSort.equals("소울슈터") || weaponSort.equals("에너지소드") || weaponSort.equals("건틀렛 리볼버")) {
                    if (weaponAddStat == 136) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 106) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 81) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 59) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 40) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("폴암")) {
                    if (weaponAddStat == 163) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 127) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 96) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 70) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 48) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채") || weaponSort.equals("차크람")) {
                    if (weaponAddStat == 170) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 133) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 101) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 73) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 50) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("한손검") || weaponSort.equals("한손도끼") || weaponSort.equals("한손둔기") || weaponSort.equals("석궁") || weaponSort.equals("케인")) {
                    if (weaponAtPower == 175) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 136) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 103) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 75) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 51) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("두손검") || weaponSort.equals("데스페라도") || weaponSort.equals("튜너") || weaponSort.equals("두손도끼") || weaponSort.equals("두손둔기") || weaponSort.equals("창")) {
                    if (weaponAddStat == 182) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 142) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 108) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 78) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 54) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("핸드캐논")) {
                    if (weaponAddStat == 186) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 142) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 108) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 78) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 54) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("완드") || weaponSort.equals("샤아닝로드") || weaponSort.equals("ESP리미터") || weaponSort.equals("매직 건틀렛")) {
                    if (weaponAddStat == 214) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 167) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 126) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 92) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 63) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("스태프")) {
                    if (weaponAddStat == 218) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 170) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 129) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 94) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 64) {
                        weaponAddGrade = 4;
                    }
                }
                atMgPowerBase = atMgPowerBase - weaponAtPower - weaponMgPower + arcaneBowAtPower[weaponStarforce] + arcaneBowAddAtPower[weaponAddGrade];

            } else if (weaponName.charAt(0) == '파') {
                if (weaponSort.equals("아대")) {
                    if (weaponAddStat == 36) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 28) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 21) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 16) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 11) {
                        weaponAddGrade = 4;
                    }

                } else if (weaponSort.equals("건")) {
                    if (weaponAtPower == 52) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 40) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 22) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 15) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("너클") || weaponSort.equals("소울슈터") || weaponSort.equals("에너지소드") || weaponSort.equals("건틀렛 리볼버")) {
                    if (weaponAddStat == 53) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 41) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 23) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 16) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("폴암")) {
                    if (weaponAddStat == 63) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 49) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 38) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 27) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 19) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채") || weaponSort.equals("차크람")) {
                    if (weaponAddStat == 66) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 52) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 39) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 29) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 20) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("한손검") || weaponSort.equals("한손도끼") || weaponSort.equals("한손둔기") || weaponSort.equals("석궁") || weaponSort.equals("케인")) {
                    if (weaponAtPower == 68) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 53) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 40) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 29) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 20) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("두손검") || weaponSort.equals("데스페라도") || weaponSort.equals("튜너") || weaponSort.equals("두손도끼") || weaponSort.equals("두손둔기") || weaponSort.equals("창")) {
                    if (weaponAddStat == 71) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 55) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 42) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 21) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("핸드캐논")) {
                    if (weaponAddStat == 72) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 56) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 43) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 21) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("완드") || weaponSort.equals("샤아닝로드") || weaponSort.equals("ESP리미터") || weaponSort.equals("매직 건틀렛")) {
                    if (weaponAddStat == 83) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 65) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 49) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 36) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 25) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("스태프")) {
                    if (weaponAddStat == 84) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 66) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 50) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 36) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 25) {
                        weaponAddGrade = 4;
                    }
                }
                atMgPowerBase = atMgPowerBase - weaponAtPower - weaponMgPower + papnirBowAtPower[weaponStarforce] + papnirBowAddAtPower[weaponAddGrade];
            } else if (weaponName.charAt(0) == '앱') {

                if (weaponSort.equals("아대")) {
                    if (weaponAddStat == 53) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 42) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 32) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 23) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 16) {
                        weaponAddGrade = 4;
                    }

                } else if (weaponSort.equals("건")) {
                    if (weaponAtPower == 77) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 60) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 46) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 33) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 23) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("너클") || weaponSort.equals("소울슈터") || weaponSort.equals("에너지소드") || weaponSort.equals("건틀렛 리볼버")) {
                    if (weaponAddStat == 79) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 62) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 47) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 34) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 24) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("폴암")) {
                    if (weaponAddStat == 95) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 74) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 56) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 41) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 28) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채") || weaponSort.equals("차크람")) {
                    if (weaponAddStat == 99) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 77) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 59) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 43) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 29) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("한손검") || weaponSort.equals("한손도끼") || weaponSort.equals("한손둔기") || weaponSort.equals("석궁") || weaponSort.equals("케인")) {
                    if (weaponAtPower == 101) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 79) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 60) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 44) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 30) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("두손검") || weaponSort.equals("데스페라도") || weaponSort.equals("튜너") || weaponSort.equals("두손도끼") || weaponSort.equals("두손둔기") || weaponSort.equals("창")) {
                    if (weaponAddStat == 106) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 82) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 63) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 46) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 31) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("핸드캐논")) {
                    if (weaponAddStat == 108) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 84) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 64) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 47) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 32) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("완드") || weaponSort.equals("샤아닝로드") || weaponSort.equals("ESP리미터") || weaponSort.equals("매직 건틀렛")) {
                    if (weaponAddStat == 124) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 97) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 73) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 54) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 37) {
                        weaponAddGrade = 4;
                    }
                } else if (weaponSort.equals("스태프")) {
                    if (weaponAddStat == 126) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 98) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 75) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat == 54) {
                        weaponAddGrade = 3;
                    } else if (weaponAddStat == 37) {
                        weaponAddGrade = 4;
                    }

                    atMgPowerBase = atMgPowerBase - weaponAtPower - weaponMgPower + absolBowAtPower[weaponStarforce] + absolBowAddAtPower[weaponAddGrade];
                }
            }


            Double finalMainStat = null;
            Double finalSubStat = null;
            Double finalStat = null;
            Double finalAtMgPower = null;
            Double finalCriticalDamage = null;
            Double finalDamage = null;
            Double finalBossDamage = null;
            Double finalCombatE = null;

            if (request.isFree()) {
                finalDamage = 1.1;
            } else {
                finalDamage = 1.0;
            }

            finalMainStat = Math.floor((mainStatBase) * ((100 + mainStatPerBase) / 100.0) + mainStatNonPer);
            finalSubStat = Math.floor((subStatBase) * ((100 + subStatPerBase) / 100.0) + subStatNonPer);
            finalStat = ((finalMainStat * 4) + finalSubStat) / 100.0;
            finalAtMgPower = Math.floor(atMgPowerBase * ((100 + atMgPowerPerBase) / 100.0));
            finalCriticalDamage = (135 + criticalDamageBase) / 100.0;
            finalBossDamage = (100 + DamageBase + BossDamageBase + addBossDamage) / 100.0;

            finalCombatE = Math.floor(finalStat * finalAtMgPower * finalCriticalDamage * finalBossDamage * finalDamage);
            BigDecimal finalCombatBD = new BigDecimal(finalCombatE);
            String finalCombat = finalCombatBD.toPlainString();
            System.out.println(finalCombat + "<=전투력");

            return finalCombat;
        }
        return null;
    }


    @Async("characterThreadPool")
    @Transactional
    public void setCharactersBaseTotalInfo(CharactersBaseTotalInfoDTO charactersBaseTotalInfoDTO) {

        CharactersBaseTotalInfo charactersBaseTotalInfo = new CharactersBaseTotalInfo(charactersBaseTotalInfoDTO.getCharactersName(),
                charactersBaseTotalInfoDTO.getAddAllStat(), charactersBaseTotalInfoDTO.getAddBossDamage(), charactersBaseTotalInfoDTO.getAddAtMgPower(), charactersBaseTotalInfoDTO.getPetAtMgPower(), charactersBaseTotalInfoDTO.getMainStatBase(), charactersBaseTotalInfoDTO.getMainStatSkill(), charactersBaseTotalInfoDTO.getMainStatPerBase(), charactersBaseTotalInfoDTO.getMainStatPerSkill(), charactersBaseTotalInfoDTO.getMainStatNonPer(), charactersBaseTotalInfoDTO.getSubStatBase(), charactersBaseTotalInfoDTO.getSubStatSkill(), charactersBaseTotalInfoDTO.getSubStatPerBase(), charactersBaseTotalInfoDTO.getSubStatPerSkill(), charactersBaseTotalInfoDTO.getSubStatNonPer(), charactersBaseTotalInfoDTO.getAtMgPowerBase(), charactersBaseTotalInfoDTO.getAtMgPowerSkill(), charactersBaseTotalInfoDTO.getAtMgPowerPerBase(), charactersBaseTotalInfoDTO.getAtMgPowerPerSkill(), charactersBaseTotalInfoDTO.getCriticalDamageBase(), charactersBaseTotalInfoDTO.getCriticalDamageSkill(), charactersBaseTotalInfoDTO.getDamageBase(), charactersBaseTotalInfoDTO.getDamageSkill(), charactersBaseTotalInfoDTO.getBossDamageBase(), charactersBaseTotalInfoDTO.getBossDamageSkill(), charactersBaseTotalInfoDTO.isFree());

        charactersBaseTotalInfoRepository.deleteByCharactersName(charactersBaseTotalInfoDTO
                .getCharactersName());
        charactersBaseTotalInfoRepository.save(charactersBaseTotalInfo);

    }

    public CompletableFuture<CharactersBaseTotalInfoDTO> getCharactersBaseTotalInfoDTO(String charactersName) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersBaseTotalInfo> charactersBaseTotalInfoOptional = charactersBaseTotalInfoRepository.findByCharactersName(charactersName);
            if (charactersBaseTotalInfoOptional.isPresent()) {
                CharactersBaseTotalInfo charactersBaseTotalInfo = charactersBaseTotalInfoOptional.get();


                CharactersBaseTotalInfoDTO charactersBaseTotalInfoDTO = new CharactersBaseTotalInfoDTO(charactersBaseTotalInfo.getCharactersName(),
                        charactersBaseTotalInfo.getAddAllStat(), charactersBaseTotalInfo.getAddBossDamage(), charactersBaseTotalInfo.getAddAtMgPower(), charactersBaseTotalInfo.getPetAtMgPower(), charactersBaseTotalInfo.getMainStatBase(), charactersBaseTotalInfo.getMainStatSkill(), charactersBaseTotalInfo.getMainStatPerBase(), charactersBaseTotalInfo.getMainStatPerSkill(), charactersBaseTotalInfo.getMainStatNonPer(), charactersBaseTotalInfo.getSubStatBase(), charactersBaseTotalInfo.getSubStatSkill(), charactersBaseTotalInfo.getSubStatPerBase(), charactersBaseTotalInfo.getSubStatPerSkill(), charactersBaseTotalInfo.getSubStatNonPer(), charactersBaseTotalInfo.getAtMgPowerBase(), charactersBaseTotalInfo.getAtMgPowerSkill(), charactersBaseTotalInfo.getAtMgPowerPerBase(), charactersBaseTotalInfo.getAtMgPowerPerSkill(), charactersBaseTotalInfo.getCriticalDamageBase(), charactersBaseTotalInfo.getCriticalDamageSkill(), charactersBaseTotalInfo.getDamageBase(), charactersBaseTotalInfo.getDamageSkill(), charactersBaseTotalInfo.getBossDamageBase(), charactersBaseTotalInfo.getBossDamageSkill(), charactersBaseTotalInfo.isFree());


                return CompletableFuture.completedFuture(charactersBaseTotalInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }


    @Async("characterThreadPool")
    @Transactional   //잠시 보류
    public CompletableFuture<HatStatInfoDTO> getEquipSimulation(int itemLevel, int starForce, int itemUpgrade,
                                                                int addOptionStat, int potentialNewMainStatPer, int potentialNewSubStatPer, int potentialNewAtMgPowerPer,
                                                                int potentialNewMainStat, int potentialNewSubStat, int potentialNewAtMgPowerStat) {

        ItemSimulationDTO itemSimulationDTO = new ItemSimulationDTO();
        HatStatInfoDTO hatStatInfoDTO = new HatStatInfoDTO(itemLevel);

        itemSimulationDTO.calculateEquipmentStats(hatStatInfoDTO, starForce, itemUpgrade, itemLevel, addOptionStat, potentialNewMainStatPer, potentialNewSubStatPer, potentialNewAtMgPowerPer, potentialNewMainStat, potentialNewSubStat, potentialNewAtMgPowerStat);

        System.out.println(hatStatInfoDTO.getMainStat() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getSubStat());
        System.out.println(hatStatInfoDTO.getAtMgPower());
        System.out.println(hatStatInfoDTO.getAllStatPer() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalMainStatPer() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalSubStatPer() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalAtMgPower() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalMainStat() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalSubStat() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalAtMgPower() + "dadadadadadad");
        System.out.println(hatStatInfoDTO.getPotentialTotalAtMgPowerPer() + "dadadadadadad");


        return CompletableFuture.completedFuture(hatStatInfoDTO);
    }


    @Async("characterThreadPool")
    @Transactional
    public String getCharactersChangeCombat(GetCharactersTotalChangedInfoDTO request, GetCharactersInfo re,
                                            int itemLevel, int starForce, int itemUpgrade
    ) {

        int addAllStat = request.getAddAllStat();
        Double addBossDamage = request.getAddBossDamage();
        int addAtMgPower = request.getAddAtMgPower();
        int petAtMgPower = request.getPetAtMgPower();
        int mainStatNonPer = request.getMainStatNonPer();
        int subStatNonPer = request.getSubStatNonPer();
        int mainStatBase = request.getMainStatBase() - request.getMainStatSkill() + addAllStat;
        int mainStatPerBase = request.getMainStatPerBase() - request.getMainStatPerSkill();
        int subStatBase = request.getSubStatBase() - request.getSubStatSkill() + addAllStat;
        int subStatPerBase = request.getSubStatPerBase() - request.getSubStatPerSkill();
        int atMgPowerBase = request.getAtMgPowerBase() - request.getAtMgPowerSkill() + addAtMgPower + petAtMgPower + 30;
        int atMgPowerPerBase = request.getAtMgPowerPerBase() - request.getAtMgPowerPerSkill();
        Double criticalDamageBase = request.getCriticalDamageBase() - request.getCriticalDamageSkill();
        Double DamageBase = request.getDamageBase() - request.getDamageSkill();
        Double BossDamageBase = request.getBossDamageBase() - request.getBossDamageSkill();
        ////전투력 실험용

        mainStatBase = 2813 + 40 + 150 + 10 + 39;
        mainStatNonPer = 25010;
        mainStatPerBase = 370;
        subStatBase = 1898 + 25 + 150 + 10 + 39;
        subStatNonPer = 560;
        subStatPerBase = 144;
        atMgPowerBase = 45 + 1808 + 35 + 21 + 30 + 5 + 129;
        atMgPowerPerBase = 99;
        criticalDamageBase = 16.0 + 25 + 13 + 4;
        DamageBase = 4.0 + 39 + 15;
        BossDamageBase = 3.0 + 158 + 5 + 15;


        /////


        BigDecimal criticalDamageBaseBD = BigDecimal.valueOf(criticalDamageBase);

        criticalDamageBaseBD = criticalDamageBaseBD.setScale(2, RoundingMode.HALF_UP);


        Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(re.getCharactersName());
        if (charactersItemEquipOptional.isPresent()) {
            CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
            JsonNode jsonInfo = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
            } catch (Exception e) {
                e.printStackTrace();

            }
            int weaponAttactPowerBase = jsonInfo.get("item_base_option").get("attack_power").asInt();
            int weaponAttactPowerAdd = jsonInfo.get("item_add_option").get("attack_power").asInt();
            int weaponAttackPowerStarforce = jsonInfo.get("item_starforce_option").get("attack_power").asInt();
            int weaponStarforce = jsonInfo.get("starforce").asInt();


            int arcaneBowBaseAt = 276;
            int arcaneBowTwoAdd = 133;
            int arcaneBowStarforce18 = 173;

            if (weaponAttactPowerAdd == 106) {
                atMgPowerBase = atMgPowerBase - weaponAttactPowerBase - weaponAttactPowerAdd - weaponAttackPowerStarforce + arcaneBowBaseAt + arcaneBowTwoAdd + arcaneBowStarforce18;
            }
            Double finalMainStat = null;
            Double finalSubStat = null;
            Double finalStat = null;
            Double finalAtMgPower = null;
            Double finalCriticalDamage = null;
            Double finalDamage = null;
            Double finalBossDamage = null;
            Double finalCombatE = null;

            if (request.isFree()) {
                finalDamage = 1.1;
            } else {
                finalDamage = 1.0;
            }

            finalMainStat = Math.floor((mainStatBase) * ((100 + mainStatPerBase) / 100.0) + mainStatNonPer);
            finalSubStat = Math.floor((subStatBase) * ((100 + subStatPerBase) / 100.0) + subStatNonPer);
            finalStat = ((finalMainStat * 4) + finalSubStat) / 100.0;
            finalAtMgPower = Math.floor(atMgPowerBase * ((100 + atMgPowerPerBase) / 100.0));
            finalCriticalDamage = (135 + criticalDamageBase) / 100.0;
            finalBossDamage = (100 + DamageBase + BossDamageBase + addBossDamage) / 100.0;

            finalCombatE = Math.floor(finalStat * finalAtMgPower * finalCriticalDamage * finalBossDamage * finalDamage);
            BigDecimal finalCombatBD = new BigDecimal(finalCombatE);
            String finalCombat = finalCombatBD.toPlainString();


            System.out.println(finalCombat + "변경후");
            return finalCombat;
        }
        return null;
    }


}

