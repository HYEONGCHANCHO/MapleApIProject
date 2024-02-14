package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharactersInfoRepository extends JpaRepository<CharactersInfo, Long> {

    Optional<CharactersInfo> findByCharactersNameAndDate(String CharactersInfo, String date);
}
