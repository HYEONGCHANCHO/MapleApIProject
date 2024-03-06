package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersItemEquip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String date;

//    @Column(columnDefinition = "json")
//    private String item_equipment;

    @Column(columnDefinition = "json")
    private String hatInfo;
    @Column(columnDefinition = "json")
    private String faceInfo;

    @Column(columnDefinition = "json")
    private String eyeInfo;
    @Column(columnDefinition = "json")
    private String earInfo;
    @Column(columnDefinition = "json")
    private String topInfo;
    @Column(columnDefinition = "json")
    private String bottomInfo;
    @Column(columnDefinition = "json")
    private String shoesInfo;
    @Column(columnDefinition = "json")
    private String glovesInfo;

    @Column(columnDefinition = "json")
    private String capeInfo;

    @Column(columnDefinition = "json")
    private String subWeaponInfo;

    @Column(columnDefinition = "json")
    private String weaponInfo;
    @Column(columnDefinition = "json")
    private String ringOneInfo;
    @Column(columnDefinition = "json")
    private String ringTwoInfo;
    @Column(columnDefinition = "json")
    private String ringThreeInfo;
    @Column(columnDefinition = "json")
    private String ringFourInfo;
    @Column(columnDefinition = "json")
    private String pendantOneInfo;

    @Column(columnDefinition = "json")
    private String medalInfo;

    @Column(columnDefinition = "json")
    private String shoulderInfo;

    @Column(columnDefinition = "json")
    private String poketInfo;

    @Column(columnDefinition = "json")
    private String heartInfo;

    @Column(columnDefinition = "json")
    private String badgeInfo;

    @Column(columnDefinition = "json")
    private String emblemInfo;
    @Column(columnDefinition = "json")
    private String pendantTwoInfo;

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

//    public String getItem_equipment() {
//        return item_equipment;
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

    protected CharactersItemEquip(){}

    public CharactersItemEquip(String charactersName, String date, String hatInfo,String faceInfo, String eyeInfo, String earInfo, String topInfo, String bottomInfo, String shoesInfo, String glovesInfo, String subWeaponInfo, String weaponInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String pendantOneInfo, String medalInfo, String shoulderInfo, String poketInfo, String heartInfo, String badgeInfo, String emblemInfo, String pendantTwoInfo) {
        this.charactersName = charactersName;
        this.date = date;
//        this.item_equipment = item_equipment;
        this.hatInfo = hatInfo;
        this.faceInfo = faceInfo;
        this.eyeInfo = eyeInfo;
        this.earInfo = earInfo;
        this.topInfo = topInfo;
        this.bottomInfo = bottomInfo;
        this.shoesInfo = shoesInfo;
        this.glovesInfo = glovesInfo;
        this.subWeaponInfo = subWeaponInfo;
        this.weaponInfo = weaponInfo;
        this.ringOneInfo = ringOneInfo;
        this.ringTwoInfo = ringTwoInfo;
        this.ringThreeInfo = ringThreeInfo;
        this.ringFourInfo = ringFourInfo;
        this.pendantOneInfo = pendantOneInfo;
        this.medalInfo = medalInfo;
        this.shoulderInfo = shoulderInfo;
        this.poketInfo = poketInfo;
        this.heartInfo = heartInfo;
        this.badgeInfo = badgeInfo;
        this.emblemInfo = emblemInfo;
        this.pendantTwoInfo = pendantTwoInfo;
    }
}
