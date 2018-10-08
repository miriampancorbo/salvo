package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @GetMapping("/games")
    public Map<String, Object> getgames(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("games", gameRepository.findAll().stream().map(Game::gamesDTO).collect(toList()));
        return dto;
    }


}




   /* Map<String, Integer> ages = new HashMap<>();
ages.put("John", 32);
        ages.put("Mary", 26);
        ages.put("Bill", 19);
*/

    /*List<Game> getAll() {
        return repo.findAll();
    }


    Map<String, Object>
    */

/* @GetMapping("/api/games")
    //public Map<String, Object> gameInfo = new HashMap<>();
    public List<Long> getGamesId() {
        return gameRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(Game::getGameInfo)
                .collect(toList());



                    @GetMapping("/api/games")
    public List<Long> getGamesId() {
        return gameRepository.findAll().stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(Game::getId)
                .collect(toList());
    }
}



    public Map <String, Object>  gamesIdAndCreated{
        gamesIdAndCreated = new HashMap<>();
    //public HashMap gamesIdAndCreated(){
        return gamesIdAndCreated.forEach((Id, id) ->
        return gamesIdAndCreated.forEach((Created, created) -> System.out.println(name + " is " + age));
                gameRepository
                .findAll().stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(Game::getId)
                .map(Game::getDate)
                .collect(toList());
    }

 */