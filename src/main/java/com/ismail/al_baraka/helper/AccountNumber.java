package com.ismail.al_baraka.helper;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class AccountNumber {
    
    public String get() {

        Random random = new Random();

        int part1 = 100 + random.nextInt(900);
        int part2 = 100 + random.nextInt(900);
        int part3 = 100 + random.nextInt(900);

        return part1 + "-" + part2 + "-" + part3;
    }
}
