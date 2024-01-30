package com.mapleApiTest.projectOne.domain.character;

import javax.persistence.*;

@Entity
public class Characters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false)
    private String name;

    protected Characters(){}

    public Characters(String name) {
        if(name==null || name.isBlank()){throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다, name"));}
            this.name = name;
    }

    public String getName() {
        return name;
    }
}
