package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

//    @Column(nullable = false)
//    private String date;

    @Column(nullable = false)
    private int character_level;
//    @Column(nullable = false)
//    private String character_server;

    @Column(nullable = false)
    private String character_class;

//    @Column(nullable = false)
//    private String character_image;

    @Column(nullable = false)
    private String world_name;

    public Long getId() {
        return id;
    }

    public String getCharactersName() {
        return charactersName;
    }

//    public String getDate() {
//        return date;
//    }

    public int getCharactersLevel() {
        return character_level;
    }




//    public String getCharacter_image() {
//        return character_image;
//    }

    public String getCharacter_class() {
        return character_class;
    }

    public String getWorld_name() {
        return world_name;
    }

    protected CharactersInfo() {
    }

    public CharactersInfo(String charactersName, int character_level, String character_class, String world_name) {
        this.charactersName = charactersName;
//        this.date = date;
        this.character_level = character_level;
        this.character_class = character_class;
//        this.character_image = character_image;
        this.world_name = world_name;
    }


}
