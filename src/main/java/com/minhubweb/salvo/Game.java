package com.minhubweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game{

    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date;

    //Methods, constructor:
    public Game(){
        this.date = LocalDateTime.now();
    }

    public Game(LocalDateTime date){ //metodo de tipo constructor. no retorna nada
        this.date = date;
    }

    //Methods, others:
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore
    public List<Player> getGame() {return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());}

    public Map<String, Object> gamesDTO() {
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this
                            .getId());
        dto.put("created", Timestamp
                            .valueOf(this.getDate())
                            .getTime());
        dto.put("gamePlayers", this
                            .gamePlayers
                            .stream()
                            .map(GamePlayer::gamePlayerDTO)
                            .collect(toList()));

        return dto;
    }

    //Set and get:
    public void setId(long id) {this.id = id;}
    public long getId() {return id;}

    public void setDate(LocalDateTime date) {this.date = date;}
    public LocalDateTime getDate() {return date;}

    public Set<GamePlayer> getGamePlayers() {return gamePlayers;}
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {this.gamePlayers = gamePlayers;}

}
