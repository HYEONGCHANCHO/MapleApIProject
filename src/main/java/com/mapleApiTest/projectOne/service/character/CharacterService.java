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
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CharacterService {

    private final CharactersKeyRepository charactersKeyRepository;
    private final CharactersInfoRepository charactersInfoRepository;
    private final CharactersStatInfoRepository charactersStatInfoRepository;


    public CharacterService(CharactersKeyRepository charactersKeyRepository, CharactersInfoRepository charactersInfoRepository, CharactersStatInfoRepository charactersStatInfoRepository) {
        this.charactersKeyRepository = charactersKeyRepository;
        this.charactersInfoRepository = charactersInfoRepository;
        this.charactersStatInfoRepository = charactersStatInfoRepository;
    }

    @Async("characterThreadPool")
    public CompletableFuture<String> getCharacterOcid(GetCharactersOcid request, String Url, String apiKey) {
        Optional<CharactersKey> charactersKeyOptional = charactersKeyRepository.findByCharactersName(request.getName());

        if (charactersKeyOptional.isPresent()) {
            CharactersKey charactersKey = charactersKeyOptional.get();
            String ocidValue = charactersKey.getOcid();
            // ocidValue를 사용하거나 처리
            System.out.println("Found ocid: " + ocidValue);
            return CompletableFuture.completedFuture(ocidValue);
        } else {
            try {
                String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("character_name", request.getName()).build().toUriString();
                HttpHeaders headers = new HttpHeaders();
                headers.set("x-nxopen-api-key", apiKey);
                ResponseEntity<String> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

                String responseBody = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String ocidValue = jsonNode.get("ocid").asText();
                charactersKeyRepository.save(new CharactersKey(request.getName(), ocidValue));

                return CompletableFuture.completedFuture(ocidValue);
            } catch (Exception exception) {
                System.err.println("에러: " + exception.getMessage());
                return CompletableFuture.completedFuture(null);
            }
        }
    }
    public CompletableFuture<String> getCharactersInfo(GetCharactersInfo request, String Url, String apiKey, String ocid) {

        Optional<CharactersInfo> charactersInfoOptional = charactersInfoRepository.findByCharactersNameAndDate(request.getCharactersName(),request.getDate());

        if (charactersInfoOptional.isPresent()) {
            CharactersInfo charactersInfo = charactersInfoOptional.get();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult;
            try {
                jsonResult = objectMapper.writeValueAsString(charactersInfo);
            } catch (Exception e) {
                // JSON 변환 중 오류 처리
                e.printStackTrace();
                return CompletableFuture.completedFuture("Error occurred during JSON conversion");
            }

            // JSON 문자열을 CompletableFuture로 감싸서 반환
            return CompletableFuture.completedFuture(jsonResult);

        } else {


        try {

            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", apiKey);

            ResponseEntity<CharactersInfo> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), CharactersInfo.class);
            // 서버 응답에서 받은 CharacterInfo
            CharactersInfo jsonResult = responseEntity.getBody();

            // "ocid" 값을 클라이언트에서 원하는 값으로 설정

            return CompletableFuture.completedFuture(jsonResult);


//            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            String responseBody = e.getResponseBodyAsString();

            System.err.println("HTTP 상태 코드: " + statusCode);
            System.err.println("응답 본문: " + responseBody);
            return null;

        } catch (Exception exception) {
            System.err.println("에러: " + exception.getMessage());

            return null;
        }
    }




}




