package com.mapleApiTest.projectOne.dto.item;


import com.fasterxml.jackson.databind.JsonNode;

public class CharactersHatInfoDTO {

    String item_equipment_slot;
    String itemName;
    int str;
    int dex;
    int intel;
    int luk;
    int hp;
    int attactPower;
    int magicPower;
    Double bossDamage;
    Double ignore;
    int allStat;
    String potentialOne;
    String potentialTwo;
    String potentialThree;
    String additionalOne;
    String additionalTwo;
    String additionalThree;
    JsonNode excetional;
    String soul;

    int strPotentialPer = 0;
    int dexPotentialPer = 0;
    int intPotentialPer = 0;
    int lukPotentialPer = 0;
    int allStatPotentialPer = 0;
    int strPotentialStat = 0;
    int dexPotentialStat = 0;
    int intPotentialStat = 0;
    int lukPotentialStat = 0;
    int atMgPotentialPer = 0;
    int atMgPotentialStat = 0;


    public int getStrPotentialPer() {
        return strPotentialPer;
    }

    public int getDexPotentialPer() {
        return dexPotentialPer;
    }

    public int getIntPotentialPer() {
        return intPotentialPer;
    }

    public int getLukPotentialPer() {
        return lukPotentialPer;
    }

    public int getAllStatPotentialPer() {
        return allStatPotentialPer;
    }

    public int getStrPotentialStat() {
        return strPotentialStat;
    }

    public int getDexPotentialStat() {
        return dexPotentialStat;
    }

    public int getIntPotentialStat() {
        return intPotentialStat;
    }

    public int getLukPotentialStat() {
        return lukPotentialStat;
    }

    public int getAtMgPotentialPer() {
        return atMgPotentialPer;
    }

    public int getAtMgPotentialStat() {
        return atMgPotentialStat;
    }

    public CharactersHatInfoDTO(String item_equipment_slot, String itemName, int str, int dex, int intel, int luk, int hp, int attactPower, int magicPower, Double bossDamage, Double ignore, int allStat, String potentialOne, String potentialTwo, String potentialThree, String additionalOne, String additionalTwo, String additionalThree, JsonNode excetional, String soul) {
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
        this.additionalTwo = additionalTwo;
        this.additionalThree = additionalThree;
        this.excetional = excetional;
        this.soul = soul;
    }


    public void processPotential(String potential) {

        char type = potential.charAt(0);

        if (potential.startsWith(String.valueOf(type))) {
            String[] parts = potential.split("\\+");
            String lastPart = parts[1];

            if (lastPart.contains("%")) {
                lastPart = lastPart.replace("%", "");
                int number = Integer.parseInt(lastPart);
                switch (type) {
                    case 'S':
                        strPotentialPer += number;
                        break;
                    case 'D':
                        dexPotentialPer += number;
                        break;
                    case 'I':
                        intPotentialPer += number;
                        break;
                    case 'L':
                        lukPotentialPer += number;
                        break;
                    case '올':
                        allStatPotentialPer += number;
                        break;
                    case '공':
                        atMgPotentialPer += number;
                        break;
                    default:
                        break;
                }
            }

            int number = Integer.parseInt(lastPart);

            switch (type) {
                case 'S':
                    strPotentialStat += number;
                    break;
                case 'D':
                    dexPotentialStat += number;
                    break;
                case 'I':
                    intPotentialStat += number;
                    break;
                case 'L':
                    lukPotentialStat += number;
                    break;
                case '공':
                    atMgPotentialStat += number;
                    break;
                default:
                    break;
            }
        }
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
