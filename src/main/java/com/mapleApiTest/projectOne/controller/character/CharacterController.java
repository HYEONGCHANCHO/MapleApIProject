package com.mapleApiTest.projectOne.controller.character;

import com.mapleApiTest.projectOne.dto.character.request.CharacterCreateRequest;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetChracterOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CharacterController {

    @Value("${external.api.key}")
    private String apiKey;

    @Value("${external.api.url}")
    private String apiUrl;

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    //    @GetMapping("/maplestory/v1/id")
//    public String getOcid(@RequestParam String characterName) {
//    return characterName;
//    }
    @GetMapping("/maplestory/v1/id")
    public ResponseEntity<String> getOcid(@RequestParam String characterName) {
        GetChracterOcid getChracterOcid = new GetChracterOcid(characterName);
        String Url = apiUrl + "/maplestory/v1/id";
        return ResponseEntity.ok(characterService.getCharacterOcid(getChracterOcid,Url,apiKey));
//        try {
//            String apiUrl = "https://open.api.nexon.com/maplestory/v1/id";
//
//            String fullUrl = UriComponentsBuilder.fromUriString(apiUrl).queryParam("character_name", characterName).build().toUriString();
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("x-nxopen-api-key", apiKey);
//
//            ResponseEntity<String> responseEntity = new RestTemplate().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
//            return responseEntity;
//        } catch (Exception exception) {
//            return ResponseEntity.status(500).body("에러 발생: " + exception.getMessage());
//        }
    }

    @GetMapping("/maplestory/v1/character/basic")
    public ResponseEntity<CharacterInfo> getCharacterInfo(@RequestParam String name, String date ){
        GetChracterInfo getChracterInfo = new GetChracterInfo(name,date);
        String Url = apiUrl + "/maplestory/v1/character/basic";


        return ResponseEntity.ok(characterService.getCharacterInfo(getChracterInfo,Url,apiKey));

    }


//    private final CharacterService characterService;
//
//    public CharacterController(CharacterService characterService) {
//        this.characterService = characterService;
//    }

//    @PostMapping("/characters")
//    public void saveCharacter(@RequestBody CharacterCreateRequest request) {
//        characterService.saveCharacter(request);
//    }
//
//    @GetMapping("https://open.api.nexon.com/maplestory/v1/id")
//    public String getCharacterOcid(@RequestBody CharacterCreateRequest request){
//        return;
//    }

}
