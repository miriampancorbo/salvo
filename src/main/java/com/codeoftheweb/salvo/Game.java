package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    @JsonIgnore
    public List<Player> getGame() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }
}

   // LocalDate date = LocalDate.parse("2011-08-03")
//@LocalDate(LocalDate date = LocalDate.parse(string, formatter));
