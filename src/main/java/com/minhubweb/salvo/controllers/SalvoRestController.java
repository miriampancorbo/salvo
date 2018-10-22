package com.minhubweb.salvo.controllers;

import com.minhubweb.salvo.models.Game;
import com.minhubweb.salvo.models.GamePlayer;
import com.minhubweb.salvo.models.Player;
import com.minhubweb.salvo.repositories.GamePlayerRepository;
import com.minhubweb.salvo.repositories.GameRepository;
import com.minhubweb.salvo.repositories.PlayerRepository;
import com.minhubweb.salvo.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    private Object gameService;

    @Autowired
    private ShipRepository shipRepository;

    @GetMapping("/games")
    public Map<String, Object> getAllGames(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();
        if(isGuest(authentication))
            dto.put("user","plis fucking log!!!");
        else
            dto.put("user", playerRepository.findByUserName(authentication.getName()).currentPlayerDTO());

        dto.put("games",gameRepository
                .findAll() //esto me trae todo
                .stream() // esto crea un "array imaginario"
                .sorted(Comparator.comparing(Game::getId)) //ordeno el stream
                .map(Game::gamesDTO)
                .collect(toList()));

        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> findGameId(@PathVariable Long gamePlayerId){
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        return gamePlayer.get().gameViewDTO();

    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}


/*
    @GetMapping("/games")
    public Map<String, Object> (Authentication authentication)
        Map<String,Object> dto = new LinkedHashMap<>();
            dto.put("myInformation",
                            playerRepository.findByUserName(authentication.getName()));
            dto.put("games",
                            gameRepository
                            .findAll()
                            .stream()
                            .map(Game::gamesDTO)
                            .collect(toList()));

        return dto;
    }

    @GetMapping("/game_view/{id}")
    public Map<String, Object> getGameView(@PathVariable("id") long id){
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(id);
        return optionalGamePlayer.get().gameViewDTO();
    }

*/
    /*public List<Map<String, Object>> getGames(){
        return gamePlayerRepository
                .findAll()
                .stream()
                .map(GamePlayer::gamePlayerDTOIndex)
                .collect(toList());
    }*/

        /*public List<Game> getAll(Authentication authentication) {
            return gameRepository.findByDate(authentication.getName());
        }*/



    /*@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;*/

    /*@RequestMapping(path = "/persons", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/




