package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String charactersLevel;
    @Column(nullable = false)
    private String charactersServer;

    public Long getId() {
        return id;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getDate() {
        return date;
    }

    public String getCharactersLevel() {
        return charactersLevel;
    }

    public String getCharactersServer() {
        return charactersServer;
    }
}
