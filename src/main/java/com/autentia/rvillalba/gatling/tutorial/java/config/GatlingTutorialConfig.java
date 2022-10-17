package com.autentia.rvillalba.gatling.tutorial.java.config;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
public class GatlingTutorialConfig{

    @Bean
    public Faker faker(){
        return new Faker();
    }

    @Bean
    public Random random(){
        return new Random();
    }

}
