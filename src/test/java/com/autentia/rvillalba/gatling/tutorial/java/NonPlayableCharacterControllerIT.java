package com.autentia.rvillalba.gatling.tutorial.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.autentia.rvillalba.gatling.tutorial.java.dto.NonPlayableCharactersResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NonPlayableCharacterControllerIT{

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenGetRandomNonPlayableCharacterWithQuantityFiveThenGetFiveRandomCharacters(){
        var quantity = 5;
        var result =
                restTemplate.getForEntity("http://localhost:" + port + "/characters/randoms/" + quantity,
                        NonPlayableCharactersResponse.class);
        assertEquals(quantity, result.getBody().getCharacters().size());
        var resultWithoutDuplicates = result.getBody().getCharacters().stream().distinct().collect(Collectors.toList());
        assertEquals(quantity, resultWithoutDuplicates.size());
    }
}
