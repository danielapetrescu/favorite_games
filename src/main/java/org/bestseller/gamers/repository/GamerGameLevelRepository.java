package org.bestseller.gamers.repository;

import org.bestseller.gamers.entities.GamerGameLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;
import org.bestseller.gamers.entities.Game;
import org.bestseller.gamers.entities.Level;

import java.util.List;

@RestController
public interface GamerGameLevelRepository extends CrudRepository<GamerGameLevel, Long> {
    List<GamerGameLevel> findByGamerId(Long gamerId);
    List<GamerGameLevel> findAllByGameName(Game gameName);

    List<GamerGameLevel> findAllByGameNameAndLevel(Game gameName, Level level);
}
