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
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("user", "guest");
        } else {
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
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_NEED_LOGIN), HttpStatus.UNAUTHORIZED);//409 403
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        Game game = gameRepository.save(new Game());
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, LocalDateTime.now()));
        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (gamePlayer.isPresent()) {
            if (!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())) {
                return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_WATCH_GAME), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(gamePlayer.get().gameViewDTO(), HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_WATCH_GAME), HttpStatus.UNAUTHORIZED);
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

    @PostMapping(path = "/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinAGame(@PathVariable Long gameId,
                                                         Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages. MSG_ERROR_UNAUTHORIZED_NEED_LOGIN), HttpStatus.UNAUTHORIZED);//401
        }
        Optional<Game> game = gameRepository.findById(gameId);
        if (!game.isPresent()) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_FORBIDDEN_NO_GAME), HttpStatus.FORBIDDEN);//403
        }
        if (game.get().getGamePlayers().size() > 1) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_FORBIDDEN), HttpStatus.FORBIDDEN);//403
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        Optional<GamePlayer> firstGamePlayer = game.get().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer().getId() == player.getId()).findFirst();
        if (firstGamePlayer.isPresent()){
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_FORBIDDEN_BE_OWN_OPPONENT), HttpStatus.FORBIDDEN);//403
        }
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(game.get(), player, LocalDateTime.now()));
        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @PostMapping(path = "/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> saveShips(@RequestBody Set<Ship> ships,
                                                         @PathVariable Long gamePlayerId,
                                                         Authentication authentication) {
        Optional<GamePlayer> currentGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_NEED_LOGIN), HttpStatus.UNAUTHORIZED);//401
        }
        if(!currentGamePlayer.isPresent() ){
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, "There is no game with that player."), HttpStatus.FORBIDDEN);//403
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        if (currentGamePlayer.get().getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_ADD_SHIPS), HttpStatus.UNAUTHORIZED);//401
        }
        if (!currentGamePlayer.get().getShips().isEmpty()) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_SHIPS_ALREADY_SAVED), HttpStatus.FORBIDDEN);//403
        }

        Optional<GamePlayer> opponentGamePlayer = currentGamePlayer.get().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() !=currentGamePlayer.get().getId()).findFirst();


        currentGamePlayer.get().addShips(ships);
        if (opponentGamePlayer.isPresent() && !opponentGamePlayer.get().getShips().isEmpty()) {
            if (currentGamePlayer.get().getId() < opponentGamePlayer.get().getId()) {
                currentGamePlayer.get().setState(State.PLAY);
                opponentGamePlayer.get().setState(State.WAIT);
            } else {
                currentGamePlayer.get().setState(State.WAIT);
                opponentGamePlayer.get().setState(State.PLAY);
            }
            gamePlayerRepository.save(opponentGamePlayer.get());
        }
        else {
            currentGamePlayer.get().setState(State.WAIT_OPPONENT);
        }
        gamePlayerRepository.save(currentGamePlayer.get());
        return new ResponseEntity<>(makeMap(Messages.KEY_SUCCESS, Messages.MSG_SUCCESS_SHIPS_ADDED), HttpStatus.CREATED); //201
    }


    @PostMapping(path = "/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> saveSalvoes(@RequestBody Salvo salvo,
                                                           @PathVariable Long gamePlayerId,
                                                           Authentication authentication) {
        Optional<GamePlayer> currentGamePlayer = gamePlayerRepository.findById(gamePlayerId);

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_NEED_LOGIN), HttpStatus.UNAUTHORIZED);//401
        }

        if(!currentGamePlayer.isPresent() ){
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_FORBIDDEN_NO_GAME), HttpStatus.FORBIDDEN);//403
        }

        if (currentGamePlayer.get().getState() != State.PLAY) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, currentGamePlayer.get().getState()), HttpStatus.FORBIDDEN);//403

        }

        Optional<GamePlayer> opponentGamePlayer = currentGamePlayer.get().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() !=currentGamePlayer.get().getId()).findFirst();

        if (!opponentGamePlayer.isPresent()) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_NO_OPPONENT), HttpStatus.FORBIDDEN);//403
        }

        int currentTurn = currentGamePlayer.get().getSalvoes().stream().sorted(Comparator.comparing(Salvo::getTurnNumber).reversed()).findFirst().orElse(new Salvo(0,null)).getTurnNumber() + 1;
        int opponentTurn = opponentGamePlayer.get().getSalvoes().stream().sorted(Comparator.comparing(Salvo::getTurnNumber).reversed()).findFirst().orElse(new Salvo(0,null)).getTurnNumber();

        if(currentTurn - opponentTurn > 1 || currentTurn - opponentTurn < -1) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_OPPONENT_TURN), HttpStatus.FORBIDDEN);//403
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        if (currentGamePlayer.get().getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_UNAUTHORIZED_SAVE_SALVOES), HttpStatus.UNAUTHORIZED);//401
        }
        if (salvo.getSalvoLocation().size()==0) {
            return new ResponseEntity<>(makeMap(Messages.KEY_ERROR, Messages.MSG_ERROR_SEND_AT_LEAST_ONE_SALVO), HttpStatus.FORBIDDEN);//403
        }
        if(salvo.getSalvoLocation().size() > currentGamePlayer.get().getShips().size()){ //AQUI RESTAR EL Sinks
        }

        salvo.setTurnNumber(currentTurn);
        currentGamePlayer.get().addSalvo(salvo);
        currentGamePlayer.get().updateState(opponentGamePlayer.get(), currentTurn);

        gamePlayerRepository.save(currentGamePlayer.get());
        gamePlayerRepository.save(opponentGamePlayer.get());
        return new ResponseEntity<>(makeMap(Messages.KEY_SUCCESS, Messages.MSG_SUCCESS_SALVOES_ADDED), HttpStatus.CREATED); //201
    }
}

