package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersAbilityInfoDTO{

    String charactersName;
    int abilityStr ;
    int abilityDex ;
    int abilityInt ;
    int abilityLuk ;
    int abilityStrPer = 0;
    int abilityDexPer = 0;
    int abilityIntPer = 0;
    int abilityLukPer = 0;
    int abilityAtMgPower ;
    Double abilityBossDamage ;


    public CharactersAbilityInfoDTO(String charactersName, int abilityStr, int abilityDex, int abilityInt, int abilityLuk, int abilityStrPer, int abilityDexPer, int abilityIntPer, int abilityLukPer, int abilityAtMgPower, Double abilityBossDamage) {
        this.charactersName = charactersName;
        this.abilityStr = abilityStr;
        this.abilityDex = abilityDex;
        this.abilityInt = abilityInt;
        this.abilityLuk = abilityLuk;
        this.abilityStrPer = abilityStrPer;
        this.abilityDexPer = abilityDexPer;
        this.abilityIntPer = abilityIntPer;
        this.abilityLukPer = abilityLukPer;
        this.abilityAtMgPower = abilityAtMgPower;
        this.abilityBossDamage = abilityBossDamage;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public int getAbilityStr() {
        return abilityStr;
    }

    public int getAbilityDex() {
        return abilityDex;
    }

    public int getAbilityInt() {
        return abilityInt;
    }

    public int getAbilityLuk() {
        return abilityLuk;
    }

    public int getAbilityStrPer() {
        return abilityStrPer;
    }

    public int getAbilityDexPer() {
        return abilityDexPer;
    }

    public int getAbilityIntPer() {
        return abilityIntPer;
    }

    public int getAbilityLukPer() {
        return abilityLukPer;
    }

    public int getAbilityAtMgPower() {
        return abilityAtMgPower;
    }

    public Double getAbilityBossDamage() {
        return abilityBossDamage;
    }
}
