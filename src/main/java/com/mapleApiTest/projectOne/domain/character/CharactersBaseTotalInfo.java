package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersBaseTotalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;
    @Column(nullable = false)
    private String charactersName;
    private  int addAllStat;
    private double addBossDamage;
    private  int addAtMgPower;
    private  int petAtMgPower;
    private  int mainStatBase;
    private  int mainStatSkill;
    private  int mainStatPerBase;
    private  int mainStatPerSkill;
    private  int mainStatNonPer;
    private  int subStatBase;
    private  int subStatSkill;
    private  int subStatPerBase;
    private  int subStatPerSkill;
    private  int subStatNonPer;
    private  int atMgPowerBase;
    private  int atMgPowerSkill;
    private  int atMgPowerPerBase;
    private  int atMgPowerPerSkill;
    private  double criticalDamageBase;
    private  double criticalDamageSkill;
    private  double damageBase;
    private  double damageSkill;
    private  double BossDamageBase;
    private  double BossDamageSkill;
    private  boolean isFree;

    protected CharactersBaseTotalInfo(){};

    public CharactersBaseTotalInfo(String charactersName, int addAllStat, double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, double criticalDamageBase, double criticalDamageSkill, double damageBase, double damageSkill, double bossDamageBase, double bossDamageSkill, boolean isFree) {
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

    public int getAddAllStat() {
        return addAllStat;
    }

    public double getAddBossDamage() {
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

    public int getMainStatNonPer() {
        return mainStatNonPer;
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

    public int getSubStatNonPer() {
        return subStatNonPer;
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

    public double getCriticalDamageBase() {
        return criticalDamageBase;
    }

    public double getCriticalDamageSkill() {
        return criticalDamageSkill;
    }

    public double getDamageBase() {
        return damageBase;
    }

    public double getDamageSkill() {
        return damageSkill;
    }

    public double getBossDamageBase() {
        return BossDamageBase;
    }

    public double getBossDamageSkill() {
        return BossDamageSkill;
    }

    public boolean isFree() {
        return isFree;
    }
}
