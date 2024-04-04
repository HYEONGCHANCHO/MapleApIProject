package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersSimbolInfoDTO {

    String charactersName;
    int simbolStr ;
    int simbolDex ;
    int simbolInt ;
    int simbolLuk ;

    public CharactersSimbolInfoDTO() {
    }

    public CharactersSimbolInfoDTO(String charactersName, int simbolStr, int simbolDex, int simbolInt, int simbolLuk) {
        this.charactersName = charactersName;
        this.simbolStr = simbolStr;
        this.simbolDex = simbolDex;
        this.simbolInt = simbolInt;
        this.simbolLuk = simbolLuk;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getSimbolStr() {
        return simbolStr;
    }

    public void setSimbolStr(int simbolStr) {
        this.simbolStr = simbolStr;
    }

    public int getSimbolDex() {
        return simbolDex;
    }

    public void setSimbolDex(int simbolDex) {
        this.simbolDex = simbolDex;
    }

    public int getSimbolInt() {
        return simbolInt;
    }

    public void setSimbolInt(int simbolInt) {
        this.simbolInt = simbolInt;
    }

    public int getSimbolLuk() {
        return simbolLuk;
    }

    public void setSimbolLuk(int simbolLuk) {
        this.simbolLuk = simbolLuk;
    }
}
