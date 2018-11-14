package com.minhubweb.salvo.models;

import jdk.nashorn.internal.objects.NativeJSON;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.mapping.Array;

import javax.lang.model.type.ArrayType;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public Salvo (int turnNumber, List<String> salvoLocation) {
        this.turnNumber = turnNumber;
        this.salvoLocation = salvoLocation;
    }

    //Methods, others:
    public Map<String, Object> salvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.turnNumber);
        dto.put("player", this.getGamePlayer().gamePlayerDTO());
        dto.put("locations", this.salvoLocation);
        dto.put("hits", checkHits());
        return dto;
    }
    /*public Map<String, Object> salvoCurrentTurnsDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("hit" , this.salvoCurrentTurnDTO());
        return dto;
    }
    public Map<String, Object> salvoOpponentTurnsDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("hit" , this.salvoOpponentTurnDTO());
        return dto;
    }*/
    public Map<String, Object> salvoCurrentTurnDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();

        Optional<GamePlayer> opponentGamePlayer = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() !=this.getGamePlayer().getId()).findFirst();

        long currentGamePlayerId = this.getGamePlayer().getId();
        long opponentGamePlayerId = opponentGamePlayer.get().getId();

        int myTurnNumer = this.turnNumber;
        int opponentTurnNumber = opponentGamePlayer.get().getSalvoes().stream().mapToInt(Salvo::getTurnNumber).max().getAsInt();

        List<Map<String,Object>> myShips = this.getGamePlayer().ships.stream().map(Ship::shipDTO).collect(Collectors.toList());
        List<Map<String,Object>> opponentShips = opponentGamePlayer.get().ships.stream().map(Ship::shipDTO).collect(Collectors.toList());

       // List<String> firstShip = myShips[0].locations;

        Set<List<String>> mySalvoLocation = this.getGamePlayer().getSalvoes().stream().map(Salvo::getSalvoLocation).collect(Collectors.toSet());
        Set<List<String>> opponentSalvoLocations = opponentGamePlayer.get().getSalvoes().stream().map(Salvo::getSalvoLocation).collect(Collectors.toSet());


        dto.put("myid", currentGamePlayerId);
        dto.put("opponentId", opponentGamePlayerId);
        dto.put("turnNumber" , myTurnNumer);
        dto.put("opponentTurnNumber" , opponentTurnNumber);
        dto.put("myships", myShips);
        //dto.put("firstShip", firstShip);
        dto.put("opponentships", opponentShips);
        dto.put("mySalvoLocation", mySalvoLocation);
        dto.put("opponentSalvo", opponentSalvoLocations);

        //dto.put("boatType" , this.getGamePlayer().ships.stream().getClass().);
        //dto.put("myBoats" ,  this.getGamePlayer().getShips());
        //dto.put("opponentBoats" ,  opponentGamePlayer.get().getShips());
        //dto.put("sunk" ,  "");
        //dto.put("numberOfHits" ,  this.hitsDTO());
        //dto.put("currentGamePlayer" ,  currentGamePlayerId);
        return dto;
    }

    private List<Map<String, Object>> checkHits() {
        Optional<GamePlayer> opponentGamePlayer = this.getGamePlayer()
                                                .getGame()
                                                .getGamePlayers()
                                                .stream()
                                                .filter(gamePlayer -> gamePlayer.getId() !=this.getGamePlayer().getId())
                                                .findFirst();


        List <String> allSalvoes= new ArrayList<>();
        this.gamePlayer.getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurnNumber() <= this.getTurnNumber())
                .forEach(salvo -> allSalvoes.addAll(salvo.getSalvoLocation()));
        List<Map<String, Object>> allSinks = new ArrayList<>();

        if (opponentGamePlayer.isPresent()) {
            allSinks =
                    opponentGamePlayer
                    .get()
                    .getShips()
                    .stream()
                    .filter(ship -> allSalvoes.containsAll(ship.getShipLocation()))
                    .map(Ship::shipDTO)
                    .collect(Collectors.toList());
        }
        return allSinks;
    }

        /*Optional<GamePlayer> opponentGamePlayer = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() !=this.getGamePlayer().getId()).findFirst();
        List <String> hits = this.salvoLocation.stream().filter(hit -> opponentGamePlayer.get().getShips().stream().anyMatch(ship -> ship.getShipLocation().contains(hit)) ).collect(Collectors.toList());
        return hits;
    }
    private List<String> checkBoatPerHit() {


        this.gamePlayer.getSalvoes().stream().map(salvo -> salvo.getSalvoLocation().stream().map(cell -> allSalvoes.add(cell) ));

        Optional<GamePlayer> opponentGamePlayer = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId() !=this.getGamePlayer().getId()).findFirst();
        opponentGamePlayer.get().getShips().stream().map(ship -> )
        return allSalvoes;*/
    /*public Map<String, Object> salvoOpponentTurnDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        long currentGamePlayer = this.getGamePlayer().getId();
        Optional<GamePlayer>  opponentGamePlayer;*/
        /*if(this.getGamePlayer().getGame().getGamePlayers().size()==2) {
            opponentGamePlayer = this.getGamePlayer().getPlayer().gamePlayers.stream().filter(gamePlayer -> gamePlayer.getId() != currentGamePlayer).findFirst();
        }
        else {opponentGamePlayer =null;}*/
        /*dto.put("turnNumber" , this.turnNumber);
        dto.put("boatType" ,  this.getGamePlayer().getShips());
        dto.put("numberOfHits" ,  this.hitsDTO());
        dto.put("sunk" ,  "");
        dto.put("opponentGamePlayer" ,  "");
        return dto;
    }
    public Map<String, Object> hitsDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        return dto;
    }*/

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
