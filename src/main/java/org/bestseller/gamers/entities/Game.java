package org.bestseller.gamers.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Game {
    MINECRAFT,
    THE_WITCHER_3,
    GRAND_THEFT_AUTO_V,
    THE_LEGEND_OF_ZELDA_BREATH_OF_THE_WILD,
    RED_DEAD_REDEMPTION_2,
    FORTNITE,
    CALL_OF_DUTY_MODERN_WARFARE,
    LEAGUE_OF_LEGENDS,
    OVERWATCH,
    SUPER_MARIO_ODYSSEY,
    DOOM_ETERNAL,
    FINAL_FANTASY_VII_REMAKE,
    ANIMAL_CROSSING_NEW_HORIZONS,
    MARIO_KART_8_DELUXE,
    DARK_SOULS_III,
    HOLLOW_KNIGHT,
    RESIDENT_EVIL_2_REMAKE,
    BLOODBORNE,
    GOD_OF_WAR,
    HADES;

    public static List<Game> random5Game()  {
        List<Game> games = Arrays.asList(Game.values());
        Collections.shuffle(games);
        return games.subList(0, 5);
    }
}
