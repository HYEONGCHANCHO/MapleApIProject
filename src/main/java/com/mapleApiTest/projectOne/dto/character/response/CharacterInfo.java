package com.mapleApiTest.projectOne.dto.character.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterInfo {
    private String date;
    private String character_name;
    private String world_name;
    private String character_gender;
    private String character_class;
    private String character_class_level;
    private int character_level;
    private long character_exp;
    private String character_exp_rate;
    private String character_guild_name;
    private String character_image;

    public CharacterInfo(String date, String character_name, String world_name, String character_gender, String character_class, String character_class_level, int character_level, long character_exp, String character_exp_rate, String character_guild_name, String character_image) {
        this.date = date;
        this.character_name = character_name;
        this.world_name = world_name;
        this.character_gender = character_gender;
        this.character_class = character_class;
        this.character_class_level = character_class_level;
        this.character_level = character_level;
        this.character_exp = character_exp;
        this.character_exp_rate = character_exp_rate;
        this.character_guild_name = character_guild_name;
        this.character_image = character_image;
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

    public String getCharacter_gender() {
        return character_gender;
    }

    public String getCharacter_class() {
        return character_class;
    }

    public String getCharacter_class_level() {
        return character_class_level;
    }

    public int getCharacter_level() {
        return character_level;
    }

    public long getCharacter_exp() {
        return character_exp;
    }

    public String getCharacter_exp_rate() {
        return character_exp_rate;
    }

    public String getCharacter_guild_name() {
        return character_guild_name;
    }

    public String getCharacter_image() {
        return character_image;
    }
    //    @JsonProperty("date")
//    private String date;
//
//    public CharacterInfo(String date) {
//        this.date = date;
//    }
//
//    public String getDate() {
//        return date;
//    }

    //    private String character_name;
//    private String worlde_name;
//    private String character_class;
//    private String chracter_level;
//    private String chracter_image;

//    public CharacterInfo(String date, String character_name, String worlde_name, String character_class, String chracter_level, String chracter_image) {
//        this.date = date;
//        this.character_name = character_name;
//        this.worlde_name = worlde_name;
//        this.character_class = character_class;
//        this.chracter_level = chracter_level;
//        this.chracter_image = chracter_image;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public String getCharacter_name() {
//        return character_name;
//    }
//
//    public String getWorlde_name() {
//        return worlde_name;
//    }
//
//    public String getCharacter_class() {
//        return character_class;
//    }
//
//    public String getChracter_level() {
//        return chracter_level;
//    }
//
//    public String getChracter_image() {
//        return chracter_image;
//    }
}




