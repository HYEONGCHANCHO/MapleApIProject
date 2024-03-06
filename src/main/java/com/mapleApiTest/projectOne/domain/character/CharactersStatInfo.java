package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersStatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String date;

//    @Column(columnDefinition = "json")
//    private String final_stat;
////
    @Column(columnDefinition = "json")
    private String damage;


        @Column(columnDefinition = "json")
    private String bossDamage;


        @Column(columnDefinition = "json")
    private String finalDamage;


        @Column(columnDefinition = "json")
    private String ignoreRate;


        @Column(columnDefinition = "json")
    private String criticalDamage;

    @Column(columnDefinition = "json")
    private String str;

    @Column(columnDefinition = "json")
    private String dex;

    @Column(columnDefinition = "json")
    private String intel;

    @Column(columnDefinition = "json")
    private String luk;
    @Column(columnDefinition = "json")
    private String hp;

    @Column(columnDefinition = "json")
    private String attackPower;
    @Column(columnDefinition = "json")
    private String magicPower;

    @Column(columnDefinition = "json")
    private String combatPower;

    protected CharactersStatInfo(){}

    public CharactersStatInfo(String charactersName, String date, String damage, String bossDamage, String finalDamage, String ignoreRate, String criticalDamage, String str, String dex, String intel, String luk, String hp, String attackPower, String magicPower, String combatPower) {
        this.charactersName = charactersName;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public String getDamage() {
        return damage;
    }

    public String getBossDamage() {
        return bossDamage;
    }

    public String getFinalDamage() {
        return finalDamage;
    }

    public String getIgnoreRate() {
        return ignoreRate;
    }

    public String getCriticalDamage() {
        return criticalDamage;
    }

    public String getStr() {
        return str;
    }

    public String getDex() {
        return dex;
    }

    public String getIntel() {
        return intel;
    }

    public String getLuk() {
        return luk;
    }

    public String getHp() {
        return hp;
    }

    public String getAttackPower() {
        return attackPower;
    }

    public String getMagicPower() {
        return magicPower;
    }

    public String getCombatPower() {
        return combatPower;
    }
}
