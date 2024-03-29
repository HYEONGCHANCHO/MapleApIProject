package com.mapleApiTest.projectOne.dto.item;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class CharactersItemInfoDTO {

    String item_equipment_slot;
    String itemName;
    int itemLevel;
    int mainStat;
    int subStat;
    int mainStatPer;
    int subStatPer;
    int atMgStat;
    int atMgPer;
    int starForce;

    public int getStarForce() {
        return starForce;
    }

    public void setStarForce(int starForce) {
        this.starForce = starForce;
    }

    int potentialMainStat;
    int potentialSubStat;
    int potentialMainStatPer;
    int potentialSubStatPer;
    int potentialAtMgStat;
    int potentialAtMgPer;
    int potentialBossDamagePer;
    int potentialDamagePer;
    int bossDamage;
    int damage;
    int criticalDamage = 0;


    int excepStr;
    int excepDex;
    int excepInt;
    int excepLuk;
    int excepAtPower;
    int excepMgPower;

    int str;
    int dex;
    int intel;
    int luk;
    int hp;
    int attactPower;
    int magicPower;
    int allStat;


    int strPotentialPer = 0;
    int dexPotentialPer = 0;
    int intPotentialPer = 0;
    int lukPotentialPer = 0;
    int allStatPotentialPer = 0;
    int allStatPotential = 0;
    int strPotentialStat = 0;
    int dexPotentialStat = 0;
    int intPotentialStat = 0;
    int lukPotentialStat = 0;
    int atPotentialStat = 0;
    int atPotentialPer = 0;
    int mgPotentialStat = 0;
    int mgPotentialPer = 0;
    int criticalDamagePotential = 0;

    int atMgPotentialStat = 0;
    int atMgPotentialStatPer = 0;


    public void processPotential(String potential, int charactersLevel) {

        if (potential != "null") {
            char type = potential.charAt(0);
//            System.out.println(charactersLevel + "level");
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
                            atPotentialPer += number;
                            break;
                        case '마':
                            mgPotentialPer += number;
                            break;
                        case '크':
                            criticalDamagePotential += number;
                            break;
                        case '보':
                            potentialBossDamagePer += number;
                            break;
                        case '데':
                            potentialDamagePer += number;
                            break;

                    }
                } else {

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
                        case '올':
                            allStatPotential += number;
                            break;
                        case '공':
                            atPotentialStat += number;
                            break;
                        case '마':
                            mgPotentialStat += number;
                            break;
                        case '캐':
                            char typeTwo = potential.charAt(13);
                            switch (typeTwo) {
                                case 'S':
                                    strPotentialStat += number * (charactersLevel / 9);
                                    break;
                                case 'D':
                                    dexPotentialStat += number * (charactersLevel / 9);
                                    break;
                                case 'I':
                                    intPotentialStat += number * (charactersLevel / 9);
                                    break;
                                case 'L':
                                    lukPotentialStat += number * (charactersLevel / 9);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

    public void processSoul(String soul) {

        if (soul != "null") {
            char type = soul.charAt(0);
            if (soul.startsWith(String.valueOf(type))) {
                String[] parts = soul.split("\\+");
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
                            atPotentialPer += number;
                            break;
                        case '마':
                            mgPotentialPer += number;
                            break;
                        default:
                            break;

                    }
                } else {

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
                            atPotentialStat += number;
                            break;
                        case '올':
                            allStatPotential += number;
                            break;
                        case '마':
                            mgPotentialStat += number;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void setCharactersMainSubStat(String charactersClass) {

        if (Arrays.asList("바이퍼", "히어로").contains(charactersClass)) {
            //스탯별로 직업 분류할것

            mainStat = str + excepStr;
            subStat = dex + excepDex;
            mainStatPer = allStat;
            subStatPer = allStat;
            atMgStat = attactPower + excepAtPower;

            potentialMainStat = strPotentialStat + allStatPotential;
            potentialSubStat = dexPotentialStat + allStatPotential;
            potentialMainStatPer = strPotentialPer + allStatPotentialPer;
            potentialSubStatPer = dexPotentialPer + allStatPotentialPer;
            potentialAtMgStat = atPotentialStat;
            potentialAtMgPer = atPotentialPer;

        }

    }

    public int getCriticalDamagePotential() {
        return criticalDamagePotential;
    }

    public void setCriticalDamagePotential(int criticalDamagePotential) {
        this.criticalDamagePotential = criticalDamagePotential;
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

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
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

    public int getAtMgPer() {
        return atMgPer;
    }

    public void setAtMgPer(int atMgPer) {
        this.atMgPer = atMgPer;
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

    public int getExcepStr() {
        return excepStr;
    }

    public void setExcepStr(int excepStr) {
        this.excepStr = excepStr;
    }

    public int getExcepDex() {
        return excepDex;
    }

    public void setExcepDex(int excepDex) {
        this.excepDex = excepDex;
    }

    public int getExcepInt() {
        return excepInt;
    }

    public void setExcepInt(int excepInt) {
        this.excepInt = excepInt;
    }

    public int getExcepLuk() {
        return excepLuk;
    }

    public void setExcepLuk(int excepLuk) {
        this.excepLuk = excepLuk;
    }

    public int getExcepAtPower() {
        return excepAtPower;
    }

    public void setExcepAtPower(int excepAtPower) {
        this.excepAtPower = excepAtPower;
    }

    public int getExcepMgPower() {
        return excepMgPower;
    }

    public void setExcepMgPower(int excepMgPower) {
        this.excepMgPower = excepMgPower;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getIntel() {
        return intel;
    }

    public void setIntel(int intel) {
        this.intel = intel;
    }

    public int getLuk() {
        return luk;
    }

    public void setLuk(int luk) {
        this.luk = luk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttactPower() {
        return attactPower;
    }

    public void setAttactPower(int attactPower) {
        this.attactPower = attactPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(int magicPower) {
        this.magicPower = magicPower;
    }

    public int getAllStat() {
        return allStat;
    }

    public void setAllStat(int allStat) {
        this.allStat = allStat;
    }

    public int getStrPotentialPer() {
        return strPotentialPer;
    }

    public void setStrPotentialPer(int strPotentialPer) {
        this.strPotentialPer = strPotentialPer;
    }

    public int getDexPotentialPer() {
        return dexPotentialPer;
    }

    public void setDexPotentialPer(int dexPotentialPer) {
        this.dexPotentialPer = dexPotentialPer;
    }

    public int getIntPotentialPer() {
        return intPotentialPer;
    }

    public void setIntPotentialPer(int intPotentialPer) {
        this.intPotentialPer = intPotentialPer;
    }

    public int getLukPotentialPer() {
        return lukPotentialPer;
    }

    public void setLukPotentialPer(int lukPotentialPer) {
        this.lukPotentialPer = lukPotentialPer;
    }

    public int getAllStatPotentialPer() {
        return allStatPotentialPer;
    }

    public void setAllStatPotentialPer(int allStatPotentialPer) {
        this.allStatPotentialPer = allStatPotentialPer;
    }

    public int getAllStatPotential() {
        return allStatPotential;
    }

    public void setAllStatPotential(int allStatPotential) {
        this.allStatPotential = allStatPotential;
    }

    public int getStrPotentialStat() {
        return strPotentialStat;
    }

    public void setStrPotentialStat(int strPotentialStat) {
        this.strPotentialStat = strPotentialStat;
    }

    public int getDexPotentialStat() {
        return dexPotentialStat;
    }

    public void setDexPotentialStat(int dexPotentialStat) {
        this.dexPotentialStat = dexPotentialStat;
    }

    public int getIntPotentialStat() {
        return intPotentialStat;
    }

    public void setIntPotentialStat(int intPotentialStat) {
        this.intPotentialStat = intPotentialStat;
    }

    public int getLukPotentialStat() {
        return lukPotentialStat;
    }

    public void setLukPotentialStat(int lukPotentialStat) {
        this.lukPotentialStat = lukPotentialStat;
    }

    public int getAtPotentialStat() {
        return atPotentialStat;
    }

    public void setAtPotentialStat(int atPotentialStat) {
        this.atPotentialStat = atPotentialStat;
    }

    public int getAtPotentialPer() {
        return atPotentialPer;
    }

    public void setAtPotentialPer(int atPotentialPer) {
        this.atPotentialPer = atPotentialPer;
    }

    public int getMgPotentialStat() {
        return mgPotentialStat;
    }

    public void setMgPotentialStat(int mgPotentialStat) {
        this.mgPotentialStat = mgPotentialStat;
    }

    public int getMgPotentialPer() {
        return mgPotentialPer;
    }

    public void setMgPotentialPer(int mgPotentialPer) {
        this.mgPotentialPer = mgPotentialPer;
    }

    public int getAtMgPotentialStat() {
        return atMgPotentialStat;
    }

    public void setAtMgPotentialStat(int atMgPotentialStat) {
        this.atMgPotentialStat = atMgPotentialStat;
    }

    public int getAtMgPotentialStatPer() {
        return atMgPotentialStatPer;
    }

    public void setAtMgPotentialStatPer(int atMgPotentialStatPer) {
        this.atMgPotentialStatPer = atMgPotentialStatPer;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
