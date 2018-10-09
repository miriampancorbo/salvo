package com.minhubweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    public Ship (String type) {this.type = type;}

    //Methods, others:
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    @ElementCollection
    @Column(name="shipLocation")
    private List<String> shipLocation = new ArrayList<>();

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
