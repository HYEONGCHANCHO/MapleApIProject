package com.mapleApiTest.projectOne.controller.character;

import com.mapleApiTest.projectOne.dto.character.request.CharacterCreateRequest;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacterController {
    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

//    @PostMapping("/characters")
//    public void saveCharacter(@RequestBody CharacterCreateRequest request) {
//        characterService.saveCharacter(request);
//    }
//
    @GetMapping("https://open.api.nexon.com/maplestory/v1/id")
    public String getCharacterOcid(@RequestBody CharacterCreateRequest request){
        return;
    }

}
