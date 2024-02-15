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

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

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


//
//    @GetMapping("/maplestory/v1/id")
//    public Mono<String> yourApiEndpoint(@RequestParam String charactersName) {
//        return characterService.getApiResponse(charactersName);
//    }

//
//    @GetMapping("/maplestory/v1/id")
//    public Mono<ResponseEntity<String>> getCharacterOcid(HttpServletRequest request, @RequestParam String charactersName) {
//
//        if (rateLimiter.tryAcquire()) {
//            GetCharactersOcid getCharactersOcid = new GetCharactersOcid(charactersName);
//            String url = request.getRequestURI();
//            System.out.println("여기여기");
//            System.out.println(url);
//
//            return characterService.getApiResponse(getCharactersOcid.getName())
//                    .map(ResponseEntity::ok);
//
////
////                    return characterService.getCharacterOcid(getCharactersOcid,url)
////                    .map(ResponseEntity::ok);
//            // 비동기적으로 getCharacterOcidAsync 메소드 호출
////           String resultMono = characterService.getCharacterOcid(getCharactersOcid, url);
////
////            System.out.println(url+"ㅇㄴㅇ"+resultMono);
////
////            // 작업이 완료될 때까지 대기하고 결과를 ResponseEntity로 감싸서 반환
////            return resultMono;
//        } else {
//            // 초당 호출 제한 초과 시 429 Too Many Requests 반환
//            return null;
//        }
//    }
//
//
//


//        @GetMapping("/maplestory/v1/id")
//    public Mono<String> yourApiEndpoint(@RequestParam String charactersName) {
////        return characterService.getApiResponse(charactersName);
//        return characterService.getCharacterOcid(charactersName);
//    }
    @GetMapping("/maplestory/v1/id")
    public Mono<String> getCharacterOcid(HttpServletRequest request, @RequestParam String charactersName) {

        if (rateLimiter.tryAcquire()) {
        GetCharactersOcid getCharactersOcid = new GetCharactersOcid(charactersName);
        String Url = request.getRequestURI();
        System.out.println("여기여기");
        System.out.println(Url);
            return characterService.getCharacterOcid(getCharactersOcid,Url);

//                    Mono<String> resultMono = characterService.getCharacterOcid(charactersName);


        // 비동기적으로 getCharacterOcidAsync 메소드 호출
//        Mono<String> resultMono = characterService.getCharacterOcid(getCharactersOcid, url);
        // 작업이 완료될 때까지 대기하고 결과를 ResponseEntity로 감싸서 반환
//        return resultMono;
        } else {
            // 초당 호출 제한 초과 시 429 Too Many Requests 반환
            return null;
        }
    }

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
