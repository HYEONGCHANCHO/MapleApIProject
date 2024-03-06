package com.mapleApiTest.projectOne.dto.character.request;


import javax.persistence.Column;

public class CharactersItemEquipDTO {

    private String charactersName;

    private String date;
//    private String item_equipment;
    private String hatInfo;
    private String faceInfo;

    private String eyeInfo;
    private String earInfo;
    private String topInfo;
    private String bottomInfo;
    private String shoesInfo;
    private String glovesInfo;

    private String subWeaponInfo;

    private String weaponInfo;
    private String ringOneInfo;
    private String ringTwoInfo;
    private String ringThreeInfo;
    private String ringFourInfo;
    private String pendantOneInfo;

    private String medalInfo;

    private String shoulderInfo;

    private String poketInfo;

    private String heartInfo;

    private String badgeInfo;

    private String emblemInfo;
    private String pendantTwoInfo;


    public CharactersItemEquipDTO(String charactersName, String date, String hatInfo,String faceInfo, String eyeInfo, String earInfo, String topInfo, String bottomInfo, String shoesInfo, String glovesInfo, String subWeaponInfo, String weaponInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String pendantOneInfo, String medalInfo, String shoulderInfo, String poketInfo, String heartInfo, String badgeInfo, String emblemInfo, String pendantTwoInfo) {
        this.charactersName = charactersName;
        this.date = date;
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

    public String getHatInfo() {
        return hatInfo;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

//    public String getItem_equipment() {
//        return item_equipment;
//    }



}
