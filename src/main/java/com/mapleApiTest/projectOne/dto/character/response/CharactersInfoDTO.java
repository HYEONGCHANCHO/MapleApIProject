package com.mapleApiTest.projectOne.dto.character.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.stream.events.Characters;

public class CharactersInfoDTO {

    private String date;
    private String character_name;
    private String world_name;
    private String character_class;
    private String character_server;
    private String character_level;
    private String character_image;

//    private String ocid;
    public CharactersInfoDTO() {
    }

    public String getDate() {
        return date;
    }

    public String getCharacter_name() {
        return character_name;
    }

    public String getWorld_name() {
        return world_name;
    }

    public String getCharacter_class() {
        return character_class;
    }

    public String getCharacter_level() {
        return character_level;
    }

    public String getCharacter_image() {
        return character_image;
    }

    public String getCharactersServer() {
        return character_server;
    }

    public CharactersInfoDTO(String date, String character_name, String world_name, String character_class, String character_server, String character_level, String character_image) {
        this.date = date;
        this.character_name = character_name;
        this.world_name = world_name;
        this.character_class = character_class;
        this.character_server = character_server;
        this.character_level = character_level;
        this.character_image = character_image;
    }


    //    public String getOcid() {
//        return ocid;
//    }
//
//    public void setOcid(String ocid) {
//        this.ocid = ocid;
    }





