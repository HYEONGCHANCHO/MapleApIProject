package com.mapleApiTest.projectOne.dto.character.request;


import java.util.Arrays;

public class CharactersArtiInfoDTO {

    String charactersName;
    int artiAllStat ;
    int artiAtMgPower ;
    Double artiDamage ;
    Double artiBossDamage ;
    Double artiCriticalDamage ;


    public CharactersArtiInfoDTO(String charactersName, int artiAllStat, int artiAtMgPower, Double artiDamage, Double artiBossDamage, Double artiCriticalDamage) {
        this.charactersName = charactersName;
        this.artiAllStat = artiAllStat;
        this.artiAtMgPower = artiAtMgPower;
        this.artiDamage = artiDamage;
        this.artiBossDamage = artiBossDamage;
        this.artiCriticalDamage = artiCriticalDamage;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getArtiAllStat() {
        return artiAllStat;
    }

    public void setArtiAllStat(int artiAllStat) {
        this.artiAllStat = artiAllStat;
    }

    public int getArtiAtMgPower() {
        return artiAtMgPower;
    }

    public void setArtiAtMgPower(int artiAtMgPower) {
        this.artiAtMgPower = artiAtMgPower;
    }

    public Double getArtiDamage() {
        return artiDamage;
    }

    public void setArtiDamage(Double artiDamage) {
        this.artiDamage = artiDamage;
    }

    public Double getArtiBossDamage() {
        return artiBossDamage;
    }

    public void setArtiBossDamage(Double artiBossDamage) {
        this.artiBossDamage = artiBossDamage;
    }

    public Double getArtiCriticalDamage() {
        return artiCriticalDamage;
    }

    public void setArtiCriticalDamage(Double artiCriticalDamage) {
        this.artiCriticalDamage = artiCriticalDamage;
    }
}
