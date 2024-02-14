package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class CharactersKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false)
    private String charactersName;

    @Column(nullable = false)
    private String ocid;

    protected CharactersKey(){}

    public CharactersKey(String charactersName, String ocidValue) {
        if(charactersName==null || charactersName.isBlank()){throw new IllegalArgumentException(String.format("잘못된 charactersName(%s)이 들어왔습니다, charactersName"));}
            this.charactersName = charactersName;
            this.ocid = ocidValue;
    }

    public Long getId() {
        return id;
    }

    public String getCharactersName() {
        return charactersName;
    }

    public String getOcid() {
        return ocid;
    }
}
