package com.mapleApiTest.projectOne.dto.item;

public class CharactersSetEffectInfoDTO {
    String charactersName;
    String ocid;
    int absolSetCount;
    int arcaneSetCount;
    int bossAcSetCount;
    int cvelSetCount;
    int lucidAcSetCount;
    int lomienSetCount;
    int eternalSetCount;
    int mystarSetCount;
    int sevenSetCount;
    int cashSetCount;

    public CharactersSetEffectInfoDTO(String charactersName, String ocid, int absolSetCount, int arcaneSetCount, int bossAcSetCount, int cvelSetCount, int lucidAcSetCount, int lomienSetCount, int eternalSetCount, int mystarSetCount, int sevenSetCount, int cashSetCount) {
        this.charactersName = charactersName;
        this.ocid = ocid;
        this.absolSetCount = absolSetCount;
        this.arcaneSetCount = arcaneSetCount;
        this.bossAcSetCount = bossAcSetCount;
        this.cvelSetCount = cvelSetCount;
        this.lucidAcSetCount = lucidAcSetCount;
        this.lomienSetCount = lomienSetCount;
        this.eternalSetCount = eternalSetCount;
        this.mystarSetCount = mystarSetCount;
        this.sevenSetCount = sevenSetCount;
        this.cashSetCount = cashSetCount;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public String getOcid() {
        return ocid;
    }

    public void setOcid(String ocid) {
        this.ocid = ocid;
    }

    public int getAbsolSetCount() {
        return absolSetCount;
    }

    public void setAbsolSetCount(int absolSetCount) {
        this.absolSetCount = absolSetCount;
    }

    public int getArcaneSetCount() {
        return arcaneSetCount;
    }

    public void setArcaneSetCount(int arcaneSetCount) {
        this.arcaneSetCount = arcaneSetCount;
    }

    public int getBossAcSetCount() {
        return bossAcSetCount;
    }

    public void setBossAcSetCount(int bossAcSetCount) {
        this.bossAcSetCount = bossAcSetCount;
    }

    public int getCvelSetCount() {
        return cvelSetCount;
    }

    public void setCvelSetCount(int cvelSetCount) {
        this.cvelSetCount = cvelSetCount;
    }

    public int getLucidAcSetCount() {
        return lucidAcSetCount;
    }

    public void setLucidAcSetCount(int lucidAcSetCount) {
        this.lucidAcSetCount = lucidAcSetCount;
    }

    public int getLomienSetCount() {
        return lomienSetCount;
    }

    public void setLomienSetCount(int lomienSetCount) {
        this.lomienSetCount = lomienSetCount;
    }

    public int getEternalSetCount() {
        return eternalSetCount;
    }

    public void setEternalSetCount(int eternalSetCount) {
        this.eternalSetCount = eternalSetCount;
    }

    public int getMystarSetCount() {
        return mystarSetCount;
    }

    public void setMystarSetCount(int mystarSetCount) {
        this.mystarSetCount = mystarSetCount;
    }

    public int getSevenSetCount() {
        return sevenSetCount;
    }

    public void setSevenSetCount(int sevenSetCount) {
        this.sevenSetCount = sevenSetCount;
    }

    public int getCashSetCount() {
        return cashSetCount;
    }

    public void setCashSetCount(int cashSetCount) {
        this.cashSetCount = cashSetCount;
    }
}
