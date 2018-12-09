package com.minhubweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date; //LocalDateTime= tipo de variable

    public Game(){
        this.date = LocalDateTime.now();
    }

    public Game(LocalDateTime date){ //metodo de tipo constructor. no retorna nada
        this.date = date;
    }

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Score> scores;

    public void addScore(Score score) {
        score.setGame(this);
        scores.add(score);
    }

    @JsonIgnore
    public List<Player> getGame() {return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());}

    public Map<String, Object> gamesDTO() {
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
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

    public void setId(long id) {this.id = id;}
    public long getId() {return id;}

    public void setDate(LocalDateTime date) {this.date = date;}
    public LocalDateTime getDate() {return date;}

    public Set<GamePlayer> getGamePlayers() {return gamePlayers;}
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {this.gamePlayers = gamePlayers;}

    public Set<Score> getScores() {return scores;}
    public void setScores(Set<Score> scores) {this.scores = scores;}
}
