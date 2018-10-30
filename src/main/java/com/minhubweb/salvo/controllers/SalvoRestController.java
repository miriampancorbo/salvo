package com.minhubweb.salvo.controllers;

import com.minhubweb.salvo.models.*;
import com.minhubweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/games")
    public Map<String, Object> getAllGames(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();
        if(isGuest(authentication)) {
            dto.put("user", "guest");
        }
        else {
            dto.put("user", playerRepository.findByUserName(authentication.getName()).currentPlayerDTO());
        }
        dto.put("games", gameRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(Game::gamesDTO)
                .collect(toList()));

        return dto;
    }
    @PostMapping(path = "/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "No user logged in"), HttpStatus.UNAUTHORIZED);//409 403
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        Game game = gameRepository.save(new Game());
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, LocalDateTime.now()));
        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/game_view/{gamePlayerId}")// cambie findGameId por getGamePlayer
    public ResponseEntity<Map<String, Object>> getGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (gamePlayer.isPresent()) {
            if (!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())) {
                return new ResponseEntity<>(makeMap("error", "Unauthorized to watch this game."), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(gamePlayer.get().gameViewDTO(), HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "Unauthorized to watch this game."), HttpStatus.UNAUTHORIZED);
        }
    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @PostMapping(path = "/players")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, @RequestParam String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name or password"), HttpStatus.FORBIDDEN);//403
        }
        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "This user name already exists. Please, try with another one."), HttpStatus.CONFLICT);//409
        }
        Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    //----------------------------------------------
    //----------------------------------


    /*@PostMapping(path = "/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinAGame(@PathVariable Long gameId,Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Should login to join a game."), HttpStatus.FORBIDDEN);//403
        }
        Optional<Game> game = gameRepository.findById(gameId);

        Player player = playerRepository.findByUserName(authentication.getName());
       // Game game = gameRepository.findById(gamePlayer.get().getPlayer().get)
        return ();
    }
}*/


       /* return new ResponseEntity<>(makeMap("error", "This user name already exists. Please, try with another one."), HttpStatus.CONFLICT);//409
        }
        Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

     if(!isGuest(authentication)){
        Player player = playerRepository.findByUserName(authentication.getName());
        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "You are not logged."), HttpStatus.UNAUTHORIZED);//409
        }

        Game game = gameRepository.save(new Game(LocalDateTime.now()));

        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, LocalDateTime.now()));
        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }
}*/