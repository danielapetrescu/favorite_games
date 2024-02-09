package org.bestseller.gamers.repository;

import org.bestseller.gamers.entities.Game;
import org.bestseller.gamers.entities.Gamer;
import org.bestseller.gamers.entities.Geography;
import org.bestseller.gamers.entities.Level;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public interface GamerRepository extends CrudRepository<Gamer, Long> {

    public List<Gamer> findAllByGeography(Geography geography);
}
