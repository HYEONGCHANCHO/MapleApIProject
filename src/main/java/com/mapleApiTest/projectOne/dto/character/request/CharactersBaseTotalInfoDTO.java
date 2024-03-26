package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersBaseTotalInfoDTO {

    private String charactersName;
    int addAllStat;
    Double addBossDamage;
    int addAtMgPower;
    int petAtMgPower;
    int mainStatBase;
    int mainStatSkill;
    int mainStatPerBase;
    int mainStatPerSkill;
    int mainStatNonPer;
    int subStatBase;
    int subStatSkill;
    int subStatPerBase;
    int subStatPerSkill;
    int subStatNonPer;
    int atMgPowerBase;
    int atMgPowerSkill;
    int atMgPowerPerBase;
    int atMgPowerPerSkill;
    Double criticalDamageBase;
    Double criticalDamageSkill;
    Double damageBase;
    Double damageSkill;
    Double BossDamageBase;
    Double BossDamageSkill;
    boolean isFree;

    public CharactersBaseTotalInfoDTO(String charactersName, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double bossDamageBase, Double bossDamageSkill, boolean isFree) {
        this.charactersName = charactersName;
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

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }


    public int getAddAllStat() {
        return addAllStat;
    }

    public void setAddAllStat(int addAllStat) {
        this.addAllStat = addAllStat;
    }

    public Double getAddBossDamage() {
        return addBossDamage;
    }

    public void setAddBossDamage(Double addBossDamage) {
        this.addBossDamage = addBossDamage;
    }

    public int getAddAtMgPower() {
        return addAtMgPower;
    }

    public void setAddAtMgPower(int addAtMgPower) {
        this.addAtMgPower = addAtMgPower;
    }

    public int getPetAtMgPower() {
        return petAtMgPower;
    }

    public void setPetAtMgPower(int petAtMgPower) {
        this.petAtMgPower = petAtMgPower;
    }

    public int getMainStatBase() {
        return mainStatBase;
    }

    public void setMainStatBase(int mainStatBase) {
        this.mainStatBase = mainStatBase;
    }

    public int getMainStatSkill() {
        return mainStatSkill;
    }

    public void setMainStatSkill(int mainStatSkill) {
        this.mainStatSkill = mainStatSkill;
    }

    public int getMainStatPerBase() {
        return mainStatPerBase;
    }

    public void setMainStatPerBase(int mainStatPerBase) {
        this.mainStatPerBase = mainStatPerBase;
    }

    public int getMainStatPerSkill() {
        return mainStatPerSkill;
    }

    public void setMainStatPerSkill(int mainStatPerSkill) {
        this.mainStatPerSkill = mainStatPerSkill;
    }

    public int getMainStatNonPer() {
        return mainStatNonPer;
    }

    public void setMainStatNonPer(int mainStatNonPer) {
        this.mainStatNonPer = mainStatNonPer;
    }

    public int getSubStatBase() {
        return subStatBase;
    }

    public void setSubStatBase(int subStatBase) {
        this.subStatBase = subStatBase;
    }

    public int getSubStatSkill() {
        return subStatSkill;
    }

    public void setSubStatSkill(int subStatSkill) {
        this.subStatSkill = subStatSkill;
    }

    public int getSubStatPerBase() {
        return subStatPerBase;
    }

    public void setSubStatPerBase(int subStatPerBase) {
        this.subStatPerBase = subStatPerBase;
    }

    public int getSubStatPerSkill() {
        return subStatPerSkill;
    }

    public void setSubStatPerSkill(int subStatPerSkill) {
        this.subStatPerSkill = subStatPerSkill;
    }

    public int getSubStatNonPer() {
        return subStatNonPer;
    }

    public void setSubStatNonPer(int subStatNonPer) {
        this.subStatNonPer = subStatNonPer;
    }

    public int getAtMgPowerBase() {
        return atMgPowerBase;
    }

    public void setAtMgPowerBase(int atMgPowerBase) {
        this.atMgPowerBase = atMgPowerBase;
    }

    public int getAtMgPowerSkill() {
        return atMgPowerSkill;
    }

    public void setAtMgPowerSkill(int atMgPowerSkill) {
        this.atMgPowerSkill = atMgPowerSkill;
    }

    public int getAtMgPowerPerBase() {
        return atMgPowerPerBase;
    }

    public void setAtMgPowerPerBase(int atMgPowerPerBase) {
        this.atMgPowerPerBase = atMgPowerPerBase;
    }

    public int getAtMgPowerPerSkill() {
        return atMgPowerPerSkill;
    }

    public void setAtMgPowerPerSkill(int atMgPowerPerSkill) {
        this.atMgPowerPerSkill = atMgPowerPerSkill;
    }

    public Double getCriticalDamageBase() {
        return criticalDamageBase;
    }

    public void setCriticalDamageBase(Double criticalDamageBase) {
        this.criticalDamageBase = criticalDamageBase;
    }

    public Double getCriticalDamageSkill() {
        return criticalDamageSkill;
    }

    public void setCriticalDamageSkill(Double criticalDamageSkill) {
        this.criticalDamageSkill = criticalDamageSkill;
    }

    public Double getDamageBase() {
        return damageBase;
    }

    public void setDamageBase(Double damageBase) {
        this.damageBase = damageBase;
    }

    public Double getDamageSkill() {
        return damageSkill;
    }

    public void setDamageSkill(Double damageSkill) {
        this.damageSkill = damageSkill;
    }

    public Double getBossDamageBase() {
        return BossDamageBase;
    }

    public void setBossDamageBas(Double bossDamageBas) {
        BossDamageBase = bossDamageBas;
    }

    public Double getBossDamageSkill() {
        return BossDamageSkill;
    }

    public void setBossDamageSkill(Double bossDamageSkill) {
        BossDamageSkill = bossDamageSkill;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
