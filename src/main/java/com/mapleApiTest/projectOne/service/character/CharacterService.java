package com.mapleApiTest.projectOne.service.character;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import com.mapleApiTest.projectOne.dto.character.request.GetCharactersInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetCharactersStatInfo;

import com.mapleApiTest.projectOne.dto.character.request.GetCharactersOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.repository.character.CharactersInfoRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersKeyRepository;
import com.mapleApiTest.projectOne.repository.character.CharactersStatInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CharacterService {

    private final CharactersKeyRepository charactersKeyRepository;
    private final CharactersInfoRepository charactersInfoRepository;
    private final CharactersStatInfoRepository charactersStatInfoRepository;


    private final WebClient webClient;

    @Value("${external.api.key}")
    private String apiKey;
    @Value("${external.api.url}")
    private String apiUrl;




//    public CharacterService(CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository) {
//        this.charactersKeyRepository = charactersKeyRepository;
//        this.charactersInfoRepository = charactersInfoRepository;
//        this.charactersStatInfoRepository = charactersStatInfoRepository;
//    }

//    public CharacterService(WebClient.Builder builder,CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository) {
//        this.webClient = builder
//                .filter((request, next) -> next.exchange(request))
//                .defaultHeader("x-nxopen-api-key", apiKey)
//                .baseUrl(apiUrl)
//                .build();
//        this.charactersKeyRepository = charactersKeyRepository;
//        this.charactersInfoRepository = charactersInfoRepository;
//        this.charactersStatInfoRepository = charactersStatInfoRepository;
//    }
    public CharacterService(WebClient.Builder builder,CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository,   @Value("${external.api.key}") String apiKey,
                            @Value("${external.api.url}") String apiUrl) {
        this.webClient = builder
                .defaultHeader("x-nxopen-api-key", apiKey)
                .baseUrl(apiUrl)
                .build();
        this.charactersKeyRepository = charactersKeyRepository;
        this.charactersInfoRepository = charactersInfoRepository;
        this.charactersStatInfoRepository = charactersStatInfoRepository;
    }

    @Async("characterThreadPool")
    public Mono<String> getCharacterOcid(GetCharactersOcid request, String Url) {
        Optional<CharactersKey> charactersKeyOptional = charactersKeyRepository.findByCharactersName(request.getName());
        System.out.println("여기여기2");
        if (charactersKeyOptional.isPresent()) {
            CharactersKey charactersKey = charactersKeyOptional.get();
            String ocidValue = charactersKey.getOcid();
            System.out.println("Found ocid: " + ocidValue);
            return Mono.just(ocidValue);
        } else {
            System.out.println("여기여기3");
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(Url)
                            .queryParam("character_name",request.getName())
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(responseBody -> {
                        System.out.println("여기여기7");
                        try {
                            System.out.println("ㅇㅇㅇㅇ3");

                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode jsonNode = objectMapper.readTree(responseBody);
                            String ocidValue = jsonNode.get("ocid").asText();
                            charactersKeyRepository.save(new CharactersKey(request.getName(), ocidValue));
                            System.out.println("여기여기4");

                            return Mono.just(ocidValue);
                        } catch (Exception exception) {
                            System.out.println("ㅇㅇㅇㅇ2");

                            System.err.println("에러: " + exception.getMessage());
                            return Mono.error(exception);
                        }
                    })
                    .onErrorResume(exception -> {
                        System.out.println("ㅇㅇㅇㅇ");

                        System.err.println("에러: " + exception.getMessage());
                        exception.printStackTrace(); // 추가된 부분
                        return Mono.error(exception);

                    });

        }


    }}


//    public CompletableFuture<String> getCharactersInfo(GetCharactersInfo request, String Url, String apiKey, String ocid) {
//
//        Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(),request.getDate());
//
//        if (charactersInfoOptional.isPresent()) {
//            CharactersInfo charactersInfo = charactersInfoOptional.get();
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResult;
//            try {
//                jsonResult = objectMapper.writeValueAsString(charactersInfo);
//            } catch (Exception e) {
//                // JSON 변환 중 오류 처리
//                e.printStackTrace();
//                return CompletableFuture.completedFuture("Error occurred during JSON conversion");
//            }
//
//            // JSON 문자열을 CompletableFuture로 감싸서 반환
//            return CompletableFuture.completedFuture(jsonResult);
//
//        } else {
//
//
//        try {
//
//            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build().toUriString();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("x-nxopen-api-key", apiKey);
//
//            ResponseEntity<CharactersInfo> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), CharactersInfo.class);
//            // 서버 응답에서 받은 CharacterInfo
//            CharactersInfo jsonResult = responseEntity.getBody();
//
//            // "ocid" 값을 클라이언트에서 원하는 값으로 설정
//
//            return CompletableFuture.completedFuture(jsonResult);
//
//
////            return responseEntity.getBody();
//        } catch (HttpClientErrorException e) {
//            HttpStatus statusCode = e.getStatusCode();
//            String responseBody = e.getResponseBodyAsString();
//
//            System.err.println("HTTP 상태 코드: " + statusCode);
//            System.err.println("응답 본문: " + responseBody);
//            return null;
//
//        } catch (Exception exception) {
//            System.err.println("에러: " + exception.getMessage());
//
//            return null;
//        }
//    }
//



//}




