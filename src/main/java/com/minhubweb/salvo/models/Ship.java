package com.minhubweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private ShipType shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="shipLocation")
    public List<String> shipLocation = new ArrayList<>();

    public Ship (){}
    public Ship (ShipType type, List<String> shipLocation) {
        this.shipType = type;
        this.shipLocation = shipLocation;
    }

    public Map<String, Object> shipDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("type", this.getShipType());
        dto.put("locations", this.getShipLocation());
        return dto;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public ShipType getShipType() {return shipType;}
    public void setShipType(ShipType shipType) {this.shipType = shipType;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

    public List<String> getShipLocation() {return shipLocation;}
    public void setShipLocation(List<String> shipLocation) {this.shipLocation = shipLocation;}
}
