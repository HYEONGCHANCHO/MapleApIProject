package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersPetEquipInfoDTO {

    String charactersName;
    int petAt =0;
    int petMg =0;

    public CharactersPetEquipInfoDTO() {
    }

    public CharactersPetEquipInfoDTO(String charactersName, int petAt, int petMg) {
        this.charactersName = charactersName;
        this.petAt = petAt;
        this.petMg = petMg;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getPetAt() {
        return petAt;
    }

    public void setPetAt(int petAt) {
        this.petAt = petAt;
    }

    public int getPetMg() {
        return petMg;
    }

    public void setPetMg(int petMg) {
        this.petMg = petMg;
    }
}
