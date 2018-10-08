package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/api/games")
    public List<Map<String, Object>> gamesIdAndCreated(){
        return gameRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Game::getId))
                .map(this::createMap)
                .collect(toList());
    }

   private Map<String, Object> createMap(Game g) {
        Map<String,Object> mapGamesInfo = new LinkedHashMap<>();
        mapGamesInfo.put("id", g.getId());
        mapGamesInfo.put("created", Timestamp.valueOf(g.getDate()).getTime());
     //   mapGamesInfo.put("gamePlayers", map.mapGamePlayersInfo);
        return mapGamesInfo;
    }

   /* private List<Map<String, Object>> mapGamePlayersInfo(GamePlayer gp){
        Map<String,Object> mapGamePlayersInfo = new LinkedHashMap<>();
        mapGamePlayersInfo.put("id", gp.getId());
        mapGamePlayersInfo.put("player", map.mapPlayers);
        return mapGamePlayersInfo;
    }

    private List<Map<String, Object>> mapPlayers(GamePlayer gp){
        Map<String,Object> mapGamePlayersInfo = new LinkedHashMap<>();
        mapPlayers.put("id", gp.getPlayersId());
        //mapPlayers.put("player", map.mapPlayers);
        return mapPlayers;
    }*/
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