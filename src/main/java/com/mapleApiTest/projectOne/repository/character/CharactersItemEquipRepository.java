package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.CharactersItemEquip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharactersItemEquipRepository extends JpaRepository<CharactersItemEquip, Long> {

    Optional<CharactersItemEquip> findByCharactersNameAndDate(String charactersName, String date);
}
