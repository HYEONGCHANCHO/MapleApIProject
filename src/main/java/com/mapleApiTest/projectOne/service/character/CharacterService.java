package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersItemEquip;
import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import com.mapleApiTest.projectOne.domain.character.CharactersStatInfo;
import com.mapleApiTest.projectOne.dto.ItemInfo.HatStatInfoDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSimulationDTO;
import com.mapleApiTest.projectOne.dto.character.request.*;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.item.*;
import com.mapleApiTest.projectOne.repository.character.CharactersInfoRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersItemEquipRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersKeyRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersStatInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CharacterService {

    private final CharactersKeyRepository charactersKeyRepository;
    private final CharactersInfoRepository charactersInfoRepository;
    private final CharactersStatInfoRepository charactersStatInfoRepository;
    private final CharactersItemEquipRepository charactersItemEquipRepository;

    private final WebClient webClient;

    private final RateLimiter rateLimiter = RateLimiter.create(300.0 / 60.0); //분당 300회
    @Value("${external.api.key}")
    private String apiKey;
    @Value("${external.api.url}")
    private String apiUrl;


    public CharacterService(WebClient.Builder builder, CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository, CharactersItemEquipRepository charactersItemEquipRepository, @Value("${external.api.key}") String apiKey, @Value("${external.api.url}") String apiUrl) {
        this.webClient = builder.defaultHeader("x-nxopen-api-key", apiKey).baseUrl(apiUrl).build();
        this.charactersKeyRepository = charactersKeyRepository;
        this.charactersInfoRepository = charactersInfoRepository;
        this.charactersStatInfoRepository = charactersStatInfoRepository;
        this.charactersItemEquipRepository = charactersItemEquipRepository;
    }

    @Async("characterThreadPool")
    @Transactional
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


    /////////////////////////////////////////////////
    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersInfoDTO> getCharactersInfo(GetCharactersInfo request, String Url, String apiKey, String ocid) {

        if (rateLimiter.tryAcquire()) {
            Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersInfoOptional.isPresent()) {
                CharactersInfo charactersInfo = charactersInfoOptional.get();
                CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getDate(), request.getCharactersName(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersLevel());
                return CompletableFuture.completedFuture(charactersInfoDTO);
            } else {
                Mono<CharactersInfoDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {
                        String world_name = jsonNode.get("world_name").asText();
                        String character_class = jsonNode.get("character_class").asText();
                        int character_level = jsonNode.get("character_level").asInt();
//                        String character_image = jsonNode.get("character_image").asText();
                        CharactersInfo charactersInfo = new CharactersInfo(request.getCharactersName(), request.getDate(), character_level, character_class, world_name);
                        charactersInfoRepository.save(charactersInfo);
                        CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getDate(), request.getCharactersName(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersLevel());
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

    /////////////////


    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersStatInfoDTO> getCharactersStatInfo(GetCharactersInfo request, String Url, String apiKey, String ocid) {

        if (rateLimiter.tryAcquire()) {
            Optional<CharactersStatInfo> charactersStatInfoOptional = charactersStatInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersStatInfoOptional.isPresent()) {
                CharactersStatInfo charactersStatInfo = charactersStatInfoOptional.get();
                CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getDate(), request.getCharactersName(), charactersStatInfo.getDamage(), charactersStatInfo.getBossDamage(), charactersStatInfo.getFinalDamage(), charactersStatInfo.getIgnoreRate(), charactersStatInfo.getCriticalDamage(), charactersStatInfo.getStr(), charactersStatInfo.getDex(), charactersStatInfo.getIntel(), charactersStatInfo.getLuk(), charactersStatInfo.getHp(), charactersStatInfo.getAttackPower(), charactersStatInfo.getMagicPower(), charactersStatInfo.getCombatPower());

                return CompletableFuture.completedFuture(charactersStatInfoDTO);
            } else {
                Mono<CharactersStatInfoDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
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
                        int attackPower = jsonNode.get("final_stat").get(40).get("stat_value").asInt();
                        int magicPower = jsonNode.get("final_stat").get(41).get("stat_value").asInt();
                        int combatPower = jsonNode.get("final_stat").get(42).get("stat_value").asInt();


                        CharactersStatInfo charactersStatInfo = new CharactersStatInfo(request.getCharactersName(), request.getDate(), damage, bossDamage, finalDamage, ignoreRate, criticalDamage, str, dex, intel, luk, hp, attackPower, magicPower, combatPower);
                        charactersStatInfoRepository.save(charactersStatInfo);
                        CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getDate(), request.getCharactersName(), damage, bossDamage, finalDamage, ignoreRate, criticalDamage, str, dex, intel, luk, hp, attackPower, magicPower, combatPower);
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


    /////////////////

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersItemEquipDTO> getCharactersItemEquip(GetCharactersInfo request, String Url, String apiKey, String ocid) {

        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();


                CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(), charactersItemEquip.getHatInfo(), charactersItemEquip.getTopInfo(), charactersItemEquip.getBottomInfo(), charactersItemEquip.getCapeInfo(), charactersItemEquip.getShoesInfo(), charactersItemEquip.getGlovesInfo(), charactersItemEquip.getShoulderInfo(), charactersItemEquip.getFaceInfo(), charactersItemEquip.getEyeInfo(), charactersItemEquip.getEarInfo(), charactersItemEquip.getPendantOneInfo(), charactersItemEquip.getPendantTwoInfo(), charactersItemEquip.getBeltInfo(), charactersItemEquip.getRingOneInfo(), charactersItemEquip.getRingTwoInfo(), charactersItemEquip.getRingThreeInfo(), charactersItemEquip.getRingFourInfo(), charactersItemEquip.getWeaponInfo(), charactersItemEquip.getSubWeaponInfo(), charactersItemEquip.getEmblemInfo(), charactersItemEquip.getBadgeInfo(), charactersItemEquip.getMedalInfo(), charactersItemEquip.getPoketInfo(), charactersItemEquip.getHeartInfo());
                return CompletableFuture.completedFuture(charactersItemEquipDTO);

            } else {
                Mono<CharactersItemEquipDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {

                        String[] equipmentTypes = {"모자", "상의", "하의", "망토", "신발", "장갑", "어깨장식", "얼굴장식", "눈장식", "귀고리", "펜던트", "펜던트2", "벨트", "반지1", "반지2", "반지3", "반지4", "무기", "보조무기", "엠블렘", "뱃지", "훈장", "포켓 아이템", "기계 심장"};

                        String[] equipmentInfo = new String[equipmentTypes.length];

                        JsonNode itemEquipmentNode = jsonNode.get("item_equipment");
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

                        CharactersItemEquip charactersItemEquip = new CharactersItemEquip(request.getCharactersName(), request.getDate(), hatInfo, topInfo, bottomInfo, capeInfo, shoesInfo, glovesInfo, shoulderInfo, faceInfo, eyeInfo, earInfo, pendantOneInfo, pendantTwoInfo, beltInfo, ringOneInfo, ringTwoInfo, ringThreeInfo, ringFourInfo, weaponInfo, subWeaponInfo, emblemInfo, badgeInfo, medalInfo, poketInfo, heartInfo);
                        charactersItemEquipRepository.save(charactersItemEquip);
                        CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(), hatInfo, topInfo, bottomInfo, capeInfo, shoesInfo, glovesInfo, shoulderInfo, faceInfo, eyeInfo, earInfo, pendantOneInfo, pendantTwoInfo, beltInfo, ringOneInfo, ringTwoInfo, ringThreeInfo, ringFourInfo, weaponInfo, subWeaponInfo, emblemInfo, badgeInfo, medalInfo, poketInfo, heartInfo);
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


    /////////////////

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersTotalInfoDTO> getCharactersTotalInfo(GetCharactersInfo request) {

        if (rateLimiter.tryAcquire()) {

            Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            Optional<CharactersStatInfo> charactersStatInfoOptional = charactersStatInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());

            if (charactersInfoOptional.isPresent() && charactersStatInfoOptional.isPresent() && charactersItemEquipOptional.isPresent()) {

                CharactersInfo charactersInfo = charactersInfoOptional.get();
                CharactersStatInfo charactersStatInfo = charactersStatInfoOptional.get();
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();

                double damageInfo = charactersStatInfo.getDamage();
                double bossDamageInfo = charactersStatInfo.getBossDamage();
                double finalDamageInfo = charactersStatInfo.getFinalDamage();
                double ignoreRateInfo = charactersStatInfo.getIgnoreRate();
                double criticalDamageInfo = charactersStatInfo.getCriticalDamage();
                int strInfo = charactersStatInfo.getStr();
                int dexInfo = charactersStatInfo.getDex();
                int intelInfo = charactersStatInfo.getIntel();
                int lukInfo = charactersStatInfo.getLuk();
                int hpInfo = charactersStatInfo.getHp();
                int attackPowerInfo = charactersStatInfo.getAttackPower();
                int magicPowerInfo = charactersStatInfo.getMagicPower();
                int combatPowerInfo = charactersStatInfo.getCombatPower();

                int equipStr;


//                equipStr = getCharactersHatInfo(request).get().getStr();


                CharactersTotalInfoDTO charactersTotalInfoDTO = new CharactersTotalInfoDTO(request.getCharactersName(), request.getDate(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersLevel(), damageInfo, bossDamageInfo, finalDamageInfo, ignoreRateInfo, criticalDamageInfo, strInfo, dexInfo, intelInfo, lukInfo, hpInfo, attackPowerInfo, magicPowerInfo, combatPowerInfo);

                return CompletableFuture.completedFuture(charactersTotalInfoDTO);


            } else {
                return null;
            }

        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }


    }


    //////////

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersHatInfoDTO> getCharactersHatInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {

                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getHatInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CharactersHatInfoDTO charactersHatInfoDTO = new CharactersHatInfoDTO(jsonInfo.get("item_equipment_slot").asText(), jsonInfo.get("item_name").asText(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("int").asInt(), jsonInfo.get("item_total_option").get("luk").asInt(), jsonInfo.get("item_total_option").get("max_hp").asInt(), jsonInfo.get("item_total_option").get("attack_power").asInt(), jsonInfo.get("item_total_option").get("magic_power").asInt(), jsonInfo.get("item_total_option").get("boss_damage").asDouble(), jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(), jsonInfo.get("item_total_option").get("all_stat").asInt(), jsonInfo.get("potential_option_1").asText(), jsonInfo.get("potential_option_2").asText(), jsonInfo.get("potential_option_3").asText(), jsonInfo.get("additional_potential_option_1").asText(), jsonInfo.get("additional_potential_option_2").asText(), jsonInfo.get("additional_potential_option_3").asText(), jsonInfo.get("item_exceptional_option"), jsonInfo.get("soul_option").asText()
                );

                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialOne(), request.getCharactersLevel());
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialTwo(), request.getCharactersLevel());
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialThree(), request.getCharactersLevel());
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalOne(), request.getCharactersLevel());
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalTwo(), request.getCharactersLevel());
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalThree(), request.getCharactersLevel());

                int hatStrPotentialPer = charactersHatInfoDTO.getStrPotentialPer();
                int hatDexPotentialPer = charactersHatInfoDTO.getDexPotentialPer();
                int hatIntPotentialPer = charactersHatInfoDTO.getIntPotentialPer();
                int hatLukPotentialPer = charactersHatInfoDTO.getLukPotentialPer();
                int hatAllStatPotentialPer = charactersHatInfoDTO.getAllStatPotentialPer();
                int hatStrPotentialStat = charactersHatInfoDTO.getStrPotentialStat();
                int hatDexPotentialStat = charactersHatInfoDTO.getDexPotentialStat();
                int hatIntPotentialStat = charactersHatInfoDTO.getIntPotentialStat();
                int hatLukPotentialStat = charactersHatInfoDTO.getLukPotentialStat();
                int hatAtMgPotentialPer = charactersHatInfoDTO.getAtMgPotentialPer();
                int hatAtMgPotentialStat = charactersHatInfoDTO.getAtMgPotentialStat();

                System.out.println("hatStrPotentialPer" + hatStrPotentialPer);
                System.out.println("hatDexPotentialPer" + hatDexPotentialPer);
                System.out.println("hatAllStatPotentialPer" + hatAllStatPotentialPer);
                System.out.println("hatAtMgPotentialStat" + hatAtMgPotentialStat);
                System.out.println("hatStrPotentialStat" + hatStrPotentialStat);
                System.out.println("hatDexPotentialStat" + hatDexPotentialStat);


                return CompletableFuture.completedFuture(charactersHatInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersTopInfoDTO> getCharactersTopInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {

                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getTopInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CharactersTopInfoDTO charactersTopInfoDTO = new CharactersTopInfoDTO(jsonInfo.get("item_equipment_slot").asText(), jsonInfo.get("item_name").asText(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("int").asInt(), jsonInfo.get("item_total_option").get("luk").asInt(), jsonInfo.get("item_total_option").get("max_hp").asInt(), jsonInfo.get("item_total_option").get("attack_power").asInt(), jsonInfo.get("item_total_option").get("magic_power").asInt(), jsonInfo.get("item_total_option").get("boss_damage").asDouble(), jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(), jsonInfo.get("item_total_option").get("all_stat").asInt(), jsonInfo.get("potential_option_1").asText(), jsonInfo.get("potential_option_2").asText(), jsonInfo.get("potential_option_3").asText(), jsonInfo.get("additional_potential_option_1").asText(), jsonInfo.get("additional_potential_option_2").asText(), jsonInfo.get("additional_potential_option_3").asText(), jsonInfo.get("item_exceptional_option"), jsonInfo.get("soul_option").asText()
                );
                return CompletableFuture.completedFuture(charactersTopInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersBottomInfoDTO> getCharactersBottomInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBottomInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersBottomInfoDTO charactersBottomInfoDTO = new CharactersBottomInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersBottomInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersCapeInfoDTO> getCharactersCapeInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getCapeInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersCapeInfoDTO charactersCapeInfoDTO = new CharactersCapeInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersCapeInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersShoesInfoDTO> getCharactersShoesInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getShoesInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersShoesInfoDTO charactersShoesInfoDTO = new CharactersShoesInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersShoesInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersGlovesInfoDTO> getCharactersGlovesInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getGlovesInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersGlovesInfoDTO charactersGlovesInfoDTO = new CharactersGlovesInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersGlovesInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersShoulderInfoDTO> getCharactersShoulderInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getShoulderInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersShoulderInfoDTO charactersShoulderInfoDTO = new CharactersShoulderInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersShoulderInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersFaceInfoDTO> getCharactersFaceInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getFaceInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersFaceInfoDTO charactersFaceInfoDTO = new CharactersFaceInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersFaceInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersEyeInfoDTO> getCharactersEyeInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEyeInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersEyeInfoDTO charactersEyeInfoDTO = new CharactersEyeInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersEyeInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersEarInfoDTO> getCharactersEarInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEarInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersEarInfoDTO charactersEarInfoDTO = new CharactersEarInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersEarInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersPendantOneInfoDTO> getCharactersPendantOneInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantOneInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersPendantOneInfoDTO charactersPendantOneInfoDTO = new CharactersPendantOneInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersPendantOneInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersPendantTwoInfoDTO> getCharactersPendantTwoInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantTwoInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersPendantTwoInfoDTO charactersPendantTwoInfoDTO = new CharactersPendantTwoInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersPendantTwoInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersBeltInfoDTO> getCharactersBeltInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBeltInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersBeltInfoDTO charactersBeltInfoDTO = new CharactersBeltInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersBeltInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersRingOneInfoDTO> getCharactersRingOneInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingOneInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersRingOneInfoDTO charactersRingOneInfoDTO = new CharactersRingOneInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersRingOneInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersRingTwoInfoDTO> getCharactersRingTwoInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingTwoInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersRingTwoInfoDTO charactersRingTwoInfoDTO = new CharactersRingTwoInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersRingTwoInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersRingThreeInfoDTO> getCharactersRingThreeInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingThreeInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersRingThreeInfoDTO charactersRingThreeInfoDTO = new CharactersRingThreeInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersRingThreeInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersRingFourInfoDTO> getCharactersRingFourInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingFourInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersRingFourInfoDTO charactersRingFourInfoDTO = new CharactersRingFourInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersRingFourInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersWeaponInfoDTO> getCharactersWeaponInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersWeaponInfoDTO charactersWeaponInfoDTO = new CharactersWeaponInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_base_option").get("attack_power").asInt(),
                        jsonInfo.get("item_add_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersWeaponInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }


    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersSubWeaponInfoDTO> getCharactersSubWeaponInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getSubWeaponInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersSubWeaponInfoDTO charactersSubWeaponInfoDTO = new CharactersSubWeaponInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersSubWeaponInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersEmblemInfoDTO> getCharactersEmblemInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEmblemInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersEmblemInfoDTO charactersEmblemInfoDTO = new CharactersEmblemInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersEmblemInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersBadgeInfoDTO> getCharactersBadgeInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBadgeInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersBadgeInfoDTO charactersBadgeInfoDTO = new CharactersBadgeInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersBadgeInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersMedalInfoDTO> getCharactersMedalInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMedalInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersMedalInfoDTO charactersMedalInfoDTO = new CharactersMedalInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersMedalInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersPoketInfoDTO> getCharactersPoketInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPoketInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersPoketInfoDTO charactersPoketInfoDTO = new CharactersPoketInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );

                return CompletableFuture.completedFuture(charactersPoketInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }

    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<CharactersHeartInfoDTO> getCharactersHeartInfo(GetCharactersInfo request) {
        if (rateLimiter.tryAcquire()) {
            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersItemEquipOptional.isPresent()) {
                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
                JsonNode jsonInfo = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonInfo = objectMapper.readTree(charactersItemEquip.getHeartInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CharactersHeartInfoDTO charactersHeartInfoDTO = new CharactersHeartInfoDTO(
                        jsonInfo.get("item_equipment_slot").asText(),
                        jsonInfo.get("item_name").asText(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("str").asInt(),
                        jsonInfo.get("item_total_option").get("int").asInt(),
                        jsonInfo.get("item_total_option").get("luk").asInt(),
                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
                        jsonInfo.get("potential_option_1").asText(),
                        jsonInfo.get("potential_option_2").asText(),
                        jsonInfo.get("potential_option_3").asText(),
                        jsonInfo.get("additional_potential_option_1").asText(),
                        jsonInfo.get("additional_potential_option_2").asText(),
                        jsonInfo.get("additional_potential_option_3").asText(),
                        jsonInfo.get("item_exceptional_option"),
                        jsonInfo.get("soul_option").asText()
                );


                return CompletableFuture.completedFuture(charactersHeartInfoDTO);
            } else {
                return null;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }


    @Async("characterThreadPool")
    @Transactional
    public String getCharactersCombat(GetCharactersTotalInfoDTO request, GetCharactersInfo re
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


        BigDecimal criticalDamageBaseBD = BigDecimal.valueOf(criticalDamageBase);

        criticalDamageBaseBD = criticalDamageBaseBD.setScale(2, RoundingMode.HALF_UP);


        Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(re.getCharactersName(), re.getDate());
        if (charactersItemEquipOptional.isPresent()) {
            CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
            JsonNode jsonInfo = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
            } catch (Exception e) {
                e.printStackTrace();

            }
//            int weaponAttactPowerBase = jsonInfo.get("item_base_option").get("attack_power").asInt();

//            int weaponAttackPowerStarforce = jsonInfo.get("item_starforce_option").get("attack_power").asInt();

            int weaponAtPowerAdd = jsonInfo.get("item_add_option").get("attack_power").asInt();
            int weaponMgPowerAdd = jsonInfo.get("item_add_option").get("magic_power").asInt();

            int weaponAddStat = 0;
            int weaponStarforce = jsonInfo.get("starforce").asInt();

            int weaponAtPower = jsonInfo.get("item_total_option").get("attack_power").asInt();
            int weaponMgPower = jsonInfo.get("item_total_option").get("magic_power").asInt();
            String weaponName = jsonInfo.get("item_name").asText();
            String weaponSort = jsonInfo.get("item_equipment_part").asText();

            int weaponAtMgStat = 0;

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
            int jenesisBowAtPower =318;

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
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채")|| weaponSort.equals("차크람")) {
                    if (weaponAddStat == 196) {
                        weaponAddGrade = 0;
                    } else if (weaponAddStat == 153) {
                        weaponAddGrade = 1;
                    } else if (weaponAddStat == 116) {
                        weaponAddGrade = 2;
                    } else if (weaponAddStat ==84) {
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
                    atMgPowerBase = atMgPowerBase - weaponAtMgStat + jenesisBowAtPower+ jenesisBowAddAtPower[weaponAddGrade];

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
                    } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채")|| weaponSort.equals("차크람")) {
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
                } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채")|| weaponSort.equals("차크람")) {
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
                    } else if (weaponSort.equals("활") || weaponSort.equals("듀얼보우건") || weaponSort.equals("에인션트 보우") || weaponSort.equals("체인") || weaponSort.equals("단검") || weaponSort.equals("부채")|| weaponSort.equals("차크람")) {
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
                System.out.println(finalCombat + "변경전");

                return finalCombat;
            }
            return null;
        }

        @Async("characterThreadPool")
        @Transactional
        public CompletableFuture<HatStatInfoDTO> getEquipSimulation ( int itemLevel, int starForce, int itemUpgrade,
        int addOptionStat, int potentialNewMainStatPer, int potentialNewSubStatPer, int potentialNewAtMgPowerPer,
        int potentialNewMainStat, int potentialNewSubStat, int potentialNewAtMgPowerStat){

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
        public String getCharactersChangeCombat (GetCharactersTotalChangedInfoDTO request, GetCharactersInfo re,
        int itemLevel, int starForce, int itemUpgrade
    ){

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


            BigDecimal criticalDamageBaseBD = BigDecimal.valueOf(criticalDamageBase);

            criticalDamageBaseBD = criticalDamageBaseBD.setScale(2, RoundingMode.HALF_UP);


            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(re.getCharactersName(), re.getDate());
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


/////////

        @Async("characterThreadPool")
        @Transactional
        public void getCharactersSetInfo (String charactersName, String ocid){
            if (rateLimiter.tryAcquire()) {
                String Url = "/maplestory/v1/character/set-effect";
                webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
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

                            CharactersSetEffectInfoDTO charactersSetEffectInfoDTO = new CharactersSetEffectInfoDTO(charactersName, ocid, absolSetCount, arcaneSetCount, bossAcSetCount, cvelSetCount, lucidAcSetCount, eternalSetCount, lomienSetCount, mystarSetCount);


                        }
                    } catch (Exception exception) {
                        System.err.println("에러: " + exception.getMessage());
                        return Mono.error(exception);
                    }
                    return Mono.empty(); // 반환값이 없는 경우에는 empty로 처리
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);
                });
            }
        }


    }

