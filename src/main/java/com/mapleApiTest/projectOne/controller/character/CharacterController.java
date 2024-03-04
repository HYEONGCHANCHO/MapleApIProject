package com.mapleApiTest.projectOne.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

import com.mapleApiTest.projectOne.dto.character.request.GetCharactersInfo;
import com.mapleApiTest.projectOne.dto.character.request.GetCharactersOcid;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.character.response.CharactersInfoDTO;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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


    @GetMapping("/maplestory/v1/id")
    public CompletableFuture<String> getCharacterOcid(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
//        String Url = request.getRequestURI();
//        String Url = "/maplestory/v1/id";
//        System.out.println(Url);
            return characterService.getCharacterOcid(getCharactersInfo);

    }


    /////////////////////////////////

    @GetMapping("/maplestory/v1/character/basic")
    public CompletableFuture<Object> getCharacterInfo(HttpServletRequest request, @RequestParam String charactersName, String date) {
//        CompletableFuture<ResponseEntity<CharacterInfo>> resultFuture = new CompletableFuture<>();
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);

            CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);

        String ocid = CompletableFutureOcid.join();


//        ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
//                String Url = apiUrl + "/maplestory/v1/character/basic";
                String Url = request.getRequestURI();

//                resultFuture.complete(ResponseEntity.ok(
                return characterService.getCharactersInfo(getCharactersInfo, Url, apiKey, ocid);

//                );
//            } catch (Exception exception) {
//                System.err.println("에러: " + exception.getMessage());
//                return null;
//            }

//        return resultFuture;

}
}
