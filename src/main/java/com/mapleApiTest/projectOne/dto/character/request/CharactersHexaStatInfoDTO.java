package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersHexaStatInfoDTO {

    String charactersName;
    int hexaStatMainStat ;
    int hexaStatAtMgPower ;
    Double hexaStatCriticalDamage ;
    Double hexaStatDamage ;
    Double hexaStatBossDamage ;

    public CharactersHexaStatInfoDTO() {
    }

    public CharactersHexaStatInfoDTO(String charactersName, int hexaStatMainStat,int hexaStatAtMgPower, Double hexaStatCriticalDamage, Double hexaStatDamage, Double hexaStatBossDamage) {
        this.charactersName = charactersName;
        this.hexaStatMainStat = hexaStatMainStat;
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

    public int getHexaStatMainStat() {
        return hexaStatMainStat;
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
