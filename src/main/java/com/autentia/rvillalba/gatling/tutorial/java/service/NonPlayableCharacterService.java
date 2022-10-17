package com.autentia.rvillalba.gatling.tutorial.java.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.autentia.rvillalba.gatling.tutorial.java.dto.CharacterType;
import com.autentia.rvillalba.gatling.tutorial.java.dto.NonPlayableCharacter;
import com.github.javafaker.Faker;

@Service
public class NonPlayableCharacterService{

    private final Faker faker;

    public NonPlayableCharacterService(final Faker faker){
        this.faker = faker;
    }

    public List<NonPlayableCharacter> getRandomNonPlayableCharacters(final Integer quantity) throws Exception{
        List<NonPlayableCharacter> list = new ArrayList<>();
        int bound = quantity;
        randomException();
        for(int i = 0; i < bound; i++){
            NonPlayableCharacter nonPlayableCharacter = generateRandom();
            list.add(nonPlayableCharacter);
        }
        return list;
    }

    private NonPlayableCharacter generateRandom() throws Exception{
        simulateTimeToCallADatabase();
        return new NonPlayableCharacter(faker.dragonBall().character(), faker.name().lastName(),
                CharacterType.values()[faker.number().numberBetween(0, CharacterType.values().length)],
                faker.number().numberBetween(40, 100),
                faker.number().numberBetween(0, 10));
    }

    private void randomException() throws Exception{
        if (faker.number().numberBetween(0, 1000) < 50){
            throw new Exception("Random exception");
        }
    }

    private static void simulateTimeToCallADatabase(){
        try{
        TimeUnit.MILLISECONDS.sleep(5L);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
