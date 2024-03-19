package com.mapleApiTest.projectOne.dto.ItemInfo;

import java.util.stream.IntStream;

public class ItemSimulationDTO {

    int statIncrementOneToFive = 2;
    int statIncrementSixToFifteen = 3;
    int statIncrementFifteenToTwenty130 = 7;
    int statIncrementFifteenToTwentyTwoLevel140 = 9;
    int statIncrementFifteenToTwentyTwoLevel150 = 11;
    int statIncrementFifteenToTwentyTwoLevel160 = 13;
    int statIncrementFifteenToTwentyTwoLevel200 = 15;
    int statIncrementFifteenToTwentyTwoLevel250 = 17;

    int atMgIncrementLevel130 = 7;
    int atMgIncrementLevel140 = 8;
    int atMgIncrementLevel150 = 9;
    int atMgIncrementLevel160 = 10;
    int atMgIncrementLevel200 = 12;
    int atMgIncrementLevel250 = 14;

    int mainStat;
    int subStat;
    int atMgPower;

    int atMgIncrement;
    int atMgIncrementAdd;

    public void calculateEquipmentStats(HatStatInfoDTO hatStatInfoDTO, int starForce, int itemUpgrade, int itemLevel, int addOptionStat, int potentialTotalMainStatPer, int potentialTotalSubStatPer, int potentialTotalAtMgPower) {

        int itemUpgradeMainStat = 0;
        int itemUpgradeSubStat = 0;
        int itemUpgradeAtMg = 0;
        if (itemUpgrade == 100) {
            itemUpgradeMainStat = 3 * 12;
        } else if (itemUpgrade == 70) {
            itemUpgradeMainStat = 4 * 12;

        } else if (itemUpgrade == 30) {
            itemUpgradeMainStat = 10 * 12;
            itemUpgradeSubStat = 3 * 12;

        } else if (itemUpgrade == 15) {
            itemUpgradeMainStat = 14 * 12;
            itemUpgradeSubStat = 4 * 12;

        } else if (itemUpgrade == 33) {
            itemUpgradeMainStat = 3 * 12;
            itemUpgradeAtMg = 3 * 12;
        } else if (itemUpgrade == 63) {
            itemUpgradeMainStat = 6 * 12;
            itemUpgradeAtMg = 3 * 12;
        } else if (itemUpgrade == 66) {
            itemUpgradeMainStat = 6 * 12;
            itemUpgradeAtMg = 6 * 12;
        }

        int itemAddOptionMainStat = 0;
        int itemAddOptionAllStat = 0;
        int itemAddOptionAtMg = 0;

        if(addOptionStat == 100){
            itemAddOptionMainStat = 60;
            itemAddOptionAllStat =4;
        } else if(addOptionStat == 110){
            itemAddOptionMainStat = 70;
            itemAddOptionAllStat =4;
        } else if(addOptionStat == 120){
            itemAddOptionMainStat = 70;
            itemAddOptionAllStat =5;
        } else if(addOptionStat == 130){
            itemAddOptionMainStat = 80;
            itemAddOptionAllStat =5;
        } else if(addOptionStat == 140){
            itemAddOptionMainStat = 80;
            itemAddOptionAllStat =6;
        } else if(addOptionStat == 150){
            itemAddOptionMainStat = 90;
            itemAddOptionAllStat =6;
        } else if(addOptionStat == 160){
            itemAddOptionMainStat = 90;
            itemAddOptionAllStat =7;
        } else if(addOptionStat == 170){
            itemAddOptionMainStat = 100;
            itemAddOptionAllStat =7;
        } else if(addOptionStat == 180){
            itemAddOptionMainStat = 110;
            itemAddOptionAllStat =7;
        } else if(addOptionStat == 190){
            itemAddOptionMainStat = 120;
            itemAddOptionAllStat =7;
        } else if(addOptionStat == 200){
            itemAddOptionMainStat = 130;
            itemAddOptionAllStat =7;
        }


        if (itemLevel == 150) {
            if (starForce < 6) {
                mainStat = hatStatInfoDTO.mainStat + starForce * statIncrementOneToFive;
                subStat = hatStatInfoDTO.subStat + starForce * statIncrementOneToFive;
                atMgPower = hatStatInfoDTO.atMgPower;
            } else if (starForce < 16) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive)

                        + (starForce - 5) * statIncrementSixToFifteen;
                subStat = hatStatInfoDTO.subStat + 10 + (starForce - 5) * statIncrementSixToFifteen;

            } else if (starForce < 22) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel150;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel150;
                atMgIncrement = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel150 + i - 16);
                }

                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement;
            } else if (starForce < 23) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel150;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel150;
                atMgIncrement = 0;
                atMgIncrementAdd = 0;
                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel150 + i - 16);
                }
                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            } else if (starForce < 26) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel150);
                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel150);
                atMgIncrement = 0;
                atMgIncrementAdd = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel150 + i - 16);

                }

                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            }


            mainStat = mainStat + itemUpgradeMainStat + itemAddOptionMainStat;
            subStat = subStat + itemUpgradeSubStat;
            atMgPower = atMgPower + itemUpgradeAtMg;
