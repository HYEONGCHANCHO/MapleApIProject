package com.mapleApiTest.projectOne.dto.item;


public class CharactersItemTotalStatInfoDTO {

    int mainStat;
    int subStat;
    int mainStatPer;
    int subStatPer;
    int atMgStat;
    int atMgStatPer;
    int bossDamage;
    int damage;
    int criticalDamage;


    public CharactersItemTotalStatInfoDTO() {
    }

    public CharactersItemTotalStatInfoDTO(int mainStat, int subStat, int mainStatPer, int subStatPer, int atMgStat, int atMgStatPer, int bossDamage, int damage, int criticalDamage) {
        this.mainStat = mainStat;
        this.subStat = subStat;
        this.mainStatPer = mainStatPer;
        this.subStatPer = subStatPer;
        this.atMgStat = atMgStat;
        this.atMgStatPer = atMgStatPer;
        this.bossDamage = bossDamage;
        this.damage = damage;
        this.criticalDamage = criticalDamage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMainStat() {
        return mainStat;
    }

    public void setMainStat(int mainStat) {
        this.mainStat = mainStat;
    }

    public int getSubStat() {
        return subStat;
    }

    public void setSubStat(int subStat) {
        this.subStat = subStat;
    }

    public int getMainStatPer() {
        return mainStatPer;
    }

    public void setMainStatPer(int mainStatPer) {
        this.mainStatPer = mainStatPer;
    }

    public int getSubStatPer() {
        return subStatPer;
    }

    public void setSubStatPer(int subStatPer) {
        this.subStatPer = subStatPer;
    }

    public int getAtMgStat() {
        return atMgStat;
    }

    public void setAtMgStat(int atMgStat) {
        this.atMgStat = atMgStat;
    }

    public int getAtMgStatPer() {
        return atMgStatPer;
    }

    public void setAtMgStatPer(int atMgStatPer) {
        this.atMgStatPer = atMgStatPer;
    }

    public int getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(int bossDamage) {
        this.bossDamage = bossDamage;
    }

    public int getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(int criticalDamage) {
        this.criticalDamage = criticalDamage;
    }
}
