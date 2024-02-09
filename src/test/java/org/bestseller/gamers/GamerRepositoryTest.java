package org.bestseller.gamers;

import org.bestseller.gamers.controller.GamerController;
import org.bestseller.gamers.entities.Credit;
import org.bestseller.gamers.entities.Game;
import org.bestseller.gamers.entities.Gamer;
import org.bestseller.gamers.entities.GamerGameLevel;
import org.bestseller.gamers.entities.Geography;
import org.bestseller.gamers.entities.Level;
import org.bestseller.gamers.repository.CreditRepository;
import org.bestseller.gamers.repository.GamerGameLevelRepository;
import org.bestseller.gamers.repository.GamerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GamerController.class)
public class GamerRepositoryTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GamerRepository gamerRepository;

    @MockBean
    private GamerGameLevelRepository gamerGameLevelRepository;

    @MockBean
    private CreditRepository creditRepository;

    Gamer dragos = new Gamer();
     Gamer dany = new Gamer();
    Gamer edy = new Gamer();
    Gamer matei = new Gamer();
    Gamer vlad = new Gamer();

    void setUpDragos() {
        dragos.setId(1L);
        dragos.setName("Dragos");
        dragos.setNickname("IDM");
        dragos.setGeography(Geography.USA);
    }
    void setUpDany() {
        dany.setId(2L);
        dany.setName("Dany");
        dany.setNickname("PDM");
        dany.setGeography(Geography.EUROPA);
    }

    void setUpEdy() {
        edy.setId(3L);
        edy.setName("Edy");
        edy.setNickname("PEL");
        edy.setGeography(Geography.EUROPA);
    }
    private Gamer updateGamerGames(Gamer gamer){
        GamerGameLevel gamerGameLevel;
        for (int i = 0; i < 5; i++) {
            gamerGameLevel = new GamerGameLevel();
            gamerGameLevel.setGameName(Game.randomGame());
            gamerGameLevel.setLevel(Level.NOOB);
            gamer.addGamerGameLevel(gamerGameLevel);
        }
        gamer.getGamerGamesLevels().forEach(ggl -> ggl.setGamer(gamer));
        return gamer;
    }
    private Gamer setGamerGameLevels(Gamer gamer){
        List<GamerGameLevel> list = gamer.getGamerGamesLevels();
        for (int i = 0; i < 5; i++) {
            list.get(i).setLevel(Level.randomLevel());
        }
        return gamer;
    }
    void setUpFullDragos() {
        dragos.setId(1L);
        dragos.setName("Dragos");
        dragos.setNickname("IDM");
        dragos.setGeography(Geography.USA);
        dragos.addGamerGameLevel(createGamerGameLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.NOOB, dragos));
        dragos.addGamerGameLevel(createGamerGameLevel(Game.LEAGUE_OF_LEGENDS, Level.PRO, dragos));
        dragos.addGamerGameLevel(createGamerGameLevel(Game.GOD_OF_WAR, Level.INVINCIBLE, dragos));
        dragos.addGamerGameLevel(createGamerGameLevel(Game.ANIMAL_CROSSING_NEW_HORIZONS, Level.NOOB, dragos));
        dragos.addGamerGameLevel(createGamerGameLevel(Game.FORTNITE, Level.PRO, dragos));
    }

    void setUpFullDany() {
        dany.setId(2L);
        dany.setName("Dany");
        dany.setNickname("PDM");
        dany.setGeography(Geography.EUROPA);
        dany.addGamerGameLevel(createGamerGameLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.NOOB, dany));
        dany.addGamerGameLevel(createGamerGameLevel(Game.LEAGUE_OF_LEGENDS, Level.INVINCIBLE, dany));
        dany.addGamerGameLevel(createGamerGameLevel(Game.GOD_OF_WAR, Level.NOOB, dany));
        dany.addGamerGameLevel(createGamerGameLevel(Game.ANIMAL_CROSSING_NEW_HORIZONS, Level.INVINCIBLE, dany));
        dany.addGamerGameLevel(createGamerGameLevel(Game.FORTNITE, Level.NOOB, dany));
    }

    void setUpFullEdy() {
        edy.setId(3L);
        edy.setName("Edy");
        edy.setNickname("PEL");
        edy.setGeography(Geography.EUROPA);
        edy.addGamerGameLevel(createGamerGameLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.NOOB, dany));
        edy.addGamerGameLevel(createGamerGameLevel(Game.LEAGUE_OF_LEGENDS, Level.INVINCIBLE, dany));
        edy.addGamerGameLevel(createGamerGameLevel(Game.GOD_OF_WAR, Level.NOOB, dany));
        edy.addGamerGameLevel(createGamerGameLevel(Game.ANIMAL_CROSSING_NEW_HORIZONS, Level.INVINCIBLE, dany));
        edy.addGamerGameLevel(createGamerGameLevel(Game.FORTNITE, Level.NOOB, dany));
    }
    GamerGameLevel createGamerGameLevel(Game nameOfGame, Level level, Gamer gamer){
        GamerGameLevel gamerGameLevel = new GamerGameLevel();
        gamerGameLevel.setGameName(nameOfGame);
        gamerGameLevel.setLevel(level);
        gamerGameLevel.setGamer(gamer);
        return gamerGameLevel;
    }
    @Test
    public void givenGamers_whenFindAll_thenReturnJsonArray()
            throws Exception {

        List<Gamer> allGamers = List.of(dragos);

        given(gamerRepository.findAll()).willReturn(allGamers);

        mvc.perform(get("/api/gamers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dragos.getName())));
    }

    @Test
    public void givenGamer_whenFindById_thenReturnJson()
            throws Exception {

        Optional<Gamer> dragosOptional = Optional.of(dragos);

        given(gamerRepository.findById(1L)).willReturn(dragosOptional);

        mvc.perform(get("/api/gamers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dragos.getName())))
                .andExpect(jsonPath("$.nickname", is(dragos.getNickname())))
                .andExpect(jsonPath("$.geography", is(dragos.getGeography())));
    }

    @Test
    public void givenGamer_whenSave_thenReturnJson()
            throws Exception {
        setUpDragos();
        given(gamerRepository.save(any(Gamer.class))).willReturn(dragos);

        mvc.perform(post("/api/gamers")
                        .content("{\"name\": \"Dragos\",  \"nickname\": \"IDM\",  \"geography\": \"USA\"}" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dragos"))
                .andExpect(jsonPath("$.nickname").value("IDM"))
                .andExpect(jsonPath("$.geography").value("USA"));
    }
    @Test
    public void givenGamer_whenUpdateGamerGames_thenReturnJson()
            throws Exception {
        setUpDragos();
        Optional<Gamer> dragosOptional = Optional.of(dragos);
        given(gamerRepository.findById(1L)).willReturn(dragosOptional);
        given(gamerRepository.save(any(Gamer.class))).willReturn(updateGamerGames(dragos));

        mvc.perform(put("/api/gamers/1/defaultGames")
                        .content("{\"name\": \"Dragos\",  \"nickname\": \"IDM\",  \"geography\": \"USA\"}" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dragos"))
                .andExpect(jsonPath("$.nickname").value("IDM"))
                .andExpect(jsonPath("$.geography").value("USA"))
                .andExpect(jsonPath("$.gamerGamesLevels[0].gameName").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[1].gameName").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[2].gameName").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[3].gameName").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[4].gameName").exists());
    }

    @Test
    public void givenGamer_whenUpdateGamerGameLevels_thenReturnJson()
            throws Exception {
        setUpDragos();
        Optional<Gamer> dragosOptional = Optional.of(dragos);
        given(gamerRepository.findById(1L)).willReturn(dragosOptional);
        updateGamerGames(dragos);
        given(gamerRepository.save(any(Gamer.class))).willReturn(setGamerGameLevels(dragos));

        mvc.perform(put("/api/gamers/1/gamerGamesAndLevels")
                        .content("{\"id\":1,\"name\":\"Dragos\",\"nickname\":\"IDM\",\"geography\":\"USA\",\"gamerGamesLevels\":[{\"gameName\":\"CALL_OF_DUTY_MODERN_WARFARE\",\"level\":\"NOOB\"},{\"gameName\":\"THE_WITCHER_3\",\"level\":\"PRO\"},{\"gameName\":\"SUPER_MARIO_ODYSSEY\",\"level\":\"INVINCIBLE\"},{\"gameName\":\"CALL_OF_DUTY_MODERN_WARFARE\",\"level\":\"NOOB\"},{\"gameName\":\"RESIDENT_EVIL_2_REMAKE\",\"level\":\"PRO\"}]}" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dragos"))
                .andExpect(jsonPath("$.nickname").value("IDM"))
                .andExpect(jsonPath("$.geography").value("USA"))
                .andExpect(jsonPath("$.gamerGamesLevels[0].level").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[1].level").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[2].level").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[3].level").exists())
                .andExpect(jsonPath("$.gamerGamesLevels[4].level").exists());
    }
     @Test
    public void givenGamer_whenMatchGameAndLevel_thenReturnJson()
            throws Exception {

        setUpFullDragos();
        setUpDany();

        given(gamerGameLevelRepository.findByGamerId(1L)).willReturn(dragos.getGamerGamesLevels());

        List<GamerGameLevel> list = new ArrayList<>();
        list.add(createGamerGameLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.NOOB, dany));
        given(gamerGameLevelRepository.findAllByGameNameAndLevel(Game.CALL_OF_DUTY_MODERN_WARFARE,
                        Level.NOOB)).willReturn(list);

         mvc.perform(get("/api/match/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dany.getName())));
    }
    @Test
    public void givenGamer_whenMatchGame_thenReturnJson()
            throws Exception {

        setUpFullDragos();
        setUpDany();

        given(gamerGameLevelRepository.findByGamerId(1L)).willReturn(dragos.getGamerGamesLevels());

        given(gamerGameLevelRepository.findAllByGameNameAndLevel(Game.CALL_OF_DUTY_MODERN_WARFARE,
                Level.NOOB)).willReturn(new ArrayList<>());

        List<GamerGameLevel> list = new ArrayList<>();
        list.add(createGamerGameLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.PRO, dany));
        given(gamerGameLevelRepository.findAllByGameName(Game.CALL_OF_DUTY_MODERN_WARFARE)).willReturn(list);

        mvc.perform(get("/api/match/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dany.getName())));
    }

    @Test
    public void givenGamer_whenMatchGeography_thenReturnJson()
            throws Exception {

        setUpDany();
        setUpEdy();

        given(gamerGameLevelRepository.findByGamerId(2L)).willReturn(dany.getGamerGamesLevels());

        given(gamerGameLevelRepository.findAllByGameNameAndLevel(Game.CALL_OF_DUTY_MODERN_WARFARE,
                Level.NOOB)).willReturn(new ArrayList<>());

        given(gamerGameLevelRepository.findAllByGameName(Game.CALL_OF_DUTY_MODERN_WARFARE)).willReturn(new ArrayList<>());

        Optional<Gamer> danyOptional = Optional.of(dany);
        given(gamerRepository.findById(2L)).willReturn(danyOptional);

        List<Gamer> gamerList = new ArrayList<Gamer>();
        gamerList.add(edy);
        given(gamerRepository.findAllByGeography(dany.getGeography())).willReturn(gamerList);

        mvc.perform(get("/api/match/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(edy.getName())));
    }
    @Test
    public void givenGamer_whenNoMatch_thenReturnNoJson()
            throws Exception {

        setUpDany();
        setUpEdy();

        given(gamerGameLevelRepository.findByGamerId(2L)).willReturn(dany.getGamerGamesLevels());

        given(gamerGameLevelRepository.findAllByGameNameAndLevel(Game.CALL_OF_DUTY_MODERN_WARFARE,
                Level.NOOB)).willReturn(new ArrayList<>());

        given(gamerGameLevelRepository.findAllByGameName(Game.CALL_OF_DUTY_MODERN_WARFARE)).willReturn(new ArrayList<>());

        Optional<Gamer> danyOptional = Optional.of(dany);
        given(gamerRepository.findById(2L)).willReturn(danyOptional);

        given(gamerRepository.findAllByGeography(dany.getGeography())).willReturn(new ArrayList<Gamer>());

        mvc.perform(get("/api/match/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenGamerAndGameAndCredit_whenHasGameAndNoCredit_thenReturnJson()
            throws Exception {

        setUpFullDany();

        Optional<Gamer> danyOptional = Optional.of(dany);
        given(gamerRepository.findById(2L)).willReturn(danyOptional);
        given(creditRepository.findAllByGamerIdAndGame(dany.getId(), Game.CALL_OF_DUTY_MODERN_WARFARE)).
                willReturn(Optional.empty());

        mvc.perform(put("/api/credits/2/CALL_OF_DUTY_MODERN_WARFARE/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game").value("CALL_OF_DUTY_MODERN_WARFARE"))
                .andExpect(jsonPath("$.credit").value(30));
    }

    @Test
    public void givenGamerAndGameAndCredit_whenHasGameAndCredit_thenReturnJson()
            throws Exception {

        setUpFullDany();

        Optional<Gamer> danyOptional = Optional.of(dany);
        given(gamerRepository.findById(2L)).willReturn(danyOptional);
        Credit credit = new Credit();
        credit.setCredit(20);
        credit.setGame(Game.CALL_OF_DUTY_MODERN_WARFARE);
        credit.setGamer(dany);

        given(creditRepository.findAllByGamerIdAndGame(dany.getId(), Game.CALL_OF_DUTY_MODERN_WARFARE)).
                willReturn(Optional.of(credit));

        mvc.perform(put("/api/credits/2/CALL_OF_DUTY_MODERN_WARFARE/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game").value("CALL_OF_DUTY_MODERN_WARFARE"))
                .andExpect(jsonPath("$.credit").value(50));
    }

    @Test
    public void givenGameAndLevel_whenOneGamer_thenReturnJson()
            throws Exception {

        setUpFullDany();

        given(creditRepository.findGamerWithMaximPointOnGameAndLevel(Game.CALL_OF_DUTY_MODERN_WARFARE, Level.NOOB)).
                willReturn(Optional.of(dany));

        mvc.perform(get("/api/maxCredit/CALL_OF_DUTY_MODERN_WARFARE/NOOB" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dany.getName()));
    }
}
