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

    @Column(columnDefinition = "json")
    private String final_stat;
//
//    @Column(nullable = false)
//    private String damegeSum;
//    @Column(nullable = false)
//    private String str;
//
//    @Column(nullable = false)
//    private String dex;
//
//    @Column(nullable = false)
//    private String intel;
//
//    @Column(nullable = false)
//    private String luk;
//    @Column(nullable = false)
//    private String hp;
//
//    @Column(nullable = false)
//    private String attackPower;
//    @Column(nullable = false)
//    private String magicPower;
//
//    @Column(nullable = false)
//    private String combatPower;

    protected CharactersStatInfo(){}

    public Long getId() {
        return id;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getFinal_stat() {
        return final_stat;
    }

    public CharactersStatInfo(String charactersName, String date, String final_stat) {
        this.charactersName = charactersName;
        this.date = date;
        this.final_stat = final_stat;
    }

    //    public String getDamegeSum() {
//        return damegeSum;
//    }
//
//    public String getStr() {
//        return str;
//    }
//
//    public String getDex() {
//        return dex;
//    }
//
//    public String getIntel() {
//        return intel;
//    }
//
//    public String getLuk() {
//        return luk;
//    }
//
//    public String getHp() {
//        return hp;
//    }
//
//    public String getAttackPower() {
//        return attackPower;
//    }
//
//    public String getMagicPower() {
//        return magicPower;
//    }
//
//    public String getCombatPower() {
//        return combatPower;
//    }
}
