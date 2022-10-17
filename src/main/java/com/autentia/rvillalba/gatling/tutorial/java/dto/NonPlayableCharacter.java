package com.autentia.rvillalba.gatling.tutorial.java.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class NonPlayableCharacter implements Serializable{
    private final String name;
    private final String surname;
    private final CharacterType type;
    private final Integer hitPoints;
    private final Integer manaPotions;

}
