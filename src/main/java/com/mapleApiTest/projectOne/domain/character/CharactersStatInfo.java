package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersStatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

//    @Column(nullable = false)
//    private String date;


//    @Column(columnDefinition = "json")
    private double damage;


//        @Column(columnDefinition = "json")
    private double bossDamage;


//        @Column(columnDefinition = "json")
    private double finalDamage;


//        @Column(columnDefinition = "json")
    private double ignoreRate;


//        @Column(columnDefinition = "json")
    private double criticalDamage;

//    @Column(columnDefinition = "json")
    private int str;

//    @Column(columnDefinition = "json")
    private int dex;

//    @Column(columnDefinition = "json")
    private int intel;

//    @Column(columnDefinition = "json")
    private int luk;
//    @Column(columnDefinition = "json")
    private int hp;

//    @Column(columnDefinition = "json")
    private int attackPower;
//    @Column(columnDefinition = "json")
    private int magicPower;

//    @Column(columnDefinition = "json")
    private int combatPower;

    protected CharactersStatInfo(){}

    public CharactersStatInfo(String charactersName, double damage, double bossDamage, double finalDamage, double ignoreRate, double criticalDamage, int str, int dex, int intel, int luk, int hp, int attackPower, int magicPower, int combatPower) {
        this.charactersName = charactersName;
//        this.date = date;
        this.damage = damage;
        this.bossDamage = bossDamage;
        this.finalDamage = finalDamage;
        this.ignoreRate = ignoreRate;
        this.criticalDamage = criticalDamage;
        this.str = str;
        this.dex = dex;
        this.intel = intel;
        this.luk = luk;
        this.hp = hp;
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.combatPower = combatPower;
    }

    public String getCharactersName() {
        return charactersName;
    }

//    public String getDate() {
//        return date;
//    }

    public double getDamage() {
        return damage;
    }

    public double getBossDamage() {
        return bossDamage;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getIgnoreRate() {
        return ignoreRate;
    }

    public double getCriticalDamage() {
        return criticalDamage;
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

    public int getAttackPower() {
        return attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getCombatPower() {
        return combatPower;
    }
}
