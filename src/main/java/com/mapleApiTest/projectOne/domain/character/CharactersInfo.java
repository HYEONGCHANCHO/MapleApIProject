package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String charactersLevel;
    @Column(nullable = false)
    private String charactersServer;

    @Column(nullable = false)
    private String character_class;

    @Column(nullable = false)
    private String character_image;

    @Column(nullable = false)
    private String world_name;

    public Long getId() {
        return id;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getCharactersLevel() {
        return charactersLevel;
    }

    public String getCharactersServer() {
        return charactersServer;
    }

    public String getCharacter_image() {
        return character_image;
    }

    public String getCharacter_class() {
        return character_class;
    }

    public String getWorld_name() {
        return world_name;
    }

    public CharactersInfo(String charactersName, String date, String charactersLevel, String charactersServer, String character_class, String character_image, String world_name) {
        this.charactersName = charactersName;
        this.date = date;
        this.charactersLevel = charactersLevel;
        this.charactersServer = charactersServer;
        this.character_class = character_class;
        this.character_image = character_image;
        this.world_name = world_name;
    }


}
