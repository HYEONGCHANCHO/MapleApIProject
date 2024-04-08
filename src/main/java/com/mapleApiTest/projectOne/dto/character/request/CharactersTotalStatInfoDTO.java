package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersTotalStatInfoDTO {

    private String charactersName;
    private int mainStat;
    private int mainStatPer;
    private int mainNonStat;
    private int subStat ;
    private int subStatPer ;
    private int  subNonStat ;
    private int atMgPower;
    private int  atMgPowerPer ;
    private Double  damage ;
    private Double bossDamage;
    private Double criticalDamage ;
    private boolean isFree;
    private int apiCombat;

    public CharactersTotalStatInfoDTO(String charactersName, int mainStat, int mainStatPer, int mainNonStat, int subStat, int subStatPer, int subNonStat, int atMgPower, int atMgPowerPer, Double damage, Double bossDamage, Double criticalDamage, boolean isFree, int apiCombat) {
        this.charactersName = charactersName;
        this.mainStat = mainStat;
        this.mainStatPer = mainStatPer;
        this.mainNonStat = mainNonStat;
        this.subStat = subStat;
        this.subStatPer = subStatPer;
        this.subNonStat = subNonStat;
        this.atMgPower = atMgPower;
        this.atMgPowerPer = atMgPowerPer;
        this.damage = damage;
        this.bossDamage = bossDamage;
        this.criticalDamage = criticalDamage;
        this.isFree = isFree;
        this.apiCombat = apiCombat;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getMainStat() {
        return mainStat;
    }

    public void setMainStat(int mainStat) {
        this.mainStat = mainStat;
    }

    public int getMainStatPer() {
        return mainStatPer;
    }

    public void setMainStatPer(int mainStatPer) {
        this.mainStatPer = mainStatPer;
    }

    public int getMainNonStat() {
        return mainNonStat;
    }

    public void setMainNonStat(int mainNonStat) {
        this.mainNonStat = mainNonStat;
    }

    public int getSubStat() {
        return subStat;
    }

    public void setSubStat(int subStat) {
        this.subStat = subStat;
    }

    public int getSubStatPer() {
        return subStatPer;
    }

    public void setSubStatPer(int subStatPer) {
        this.subStatPer = subStatPer;
    }

    public int getSubNonStat() {
        return subNonStat;
    }

    public void setSubNonStat(int subNonStat) {
        this.subNonStat = subNonStat;
    }

    public int getAtMgPower() {
        return atMgPower;
    }

    public void setAtMgPower(int atMgPower) {
        this.atMgPower = atMgPower;
    }

    public int getAtMgPowerPer() {
        return atMgPowerPer;
    }

    public void setAtMgPowerPer(int atMgPowerPer) {
        this.atMgPowerPer = atMgPowerPer;
    }

    public Double getDamage() {
        return damage;
    }

    public void setDamage(Double damage) {
        this.damage = damage;
    }

    public Double getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(Double bossDamage) {
        this.bossDamage = bossDamage;
    }

    public Double getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(Double criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getApiCombat() {
        return apiCombat;
    }

    public void setApiCombat(int apiCombat) {
        this.apiCombat = apiCombat;
    }
}
