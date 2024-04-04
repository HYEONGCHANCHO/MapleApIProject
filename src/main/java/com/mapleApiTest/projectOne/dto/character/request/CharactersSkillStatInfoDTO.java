package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersSkillStatInfoDTO {

    String charactersName;
    int skillStatAllStat ;
    int skillStatAtMgPower ;

    boolean isFree =false;
    public CharactersSkillStatInfoDTO() {
    }

    public CharactersSkillStatInfoDTO(String charactersName,  int skillStatAllStat, int skillStatAtMgPower,boolean isFree) {
        this.charactersName = charactersName;
        this.skillStatAllStat = skillStatAllStat;
        this.skillStatAtMgPower = skillStatAtMgPower;
        this.isFree = isFree;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    public int getSkillStatAllStat() {
        return skillStatAllStat;
    }

    public void setSkillStatAllStat(int skillStatAllStat) {
        this.skillStatAllStat = skillStatAllStat;
    }

    public int getSkillStatAtMgPower() {
        return skillStatAtMgPower;
    }

    public void setSkillStatAtMgPower(int skillStatAtMgPower) {
        this.skillStatAtMgPower = skillStatAtMgPower;
    }

    public boolean isFree() {
        return isFree;
    }
}
