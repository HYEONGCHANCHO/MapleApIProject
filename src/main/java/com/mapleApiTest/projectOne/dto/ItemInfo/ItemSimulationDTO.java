package com.mapleApiTest.projectOne.dto.ItemInfo;

import java.util.stream.IntStream;

public class ItemSimulationDTO {

    int statIncrementOneToFive = 2;
    int statIncrementSixToFifteen = 3;
    //    int statIncrementFifteenToTwentyTwo = 15;
    int statIncrementFifteenToTwenty130 = 7;
    int statIncrementFifteenToTwentyTwoLevel140 = 9;
    int statIncrementFifteenToTwentyTwoLevel150 = 11;
    int statIncrementFifteenToTwentyTwoLevel160 = 13;
    int statIncrementFifteenToTwentyTwoLevel200 = 15;

    int atMgIncrementLevel130 = 7;
    int atMgIncrementLevel140 = 8;
    int atMgIncrementLevel150 = 9;
    int atMgIncrementLevel160 = 10;
    int atMgIncrementLevel200 = 12;

    int mainStat;
    int subStat;
    int atMgPower;

    int atMgIncrement;
int atMgIncrementAdd;

    public void calculateEquipmentStats(ArcaneHatDTO arcaneHatDTO, int starForce) {
        if (starForce < 6) {
            mainStat = arcaneHatDTO.mainStat + starForce * statIncrementOneToFive;
            subStat = arcaneHatDTO.subStat + starForce * statIncrementOneToFive;
            atMgPower = arcaneHatDTO.atMgPower;
        } else if (starForce < 16) {
            mainStat = arcaneHatDTO.mainStat + (5 * statIncrementOneToFive)

                    + (starForce - 5) * statIncrementSixToFifteen;
            subStat = arcaneHatDTO.subStat + 10 + (starForce - 5) * statIncrementSixToFifteen;

        } else if (arcaneHatDTO.getItemLevel() == 200 && starForce < 22) {
            mainStat = arcaneHatDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;

            subStat = arcaneHatDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;
            atMgIncrement=0;

            for (int i = 16; i <= starForce; i++) {
                atMgIncrement += (atMgIncrementLevel200 + i - 16);
            }

            atMgPower = arcaneHatDTO.atMgPower + atMgIncrement;
        } else if (arcaneHatDTO.getItemLevel() == 200 && starForce <23) {
            mainStat = arcaneHatDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;

            subStat = arcaneHatDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (starForce - 15) * statIncrementFifteenToTwentyTwoLevel200;
            atMgIncrement=0;
            atMgIncrementAdd=0;
            for (int i = 16; i <= starForce; i++) {
                atMgIncrement += (atMgIncrementLevel200 + i - 16);
            }
  for (int i = 21; i <= starForce; i++) {
      atMgIncrementAdd += (i-20)*2-1;
            }


            atMgPower = arcaneHatDTO.atMgPower + atMgIncrement + atMgIncrementAdd;

        }

        else if (arcaneHatDTO.getItemLevel() == 200 && starForce < 26) {
            mainStat = arcaneHatDTO.mainStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel200);
            subStat = arcaneHatDTO.subStat + (5 * statIncrementOneToFive) + (10 * statIncrementSixToFifteen) + (7 * statIncrementFifteenToTwentyTwoLevel200);
            atMgIncrement=0;
            for (int i = 16; i <= starForce; i++) {
                atMgIncrement += (atMgIncrementLevel200 + i - 16);

            }

            for (int i = 21; i <= starForce; i++) {
                atMgIncrementAdd += (i-20)*2-1;
            }

            atMgPower = arcaneHatDTO.atMgPower + atMgIncrement + atMgIncrementAdd;



        }
// 계산된 능력치를 설정
        arcaneHatDTO.setMainStat(mainStat);
        arcaneHatDTO.setSubStat(subStat);
        arcaneHatDTO.setAtMgPower(atMgPower);

    }}
