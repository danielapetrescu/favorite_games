package org.bestseller.gamers.repository;

import org.bestseller.gamers.entities.Credit;
import org.bestseller.gamers.entities.Game;
import org.bestseller.gamers.entities.Gamer;
import org.bestseller.gamers.entities.Level;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@RestController
public interface CreditRepository  extends CrudRepository<Credit, Long> {
    Optional<Credit> findAllByGamerIdAndGame(Long gamerId, Game game);

    @Query("SELECT result.gamer " +
            "FROM (" +
            "    SELECT DISTINCT c.gamer AS gamer, c.credit as credit, ggl.level as level, c.game as game" +
            "    FROM Credit c, GamerGameLevel ggl " +
            "    WHERE c.gamer = ggl.gamer" +
            ") AS result " +
            "WHERE result.game =:game AND result.level =:level "+
            "GROUP BY result.gamer " +
            "ORDER BY MAX(result.credit) DESC " +
            "LIMIT 1")
    Optional<Gamer> findGamerWithMaximPointOnGameAndLevel(@Param("game") Game game, @Param("level") Level level);
}
