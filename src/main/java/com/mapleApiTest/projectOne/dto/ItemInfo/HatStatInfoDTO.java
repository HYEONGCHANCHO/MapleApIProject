package com.mapleApiTest.projectOne.dto.ItemInfo;


import com.fasterxml.jackson.databind.JsonNode;

public class HatStatInfoDTO {

    String item_equipment_slot;
    String itemName;
    int itemLevel;
    int mainStat;
    int subStat;
    int atMgPower;
    int damage;
    int allStat;
    String potentialOne;
    String potentialTwo;
    String potentialThree;
    String additionalOne;
    String additionalTwo;
    String additionalThree;
    JsonNode excetional;
    String soul;


    public HatStatInfoDTO(int itemLevel) {
        this.itemLevel = itemLevel;
        // itemname에 따라 초기값 설정
        if (itemLevel == 150) {

            this.mainStat = 40;
            this.subStat = 40;
            this.atMgPower = 2;


        } else if (itemLevel == 160) {
            this.mainStat = 45;
            this.subStat = 45;
            this.atMgPower = 3;


        } else if (itemLevel == 200) {
            this.mainStat = 65;
            this.subStat = 65;
            this.atMgPower = 7;


        } else if (itemLevel == 250) {
            this.mainStat = 80;
            this.subStat = 80;
            this.atMgPower = 10;


        }
    }


    public String getItem_equipment_slot() {
        return item_equipment_slot;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemLevel() {
        return itemLevel;
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


    public void setItem_equipment_slot(String item_equipment_slot) {
        this.item_equipment_slot = item_equipment_slot;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public void setMainStat(int mainStat) {
        this.mainStat = mainStat;
    }

    public void setSubStat(int subStat) {
        this.subStat = subStat;
    }

    public void setAtMgPower(int atMgPower) {
        this.atMgPower = atMgPower;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAllStat(int allStat) {
        this.allStat = allStat;
    }

    public void setPotentialOne(String potentialOne) {
        this.potentialOne = potentialOne;
    }

    public void setPotentialTwo(String potentialTwo) {
        this.potentialTwo = potentialTwo;
    }

    public void setPotentialThree(String potentialThree) {
        this.potentialThree = potentialThree;
    }

    public void setAdditionalOne(String additionalOne) {
        this.additionalOne = additionalOne;
    }

    public void setAdditionalTwo(String additionalTwo) {
        this.additionalTwo = additionalTwo;
    }

    public void setAdditionalThree(String additionalThree) {
        this.additionalThree = additionalThree;
    }

    public void setExcetional(JsonNode excetional) {
        this.excetional = excetional;
    }

    public void setSoul(String soul) {
        this.soul = soul;
    }
}
