package org.bestseller.gamers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class GamerGameLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Game gameName;
    @Enumerated(EnumType.STRING)
    private Level level;

    @JsonIgnore
    @ManyToOne(optional=false)
    @JoinColumn(name = "gamer_id")
    private Gamer gamer;

    public Gamer getGamer() {
        return gamer;
    }

    public void setGamer(Gamer gamer) {
        this.gamer = gamer;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    public Game getGameName() {
        return gameName;
    }

    public void setGameName(Game gameName) {
        this.gameName = gameName;
    }
}
