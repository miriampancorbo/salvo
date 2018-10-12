package com.minhubweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class GamePlayer {
    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime joinDate;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Salvo> salvos = new HashSet<>();


    //Methods, constructors
    public GamePlayer(){}

    public GamePlayer(Game game, Player player, Set<Ship> ships) {
        this.game = game;
        this.player = player;
        this.addShips(ships);
    }

    public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }


    //Methods, others


    public void addShips(Set<Ship> ships){
        ships.stream().forEach(ship -> {
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }
    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }


    public void addSalvos(Set<Salvo> salvos){
        salvos.stream().forEach(salvo -> {
            salvo.setGamePlayer(this);
            this.salvos.add(salvo);
        });
    }

    public Map<String, Object> gamePlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO());
        return dto;
    }

    public Map<String, Object> getGameViewDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id",this.game.getId());
        dto.put("created",this.game.getDate());
        dto.put("gamePlayers",this.game.getGamePlayers().stream().map(GamePlayer::gamePlayerDTO));
        dto.put("ship", this.ships.stream().map(Ship::shipDTO));
        return dto;
    }

    public Map<String, Object> gamePlayerId(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        return dto;
    }


    //Set and get
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) {this.joinDate = joinDate;}

    public Player getPlayer() {return player;}
    public void setPlayer(Player player) {this.player = player;}

    public Game getGame() {return game;}
    public void setGame(Game game) {this.game = game;}

    public Set<Ship> getShips() {return ships;}
    public void setShips(Set<Ship> ships) {this.ships = ships;}

    public Set<Salvo> getSalvos() {return salvos;}
    public void setSalvos(Set<Salvo> salvos) {this.salvos = salvos;}
}
