package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersSkillStatInfoDTO {

    String charactersName;
    int skillStatAllStat ;
    int skillStatAtMgPower ;


    double eventAllStat;
    double eventAtMgPower;
    double eventBossDamage;

    boolean isFree =false;
    public CharactersSkillStatInfoDTO() {
    }

    public CharactersSkillStatInfoDTO(String charactersName,  int skillStatAllStat, int skillStatAtMgPower, double eventAllStat, double eventAtMgPower, double eventBossDamage,boolean isFree) {
        this.charactersName = charactersName;
        this.skillStatAllStat = skillStatAllStat;
        this.skillStatAtMgPower = skillStatAtMgPower;
        this.eventAllStat = eventAllStat;
        this.eventAtMgPower = eventAtMgPower;
        this.eventBossDamage = eventBossDamage;
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

    public double getEventAllStat() {
        return eventAllStat;
    }

    public void setEventAllStat(double eventAllStat) {
        this.eventAllStat = eventAllStat;
    }

    public double getEventAtMgPower() {
        return eventAtMgPower;
    }

    public void setEventAtMgPower(double eventAtMgPower) {
        this.eventAtMgPower = eventAtMgPower;
    }

    public double getEventBossDamage() {
        return eventBossDamage;
    }

    public void setEventBossDamage(double eventBossDamage) {
        this.eventBossDamage = eventBossDamage;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isFree() {
        return isFree;
    }
}
