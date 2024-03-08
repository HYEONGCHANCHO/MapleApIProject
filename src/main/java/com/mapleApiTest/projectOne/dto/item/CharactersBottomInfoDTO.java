package com.mapleApiTest.projectOne.dto.item;


import com.fasterxml.jackson.databind.JsonNode;

public class CharactersBottomInfoDTO {

    String item_equipment_slot ;
    String itemName ;
    int str ;
    int dex ;
    int intel ;
    int luk ;
    int hp ;
    int attactPower ;
    int magicPower ;
    Double bossDamage;
    Double ignore ;
    int allStat ;
    String potentialOne ;
    String potentialTwo ;
    String potentialThree ;
    String additionalOne ;
    String additionalTwo ;
    String additionalThree ;
    JsonNode excetional ;
    String soul ;

    public CharactersBottomInfoDTO(String item_equipment_slot, String itemName, int str, int dex, int intel, int luk, int hp, int attactPower, int magicPower, Double bossDamage, Double ignore, int allStat, String potentialOne, String potentialTwo, String potentialThree, String additionalOne, String additionalTwo, String additionalThree, JsonNode excetional, String soul) {
        this.item_equipment_slot = item_equipment_slot;
        this.itemName = itemName;
        this.str = str;
        this.dex = dex;
        this.intel = intel;
        this.luk = luk;
        this.hp = hp;
        this.attactPower = attactPower;
        this.magicPower = magicPower;
        this.bossDamage = bossDamage;
        this.ignore = ignore;
        this.allStat = allStat;
        this.potentialOne = potentialOne;
        this.potentialTwo = potentialTwo;
        this.potentialThree = potentialThree;
        this.additionalOne = additionalOne;
        this.additionalTwo = additionalTwo;
        this.additionalThree = additionalThree;
        this.excetional = excetional;
        this.soul = soul;
    }

    public String getItem_equipment_slot() {
        return item_equipment_slot;
    }

    public String getItemName() {
        return itemName;
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

    public int getAttactPower() {
        return attactPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public Double getBossDamage() {
        return bossDamage;
    }

    public Double getIgnore() {
        return ignore;
    }

    public int getAllStat() {
        return allStat;
    }

    public String getPotentialOne() {
        return potentialOne;
    }

    public String getPotentialTwo() {
        return potentialTwo;
    }

    public String getPotentialThree() {
        return potentialThree;
    }

    public String getAdditionalOne() {
        return additionalOne;
    }

    public String getAdditionalTwo() {
        return additionalTwo;
    }

    public String getAdditionalThree() {
        return additionalThree;
    }

    public JsonNode getExcetional() {
        return excetional;
    }

    public String getSoul() {
        return soul;
    }
}
