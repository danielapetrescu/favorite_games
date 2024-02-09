package org.bestseller.gamers.entities;

import jakarta.persistence.Entity;

import java.util.Random;

public enum Level {
    NOOB,
    PRO,
    INVINCIBLE;

    private static final Random PRNG = new Random();
    public static Level randomLevel()  {
        Level[] levels = values();
        return levels[PRNG.nextInt(levels.length)];
    }
}
