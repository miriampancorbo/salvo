package com.minhubweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;

    private String password;

    //Methods, constructors:
    public Player(){ }

    public Player(String userName, String password) {this.userName = userName;}

    //Methods, others:
    @OneToMany(mappedBy="player", fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Score> scores;

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public List<Game> getGames() {return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());}

    public Map<String, Object> playerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("userName", this.getUserName());

        return dto;
    }

    public Score getGameScore(Game game){return scores.stream().filter(score -> score.getGame().getId() == game.getId()).findAny().orElse(null);}

    //Set and get:
    public void setId(long id) {this.id = id;}
    public long getId() {return id;}


    public void setUserName(String userName) {this.userName = userName;}
    public String getUserName() {return userName;}

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {this.gamePlayers = gamePlayers;}

    public Set<Score> getScores() {return scores;}
    public void setScores(Set<Score> scores) {this.scores = scores;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}







