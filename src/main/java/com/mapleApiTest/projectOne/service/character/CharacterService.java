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
                        String character_level = jsonNode.get("character_level").asText();
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

                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialOne()); ;
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialTwo()); ;
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getPotentialThree()); ;
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalOne()); ;
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalTwo()); ;
                charactersHatInfoDTO.processPotential(charactersHatInfoDTO.getAdditionalThree()); ;

                int hatStrPotentialPer=charactersHatInfoDTO.getStrPotentialPer();
                int hatDexPotentialPer=charactersHatInfoDTO.getDexPotentialPer() ;
                int hatIntPotentialPer=charactersHatInfoDTO.getIntPotentialPer() ;
                int hatLukPotentialPer=charactersHatInfoDTO.getLukPotentialPer() ;
                int hatAllStatPotentialPer=charactersHatInfoDTO.getAllStatPotentialPer() ;
                int hatStrPotentialStat=charactersHatInfoDTO.getStrPotentialStat() ;
                int hatDexPotentialStat=charactersHatInfoDTO.getDexPotentialStat() ;
                int hatIntPotentialStat=charactersHatInfoDTO.getIntPotentialStat() ;
                int hatLukPotentialStat=charactersHatInfoDTO.getLukPotentialStat() ;
                int hatAtMgPotentialPer=charactersHatInfoDTO.getAtMgPotentialPer() ;
                int hatAtMgPotentialStat=charactersHatInfoDTO.getAtMgPotentialStat() ;

                System.out.println("hatStrPotentialPer"+hatStrPotentialPer);
                System.out.println("hatDexPotentialPer"+hatDexPotentialPer);
                System.out.println("hatAllStatPotentialPer"+hatAllStatPotentialPer);
                System.out.println("hatAtMgPotentialStat"+hatAtMgPotentialStat);
                System.out.println("hatStrPotentialStat"+hatStrPotentialStat);
                System.out.println("hatDexPotentialStat"+hatDexPotentialStat);




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
            System.out.println(finalCombat+"변경전");

            return finalCombat;
        }
        return null;
    }


    @Async("characterThreadPool")
    @Transactional
    public CompletableFuture<HatStatInfoDTO> getEquipSimulation(int itemLevel,int starForce, int itemUpgrade,int addOptionStat, int potentialTotalMainStatPer, int potentialTotalSubStatPer, int potentialTotalAtMgPower) {

        ItemSimulationDTO itemSimulationDTO = new ItemSimulationDTO();
        HatStatInfoDTO hatStatInfoDTO = new HatStatInfoDTO(itemLevel);

        itemSimulationDTO.calculateEquipmentStats(hatStatInfoDTO, starForce, itemUpgrade, itemLevel,addOptionStat,potentialTotalMainStatPer,potentialTotalSubStatPer,potentialTotalAtMgPower);
        System.out.println(hatStatInfoDTO.getMainStat()+"dadadadadadad");
        System.out.println(hatStatInfoDTO.getSubStat());
        System.out.println(hatStatInfoDTO.getAtMgPower());
        return CompletableFuture.completedFuture(hatStatInfoDTO);
    }

    @Async("characterThreadPool")
    @Transactional
    public String getCharactersChangeCombat(GetCharactersTotalChangedInfoDTO request, GetCharactersInfo re,int itemLevel,int starForce,int itemUpgrade
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


            System.out.println(finalCombat+"변경후");
            return finalCombat;
        }
        return null;
    }











}




