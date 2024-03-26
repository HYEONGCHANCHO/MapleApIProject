package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersItemEquip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

//    @Column(nullable = false)
//    private String date;

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




    public String getCharactersName() {
        return charactersName;
    }

//    public String getDate() {
//        return date;
//    }

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

    protected CharactersItemEquip(){}

    public CharactersItemEquip(String charactersName,  String hatInfo, String topInfo, String bottomInfo, String capeInfo, String shoesInfo, String glovesInfo, String shoulderInfo, String faceInfo, String eyeInfo, String earInfo, String pendantOneInfo, String pendantTwoInfo, String beltInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String weaponInfo, String subWeaponInfo, String emblemInfo, String badgeInfo, String medalInfo, String poketInfo, String heartInfo) {
        this.charactersName = charactersName;
//        this.date = date;
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
    }
}
