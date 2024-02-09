package org.bestseller.gamers.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAMERS")
public class Gamer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Geography geography;
    @OneToMany(mappedBy = "gamer", cascade = CascadeType.ALL)
    private List<GamerGameLevel> gamerGameLevels = new ArrayList<GamerGameLevel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Geography getGeography() {
        return geography;
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
    }

    public List<GamerGameLevel> getGamerGamesLevels() {
        return gamerGameLevels;
    }

    public void setGamerGamesLevels(List<GamerGameLevel> gamerGameLevels) {
        this.gamerGameLevels = gamerGameLevels;
    }

    public void addGamerGameLevel(GamerGameLevel gamerGameLevels){
        this.gamerGameLevels.add(gamerGameLevels);
    }
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;

        if ( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        Gamer other = (Gamer) obj;

        return id != null &&
                id.equals(other.getId());
    }
}
