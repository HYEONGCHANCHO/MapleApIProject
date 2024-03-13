package com.mapleApiTest.projectOne.dto.ItemInfo;


import com.fasterxml.jackson.databind.JsonNode;

public class ArcaneHatDTO {

    String item_equipment_slot;
    String itemName;
    int mainStat = 65;
    int subStat = 65;
    int atMgPower = 7;
    int damage =0;
    int allStat=0;
    String potentialOne =null;
    String potentialTwo=null;
    String potentialThree=null;
    String additionalOne=null;
    String additionalTwo=null;
    String additionalThree=null;
    JsonNode excetional=null;
    String soul=null;

    public String getItem_equipment_slot() {
        return item_equipment_slot;
    }

    public String getItemName() {
        return itemName;
    }

    public int getMainStat() {
        return mainStat;
    }

    public int getSubStat() {
        return subStat;
    }

    public int getAtMgPower() {
        return atMgPower;
    }

    public int getDamage() {
        return damage;
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
