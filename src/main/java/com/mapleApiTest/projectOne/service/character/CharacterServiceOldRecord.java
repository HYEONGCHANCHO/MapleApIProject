package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.domain.character.*;
import com.mapleApiTest.projectOne.dto.ItemInfo.HatStatInfoDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSetEffectDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSimulationDTO;
import com.mapleApiTest.projectOne.dto.character.request.*;
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

//@Service
public class CharacterServiceOldRecord {

//    private final CharactersKeyRepository charactersKeyRepository;
//    private final CharactersInfoRepository charactersInfoRepository;
//    private final CharactersStatInfoRepository charactersStatInfoRepository;
//    private final CharactersItemEquipRepository charactersItemEquipRepository;
//    private final CharactersBaseTotalInfoRepository charactersBaseTotalInfoRepository;
//
//    private final WebClient webClient;
//
//    private final RateLimiter rateLimiter = RateLimiter.create(300.0 / 60.0); //분당 300회
//    @Value("${external.api.key}")
//    private String apiKey;
//    @Value("${external.api.url}")
//    private String apiUrl;
//
//
//    public CharacterServiceOldRecord(WebClient.Builder builder, CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository, CharactersItemEquipRepository charactersItemEquipRepository, CharactersBaseTotalInfoRepository charactersBaseTotalInfoRepository, @Value("${external.api.key}") String apiKey, @Value("${external.api.url}") String apiUrl) {
//        this.webClient = builder.defaultHeader("x-nxopen-api-key", apiKey).baseUrl(apiUrl).build();
//        this.charactersKeyRepository = charactersKeyRepository;
//        this.charactersInfoRepository = charactersInfoRepository;
//        this.charactersStatInfoRepository = charactersStatInfoRepository;
//        this.charactersItemEquipRepository = charactersItemEquipRepository;
//        this.charactersBaseTotalInfoRepository = charactersBaseTotalInfoRepository;
//    }

//
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersTopInfoDTO> getCharactersTopInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getTopInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                CharactersTopInfoDTO charactersTopInfoDTO = new CharactersTopInfoDTO(jsonInfo.get("item_equipment_slot").asText(), jsonInfo.get("item_name").asText(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("str").asInt(), jsonInfo.get("item_total_option").get("int").asInt(), jsonInfo.get("item_total_option").get("luk").asInt(), jsonInfo.get("item_total_option").get("max_hp").asInt(), jsonInfo.get("item_total_option").get("attack_power").asInt(), jsonInfo.get("item_total_option").get("magic_power").asInt(), jsonInfo.get("item_total_option").get("boss_damage").asDouble(), jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(), jsonInfo.get("item_total_option").get("all_stat").asInt(), jsonInfo.get("potential_option_1").asText(), jsonInfo.get("potential_option_2").asText(), jsonInfo.get("potential_option_3").asText(), jsonInfo.get("additional_potential_option_1").asText(), jsonInfo.get("additional_potential_option_2").asText(), jsonInfo.get("additional_potential_option_3").asText(), jsonInfo.get("item_exceptional_option"), jsonInfo.get("soul_option").asText()
//                );
//                return CompletableFuture.completedFuture(charactersTopInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersBottomInfoDTO> getCharactersBottomInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBottomInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersBottomInfoDTO charactersBottomInfoDTO = new CharactersBottomInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersBottomInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersCapeInfoDTO> getCharactersCapeInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getCapeInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersCapeInfoDTO charactersCapeInfoDTO = new CharactersCapeInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersCapeInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersShoesInfoDTO> getCharactersShoesInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getShoesInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersShoesInfoDTO charactersShoesInfoDTO = new CharactersShoesInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersShoesInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersGlovesInfoDTO> getCharactersGlovesInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getGlovesInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersGlovesInfoDTO charactersGlovesInfoDTO = new CharactersGlovesInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersGlovesInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersShoulderInfoDTO> getCharactersShoulderInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getShoulderInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersShoulderInfoDTO charactersShoulderInfoDTO = new CharactersShoulderInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersShoulderInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersFaceInfoDTO> getCharactersFaceInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getFaceInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersFaceInfoDTO charactersFaceInfoDTO = new CharactersFaceInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersFaceInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersEyeInfoDTO> getCharactersEyeInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEyeInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersEyeInfoDTO charactersEyeInfoDTO = new CharactersEyeInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersEyeInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersEarInfoDTO> getCharactersEarInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEarInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersEarInfoDTO charactersEarInfoDTO = new CharactersEarInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersEarInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersPendantOneInfoDTO> getCharactersPendantOneInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantOneInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersPendantOneInfoDTO charactersPendantOneInfoDTO = new CharactersPendantOneInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersPendantOneInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersPendantTwoInfoDTO> getCharactersPendantTwoInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPendantTwoInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersPendantTwoInfoDTO charactersPendantTwoInfoDTO = new CharactersPendantTwoInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersPendantTwoInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersBeltInfoDTO> getCharactersBeltInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBeltInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersBeltInfoDTO charactersBeltInfoDTO = new CharactersBeltInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersBeltInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersRingOneInfoDTO> getCharactersRingOneInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingOneInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersRingOneInfoDTO charactersRingOneInfoDTO = new CharactersRingOneInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersRingOneInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersRingTwoInfoDTO> getCharactersRingTwoInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingTwoInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersRingTwoInfoDTO charactersRingTwoInfoDTO = new CharactersRingTwoInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersRingTwoInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersRingThreeInfoDTO> getCharactersRingThreeInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingThreeInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersRingThreeInfoDTO charactersRingThreeInfoDTO = new CharactersRingThreeInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersRingThreeInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersRingFourInfoDTO> getCharactersRingFourInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getRingFourInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersRingFourInfoDTO charactersRingFourInfoDTO = new CharactersRingFourInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersRingFourInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersWeaponInfoDTO> getCharactersWeaponInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getWeaponInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersWeaponInfoDTO charactersWeaponInfoDTO = new CharactersWeaponInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_base_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_add_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersWeaponInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersSubWeaponInfoDTO> getCharactersSubWeaponInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getSubWeaponInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersSubWeaponInfoDTO charactersSubWeaponInfoDTO = new CharactersSubWeaponInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersSubWeaponInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersEmblemInfoDTO> getCharactersEmblemInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getEmblemInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersEmblemInfoDTO charactersEmblemInfoDTO = new CharactersEmblemInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersEmblemInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersBadgeInfoDTO> getCharactersBadgeInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getBadgeInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersBadgeInfoDTO charactersBadgeInfoDTO = new CharactersBadgeInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersBadgeInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersMedalInfoDTO> getCharactersMedalInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getMedalInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersMedalInfoDTO charactersMedalInfoDTO = new CharactersMedalInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersMedalInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersPoketInfoDTO> getCharactersPoketInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getPoketInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersPoketInfoDTO charactersPoketInfoDTO = new CharactersPoketInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//                return CompletableFuture.completedFuture(charactersPoketInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//
//    @Async("characterThreadPool")
//    @Transactional
//    public CompletableFuture<CharactersHeartInfoDTO> getCharactersHeartInfo(GetCharactersInfo request) {
//        if (rateLimiter.tryAcquire()) {
//            Optional<CharactersItemEquip> charactersItemEquipOptional = charactersItemEquipRepository.findByCharactersName(request.getCharactersName());
//            if (charactersItemEquipOptional.isPresent()) {
//                CharactersItemEquip charactersItemEquip = charactersItemEquipOptional.get();
//                JsonNode jsonInfo = null;
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    jsonInfo = objectMapper.readTree(charactersItemEquip.getHeartInfo());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                CharactersHeartInfoDTO charactersHeartInfoDTO = new CharactersHeartInfoDTO(
//                        jsonInfo.get("item_equipment_slot").asText(),
//                        jsonInfo.get("item_name").asText(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("str").asInt(),
//                        jsonInfo.get("item_total_option").get("int").asInt(),
//                        jsonInfo.get("item_total_option").get("luk").asInt(),
//                        jsonInfo.get("item_total_option").get("max_hp").asInt(),
//                        jsonInfo.get("item_total_option").get("attack_power").asInt(),
//                        jsonInfo.get("item_total_option").get("magic_power").asInt(),
//                        jsonInfo.get("item_total_option").get("boss_damage").asDouble(),
//                        jsonInfo.get("item_total_option").get("ignore_monster_armor").asDouble(),
//                        jsonInfo.get("item_total_option").get("all_stat").asInt(),
//                        jsonInfo.get("potential_option_1").asText(),
//                        jsonInfo.get("potential_option_2").asText(),
//                        jsonInfo.get("potential_option_3").asText(),
//                        jsonInfo.get("additional_potential_option_1").asText(),
//                        jsonInfo.get("additional_potential_option_2").asText(),
//                        jsonInfo.get("additional_potential_option_3").asText(),
//                        jsonInfo.get("item_exceptional_option"),
//                        jsonInfo.get("soul_option").asText()
//                );
//
//
//                return CompletableFuture.completedFuture(charactersHeartInfoDTO);
//            } else {
//                return null;
//            }
//        } else {
//            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
//        }
//    }
//



}

