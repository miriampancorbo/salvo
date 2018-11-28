package com.minhubweb.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime finishDate;

    private int points;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //Methods, constructors
    public Score(){};

    public Score(Game game, Player player,  int points, LocalDateTime finishDate) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.finishDate = finishDate;
    }

    public Score(GamePlayer gamePlayer,  int points, LocalDateTime finishDate) {
        this.game = gamePlayer.getGame();
        this.player = gamePlayer.getPlayer();
        this.points = points;
        this.finishDate = finishDate;
    }


    public Map<String, Object> scoreDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO());
        dto.put("points", this.getPoints());
        dto.put("finishDates", this.getFinishDate());
        return dto;
    }

    //Set and get

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public Player getPlayer() {return player;}
    public void setPlayer(Player player) {this.player = player;}

    public Game getGame() {return game;}
    public void setGame(Game game) {this.game = game;}

    public LocalDateTime getFinishDate() {return finishDate;}
    public void setFinishDate(LocalDateTime finishDate) {this.finishDate = finishDate;}

    public int getPoints() {return points;}
    public void setPoints(int points) {this.points = points;}


}
