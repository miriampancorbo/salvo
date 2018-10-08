package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

    /*public Map gameInfoMap;// = new HashMap<>();

    public Map <String, Object> GameInfoMap{
        String = this.id;
        Object = LocalDateTime.now().toInstant(ZoneOffset.MAX).toEpochMilli();
    }*/

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }//NO SE SI HACE FLATA

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }//NO SE SI HACE FLATA

    private LocalDateTime date;

    public Game(){
        this.date = LocalDateTime.now();
    }

    public Game(LocalDateTime date){ //metodo de tipo constructor. no retorna nada
        this.date = date;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }


    //SET AND GET:

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}

/*    public Map<String, Object> getGameInfoMap() {return gameInfoMap;}

    public void setGameInfoMap(Map<String, Object> gameInfo) {this.gameInfoMap = gameInfo;}
*/

    @JsonIgnore
    public List<Player> getGame() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    public Map<String, Object> gamesDTO() {
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", Timestamp.valueOf(this.getDate()).getTime());
        dto.put("gamePlayers", this.getGamePlayers().stream().map(GamePlayer::gamePlayerDTO).collect(toList()));
        return dto;
    }

}

   // LocalDate date = LocalDate.parse("2011-08-03")
//@LocalDate(LocalDate date = LocalDate.parse(string, formatter));
