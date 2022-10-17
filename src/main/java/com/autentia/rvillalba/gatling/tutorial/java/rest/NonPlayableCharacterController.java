package com.autentia.rvillalba.gatling.tutorial.java.rest;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autentia.rvillalba.gatling.tutorial.java.dto.NonPlayableCharactersResponse;
import com.autentia.rvillalba.gatling.tutorial.java.service.NonPlayableCharacterService;

@RestController
@RequestMapping("/characters")
public class NonPlayableCharacterController{

    private final NonPlayableCharacterService nonPlayableCharacterService;

    public NonPlayableCharacterController(final NonPlayableCharacterService nonPlayableCharacterService){
        this.nonPlayableCharacterService = nonPlayableCharacterService;
    }

    @GetMapping("/randoms/{quantity}")
    public NonPlayableCharactersResponse getRandomNonPlayableCharacters(@PathVariable Optional<Integer> quantity)
            throws Exception{
        return new NonPlayableCharactersResponse(nonPlayableCharacterService.getRandomNonPlayableCharacters(
                quantity.filter(quant -> quant > 0).orElse(1)));
    }

}