//
//// 계산된 능력치를 설정
            hatStatInfoDTO.setMainStat(mainStat);
            hatStatInfoDTO.setSubStat(subStat);
            hatStatInfoDTO.setAtMgPower(atMgPower);
        } else if (itemLevel == 160) {
            if (starForce < 6) {
                mainStat = hatStatInfoDTO.mainStat + starForce * statIncrementOneToFive;
                subStat = hatStatInfoDTO.subStat + starForce * statIncrementOneToFive;
                atMgPower = hatStatInfoDTO.atMgPower;
            } else if (starForce < 16) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive)

                        + (starForce - 5) * statIncrementSixToFifteen;
                subStat = hatStatInfoDTO.subStat + 10 + (starForce - 5) * statIncrementSixToFifteen;

            } else if (starForce < 22) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel160;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel160;
                atMgIncrement = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel160 + i - 16);
                }

                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement;
            } else if (starForce < 23) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel160;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel160;
                atMgIncrement = 0;
                atMgIncrementAdd = 0;
                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel160 + i - 16);
                }
                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            } else if (starForce < 26) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel150);
                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel150);
                atMgIncrement = 0;
                atMgIncrementAdd = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel150 + i - 16);

                }

                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            }

            mainStat = mainStat + itemUpgradeMainStat+ itemAddOptionMainStat;
            subStat = subStat + itemUpgradeSubStat;
            atMgPower = atMgPower + itemUpgradeAtMg;

// 계산된 능력치를 설정
            hatStatInfoDTO.setMainStat(mainStat);
            hatStatInfoDTO.setSubStat(subStat);
            hatStatInfoDTO.setAtMgPower(atMgPower);
        } else if (itemLevel == 200) {
            if (starForce < 6) {
                mainStat = hatStatInfoDTO.mainStat + starForce * statIncrementOneToFive;
                subStat = hatStatInfoDTO.subStat + starForce * statIncrementOneToFive;
                atMgPower = hatStatInfoDTO.atMgPower;
            } else if (starForce < 16) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive)

                        + (starForce - 5) * statIncrementSixToFifteen;
                subStat = hatStatInfoDTO.subStat + 10 + (starForce - 5) * statIncrementSixToFifteen;

            } else if (starForce < 22) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;
                atMgIncrement = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel200 + i - 16);
                }

                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement;
            } else if (starForce < 23) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;
                atMgIncrement = 0;
                atMgIncrementAdd = 0;
                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel200 + i - 16);
                }
                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            } else if (starForce < 26) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel200);
                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel200);
                atMgIncrement = 0;
                atMgIncrementAdd = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel200 + i - 16);

                }

                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            }
            mainStat = mainStat + itemUpgradeMainStat+ itemAddOptionMainStat;
            subStat = subStat + itemUpgradeSubStat;
            atMgPower = atMgPower + itemUpgradeAtMg;

// 계산된 능력치를 설정
            hatStatInfoDTO.setMainStat(mainStat);
            hatStatInfoDTO.setSubStat(subStat);
            hatStatInfoDTO.setAtMgPower(atMgPower);


        } else if (itemLevel == 250) {
            if (starForce < 6) {
                mainStat = hatStatInfoDTO.mainStat + starForce * statIncrementOneToFive;
                subStat = hatStatInfoDTO.subStat + starForce * statIncrementOneToFive;
                atMgPower = hatStatInfoDTO.atMgPower;
            } else if (starForce < 16) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive)

                        + (starForce - 5) * statIncrementSixToFifteen;
                subStat = hatStatInfoDTO.subStat + 10 + (starForce - 5) * statIncrementSixToFifteen;

            } else if (starForce < 22) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel250;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel250;
                atMgIncrement = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel250 + i - 16);
                }

                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement;
            } else if (starForce < 23) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel250;

                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel250;
                atMgIncrement = 0;
                atMgIncrementAdd = 0;
                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel250 + i - 16);
                }
                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            } else if (starForce < 26) {
                mainStat = hatStatInfoDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel250);
                subStat = hatStatInfoDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel250);
                atMgIncrement = 0;
                atMgIncrementAdd = 0;

                for (int i = 16; i <= starForce; i++) {
                    atMgIncrement += (atMgIncrementLevel250 + i - 16);

                }

                for (int i = 22; i <= starForce; i++) {
                    atMgIncrementAdd += i - 21;
                }
                atMgPower = hatStatInfoDTO.atMgPower + atMgIncrement + atMgIncrementAdd;
            }

            mainStat = mainStat + itemUpgradeMainStat+ itemAddOptionMainStat;
            subStat = subStat + itemUpgradeSubStat;
            atMgPower = atMgPower + itemUpgradeAtMg;

// 계산된 능력치를 설정
            hatStatInfoDTO.setMainStat(mainStat);
            hatStatInfoDTO.setSubStat(subStat);
            hatStatInfoDTO.setAtMgPower(atMgPower);
            hatStatInfoDTO.setAllStat(hatStatInfoDTO.getAllStat()+itemAddOptionAllStat);
            hatStatInfoDTO.setPotentialTotalMainStatPer(potentialTotalMainStatPer+itemAddOptionAllStat);
            hatStatInfoDTO.setPotentialTotalSubStatPer(potentialTotalSubStatPer+itemAddOptionAllStat);
            hatStatInfoDTO.setPotentialTotalAtMgPower(potentialTotalAtMgPower);

        }


    }
}
