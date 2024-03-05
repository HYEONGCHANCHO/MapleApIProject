package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersItemEquipDTO {

    private String charactersName;

    private String date;
    private String item_equipment;

    public CharactersItemEquipDTO(String charactersName, String date, String item_equipment) {
        this.charactersName = charactersName;
        this.date = date;
        this.item_equipment = item_equipment;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getItem_equipment() {
        return item_equipment;
    }



}
