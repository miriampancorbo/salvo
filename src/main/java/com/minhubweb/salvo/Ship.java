package com.minhubweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    //public enum Type {CRUISER, DESTROYER, BATTLESHIP};
    private String type;

    //Methods, constructors:
    public Ship (){}
    public Ship (String type, List<String> shipLocation) {
        this.type = type;
        this.shipLocation = shipLocation;
    }

    //Methods, others:
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="shipLocation")
    public List<String> shipLocation = new ArrayList<>();


    public Map<String, Object> shipDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("type", this.getType());
        dto.put("locations", this.getShipLocation());
        return dto;
    }

    //Set and get:
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

    public List<String> getShipLocation() {return shipLocation;}
    public void setShipLocation(List<String> shipLocation) {this.shipLocation = shipLocation;}
}
