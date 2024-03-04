package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import com.mapleApiTest.projectOne.dto.character.request.GetCharactersInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetCharactersOcid;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.character.response.CharactersInfoDTO;
import com.mapleApiTest.projectOne.repository.character.CharactersInfoRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersKeyRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersStatInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CharacterService {

    private final CharactersKeyRepository charactersKeyRepository;
    private final CharactersInfoRepository charactersInfoRepository;
    private final CharactersStatInfoRepository charactersStatInfoRepository;
    private final WebClient webClient;

    private final RateLimiter rateLimiter = RateLimiter.create(300.0 / 60.0); //분당 300회
    @Value("${external.api.key}")
    private String apiKey;
    @Value("${external.api.url}")
    private String apiUrl;


    public CharacterService(WebClient.Builder builder, CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository, @Value("${external.api.key}") String apiKey, @Value("${external.api.url}") String apiUrl) {
        this.webClient = builder.defaultHeader("x-nxopen-api-key", apiKey).baseUrl(apiUrl).build();
        this.charactersKeyRepository = charactersKeyRepository;
        this.charactersInfoRepository = charactersInfoRepository;
        this.charactersStatInfoRepository = charactersStatInfoRepository;
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
    public CompletableFuture<Object> getCharactersInfo(GetCharactersInfo request, String Url, String apiKey, String ocid) {

        if (rateLimiter.tryAcquire()) {
            Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(), request.getDate());
            if (charactersInfoOptional.isPresent()) {
                CharactersInfo charactersInfo = charactersInfoOptional.get();
                CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getDate(), request.getCharactersName(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersServer(), charactersInfo.getCharactersLevel(), charactersInfo.getCharacter_image());
                return CompletableFuture.completedFuture(charactersInfoDTO);
            } else {
                Mono<Object> MonoResult
                        = webClient.get().uri(uriBuilder -> uriBuilder.path(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build()).retrieve().bodyToMono(String.class).flatMap(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        String world_name = jsonNode.get("world_name").asText();
                        String character_class = jsonNode.get("character_class").asText();
                        String character_level = jsonNode.get("character_level").asText();
                        String character_image = jsonNode.get("character_image").asText();
                        String character_server = jsonNode.get("character_server").asText();
                        CharactersInfo charactersInfo = new CharactersInfo(request.getCharactersName(), request.getDate(), character_level, character_server, character_image, character_class, world_name);
                        charactersInfoRepository.save(charactersInfo);
                        CharactersInfoDTO charactersInfoDTO = new CharactersInfoDTO(request.getDate(), request.getCharactersName(), charactersInfo.getWorld_name(), charactersInfo.getCharacter_class(), charactersInfo.getCharactersServer(), charactersInfo.getCharactersLevel(), charactersInfo.getCharacter_image());
                        return Mono.just(charactersInfoDTO);
                    } catch (Exception exception) {
                        System.err.println("에러: " + exception.getMessage());
                        return Mono.just("없는 데이터");
                    }
                }).onErrorResume(exception -> {
                    System.err.println("에러: " + exception.getMessage());
                    exception.printStackTrace(); // 추가된 부분
                    return Mono.error(exception);

                });
                CompletableFuture<Object> completableFutureResult = new CompletableFuture<>();
                MonoResult.subscribe(completableFutureResult::complete, completableFutureResult::completeExceptionally);
                return completableFutureResult;
            }
        } else {
            return CompletableFuture.failedFuture(new RuntimeException("Rate limit exceeded"));
        }
    }
}

//                String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build().toUriString();
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.set("x-nxopen-api-key", apiKey);
//
//                ResponseEntity<CharactersInfo> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), CharactersInfo.class);
//                // 서버 응답에서 받은 CharacterInfo
//
//                ///
//                CharactersInfo charactersInfo = responseEntity.getBody();
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                String jsonResult;
//                try {
//                    jsonResult = objectMapper.writeValueAsString(charactersInfo);
//                } catch (Exception e) {
//                    // JSON 변환 중 오류 처리
//                    e.printStackTrace();
//                    return CompletableFuture.completedFuture("Error occurred during JSON conversion");
//                }
//                ///
//
////                return null;
//                return CompletableFuture.completedFuture(jsonResult);
//
////            return responseEntity.getBody();
//            } catch (HttpClientErrorException e) {
//                HttpStatus statusCode = e.getStatusCode();
//                String responseBody = e.getResponseBodyAsString();
//
////                System.err.println("HTTP 상태 코드: " + statusCode);
////                System.err.println("응답 본문: " + responseBody);
//                return null;
//
//            } catch (Exception exception) {
//                System.err.println("에러: " + exception.getMessage());
//                return null;
//            }
//        }
//    }
//}



