package com.mapleApiTest.projectOne.dto.item;


public class CharactersItemStatInfoDTO {

    String item_equipment_slot;
    String itemName;
    int mainStat;
    int subStat;
    int mainStatPer;
    int subStatPer;
    int atMgStat;
    int potentialMainStat;
    int potentialSubStat;
    int potentialMainStatPer;
    int potentialSubStatPer;
    int potentialAtMgStat;
    int potentialAtMgPer;
    int bossDamage;
    int damage;
    int criticalDamage;

    int potentialBossDamagePer;
    int potentialDamagePer;
    int potentialCriticalDamage;

    public CharactersItemStatInfoDTO() {
    }

    public CharactersItemStatInfoDTO(String item_equipment_slot, int mainStat, int subStat, int mainStatPer, int subStatPer, int atMgStat, int bossDamage, int damage) {
        this.item_equipment_slot = item_equipment_slot;
        this.mainStat = mainStat;
        this.subStat = subStat;
        this.mainStatPer = mainStatPer;
        this.subStatPer = subStatPer;
        this.atMgStat = atMgStat;
        this.bossDamage = bossDamage;
        this.damage = damage;
    }

    public CharactersItemStatInfoDTO(String item_equipment_slot, String itemName, int mainStat, int subStat, int mainStatPer, int subStatPer, int atMgStat, int potentialMainStat, int potentialSubStat, int potentialMainStatPer, int potentialSubStatPer, int potentialAtMgStat, int potentialAtMgPer, int bossDamage, int damage, int criticalDamage, int potentialBossDamagePer, int potentialDamagePer, int potentialCriticalDamage) {
        this.item_equipment_slot = item_equipment_slot;
        this.itemName = itemName;
        this.mainStat = mainStat;
        this.subStat = subStat;
        this.mainStatPer = mainStatPer;
        this.subStatPer = subStatPer;
        this.atMgStat = atMgStat;
        this.potentialMainStat = potentialMainStat;
        this.potentialSubStat = potentialSubStat;
        this.potentialMainStatPer = potentialMainStatPer;
        this.potentialSubStatPer = potentialSubStatPer;
        this.potentialAtMgStat = potentialAtMgStat;
        this.potentialAtMgPer = potentialAtMgPer;
        this.bossDamage = bossDamage;
        this.damage = damage;
        this.criticalDamage = criticalDamage;
        this.potentialBossDamagePer = potentialBossDamagePer;
        this.potentialDamagePer = potentialDamagePer;
        this.potentialCriticalDamage = potentialCriticalDamage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPotentialCriticalDamage() {
        return potentialCriticalDamage;
    }

    public void setPotentialCriticalDamage(int potentialCriticalDamage) {
        this.potentialCriticalDamage = potentialCriticalDamage;
    }

    public String getItem_equipment_slot() {
        return item_equipment_slot;
    }

    public void setItem_equipment_slot(String item_equipment_slot) {
        this.item_equipment_slot = item_equipment_slot;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getPotentialMainStat() {
        return potentialMainStat;
    }

    public void setPotentialMainStat(int potentialMainStat) {
        this.potentialMainStat = potentialMainStat;
    }

    public int getPotentialSubStat() {
        return potentialSubStat;
    }

    public void setPotentialSubStat(int potentialSubStat) {
        this.potentialSubStat = potentialSubStat;
    }

    public int getPotentialMainStatPer() {
        return potentialMainStatPer;
    }

    public void setPotentialMainStatPer(int potentialMainStatPer) {
        this.potentialMainStatPer = potentialMainStatPer;
    }

    public int getPotentialSubStatPer() {
        return potentialSubStatPer;
    }

    public void setPotentialSubStatPer(int potentialSubStatPer) {
        this.potentialSubStatPer = potentialSubStatPer;
    }

    public int getPotentialAtMgStat() {
        return potentialAtMgStat;
    }

    public void setPotentialAtMgStat(int potentialAtMgStat) {
        this.potentialAtMgStat = potentialAtMgStat;
    }

    public int getPotentialAtMgPer() {
        return potentialAtMgPer;
    }

    public void setPotentialAtMgPer(int potentialAtMgPer) {
        this.potentialAtMgPer = potentialAtMgPer;
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

    public int getPotentialBossDamagePer() {
        return potentialBossDamagePer;
    }

    public void setPotentialBossDamagePer(int potentialBossDamagePer) {
        this.potentialBossDamagePer = potentialBossDamagePer;
    }

    public int getPotentialDamagePer() {
        return potentialDamagePer;
    }

    public void setPotentialDamagePer(int potentialDamagePer) {
        this.potentialDamagePer = potentialDamagePer;
    }
}
