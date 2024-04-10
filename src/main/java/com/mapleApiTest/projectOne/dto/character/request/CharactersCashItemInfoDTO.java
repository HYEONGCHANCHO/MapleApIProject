package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersCashItemInfoDTO {

    String charactersName;
    int cashItemStr ;
    int cashItemDex ;
    int cashItemInt ;
    int cashItemLuk ;
    int cashItemAtPower ;
    int cashItemMgPower ;

    public CharactersCashItemInfoDTO() {
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getCashItemStr() {
        return cashItemStr;
    }

    public void setCashItemStr(int cashItemStr) {
        this.cashItemStr = cashItemStr;
    }

    public int getCashItemDex() {
        return cashItemDex;
    }

    public void setCashItemDex(int cashItemDex) {
        this.cashItemDex = cashItemDex;
    }

    public int getCashItemInt() {
        return cashItemInt;
    }

    public void setCashItemInt(int cashItemInt) {
        this.cashItemInt = cashItemInt;
    }

    public int getCashItemLuk() {
        return cashItemLuk;
    }

    public void setCashItemLuk(int cashItemLuk) {
        this.cashItemLuk = cashItemLuk;
    }


    public CharactersCashItemInfoDTO(String charactersName, int cashItemStr, int cashItemDex, int cashItemInt, int cashItemLuk, int cashItemAtPower, int cashItemMgPower) {
        this.charactersName = charactersName;
        this.cashItemStr = cashItemStr;
        this.cashItemDex = cashItemDex;
        this.cashItemInt = cashItemInt;
        this.cashItemLuk = cashItemLuk;
        this.cashItemAtPower = cashItemAtPower;
        this.cashItemMgPower = cashItemMgPower;
    }

    public int getCashItemAtPower() {
        return cashItemAtPower;
    }

    public void setCashItemAtPower(int cashItemAtPower) {
        this.cashItemAtPower = cashItemAtPower;
    }

    public int getCashItemMgPower() {
        return cashItemMgPower;
    }

    public void setCashItemMgPower(int cashItemMgPower) {
        this.cashItemMgPower = cashItemMgPower;
    }
}
