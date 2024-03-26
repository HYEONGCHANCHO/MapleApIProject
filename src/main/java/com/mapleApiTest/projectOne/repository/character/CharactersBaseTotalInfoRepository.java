package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.CharactersBaseTotalInfo;
import com.mapleApiTest.projectOne.domain.character.CharactersStatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharactersBaseTotalInfoRepository extends JpaRepository<CharactersBaseTotalInfo, Long> {

    Optional<CharactersBaseTotalInfo> findByCharactersName(String charactersName);

    void deleteByCharactersName(String charactersName);
}
