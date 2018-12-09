package com.minhubweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Salvo {
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

    public Salvo (){}
    public Salvo (int turnNumber, List<String> salvoLocation) {
        this.turnNumber = turnNumber;
        this.salvoLocation = salvoLocation;
    }

    public Map<String, Object> salvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.turnNumber);
        dto.put("player", this.getGamePlayer().gamePlayerDTO());
        dto.put("locations", this.salvoLocation);
        dto.put("hits", getPlayerHits());
        return dto;
    }

    public Map<String, List<String>> getPlayerHits() {
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

    private boolean hasTurnAndPlayerId(GamePlayer opponent, Salvo salvo) {
        return salvo.getTurnNumber() == turnNumber && salvo.getGamePlayer().getPlayer().getId() == opponent.getPlayer().getId();
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

    public Optional<GamePlayer> getOpponentGamePlayerOptional() {
        return getGamePlayer()
                .getGame()
                .getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != getGamePlayer().getId())
                .findFirst();
    }

    public List<Map<String, Object>> getPlayerSunks() {
        Optional<GamePlayer> opponentGamePlayer = getOpponentGamePlayerOptional();

        List <String> allSalvoes= new ArrayList<>();
        opponentGamePlayer.get().getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurnNumber() <= this.getTurnNumber())
                .forEach(salvo -> allSalvoes.addAll(salvo.getSalvoLocation()));
        List<Map<String, Object>> allSinks = new ArrayList<>();
        allSinks =
                this.gamePlayer
                        .getShips()
                        .stream()
                        .filter(ship -> allSalvoes.containsAll(ship.getShipLocation()))
                        .map(Ship::shipDTO)
                        .collect(toList());
        return allSinks;
    }


    public List<Map<String, Object>> getOpponentSunks() {
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

    public Map<String, Object> numberLefts() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayerId", this.getGamePlayer().getId());
        long playerId =this.getGamePlayer().getPlayer().getId();
        dto.put("playerId", playerId);
        dto.put("turn", this.turnNumber);
        dto.put("lefts", 5 - this.getPlayerSunks().size());
        return dto;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public int getTurnNumber() {return turnNumber;}
    public void setTurnNumber(int turnNumber) {this.turnNumber = turnNumber;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

    public List<String> getSalvoLocation() {return salvoLocation;}
    public void setSalvoLocation(List<String> salvoLocation) {this.salvoLocation = salvoLocation;}
}
