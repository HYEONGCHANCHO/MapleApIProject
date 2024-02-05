package com.mapleApiTest.projectOne.service.character;


import com.mapleApiTest.projectOne.dto.character.request.GetChracterInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CharacterService {
    public String getCharacterOcid(GetChracterOcid request, String Url, String apiKey) {
        try {
            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("character_name", request.getName()).build().toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", apiKey);
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return responseEntity.getBody();
        } catch (Exception exception) {
            System.err.println("에러: " + exception.getMessage());
            return null;
        }
    }

    public CharacterInfo getCharacterInfo(GetChracterInfo request, String Url, String apiKey, String ocid) {
        try {

            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("ocid", ocid).queryParam("date", request.getDate()).build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", apiKey);

            ResponseEntity<CharacterInfo> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), CharacterInfo.class);

            return responseEntity.getBody();
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




