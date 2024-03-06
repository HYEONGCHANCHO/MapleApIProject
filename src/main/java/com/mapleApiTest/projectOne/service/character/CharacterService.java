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
                        String damage = jsonNode.get("final_stat").get(2).toString();

                        String bossDamage = jsonNode.get("final_stat").get(3).toString();
                        String finalDamage = jsonNode.get("final_stat").get(4).toString();
                        String ignoreRate = jsonNode.get("final_stat").get(5).toString();
                        String criticalDamage = jsonNode.get("final_stat").get(7).toString();
                        String str = jsonNode.get("final_stat").get(16).toString();
                        String dex = jsonNode.get("final_stat").get(17).toString();
                        String intel = jsonNode.get("final_stat").get(18).toString();
                        String luk = jsonNode.get("final_stat").get(19).toString();
                        String hp = jsonNode.get("final_stat").get(20).toString();
                        String attackPower = jsonNode.get("final_stat").get(40).toString();
                        String magicPower = jsonNode.get("final_stat").get(41).toString();
                        String combatPower = jsonNode.get("final_stat").get(42).toString();




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
                CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(),charactersItemEquip.getHatInfo(),charactersItemEquip.getFaceInfo(),charactersItemEquip.getEyeInfo(),charactersItemEquip.getEarInfo(),charactersItemEquip.getTopInfo(),charactersItemEquip.getBottomInfo(),charactersItemEquip.getShoesInfo(),charactersItemEquip.getGlovesInfo(),charactersItemEquip.getSubWeaponInfo(),charactersItemEquip.getWeaponInfo(),charactersItemEquip.getRingOneInfo(),charactersItemEquip.getRingTwoInfo(),charactersItemEquip.getRingThreeInfo(),charactersItemEquip.getRingFourInfo(), charactersItemEquip.getPendantOneInfo(),charactersItemEquip.getMedalInfo(),charactersItemEquip.getShoulderInfo(),charactersItemEquip.getPoketInfo(),charactersItemEquip.getHeartInfo(),charactersItemEquip.getBadgeInfo(),charactersItemEquip.getEmblemInfo(),charactersItemEquip.getPendantTwoInfo());
                return CompletableFuture.completedFuture(charactersItemEquipDTO);
            } else {
                Mono<CharactersItemEquipDTO> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(JsonNode.class).flatMap(jsonNode -> {
                    try {
//                        String item_equipment = jsonNode.get("item_equipment").toString();


//                        String[] equipmentTypes = {"hat","face", "eye", "ear", "top", "bottom", "shoes", "gloves", "subWeapon", "weapon", "ringOne", "ringTwo", "ringThree", "ringFour", "pendantOne", "medal", "shoulder", "poket", "heart", "badge", "emblem", "pendantTwo"};
//
//                        String[] equipmentInfo = new String[equipmentTypes.length];
//
//                        for (int i = 0; i < equipmentTypes.length; i++) {
//                            equipmentInfo[i] = jsonNode.get("item_equipment").get(i).toString();
//                        }


                        String[] equipmentTypes = {"망토","모자","얼굴장식", "눈장식", "귀고리", "상의", "하의", "신발", "장갑", "밸트" ,"보조무기", "무기", "반지1", "반지2", "반지3", "반지4", "팬던트", "훈장", "어깨장식", "포켓 아이템", "기계 심장", "뱃지", "엠블렘", "팬던트2"};

                        //위 순서랑 아래 인포 순서랑 맞추면 됨.
                        String[] equipmentInfo = new String[equipmentTypes.length];

// "item_equipment" 배열에 대한 루프
                        JsonNode itemEquipmentNode = jsonNode.get("item_equipment");
                        for (JsonNode equipmentNode : itemEquipmentNode) {
                            // "item_equipment_part" 값 가져오기
                            String equipmentPart = equipmentNode.get("item_equipment_part").asText();

                            // "equipmentTypes" 배열에서 일치하는 값의 인덱스 찾기
                            int index = Arrays.asList(equipmentTypes).indexOf(equipmentPart);

                            // "equipmentTypes" 배열에 있는 값과 일치하는 경우에만 데이터 할당
                            if (index >= 0) {
                                equipmentInfo[index] = equipmentNode.toString();
                            }
                        }



                        String hatInfo = equipmentInfo[0];
                        String faceInfo = equipmentInfo[1];
                        String eyeInfo = equipmentInfo[2];
                        String earInfo = equipmentInfo[3];
                        String topInfo = equipmentInfo[4];
                        String bottomInfo = equipmentInfo[5];
                        String shoesInfo = equipmentInfo[6];
                        String glovesInfo = equipmentInfo[7];
                        String subWeaponInfo = equipmentInfo[8];
                        String weaponInfo = equipmentInfo[9];
                        String ringOneInfo = equipmentInfo[10];
                        String ringTwoInfo = equipmentInfo[11];
                        String ringThreeInfo = equipmentInfo[12];
                        String ringFourInfo = equipmentInfo[13];
                        String pendantOneInfo = equipmentInfo[14];
                        String medalInfo = equipmentInfo[15];
                        String shoulderInfo = equipmentInfo[16];
                        String poketInfo = equipmentInfo[17];
                        String heartInfo = equipmentInfo[18];
                        String badgeInfo = equipmentInfo[19];
                        String emblemInfo = equipmentInfo[20];
                        String pendantTwoInfo = equipmentInfo[21];

                        CharactersItemEquip charactersItemEquip = new CharactersItemEquip(request.getCharactersName(), request.getDate(),hatInfo,faceInfo,eyeInfo,earInfo,topInfo,bottomInfo,shoesInfo,glovesInfo,subWeaponInfo,weaponInfo,ringOneInfo,ringTwoInfo,ringThreeInfo,ringFourInfo,pendantOneInfo,medalInfo,shoulderInfo,poketInfo,heartInfo,badgeInfo,emblemInfo,pendantTwoInfo);
                        charactersItemEquipRepository.save(charactersItemEquip);
                        CharactersItemEquipDTO charactersItemEquipDTO = new CharactersItemEquipDTO(request.getDate(), request.getCharactersName(), hatInfo,faceInfo,eyeInfo,earInfo,topInfo,bottomInfo,shoesInfo,glovesInfo,subWeaponInfo,weaponInfo,ringOneInfo,ringTwoInfo,ringThreeInfo,ringFourInfo,pendantOneInfo,medalInfo,shoulderInfo,poketInfo,heartInfo,badgeInfo,emblemInfo,pendantTwoInfo);
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


    ///////////////////
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersTotalInfoDTO> getCharactersTotalInfo(GetCharactersInfo request) {
//
//        if (rateLimiter.tryAcquire()) {
//
//            Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
//
//
//            Optional<CharactersStatInfo> charactersStatInfoOptional = charactersStatInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
//
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
//            if (charactersItemEquipOptional.isPresent()&&charactersStatInfoOptional.isPresent()&&charactersItemEquipOptional.isPresent()) {
//
//                CharactersInfo charactersInfo = charactersInfoOptional.get();
//                CharactersStatInfo charactersStatInfo = charactersStatInfoOptional.get();
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode finalEquipNode = null;
//                try {
//                    finalEquipNode = objectMapper.readTree(charactersItemEquip.getItem_equipment());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String hatInfo = finalEquipNode.get(0).toString();
//
////                finalEquipNode.get(0).toString()
//                System.out.println("hatInfo: " + hatInfo+ "dddsdsds");
//
//                CharactersTotalInfoDTO charactersTotalInfoDTO = new CharactersTotalInfoDTO(request.getCharactersName(),request.getDate(),charactersInfo.getWorld_name(),charactersInfo.getCharacter_class(),charactersInfo.getCharactersLevel(),hatInfo);
//
//                return CompletableFuture.completedFuture(charactersTotalInfoDTO);
//
//
//            } else {
//                return null;
//            }
////           (exception -> {
////                    System.err.println("에러: " + exception.getMessage());
////                    exception.printStackTrace(); // 추가된 부분
////                    return Mono.error(exception);
////                });
//
////            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//
//
//    }




}

