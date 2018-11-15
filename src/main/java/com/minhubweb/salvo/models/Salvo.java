package com.minhubweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

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

    /**
     * Generates the salvoDTO from current state
     * @return the corresponding DTO
     */
    public Map<String, Object> salvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.turnNumber);
        dto.put("player", this.getGamePlayer().gamePlayerDTO());
        dto.put("locations", this.salvoLocation);
        dto.put("sunks", getSunks());
        return dto;
    }


    /**
     * Get map of ships with hits that the current player has performed
     * @return
     */
    public Map<String, List<String>> getMyHits() {
        Optional<GamePlayer> opponent = getOpponentGamePlayerOptional();
        return opponent.isPresent() ? getHitsOver(opponent.get(), salvoLocation) : new HashMap<>();
    }

    public Map<String, List<String>> getOpponentHits() {
        Optional<GamePlayer> opponent = getOpponentGamePlayerOptional();
        List<String> opponentSalvoLocationsThisTurn = new ArrayList<>();
        if (opponent.isPresent()) {
            Optional<Salvo> salvoOptional = opponent.get().getSalvoes().stream()
                    .filter(s -> hasTurnAndPlayerId(opponent.get(), s))
                    .findFirst();
            if (salvoOptional.isPresent()) {
                opponentSalvoLocationsThisTurn = salvoOptional.get().getSalvoLocation();
            }
        }

        return getHitsOver(gamePlayer, opponentSalvoLocationsThisTurn);

    }

    private boolean hasTurnAndPlayerId(GamePlayer opponent, Salvo s) {
        return s.getTurnNumber() == turnNumber && s.getGamePlayer().getPlayer().getId() == opponent.getPlayer().getId();
    }

    private Map<String, List<String>> getHitsOver(GamePlayer gamePlayer, List<String> opponentSalvoLocations) {
        Map<String, List<String>> hits = new HashMap<>();

        if (gamePlayer.getShips() != null) {
            for (Ship ship : gamePlayer.getShips()) {
                List<String> shipHits = getShipHits(ship, opponentSalvoLocations);
                if (!shipHits.isEmpty()) {
                    hits.put(ship.getShipType().toString(), shipHits);
                }
            }
        }

        return hits;
    }


    private List<String> getShipHits(Ship ship, List<String> opponentSalvoLocations) {
        List<String> shipHits = new ArrayList<>();
        for (String hit : opponentSalvoLocations) {
                if (ship.getShipLocation().contains(hit)) {
                shipHits.add(hit);
            }
        }
        return shipHits;
    }

    private Optional<GamePlayer> getOpponentGamePlayerOptional() {
        return getGamePlayer()
                .getGame()
                .getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != getGamePlayer().getId())
                .findFirst();
    }

    private List<Map<String, Object>> getSunks() {
        Optional<GamePlayer> opponentGamePlayer = getOpponentGamePlayerOptional();


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
                    .collect(toList());
        }
        return allSinks;
    }

        /*
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
