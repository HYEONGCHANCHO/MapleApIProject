package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersItemEquip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(columnDefinition = "json")
    private String hatInfo;
    @Column(columnDefinition = "json")
    private String topInfo;
    @Column(columnDefinition = "json")
    private String bottomInfo;
    @Column(columnDefinition = "json")
    private String capeInfo;
    @Column(columnDefinition = "json")
    private String shoesInfo;
    @Column(columnDefinition = "json")
    private String glovesInfo;
    @Column(columnDefinition = "json")
    private String shoulderInfo;
    @Column(columnDefinition = "json")
    private String faceInfo;
    @Column(columnDefinition = "json")
    private String eyeInfo;
    @Column(columnDefinition = "json")
    private String earInfo;
    @Column(columnDefinition = "json")
    private String pendantOneInfo;
    @Column(columnDefinition = "json")
    private String pendantTwoInfo;
    @Column(columnDefinition = "json")
    private String beltInfo;
    @Column(columnDefinition = "json")
    private String ringOneInfo;
    @Column(columnDefinition = "json")
    private String ringTwoInfo;
    @Column(columnDefinition = "json")
    private String ringThreeInfo;
    @Column(columnDefinition = "json")
    private String ringFourInfo;
    @Column(columnDefinition = "json")
    private String weaponInfo;
    @Column(columnDefinition = "json")
    private String subWeaponInfo;
    @Column(columnDefinition = "json")
    private String emblemInfo;
    @Column(columnDefinition = "json")
    private String badgeInfo;
    @Column(columnDefinition = "json")
    private String medalInfo;
    @Column(columnDefinition = "json")
    private String poketInfo;
    @Column(columnDefinition = "json")
    private String heartInfo;

   @Column(columnDefinition = "json")
    private String titleInfo;
   @Column(columnDefinition = "json")
    private String dragonHat;
   @Column(columnDefinition = "json")
    private String dragonPendant;
   @Column(columnDefinition = "json")
    private String dragonWing;
   @Column(columnDefinition = "json")
    private String dragonTail;
   @Column(columnDefinition = "json")
    private String mechanicEngine;
   @Column(columnDefinition = "json")
    private String mechanicArm;
   @Column(columnDefinition = "json")
    private String mechanicLeg;
   @Column(columnDefinition = "json")
    private String mechanicTran;






    public String getCharactersName() {
        return charactersName;
    }

    public String getHatInfo() {
        return hatInfo;
    }

    public String getFaceInfo() {
        return faceInfo;
    }

    public String getEyeInfo() {
        return eyeInfo;
    }

    public String getEarInfo() {
        return earInfo;
    }

    public String getTopInfo() {
        return topInfo;
    }

    public String getBottomInfo() {
        return bottomInfo;
    }

    public String getShoesInfo() {
        return shoesInfo;
    }

    public String getGlovesInfo() {
        return glovesInfo;
    }

    public String getSubWeaponInfo() {
        return subWeaponInfo;
    }

    public String getWeaponInfo() {
        return weaponInfo;
    }

    public String getRingOneInfo() {
        return ringOneInfo;
    }

    public String getRingTwoInfo() {
        return ringTwoInfo;
    }

    public String getRingThreeInfo() {
        return ringThreeInfo;
    }

    public String getRingFourInfo() {
        return ringFourInfo;
    }

    public String getPendantOneInfo() {
        return pendantOneInfo;
    }

    public String getMedalInfo() {
        return medalInfo;
    }

    public String getShoulderInfo() {
        return shoulderInfo;
    }

    public String getPoketInfo() {
        return poketInfo;
    }

    public String getHeartInfo() {
        return heartInfo;
    }

    public String getBadgeInfo() {
        return badgeInfo;
    }

    public String getEmblemInfo() {
        return emblemInfo;
    }

    public String getPendantTwoInfo() {
        return pendantTwoInfo;
    }

    public String getCapeInfo() {
        return capeInfo;
    }

    public String getBeltInfo() {
        return beltInfo;
    }

    public String getTitleInfo() {
        return titleInfo;
    }

    public String getDragonHat() {
        return dragonHat;
    }

    public String getDragonPendant() {
        return dragonPendant;
    }

    public String getDragonWing() {
        return dragonWing;
    }

    public String getDragonTail() {
        return dragonTail;
    }

    public String getMechanicEngine() {
        return mechanicEngine;
    }

    public String getMechanicArm() {
        return mechanicArm;
    }

    public String getMechanicLeg() {
        return mechanicLeg;
    }

    public String getMechanicTran() {
        return mechanicTran;
    }

    protected CharactersItemEquip(){}

    public CharactersItemEquip(String charactersName, String hatInfo, String topInfo, String bottomInfo, String capeInfo, String shoesInfo, String glovesInfo, String shoulderInfo, String faceInfo, String eyeInfo, String earInfo, String pendantOneInfo, String pendantTwoInfo, String beltInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String weaponInfo, String subWeaponInfo, String emblemInfo, String badgeInfo, String medalInfo, String poketInfo, String heartInfo, String titleInfo, String dragonHat, String dragonPendant, String dragonWing, String dragonTail, String mechanicEngine, String mechanicArm, String mechanicLeg, String mechanicTran) {
        this.charactersName = charactersName;
        this.hatInfo = hatInfo;
        this.topInfo = topInfo;
        this.bottomInfo = bottomInfo;
        this.capeInfo = capeInfo;
        this.shoesInfo = shoesInfo;
        this.glovesInfo = glovesInfo;
        this.shoulderInfo = shoulderInfo;
        this.faceInfo = faceInfo;
        this.eyeInfo = eyeInfo;
        this.earInfo = earInfo;
        this.pendantOneInfo = pendantOneInfo;
        this.pendantTwoInfo = pendantTwoInfo;
        this.beltInfo = beltInfo;
        this.ringOneInfo = ringOneInfo;
        this.ringTwoInfo = ringTwoInfo;
        this.ringThreeInfo = ringThreeInfo;
        this.ringFourInfo = ringFourInfo;
        this.weaponInfo = weaponInfo;
        this.subWeaponInfo = subWeaponInfo;
        this.emblemInfo = emblemInfo;
        this.badgeInfo = badgeInfo;
        this.medalInfo = medalInfo;
        this.poketInfo = poketInfo;
        this.heartInfo = heartInfo;
        this.titleInfo = titleInfo;
        this.dragonHat = dragonHat;
        this.dragonPendant = dragonPendant;
        this.dragonWing = dragonWing;
        this.dragonTail = dragonTail;
        this.mechanicEngine = mechanicEngine;
        this.mechanicArm = mechanicArm;
        this.mechanicLeg = mechanicLeg;
        this.mechanicTran = mechanicTran;
    }
}
