package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository; //HACER QUE SOLO SEAN LOS IDS

    @GetMapping("/api/games")
    public List<Long> getGamesId() {
        return gameRepository.findAll().stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(Game::getId)
                .collect(toList());
    }
}

    /*List<Game> getAll() {
        return repo.findAll();
    }

    Map<String, Object>
    */

