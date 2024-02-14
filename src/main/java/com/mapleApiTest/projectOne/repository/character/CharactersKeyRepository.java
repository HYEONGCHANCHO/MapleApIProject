package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import com.mapleApiTest.projectOne.dto.character.response.CharactersResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharactersKeyRepository extends JpaRepository<CharactersKey, Long> {

    Optional<CharactersKey> findByCharactersName(String charactersName);
}
