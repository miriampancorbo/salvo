package com.minhubweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    public int turnNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvoLocation")
    public List<String> salvoLocation = new ArrayList<>();

    //Methods, constructors:
    public Salvo (){}
    public Salvo (Integer turnNumber, List<String> salvoLocation) {
        this.turnNumber = turnNumber;
        this.salvoLocation = salvoLocation;
    }

    //Methods, others:
    public Map<String, Object> salvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.turnNumber);
        dto.put("player", this.getGamePlayer().gamePlayerDTO());
        dto.put("locations", this.salvoLocation);
        return dto;
    }
    //Set and get
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public int getTurnNumber() {return turnNumber;}
    public void setTurnNumber(int turnNumber) {this.turnNumber = turnNumber;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

    public List<String> getSalvoLocation() {return salvoLocation;}
    public void setSalvoLocation(List<String> salvoLocation) {this.salvoLocation = salvoLocation;}
}
