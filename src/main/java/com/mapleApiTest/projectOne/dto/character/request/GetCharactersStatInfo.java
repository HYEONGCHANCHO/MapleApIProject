package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCharactersStatInfo {

    private String charactersName;

    private String date;

    private String damegeSum;
    private String str;

    private String dex;
    @JsonProperty("int")
    private String intel;

    private String luk;
    private String hp;

    private String attackPower;
    private String magicPower;

    private String combatPower;




}
