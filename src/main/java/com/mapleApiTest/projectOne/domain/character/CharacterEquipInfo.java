package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharacterEquipInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String date;

    


    protected CharacterEquipInfo(){}


}
