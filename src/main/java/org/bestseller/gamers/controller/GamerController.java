package org.bestseller.gamers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bestseller.gamers.entities.Credit;
import org.bestseller.gamers.entities.Game;
import org.bestseller.gamers.entities.GamerGameLevel;
import org.bestseller.gamers.entities.Gamer;
import org.bestseller.gamers.entities.Geography;
import org.bestseller.gamers.entities.Level;
import org.bestseller.gamers.repository.CreditRepository;
import org.bestseller.gamers.repository.GamerGameLevelRepository;
import org.bestseller.gamers.repository.GamerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GamerController {
    private final GamerRepository gamerRepository;
    private final GamerGameLevelRepository gamerGameLevelRepository;

    private final CreditRepository creditRepository;

    public GamerController(final GamerRepository gamerRepository,
                           final GamerGameLevelRepository gamerGameLevelRepository,
                           final CreditRepository creditRepository) {
        this.gamerRepository = gamerRepository;
        this.gamerGameLevelRepository = gamerGameLevelRepository;
        this.creditRepository = creditRepository;
    }

    @GetMapping("/gamers")
    @Operation(summary = "Get all gamers")
    public Iterable<Gamer> getAllGamers(){
        return gamerRepository.findAll();
    }

    @GetMapping("/gamers/{id}")
    @Operation(summary = "Get a gamer by ID")
    public ResponseEntity<Gamer> getGamerById(@PathVariable("id") Long id) {
        Optional<Gamer> gamerOptional = gamerRepository.findById(id);
        return gamerOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/gamers")
    @Operation(summary = "Create a new gamer", description = "Creates a new gamer based on the provided details")
    @ResponseStatus(HttpStatus.CREATED)
    public Gamer createNewGamer(
            @Parameter(description = "Details of the gamer to be created", required = true, in = ParameterIn.DEFAULT)
            @RequestBody Gamer gamer){
        return gamerRepository.save(gamer);
    }

    @PutMapping("/gamer/{id}")
    @Operation(summary = "Update a gamer", description = "Updates the details of an existing gamer")
    public Gamer updateGamer(
            @Parameter(description = "ID of the gamer to be updated", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") Long id,
            @Parameter(description = "Updated details of the gamer", required = true)
            @RequestBody Gamer gamer){
        Optional<Gamer> gamerToUpdateOptional = gamerRepository.findById(id);
        if(gamerToUpdateOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gamer doesn't exist");
        }
        Gamer gamerToUpdate = gamerToUpdateOptional.get();
        String name = gamer.getName();
        if(name != null){
            gamerToUpdate.setName(name);
        }

        String nickname = gamer.getNickname();
        if(nickname != null){
            gamerToUpdate.setNickname(nickname);
        }

        Geography geo = gamer.getGeography();
        if(geo != null){
            gamerToUpdate.setGeography(geo);
        }

        List<GamerGameLevel> gamesLevels = gamer.getGamerGamesLevels();
        if(gamesLevels != null && !gamesLevels.isEmpty()){
            gamerToUpdate.getGamerGamesLevels().clear();
            gamerToUpdate.getGamerGamesLevels().addAll(gamesLevels);
        }
        return gamerRepository.save(gamerToUpdate);
    }

    @PutMapping("/gamers/{id}/defaultGames")
    @Operation(summary = "Set default games", description = "Setup 5 arbitrary games for an existing gamer")
    @ApiResponse(responseCode = "404",
            description = "Gamer not found")
    public Gamer setDefaultGames(
            @Parameter(description = "ID of the gamer to be updated", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") Long id) {

        Optional<Gamer> gamerToUpdateOptional = gamerRepository.findById(id);
        if (gamerToUpdateOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer doesn't exist");
        }
        Gamer gamerToUpdate = gamerToUpdateOptional.get();
        gamerToUpdate.getGamerGamesLevels().clear();
        GamerGameLevel gamerGameLevel = new GamerGameLevel();
        List<Game> list = Game.random5Game();
        for (int i = 0; i < 5; i++) {
            gamerGameLevel = new GamerGameLevel();
            gamerGameLevel.setGameName(list.get(i));
            gamerGameLevel.setLevel(Level.NOOB);
            gamerToUpdate.addGamerGameLevel(gamerGameLevel);
        }
        gamerToUpdate.getGamerGamesLevels().forEach(ggl -> ggl.setGamer(gamerToUpdate));
        return gamerRepository.save(gamerToUpdate);
    }
    @PutMapping("/gamers/{id}/gamerGamesAndLevels")
    @Operation(summary = "Update games and level",
            description = "Update games and levels for the gamer with the information received in request")
    @ApiResponse(responseCode = "404",
            description = "Gamer or game not found")
   public Gamer setGamerGamesAndLevels(
            @Parameter(description = "ID of the gamer to be updated", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") Long id,
            @Parameter(description = "Updated details of the gamer", required = true)
            @RequestBody Gamer gamer) {

        Optional<Gamer> gamerToUpdateOptional = gamerRepository.findById(id);
        if (gamerToUpdateOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer doesn't exist");
        }
        Gamer gamerToUpdate = gamerToUpdateOptional.get();
        List<GamerGameLevel> oldList = gamerToUpdate.getGamerGamesLevels();
        List<GamerGameLevel> newList = gamer.getGamerGamesLevels();
        if (newList == null || newList.size() != 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File doesn't contain Gamer with at least 5 games");
        }
        for (int i = 0; i < 5; i++) {
            oldList.get(i).setGameName(newList.get(i).getGameName());
            oldList.get(i).setLevel(newList.get(i).getLevel());
        }

        return gamerRepository.save(gamerToUpdate);
    }

    @GetMapping("/match/{id}")
    @Operation(summary = "Get matched gamers",
            description = "Retrieves gamers matching criteria by game and level, game, or geography")
    @ApiResponse(responseCode = "200",
            description = "Gamers matching the criteria are successfully retrieved",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Gamer.class)))
    @ApiResponse(responseCode = "404",
            description = "Gamer not found")
    public List<Gamer> getMatchGamer(
            @Parameter(description = "ID of the gamer for matching purposes", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") Long id){

        List<GamerGameLevel> listForGamer = gamerGameLevelRepository.findByGamerId(id);

        // search for same game and level
        List<Gamer> result = listForGamer.stream()
                .flatMap(ggl -> gamerGameLevelRepository.findAllByGameNameAndLevel(ggl.getGameName(), ggl.getLevel()).stream())
                .map(GamerGameLevel::getGamer)
                .filter(gamer -> !gamer.getId().equals(id))
                .distinct()
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(result)){
            return result;
        }

        // search for same game
        result = listForGamer.stream()
                .flatMap(ggl ->  gamerGameLevelRepository.findAllByGameName(
                        ggl.getGameName()).stream())
                .map(GamerGameLevel::getGamer)
                .filter(gamer -> !gamer.getId().equals(id))
                .distinct()
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(result)){
            return result;
        }

        Optional<Gamer> gamerOptional = gamerRepository.findById(id);
        if(gamerOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer doesn't exist");
        }

        Gamer gamer = gamerOptional.get();
        // search for same geography
        return gamerRepository.findAllByGeography(gamer.getGeography())
                .stream()
                .filter(gm -> !gm.getId().equals(id))
                .distinct()
                .collect(Collectors.toList());
    }
    @PutMapping("/credits/{id}/{gameName}/{credits}")
    @Operation(summary = "Update or add gamer credits",
            description = "Updates or adds credits for a specific game of a gamer")
    @ApiResponse(responseCode = "200",
            description = "Gamer credits updated or added successfully")
    @ApiResponse(responseCode = "404",
            description = "Gamer or game not found")
    public Credit updateOrAddGamerCredits(@PathVariable("id") Long id, @PathVariable("gameName") Game game,
                                       @PathVariable("credits") Integer credits){

        Optional<Gamer> gamerOptional = gamerRepository.findById(id);
        if(gamerOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer doesn't exist");
        }
        Gamer gamer = gamerOptional.get();

        List<Game> listOfGames = gamer.getGamerGamesLevels().stream().
                map(GamerGameLevel::getGameName).toList();

        if(!listOfGames.contains(game)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gamer doesn't have game:" + game.name());
        }

        Optional<Credit> creditOptional = creditRepository.findAllByGamerIdAndGame(gamer.getId(), game);
        Credit credit;
        if(creditOptional.isEmpty()){
            credit = new Credit();
            credit.setGamer(gamer);
            credit.setGame(game);
        } else {
            credit = creditOptional.get();
        }
        credit.setCredit(credit.getCredit()+credits);

        creditRepository.save(credit);
        return credit;
    }

    @GetMapping("/maxCredit/{game}/{level}")
    @Operation(summary = "Get gamer with max credit for game and level",
            description = "Retrieves the gamer with the maximum credit for the specified game and level")
    @ApiResponse(responseCode = "200",
            description = "Gamer with max credit retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Gamer.class)))
    public Optional<Gamer> getGamerWithMaxCreditforGameAndLevel(
            @Parameter(description = "Name of the game", example = "MINECRAFT", required = true)
            @PathVariable("game") Game game,
            @Parameter(description = "Level of the gamer", example = "NOOB", required = true)
            @PathVariable("level") Level level){
        return creditRepository.findGamerWithMaximPointOnGameAndLevel(game, level);
    }
}
