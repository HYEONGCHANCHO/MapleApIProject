package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersItemEquip;
import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import com.mapleApiTest.projectOne.domain.character.CharactersStatInfo;
import com.mapleApiTest.projectOne.dto.character.request.*;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
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
                CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getDate(), request.getCharactersName(), charactersStatInfo.getDamage(),charactersStatInfo.getBossDamage(),charactersStatInfo.getFinalDamage(),charactersStatInfo.getIgnoreRate(),charactersStatInfo.getCriticalDamage(),charactersStatInfo.getStr(),charactersStatInfo.getDex(),charactersStatInfo.getIntel(),charactersStatInfo.getLuk(),charactersStatInfo.getHp(),charactersStatInfo.getAttackPower(),charactersStatInfo.getMagicPower(),charactersStatInfo.getCombatPower());

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


                        CharactersStatInfo charactersStatInfo = new CharactersStatInfo(request.getCharactersName(), request.getDate(), damage,bossDamage,finalDamage,ignoreRate,criticalDamage,str,dex,intel,luk,hp,attackPower,magicPower,combatPower);
                        charactersStatInfoRepository.save(charactersStatInfo);
                        CharactersStatInfoDTO charactersStatInfoDTO = new CharactersStatInfoDTO(request.getDate(), request.getCharactersName(), damage,bossDamage,finalDamage,ignoreRate,criticalDamage,str,dex,intel,luk,hp,attackPower,magicPower,combatPower);
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
                CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(),charactersItemEquip.getHatInfo(),charactersItemEquip.getTopInfo(),charactersItemEquip.getBottomInfo(),charactersItemEquip.getCapeInfo(),charactersItemEquip.getShoesInfo(),charactersItemEquip.getGlovesInfo(),charactersItemEquip.getShoulderInfo(),charactersItemEquip.getFaceInfo(),charactersItemEquip.getEyeInfo(),charactersItemEquip.getEarInfo(),charactersItemEquip.getPendantOneInfo(),charactersItemEquip.getPendantTwoInfo(),charactersItemEquip.getBeltInfo(),charactersItemEquip.getRingOneInfo(),charactersItemEquip.getRingTwoInfo(),charactersItemEquip.getRingThreeInfo(),charactersItemEquip.getRingFourInfo(),charactersItemEquip.getWeaponInfo(),charactersItemEquip.getSubWeaponInfo(),charactersItemEquip.getEmblemInfo(),charactersItemEquip.getBadgeInfo(),charactersItemEquip.getMedalInfo(),charactersItemEquip.getPoketInfo(),charactersItemEquip.getHeartInfo());
                return CompletableFuture.completedFuture(charactersItemEquipDTO);

            } else {
                Mono<CharactersItemEquipDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {

                        String[] equipmentTypes = {"모자","상의","하의","망토","신발","장갑","어깨장식","얼굴장식","눈장식","귀고리","펜던트","펜던트2","벨트" ,"반지1","반지2","반지3", "반지4","무기","보조무기","엠블렘" ,"뱃지" ,"훈장","포켓 아이템","기계 심장"};

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

                        CharactersItemEquip charactersItemEquip = new CharactersItemEquip(request.getCharactersName(), request.getDate(),hatInfo,topInfo,bottomInfo,capeInfo,shoesInfo,glovesInfo,shoulderInfo,faceInfo,eyeInfo,earInfo,pendantOneInfo,pendantTwoInfo,beltInfo,ringOneInfo,ringTwoInfo,ringThreeInfo,ringFourInfo,weaponInfo,subWeaponInfo,emblemInfo,badgeInfo,medalInfo,poketInfo,heartInfo);
                        charactersItemEquipRepository.save(charactersItemEquip);
                        CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(),hatInfo,topInfo,bottomInfo,capeInfo,shoesInfo,glovesInfo,shoulderInfo,faceInfo,eyeInfo,earInfo,pendantOneInfo,pendantTwoInfo,beltInfo,ringOneInfo,ringTwoInfo,ringThreeInfo,ringFourInfo,weaponInfo,subWeaponInfo,emblemInfo,badgeInfo,medalInfo,poketInfo,heartInfo);
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

            if (charactersItemEquipOptional.isPresent()&&charactersStatInfoOptional.isPresent()&&charactersItemEquipOptional.isPresent()) {

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




//
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode damageInfoNode = null;
//                JsonNode bossDamageInfoNode = null;
//                JsonNode finalDamageInfoNode = null;
//                JsonNode ignoreRateInfoNode = null;
//                JsonNode criticalDamageInfoNode = null;
//                JsonNode strInfoNode = null;
//                JsonNode dexInfoNode = null;
//                JsonNode intelInfoNode = null;
//                JsonNode lukInfoNode = null;
//                JsonNode hpInfoNode = null;
//                JsonNode attackPowerInfoNode = null;
//                JsonNode magicPowerInfoNode = null;
//                JsonNode combatPowerInfoNode = null;
//                try {
//                    damageInfoNode = objectMapper.readTree(charactersStatInfo.getDamage());
//                    bossDamageInfoNode = objectMapper.readTree(charactersStatInfo.getBossDamage());
//                    finalDamageInfoNode = objectMapper.readTree(charactersStatInfo.getFinalDamage());
//                    ignoreRateInfoNode = objectMapper.readTree(charactersStatInfo.getIgnoreRate());
//                    criticalDamageInfoNode = objectMapper.readTree(charactersStatInfo.getCriticalDamage());
//                    strInfoNode = objectMapper.readTree(charactersStatInfo.getStr());
//                    dexInfoNode = objectMapper.readTree(charactersStatInfo.getDex());
//                    intelInfoNode = objectMapper.readTree(charactersStatInfo.getIntel());
//                    lukInfoNode = objectMapper.readTree(charactersStatInfo.getLuk());
//                    hpInfoNode = objectMapper.readTree(charactersStatInfo.getHp());
//                    attackPowerInfoNode = objectMapper.readTree(charactersStatInfo.getAttackPower());
//                    magicPowerInfoNode = objectMapper.readTree(charactersStatInfo.getMagicPower());
//                    combatPowerInfoNode = objectMapper.readTree(charactersStatInfo.getCombatPower());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                int damageInfo = damageInfoNode.get("stat_value").asInt();
//                int bossDamageInfo = bossDamageInfoNode.get("stat_value").asInt();
//                int finalDamageInfo = finalDamageInfoNode.get("stat_value").asInt();
//                double ignoreRateInfo = ignoreRateInfoNode.get("stat_value").asDouble();
//                double criticalDamageInfo = criticalDamageInfoNode.get("stat_value").asDouble();
//                int strInfo = strInfoNode.get("stat_value").asInt();
//                int dexInfo = dexInfoNode.get("stat_value").asInt();
//                int intelInfo = intelInfoNode.get("stat_value").asInt();
//                int lukInfo = lukInfoNode.get("stat_value").asInt();
//                int hpInfo = hpInfoNode.get("stat_value").asInt();
//                int attackPowerInfo = attackPowerInfoNode.get("stat_value").asInt();
//                int magicPowerInfo = magicPowerInfoNode.get("stat_value").asInt();
//                int combatPowerInfo = combatPowerInfoNode.get("stat_value").asInt();

                CharactersTotalInfoDTO charactersTotalInfoDTO = new CharactersTotalInfoDTO(request.getCharactersName(),request.getDate(),charactersInfo.getWorld_name(),charactersInfo.getCharacter_class(),charactersInfo.getCharactersLevel(),damageInfo,bossDamageInfo,finalDamageInfo,ignoreRateInfo,criticalDamageInfo,strInfo,dexInfo,intelInfo,lukInfo,hpInfo,attackPowerInfo,magicPowerInfo,combatPowerInfo);

                return CompletableFuture.completedFuture(charactersTotalInfoDTO);


            } else {
                return null;
            }
//           (exception -> {
//                    System.err.println("에러: " + exception.getMessage());
//                    exception.printStackTrace(); // 추가된 부분
//                    return Mono.error(exception);
//                });

//            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }


    }




}

