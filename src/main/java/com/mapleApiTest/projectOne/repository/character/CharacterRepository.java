package com.mapleApiTest.projectOne.repository.character;

import com.mapleApiTest.projectOne.domain.character.Characters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Characters, Long> {

    Optional<Character> findByName(String name);

}
