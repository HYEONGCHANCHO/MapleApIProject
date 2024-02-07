package com.mapleApiTest.projectOne.dto.character.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterInfo {

    private String date;
    private String character_name;
    private String world_name;
    private String character_class;
    private int character_level;
    private String character_image;

    private String ocid;
    public CharacterInfo() {
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

    public int getCharacter_level() {
        return character_level;
    }

    public String getCharacter_image() {
        return character_image;
    }

    public String getOcid() {
        return ocid;
    }

    public void setOcid(String ocid) {
        this.ocid = ocid;
    }
}




