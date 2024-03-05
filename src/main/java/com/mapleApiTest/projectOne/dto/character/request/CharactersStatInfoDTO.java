package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersStatInfoDTO {

    private String charactersName;

    private String date;
    private String final_stat;

    public CharactersStatInfoDTO(String charactersName, String date, String final_stat) {
        this.charactersName = charactersName;
        this.date = date;
        this.final_stat = final_stat;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getFinal_stat() {
        return final_stat;
    }
    //    private String damegeSum;
//    private String str;
//
//    private String dex;
//    @JsonProperty("int")
//    private String intel;
//
//    private String luk;
//    private String hp;
//
//    private String attackPower;
//    private String magicPower;
//
//    private String combatPower;




}
