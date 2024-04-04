package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class CharactersStatInfoDTO {

    private String charactersName;

//    private String date;

    private double damage;


    private double bossDamage;


    private double finalDamage;


    private double ignoreRate;


    private double criticalDamage;

    private int str;

    private int dex;

    private int intel;

    private int luk;
    private int hp;

    private int apStr;
    private int apDex;
    private int apInt;
    private int apLuk;

    private int attackPower;
    private int magicPower;

    private int combatPower;

    public String getCharactersName() {
        return charactersName;
    }

//    public String getDate() {
//        return date;
//    }

    public double getDamage() {
        return damage;
    }

    public double getBossDamage() {
        return bossDamage;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getIgnoreRate() {
        return ignoreRate;
    }

    public double getCriticalDamage() {
        return criticalDamage;
    }

    public int getStr() {
        return str;
    }

    public int getDex() {
        return dex;
    }

    public int getIntel() {
        return intel;
    }

    public int getLuk() {
        return luk;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getCombatPower() {
        return combatPower;
    }

    public int getApStr() {
        return apStr;
    }

    public int getApDex() {
        return apDex;
    }

    public int getApInt() {
        return apInt;
    }

    public int getApLuk() {
        return apLuk;
    }

    public CharactersStatInfoDTO(String charactersName, double damage, double bossDamage, double finalDamage, double ignoreRate, double criticalDamage, int str, int dex, int intel, int luk, int hp, int apStr, int apDex, int apInt, int apLuk, int attackPower, int magicPower, int combatPower) {
        this.charactersName = charactersName;
//        this.date = date;
        this.damage = damage;
        this.bossDamage = bossDamage;
        this.finalDamage = finalDamage;
        this.ignoreRate = ignoreRate;
        this.criticalDamage = criticalDamage;
        this.str = str;
        this.dex = dex;
        this.intel = intel;
        this.luk = luk;
        this.hp = hp;
        this.apStr = apStr;
        this.apDex = apDex;
        this.apInt = apInt;
        this.apLuk = apLuk;
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.combatPower = combatPower;
    }
}
