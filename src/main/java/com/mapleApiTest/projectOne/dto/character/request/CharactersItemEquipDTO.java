package com.mapleApiTest.projectOne.dto.character.request;


import javax.persistence.Column;

public class CharactersItemEquipDTO {

    private String charactersName;
    private String hatInfo;
    private String topInfo;
    private String bottomInfo;
    private String capeInfo;
    private String shoesInfo;
    private String glovesInfo;
    private String shoulderInfo;
    private String faceInfo;
    private String eyeInfo;
    private String earInfo;
    private String pendantOneInfo;
    private String pendantTwoInfo;
    private String beltInfo;
    private String ringOneInfo;
    private String ringTwoInfo;
    private String ringThreeInfo;
    private String ringFourInfo;
    private String weaponInfo;
    private String subWeaponInfo;
    private String emblemInfo;
    private String badgeInfo;
    private String medalInfo;
    private String poketInfo;
    private String heartInfo;
    private String titleInfo;
    private String dragonHat;
    private String dragonPendant;
    private String dragonWing;
    private String dragonTail;
    private String mechanicEngine;
    private String mechanicArm;
    private String mechanicLeg;
    private String mechanicTran;


    public CharactersItemEquipDTO(String charactersName, String hatInfo, String topInfo, String bottomInfo, String capeInfo, String shoesInfo, String glovesInfo, String shoulderInfo, String faceInfo, String eyeInfo, String earInfo, String pendantOneInfo, String pendantTwoInfo, String beltInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String weaponInfo, String subWeaponInfo, String emblemInfo, String badgeInfo, String medalInfo, String poketInfo, String heartInfo, String titleInfo, String dragonHat, String dragonPendant, String dragonWing, String dragonTail, String mechanicEngine, String mechanicArm, String mechanicLeg, String mechanicTran) {
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

    public String getFaceInfo() {
        return faceInfo;
    }

    public String getCapeInfo() {
        return capeInfo;
    }

    public String getBeltInfo() {
        return beltInfo;
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
}
