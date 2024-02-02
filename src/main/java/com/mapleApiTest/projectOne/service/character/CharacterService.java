package com.mapleApiTest.projectOne.service.character;

//import com.mapleApiTest.projectOne.domain.character.Characters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapleApiTest.projectOne.dto.character.request.CharacterCreateRequest;
//import com.mapleApiTest.projectOne.repository.character.CharacterRepository;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.Collectors;

@Service
public class CharacterService {


    public CharacterInfo getCharacterInfo(GetChracterInfo request, String Url, String apiKey) {
        try {
            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("ocid", "626b24506ebda9358222b53d5f80701cefe8d04e6d233bd35cf2fabdeb93fb0d").queryParam("date", request.getDate()).build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", apiKey);

//            ResponseEntity<CharacterInfo> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), CharacterInfo.class);

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
//            return responseEntity;

            System.out.println("======여기3");
            System.out.println(responseEntity.getBody());

//
//            ObjectMapper objectMapper = new ObjectMapper();
//                CharacterInfo characterInfo = objectMapper.readValue(responseEntity.getBody(), CharacterInfo.class);

            ObjectMapper objectMapper = new ObjectMapper();
            CharacterInfo characterInfo = objectMapper.readValue(responseEntity.getBody(), CharacterInfo.class);


            // 매핑된 객체 사용
//                System.out.println("Name: " + characterInfo.getCharacter_name());
//                System.out.println("Level: " + characterInfo.getChracter_level());
            System.out.println("======여기4");

            return null;
//            CharacterInfo characterInfo = responseEntity.getBody();
//            System.out.println("======여기");
//            return characterInfo;
        } catch (Exception exception) {
            System.out.println("======여기x");

            return null;
        }
    }


    public String getCharacterOcid(GetChracterOcid request, String Url, String apiKey) {
        try {
            String fullUrl = UriComponentsBuilder.fromUriString(Url).queryParam("character_name", request.getName()).build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", apiKey);

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return responseEntity.toString();
        } catch (Exception exception) {
            return null;
    }

}}




