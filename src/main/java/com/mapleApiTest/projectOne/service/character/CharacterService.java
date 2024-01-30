package com.mapleApiTest.projectOne.service.character;

import com.mapleApiTest.projectOne.domain.character.Characters;
import com.mapleApiTest.projectOne.dto.character.request.CharacterCreateRequest;
import com.mapleApiTest.projectOne.repository.character.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Transactional
    public void saveCharacter(CharacterCreateRequest request) {
        characterRepository.save(new Characters(request.getName()));
    }

//    @Transactional
//    public List<CharacterResponse> getCharacter(){
//        return characterRepository.findAll().stream().map(CharacterResponse::new).collect(Collectors.toList());
//    }


}
