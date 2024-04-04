package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersHyperStatInfoDTO {

    String charactersName;
    int HyperStatStr ;
    int HyperStatDex ;
    int HyperStatInt ;
    int HyperStatLuk ;
    int HyperStatAtMgPower ;
    Double HyperStatCriticalDamage ;
    Double HyperStatDamage ;
    Double HyperStatBossDamage ;

    public CharactersHyperStatInfoDTO() {
    }


    public CharactersHyperStatInfoDTO(String charactersName, int hyperStatStr, int hyperStatDex, int hyperStatInt, int hyperStatLuk, int hyperStatAtMgPower, Double hyperStatCriticalDamage, Double hyperStatDamage, Double hyperStatBossDamage) {
        this.charactersName = charactersName;
        HyperStatStr = hyperStatStr;
        HyperStatDex = hyperStatDex;
        HyperStatInt = hyperStatInt;
        HyperStatLuk = hyperStatLuk;
        HyperStatAtMgPower = hyperStatAtMgPower;
        HyperStatCriticalDamage = hyperStatCriticalDamage;
        HyperStatDamage = hyperStatDamage;
        HyperStatBossDamage = hyperStatBossDamage;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getHyperStatStr() {
        return HyperStatStr;
    }

    public void setHyperStatStr(int hyperStatStr) {
        HyperStatStr = hyperStatStr;
    }

    public int getHyperStatDex() {
        return HyperStatDex;
    }

    public void setHyperStatDex(int hyperStatDex) {
        HyperStatDex = hyperStatDex;
    }

    public int getHyperStatInt() {
        return HyperStatInt;
    }

    public void setHyperStatInt(int hyperStatInt) {
        HyperStatInt = hyperStatInt;
    }

    public int getHyperStatLuk() {
        return HyperStatLuk;
    }

    public void setHyperStatLuk(int hyperStatLuk) {
        HyperStatLuk = hyperStatLuk;
    }

    public int getHyperStatAtMgPower() {
        return HyperStatAtMgPower;
    }

    public void setHyperStatAtMgPower(int hyperStatAtMgPower) {
        HyperStatAtMgPower = hyperStatAtMgPower;
    }

    public Double getHyperStatCriticalDamage() {
        return HyperStatCriticalDamage;
    }

    public void setHyperStatCriticalDamage(Double hyperStatCriticalDamage) {
        HyperStatCriticalDamage = hyperStatCriticalDamage;
    }

    public Double getHyperStatDamage() {
        return HyperStatDamage;
    }

    public void setHyperStatDamage(Double hyperStatDamage) {
        HyperStatDamage = hyperStatDamage;
    }

    public Double getHyperStatBossDamage() {
        return HyperStatBossDamage;
    }

    public void setHyperStatBossDamage(Double hyperStatBossDamage) {
        HyperStatBossDamage = hyperStatBossDamage;
    }
}
