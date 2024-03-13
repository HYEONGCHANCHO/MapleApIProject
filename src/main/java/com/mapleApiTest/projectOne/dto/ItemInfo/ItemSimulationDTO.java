package com.mapleApiTest.projectOne.dto.ItemInfo;

public class ItemSimulationDTO {

    int statIncrementOneToFive = 2;
    int statIncrementSixToFifteen = 3;
    int statIncrementFifteenToTwentyTwo = 15;
    public static void calculateEquipmentStats(ArcaneHatDTO arcaneHatDTO, int starforce) {
        int mainStat = arcaneHatDTO.mainStat + starforce * equipment.getStarforceIncrementMainStat();
        int subStat = equipment.getBaseSubStat() + starforce * equipment.getStarforceIncrementSubStat();
        int attackPower = equipment.getBaseAttackPower() + starforce * equipment.getStarforceIncrementAttackPower();

        // 계산된 능력치를 설정
        equipment.setMainStat(mainStat);
        equipment.setSubStat(subStat);
        equipment.setAttackPower(attackPower);
    }




}
