package com.minhubweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {
    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private State state;

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
    Set<Salvo> salvoes = new HashSet<>();


    //Methods, constructors
    public GamePlayer(){}

    public GamePlayer(Game game, Player player, LocalDateTime joinDate, Set<Ship> ships, Set<Salvo> salvoes) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
        this.addShips(ships);
        this.addSalvoes(salvoes);
        this.state = State.PLACE_SHIPS;
    }
    public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
        this.state = State.PLACE_SHIPS;
    }
    public GamePlayer(Set<Ship> ships, Set<Salvo> salvoes) {
        this.addShips(ships);
        this.addSalvoes(salvoes);
    }
    //Methods, others
    public void addShips(Set<Ship> ships){
        ships.stream().forEach(ship -> {
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }

    public void addSalvoes(Set<Salvo> salvoes){
        salvoes.stream().forEach(salvo -> {
            this.addSalvo(salvo);
        });
    }

    public Map<String, Object> gamePlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO());
        if (this.getScore().isPresent()) {
            dto.put("scores", this.getScore().get().getPoints());
        }
        else{
            dto.put("scores", this.getScore());}
        return dto;
    }

    public Map<String, Object> gameViewDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id",this.game.getId());
        dto.put("created",this.game.getDate());
        dto.put("statusPlayer", this.state);
        dto.put("gamePlayers",this.game
                                .getGamePlayers()
                                .stream()
                                .map(GamePlayer::gamePlayerDTO));
        dto.put("ship", this.ships
                            .stream()
                            .map(Ship::shipDTO));
        dto.put("salvo", this.getGame()
                            .getGamePlayers()
                            .stream()
                            .flatMap(gamePlayer -> gamePlayer
                                                    .salvoes
                                                    .stream()
                                                    .map(Salvo::salvoDTO)));
        dto.put("playerHits",this.getSalvoes().stream().collect(Collectors.toMap(Salvo::getTurnNumber, Salvo::getPlayerHits)));
        dto.put("opponentHits",this.getSalvoes().stream().collect(Collectors.toMap(Salvo::getTurnNumber, Salvo::getOpponentHits)));
        dto.put("playerSunkBoats", getPlayerSunks());
        dto.put("opponentSunkBoats", getOpponentSunks());
        dto.put("lefts", this.getGame()
                .getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer
                        .salvoes
                        .stream()
                        .map(Salvo::numberLefts)));
        return dto;
    }

    private Map<Integer, List<Map<String, Object>>> getOpponentSunks() {
        return this.getSalvoes().stream().collect(Collectors.toMap(Salvo::getTurnNumber, Salvo::getOpponentSunks));
    }

    private Map<Integer, List<Map<String, Object>>> getPlayerSunks() {
        return this.getSalvoes().stream().collect(Collectors.toMap(Salvo::getTurnNumber, Salvo::getPlayerSunks));
    }


    public int getNumberOfSunkInTurn(int turn) {
        return getPlayerSunks().get(turn).size();
    }


    private Optional<GamePlayer> getOpponentGamePlayerOptional() {
        return getGame()
                .getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != this.getId())
                .findFirst();
    }


    public  void updateState(GamePlayer opponent, int turn){
        // I am second player
        if (this.getId() > opponent.getId()) {
            int playerSunks = this.getNumberOfSunkInTurn(turn);
            int opponentSunks = opponent.getNumberOfSunkInTurn(turn);
            LocalDateTime now = LocalDateTime.now();

    /*public  void updateState(GamePlayer current, GamePlayer opponent, int turn){
         I am second player
        if (current.getId() > opponent.getId()) {
            int playerSunks = current.getNumberOfSunkInTurn(turn);
            int opponentSunks = opponent.getNumberOfSunkInTurn(turn);
            LocalDateTime now = LocalDateTime.now();*/

            if (playerSunks == 5 && opponentSunks == 5) {
                this.setState(State.TIE);
                this.getGame().addScore(new Score(this, 2, now));
                opponent.setState(State.TIE);
                opponent.getGame().addScore(new Score(opponent, 2, now));
            } else if (opponentSunks == 5) {
                this.setState(State.WIN);
                this.getGame().addScore(new Score(this, 3, now));
                opponent.setState(State.LOSE);
                opponent.getGame().addScore(new Score(opponent, 0, now));
            } else if (playerSunks == 5) {
                this.setState(State.LOSE);
                this.getGame().addScore(new Score(this, 0, now));
                opponent.setState(State.WIN);
                opponent.getGame().addScore(new Score(opponent, 3, now));
            } else {
                this.setState(State.WAIT);
                opponent.setState(State.PLAY);
            }

        } else {
            this.setState(State.WAIT);
            opponent.setState(State.PLAY);
        }
    }





    public Map<String, Object> gamePlayerId(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        return dto;
    }

    public Optional<Score> getScore(){return this.player.getGameScore(this.game);}

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

    public Set<Salvo> getSalvoes() {return salvoes;}
    public void setSalvoes(Set<Salvo> salvoes) {this.salvoes = salvoes;}

    public State getState() {return state;}
    public void setState(State state) {this.state = state;}
}
