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

    @Column(columnDefinition = "json")
    private String item_equipment;



    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getItem_equipment() {
        return item_equipment;
    }

    protected CharactersItemEquip(){}

    public CharactersItemEquip(String charactersName, String date, String item_equipment) {
        this.charactersName = charactersName;
        this.date = date;
        this.item_equipment = item_equipment;
    }

}
