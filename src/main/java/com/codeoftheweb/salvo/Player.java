package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

    private String userName;

    public Player(){ }

    public Player(String userName) {
        this.userName = userName;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; } //NO SE SI HACE FALTA

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }//NO SE SI HACE FLATA

    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    //SET AND GET:

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}







