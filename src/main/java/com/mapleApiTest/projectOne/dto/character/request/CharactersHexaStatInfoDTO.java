package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersHexaStatInfoDTO {

    String charactersName;
    int hexaStatStr ;
    int hexaStatDex ;
    int hexaStatInt ;
    int hexaStatLuk ;
    int hexaStatAtMgPower ;
    Double hexaStatCriticalDamage ;
    Double hexaStatDamage ;
    Double hexaStatBossDamage ;

    public CharactersHexaStatInfoDTO() {
    }

    public CharactersHexaStatInfoDTO(String charactersName, int hexaStatStr, int hexaStatDex, int hexaStatInt, int hexaStatLuk, int hexaStatAtMgPower, Double hexaStatCriticalDamage, Double hexaStatDamage, Double hexaStatBossDamage) {
        this.charactersName = charactersName;
        this.hexaStatStr = hexaStatStr;
        this.hexaStatDex = hexaStatDex;
        this.hexaStatInt = hexaStatInt;
        this.hexaStatLuk = hexaStatLuk;
        this.hexaStatAtMgPower = hexaStatAtMgPower;
        this.hexaStatCriticalDamage = hexaStatCriticalDamage;
        this.hexaStatDamage = hexaStatDamage;
        this.hexaStatBossDamage = hexaStatBossDamage;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getHexaStatStr() {
        return hexaStatStr;
    }

    public void setHexaStatStr(int hexaStatStr) {
        this.hexaStatStr = hexaStatStr;
    }

    public int getHexaStatDex() {
        return hexaStatDex;
    }

    public void setHexaStatDex(int hexaStatDex) {
        this.hexaStatDex = hexaStatDex;
    }

    public int getHexaStatInt() {
        return hexaStatInt;
    }

    public void setHexaStatInt(int hexaStatInt) {
        this.hexaStatInt = hexaStatInt;
    }

    public int getHexaStatLuk() {
        return hexaStatLuk;
    }

    public void setHexaStatLuk(int hexaStatLuk) {
        this.hexaStatLuk = hexaStatLuk;
    }

    public int getHexaStatAtMgPower() {
        return hexaStatAtMgPower;
    }

    public void setHexaStatAtMgPower(int hexaStatAtMgPower) {
        this.hexaStatAtMgPower = hexaStatAtMgPower;
    }

    public Double getHexaStatCriticalDamage() {
        return hexaStatCriticalDamage;
    }

    public void setHexaStatCriticalDamage(Double hexaStatCriticalDamage) {
        this.hexaStatCriticalDamage = hexaStatCriticalDamage;
    }

    public Double getHexaStatDamage() {
        return hexaStatDamage;
    }

    public void setHexaStatDamage(Double hexaStatDamage) {
        this.hexaStatDamage = hexaStatDamage;
    }

    public Double getHexaStatBossDamage() {
        return hexaStatBossDamage;
    }

    public void setHexaStatBossDamage(Double hexaStatBossDamage) {
        this.hexaStatBossDamage = hexaStatBossDamage;
    }
}
