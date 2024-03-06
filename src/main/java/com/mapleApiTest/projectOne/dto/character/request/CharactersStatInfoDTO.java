package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class CharactersStatInfoDTO {

    private String charactersName;

    private String date;
//    private String final_stat;

    private String damage;


    private String bossDamage;


    private String finalDamage;


    private String ignoreRate;


    private String criticalDamage;

    private String str;

    private String dex;

    private String intel;

    private String luk;
    private String hp;

    private String attackPower;
    private String magicPower;

    private String combatPower;

    public CharactersStatInfoDTO(String charactersName, String date,String damage, String bossDamage, String finalDamage, String ignoreRate, String criticalDamage, String str, String dex, String intel, String luk, String hp, String attackPower, String magicPower, String combatPower) {
        this.charactersName = charactersName;
        this.date = date;
//        this.final_stat = final_stat;
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
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.combatPower = combatPower;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

//    public String getFinal_stat() {
//        return final_stat;
//    }

    public String getDamage() {
        return damage;
    }

    public String getBossDamage() {
        return bossDamage;
    }

    public String getFinalDamage() {
        return finalDamage;
    }

    public String getIgnoreRate() {
        return ignoreRate;
    }

    public String getCriticalDamage() {
        return criticalDamage;
    }

    public String getStr() {
        return str;
    }

    public String getDex() {
        return dex;
    }

    public String getIntel() {
        return intel;
    }

    public String getLuk() {
        return luk;
    }

    public String getHp() {
        return hp;
    }

    public String getAttackPower() {
        return attackPower;
    }

    public String getMagicPower() {
        return magicPower;
    }

    public String getCombatPower() {
        return combatPower;
    }


    //    public String getFinal_stat() {
//        return final_stat;
//    }
    //    private String damegeSum;
//    private String str;
//
//    private String dex;
//    @JsonProperty("int")
//    private String intel;
//
//    private String luk;
//    private String hp;
//
//    private String attackPower;
//    private String magicPower;
//
//    private String combatPower;




}
