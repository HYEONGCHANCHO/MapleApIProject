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
    int attactPower=0;
    int magicPower=0;
    int allStat=0;


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


    int allStatTitlePer = 0;
    int allStatTitle = 0;
    int atTitleStat = 0;
    int mgTitleStat = 0;
    //    int criticalDamageTitle = 0;
    int bossDamageTitlePer = 0;
    int damageTitlePer = 0;

    int strTitlePer = 0;
    int dexTitlePer = 0;
    int intTitlePer = 0;
    int lukTitlePer = 0;
    int strTitleStat = 0;
    int dexTitleStat = 0;
    int intTitleStat = 0;
    int lukTitleStat = 0;

    public void processPotential(String potential, int charactersLevel) {

        if (potential != "null") {
            char type = potential.charAt(0);
//            System.out.println(charactersLevel + "level");
            if (potential.startsWith(String.valueOf(type))) {
                String[] parts = potential.split("\\+");
                String lastPart=null;
                if (parts.length > 1 && parts[1] != null) {
                    lastPart = parts[1];
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
                        case '보':
                            potentialBossDamagePer += number;
                            break;
                        case '데':
                            potentialDamagePer += number;
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

    public void processTitle(String title) {

        if (title != null) {
            String[] titleParts = title.split("\\\\n");
            for (String titlepart : titleParts) {
                String[] parts = titlepart.split(",");
                    int value = 0; // 숫자를 저장할 변수 초기화
                    int levelAtMgValue = 0;
                    double doubleValue = 0.0;
                    for (String part : parts) {
                        String[] tokens = part.split("\\s+");
                        for (String token : tokens) {
                          if (token.matches("\\d+%")) { // 숫자 뒤에 %가 있는 경우
                                // %를 제거하고 숫자만 추출하여 정수로 변환
                                value = Integer.parseInt(token.replaceAll("%", ""));
                                System.out.println("value :" + value);
                            } else if (token.matches("\\d+\\.\\d+%")) { // 소수점이 포함된 숫자인 경우
                                doubleValue = Double.parseDouble(token.replaceAll("%", ""));
                                System.out.println("double value: " + doubleValue);
                            } else if (token.matches("\\d+")) { // 그냥 숫자인 경우
                                value = Integer.parseInt(token);
                                System.out.println("value :" + value);
                            }
                        }

                        // 추출된 숫자에 따라 적절한 변수에 값을 누적하여 저장
                      if (part.contains("공격력") || part.contains("마력")) {
                          atTitleStat += value;
                          mgTitleStat += value;
                        } else if (part.contains("STR")) {
                            strTitleStat += value;
                        } else if (part.contains("DEX")) {
                            dexTitleStat += value;
                        } else if (part.contains("LUK")) {
                            lukTitleStat += value;
                        } else if (part.contains("INT")) {
                            intTitleStat += value;
                       } else if (part.contains("올스탯")) {
                          strTitleStat += value;
                          dexTitleStat += value;
                          intTitleStat += value;
                          lukTitleStat += value;
                        } else if (part.contains("보스 몬스터 공격 시 데미지")) {
                            bossDamageTitlePer += value + doubleValue;
                        }
                    }
                }


                ////////////////////////////////////////////////////////////////////////////////
//                if (!part.isEmpty()) { // 빈 문자열은 무시합니다.
//                    char firstWord ='a';
//                    if (Character.isLetter(part.charAt(0))) {
//                        firstWord = part.charAt(0);
//                    } else if (Character.isLetter(part.charAt(1))) {
//                        firstWord = part.charAt(1);
//                    } else if (Character.isLetter(part.charAt(2))) {
//                        firstWord = part.charAt(2);
//                    }
//                    if (part.contains("+")) {
//                        String[] parts = part.split("\\+");
//                        String lastPart = parts[1];
//                        if (lastPart.contains("%")) {
//                            lastPart = lastPart.replace("%", "").trim();
//                            int number = Integer.parseInt(lastPart);
//                            switch (firstWord) {
//                                case 'S':
//                                    strTitlePer += number;
//                                    break;
//                                case 'D':
//                                    dexTitlePer += number;
//                                    break;
//                                case 'I':
//                                    intTitlePer += number;
//                                    break;
//                                case 'L':
//                                    lukTitlePer += number;
//                                    break;
//                                case '올':
//                                    allStatTitlePer += number;
//                                    break;
//                                case '보':
//                                    bossDamageTitlePer += number;
//                                    break;
//                                case '데':
//                                    damageTitlePer += number;
//                                    break;
//                                default:
//                                    break;
//                            }
//                        } else {
//
//                            int number = Integer.parseInt(lastPart);
//
//                            switch (firstWord) {
//                                case 'S':
//                                    strTitleStat += number;
//                                    break;
//                                case 'D':
//                                    dexTitleStat += number;
//                                    break;
//                                case 'I':
//                                    intTitleStat += number;
//                                    break;
//                                case 'L':
//                                    lukTitleStat += number;
//                                    break;
//                                case '공':
//                                    System.out.println("아ㅏㅏㅏㅏㅏㅏㅏㅏatTitleStat"+atTitleStat);
//                                    atTitleStat += number;
//                                    System.out.println("아ㅏㅏㅏㅏㅏㅏㅏㅏatTitleStat"+atTitleStat);
//                                    break;
//                                case '올':
//                                    allStatTitle += number;
//                                    break;
//                                case '마':
//                                    System.out.println("아ㅏㅏㅏㅏㅏㅏㅏㅏmgTitleStat"+mgTitleStat);
//                                    mgTitleStat += number;
//                                    System.out.println("아ㅏㅏㅏㅏㅏㅏㅏㅏmgTitleStat"+mgTitleStat);
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
                }
//            }
//        }
    }

    public void setCharactersMainSubStat(String charactersClass) {

        if (Arrays.asList("바이퍼", "히어로").contains(charactersClass)) {
            //스탯별로 직업 분류할것

            mainStat = str + excepStr + strTitleStat + allStatTitle;
            subStat = dex + excepDex + dexTitleStat + allStatTitle;
            mainStatPer = allStat + strTitlePer + allStatTitlePer;
            subStatPer = allStat + dexTitlePer + allStatTitlePer;
            atMgStat = attactPower + excepAtPower + atTitleStat;
System.out.println("    아ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ어어어어");
System.out.println("atMgStat"+atMgStat);
System.out.println("attactPower"+attactPower);
System.out.println("excepAtPower"+excepAtPower);
System.out.println("atTitleStat"+atTitleStat);
            potentialMainStat = strPotentialStat + allStatPotential;
            potentialSubStat = dexPotentialStat + allStatPotential;
            potentialMainStatPer = strPotentialPer + allStatPotentialPer;
            potentialSubStatPer = dexPotentialPer + allStatPotentialPer;
            potentialAtMgStat = atPotentialStat;
            potentialAtMgPer = atPotentialPer;

        }

    }

    public int getAllStatTitlePer() {
        return allStatTitlePer;
    }

    public int getAllStatTitle() {
        return allStatTitle;
    }


    public int getBossDamageTitlePer() {
        return bossDamageTitlePer;
    }

    public int getDamageTitlePer() {
        return damageTitlePer;
    }

    public int getStrTitlePer() {
        return strTitlePer;
    }

    public int getDexTitlePer() {
        return dexTitlePer;
    }

    public int getIntTitlePer() {
        return intTitlePer;
    }

    public int getLukTitlePer() {
        return lukTitlePer;
    }

    public int getStrTitleStat() {
        return strTitleStat;
    }

    public int getDexTitleStat() {
        return dexTitleStat;
    }

    public int getIntTitleStat() {
        return intTitleStat;
    }

    public int getLukTitleStat() {
        return lukTitleStat;
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

    public int getAtTitleStat() {
        return atTitleStat;
    }

    public void setAtTitleStat(int atTitleStat) {
        this.atTitleStat = atTitleStat;
    }

    public int getMgTitleStat() {
        return mgTitleStat;
    }

    public void setMgTitleStat(int mgTitleStat) {
        this.mgTitleStat = mgTitleStat;
    }
}
