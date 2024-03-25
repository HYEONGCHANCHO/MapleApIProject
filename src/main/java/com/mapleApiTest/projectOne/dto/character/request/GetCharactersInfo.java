package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class GetCharactersInfo {


    @JsonProperty("character_name")
    private String charactersName;

    private String date;

    @JsonProperty("character_class")
    private String charactersClass;

    @JsonProperty("character_level")
    private int charactersLevel;

    private String worldName;

    @JsonProperty("character_image")
    private String charactersImage;

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getCharactersClass() {
        return charactersClass;
    }

    public int getCharactersLevel() {
        return charactersLevel;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getCharactersImage() {
        return charactersImage;
    }

    public GetCharactersInfo(String charactersName, String date) {
        this.charactersName = charactersName;
        this.date = date;
    }

    public GetCharactersInfo(String charactersName) {
        this.charactersName = charactersName;
    }
}
