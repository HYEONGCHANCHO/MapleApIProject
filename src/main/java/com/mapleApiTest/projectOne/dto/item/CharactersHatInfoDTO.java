package com.mapleApiTest.projectOne.dto.item;


import com.fasterxml.jackson.databind.JsonNode;

public class CharactersHatInfoDTO {

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
    Double bossDamage;
    int criticalDamage;

    public CharactersHatInfoDTO() {
    }

    public CharactersHatInfoDTO(String item_equipment_slot, String itemName, int mainStat, int subStat, int mainStatPer, int subStatPer, int atMgStat, int potentialMainStat, int potentialSubStat, int potentialMainStatPer, int potentialSubStatPer, int potentialAtMgStat, int potentialAtMgPer, Double bossDamage, int criticalDamage) {
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
        this.criticalDamage = criticalDamage;
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

    public Double getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(Double bossDamage) {
        this.bossDamage = bossDamage;
    }

    public int getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(int criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    //
//
//    int str;
//    int dex;
//    int intel;
//    int luk;
//    int hp;
//    int attactPower;
//    int magicPower;
//    Double bossDamage;
//    Double ignore;
//    int allStat;
//    String potentialOne;
//    String potentialTwo;
//    String potentialThree;
//    String additionalOne;
//    String additionalTwo;
//    String additionalThree;
//    JsonNode excetional;
//    String soul;
//
//    int strPotentialPer = 0;
//    int dexPotentialPer = 0;
//    int intPotentialPer = 0;
//    int lukPotentialPer = 0;
//    int allStatPotentialPer = 0;
//    int strPotentialStat = 0;
//    int dexPotentialStat = 0;
//    int intPotentialStat = 0;
//    int lukPotentialStat = 0;
//    int atMgPotentialPer = 0;
//    int atMgPotentialStat = 0;
//
//
//    public int getStrPotentialPer() {
//        return strPotentialPer;
//    }
//
//    public int getDexPotentialPer() {
//        return dexPotentialPer;
//    }
//
//    public int getIntPotentialPer() {
//        return intPotentialPer;
//    }
//
//    public int getLukPotentialPer() {
//        return lukPotentialPer;
//    }
//
//    public int getAllStatPotentialPer() {
//        return allStatPotentialPer;
//    }
//
//    public int getStrPotentialStat() {
//        return strPotentialStat;
//    }
//
//    public int getDexPotentialStat() {
//        return dexPotentialStat;
//    }
//
//    public int getIntPotentialStat() {
//        return intPotentialStat;
//    }
//
//    public int getLukPotentialStat() {
//        return lukPotentialStat;
//    }
//
//    public int getAtMgPotentialPer() {
//        return atMgPotentialPer;
//    }
//
//    public int getAtMgPotentialStat() {
//        return atMgPotentialStat;
//    }
//
//    public CharactersHatInfoDTO(String item_equipment_slot, String itemName, int str, int dex, int intel, int luk, int hp, int attactPower, int magicPower, Double bossDamage, Double ignore, int allStat, String potentialOne, String potentialTwo, String potentialThree, String additionalOne, String additionalTwo, String additionalThree, JsonNode excetional, String soul) {
//        this.item_equipment_slot = item_equipment_slot;
//        this.itemName = itemName;
//        this.str = str;
//        this.dex = dex;
//        this.intel = intel;
//        this.luk = luk;
//        this.hp = hp;
//        this.attactPower = attactPower;
//        this.magicPower = magicPower;
//        this.bossDamage = bossDamage;
//        this.ignore = ignore;
//        this.allStat = allStat;
//        this.potentialOne = potentialOne;
//        this.potentialTwo = potentialTwo;
//        this.potentialThree = potentialThree;
//        this.additionalOne = additionalOne;
//        this.additionalTwo = additionalTwo;
//        this.additionalThree = additionalThree;
//        this.excetional = excetional;
//        this.soul = soul;
//    }
//
//
//    public void processPotential(String potential,int charactersLevel) {
//
//        char type = potential.charAt(0);
//System.out.println(charactersLevel+"level");
//        if (potential.startsWith(String.valueOf(type))) {
//            String[] parts = potential.split("\\+");
//            String lastPart = parts[1];
//
//            if (lastPart.contains("%")) {
//                lastPart = lastPart.replace("%", "");
//                int number = Integer.parseInt(lastPart);
//                switch (type) {
//                    case 'S':
//                        strPotentialPer += number;
//                        break;
//                    case 'D':
//                        dexPotentialPer += number;
//                        break;
//                    case 'I':
//                        intPotentialPer += number;
//                        break;
//                    case 'L':
//                        lukPotentialPer += number;
//                        break;
//                    case '올':
//                        allStatPotentialPer += number;
//                        break;
//                    case '공':
//                        atMgPotentialPer += number;
//                        break;
//
//                }
//            } else {
//
//                int number = Integer.parseInt(lastPart);
//
//                switch (type) {
//                    case 'S':
//                        strPotentialStat += number;
//                        break;
//                    case 'D':
//                        dexPotentialStat += number;
//                        break;
//                    case 'I':
//                        intPotentialStat += number;
//                        break;
//                    case 'L':
//                        lukPotentialStat += number;
//                        break;
//                    case '공':
//                        atMgPotentialStat += number;
//                        break;
//                    case '캐':
//                        char typeTwo = potential.charAt(13);
//                        switch (typeTwo) {
//                            case 'S':
//                                strPotentialStat += number*(charactersLevel/9);
//                                break;
//                            case 'D':
//                                dexPotentialStat += number*(charactersLevel/9);
//                                break;
//                            case 'I':
//                                intPotentialStat += number*(charactersLevel/9);
//                                break;
//                            case 'L':
//                                lukPotentialStat += number*(charactersLevel/9);
//                                break;
//                            default:
//                                break;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    }
//
//
//    public String getItem_equipment_slot() {
//        return item_equipment_slot;
//    }
//
//    public String getItemName() {
//        return itemName;
//    }
//
//    public int getStr() {
//        return str;
//    }
//
//    public int getDex() {
//        return dex;
//    }
//
//    public int getIntel() {
//        return intel;
//    }
//
//    public int getLuk() {
//        return luk;
//    }
//
//    public int getHp() {
//        return hp;
//    }
//
//    public int getAttactPower() {
//        return attactPower;
//    }
//
//    public int getMagicPower() {
//        return magicPower;
//    }
//
//    public Double getBossDamage() {
//        return bossDamage;
//    }
//
//    public Double getIgnore() {
//        return ignore;
//    }
//
//    public int getAllStat() {
//        return allStat;
//    }
//
//    public String getPotentialOne() {
//        return potentialOne;
//    }
//
//    public String getPotentialTwo() {
//        return potentialTwo;
//    }
//
//    public String getPotentialThree() {
//        return potentialThree;
//    }
//
//    public String getAdditionalOne() {
//        return additionalOne;
//    }
//
//    public String getAdditionalTwo() {
//        return additionalTwo;
//    }
//
//    public String getAdditionalThree() {
//        return additionalThree;
//    }
//
//    public JsonNode getExcetional() {
//        return excetional;
//    }
//
//    public String getSoul() {
//        return soul;
//    }
}
