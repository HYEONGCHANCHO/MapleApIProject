package com.mapleApiTest.projectOne.dto.character.request;


public class GetCharactersTotalChangedInfoDTO {

    private int addAllStat;
    private Double addBossDamage;
    private int addAtMgPower;
    private int petAtMgPower;

    private int mainStatBase;
    private int mainStatSkill;



    private int mainStatPerBase;
    private int mainStatPerSkill;
    private int mainStatNonPer;
    private int subStatBase;
    private int subStatSkill;

    private int subStatPerBase;
    private int subStatPerSkill;

    private int subStatNonPer;


    private int atMgPowerBase;
    private int atMgPowerSkill;

    private int atMgPowerPerBase;
    private int atMgPowerPerSkill;

    private Double criticalDamageBase;
    private Double criticalDamageSkill;

    private Double damageBase;
    private Double damageSkill;

    private Double BossDamageBase;
    private Double BossDamageSkill;

    boolean isFree;

    public int getAddAllStat() {
        return addAllStat;
    }

    public Double getAddBossDamage() {
        return addBossDamage;
    }

    public int getAddAtMgPower() {
        return addAtMgPower;
    }

    public int getPetAtMgPower() {
        return petAtMgPower;
    }

    public int getMainStatBase() {
        return mainStatBase;
    }

    public int getMainStatSkill() {
        return mainStatSkill;
    }

    public int getMainStatPerBase() {
        return mainStatPerBase;
    }

    public int getMainStatPerSkill() {
        return mainStatPerSkill;
    }

    public int getSubStatBase() {
        return subStatBase;
    }

    public int getSubStatSkill() {
        return subStatSkill;
    }

    public int getSubStatPerBase() {
        return subStatPerBase;
    }

    public int getSubStatPerSkill() {
        return subStatPerSkill;
    }

    public int getAtMgPowerBase() {
        return atMgPowerBase;
    }

    public int getAtMgPowerSkill() {
        return atMgPowerSkill;
    }

    public int getAtMgPowerPerBase() {
        return atMgPowerPerBase;
    }

    public int getAtMgPowerPerSkill() {
        return atMgPowerPerSkill;
    }

    public Double getCriticalDamageBase() {
        return criticalDamageBase;
    }

    public Double getCriticalDamageSkill() {
        return criticalDamageSkill;
    }

    public Double getDamageBase() {
        return damageBase;
    }

    public Double getDamageSkill() {
        return damageSkill;
    }

    public Double getBossDamageBase() {
        return BossDamageBase;
    }

    public Double getBossDamageSkill() {
        return BossDamageSkill;
    }

    public boolean isFree() {
        return isFree;
    }

    public int getMainStatNonPer() {
        return mainStatNonPer;
    }

    public int getSubStatNonPer() {
        return subStatNonPer;
    }

    public GetCharactersTotalChangedInfoDTO(int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double bossDamageBase, Double bossDamageSkill, boolean isFree) {
        this.addAllStat = addAllStat;
        this.addBossDamage = addBossDamage;
        this.addAtMgPower = addAtMgPower;
        this.petAtMgPower = petAtMgPower;
        this.mainStatBase = mainStatBase;
        this.mainStatSkill = mainStatSkill;
        this.mainStatPerBase = mainStatPerBase;
        this.mainStatPerSkill = mainStatPerSkill;
        this.mainStatNonPer = mainStatNonPer;
        this.subStatBase = subStatBase;
        this.subStatSkill = subStatSkill;
        this.subStatPerBase = subStatPerBase;
        this.subStatPerSkill = subStatPerSkill;
        this.subStatNonPer = subStatNonPer;
        this.atMgPowerBase = atMgPowerBase;
        this.atMgPowerSkill = atMgPowerSkill;
        this.atMgPowerPerBase = atMgPowerPerBase;
        this.atMgPowerPerSkill = atMgPowerPerSkill;
        this.criticalDamageBase = criticalDamageBase;
        this.criticalDamageSkill = criticalDamageSkill;
        this.damageBase = damageBase;
        this.damageSkill = damageSkill;
        BossDamageBase = bossDamageBase;
        BossDamageSkill = bossDamageSkill;
        this.isFree = isFree;
    }
}
