package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.CharactersInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersStatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharactersStatInfoRepository extends JpaRepository<CharactersStatInfo, Long> {

//    Optional<CharactersStatInfo> findByCharactersNameAndDate(String CharactersInfo);
}
