package com.mapleApiTest.projectOne.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

import com.mapleApiTest.projectOne.dto.character.request.GetCharactersOcid;
import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
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

//    private final RateLimiter rateLimiter = RateLimiter.create(5.0);

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }


    @GetMapping("/maplestory/v1/id")
    public CompletableFuture<String> getCharacterOcid(HttpServletRequest request, @RequestParam String charactersName) {
        GetCharactersOcid getCharactersOcid = new GetCharactersOcid(charactersName);
        String Url = request.getRequestURI();
        System.out.println(Url);
            return characterService.getCharacterOcid(getCharactersOcid,Url);

    }

//    @GetMapping("/maplestory/v1/")
//    public Mono<String> getCharacterOcid2(HttpServletRequest request, @RequestParam String charactersName) {
//        GetCharactersOcid getCharactersOcid = new GetCharactersOcid(charactersName);
//        String Url = request.getRequestURI()+"id";
//        return characterService.getCharacterOcid2(getCharactersOcid,Url);
//        }










    /////////////////////////////////

//    @GetMapping("/maplestory/v1/character/basic")
//    @Async("characterThreadPool")
//
//    public CompletableFuture<ResponseEntity<CharacterInfo>> getCharacterInfo(@RequestParam String ocid, String date) {
//        CompletableFuture<ResponseEntity<CharacterInfo>> resultFuture = new CompletableFuture<>();
//
//        if (rateLimiter.tryAcquire()) {
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                GetChracterInfo getChracterInfo = new GetChracterInfo(date, ocid);
//                String Url = apiUrl + "/maplestory/v1/character/basic";
//
//                System.out.println("여기여기222");
//
//
//                resultFuture.complete(ResponseEntity.ok(characterService.getCharacterInfo(getChracterInfo, Url, apiKey, ocid)));
//            } catch (Exception exception) {
//                System.err.println("에러: " + exception.getMessage());
//                return null;
//            }
//        }else {
//                System.err.println("호출 제한 초과");
//            resultFuture.complete(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build());
//
//        }
//        System.out.println("resultFuture ::"+ resultFuture);
//
//        return resultFuture;
//
//
//}
}
