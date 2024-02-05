package com.mapleApiTest.projectOne.controller.character;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacterController {

    @Value("${external.api.key}")
    private String apiKey;

    @Value("${external.api.url}")
    private String apiUrl;

    private final RateLimiter rateLimiter = RateLimiter.create(5.0);



    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/maplestory/v1/id")
    public ResponseEntity<String> getOcid(@RequestParam String characterName) {

        GetChracterOcid getChracterOcid = new GetChracterOcid(characterName);
        String Url = apiUrl + "/maplestory/v1/id";
        return ResponseEntity.ok(characterService.getCharacterOcid(getChracterOcid, Url, apiKey));

    }

    @GetMapping("/maplestory/v1/character/basic")
    public ResponseEntity<CharacterInfo> getCharacterInfo(@RequestParam String name, String date) {
        if (rateLimiter.tryAcquire()) {

            ResponseEntity<String> responseEntity = getOcid(name);
            String responseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String ocid = jsonNode.get("ocid").asText();
                GetChracterInfo getChracterInfo = new GetChracterInfo(name, date, ocid);
                String Url = apiUrl + "/maplestory/v1/character/basic";
                return ResponseEntity.ok(characterService.getCharacterInfo(getChracterInfo, Url, apiKey, ocid));
            } catch (Exception exception) {
                System.err.println("에러: " + exception.getMessage());
                return null;
            }
        }else {
                System.err.println("호출 제한 초과");
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}}
