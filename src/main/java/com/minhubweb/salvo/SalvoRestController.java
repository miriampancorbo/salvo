package com.minhubweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @GetMapping("/games")
    public List<Map<String, Object>> getGames(){
        return gameRepository
                .findAll()
                .stream()
                .map(Game::gamesDTO)
                .collect(toList());
    }

    @GetMapping("/game_view/{id}")
    public Map<String, Object> getGameView(@PathVariable("id") long id){
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(id);
        return optionalGamePlayer.get().gameViewDTO();
    }
}




