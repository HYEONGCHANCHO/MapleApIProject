package com.mapleApiTest.projectOne.dto.character.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class GetChracterInfo {


    private String name;
    private String date;
    private String ocid;


    public GetChracterInfo(String name, String date, String ocid) {
        this.name = name;
        this.date = date;
        this.ocid = ocid;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getOcid() {
        return ocid;
    }
}
