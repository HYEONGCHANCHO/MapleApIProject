package com.mapleApiTest.projectOne.dto.character.request;

public class GetChracterInfo {

    private String name;
    private String date;

    public GetChracterInfo(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
