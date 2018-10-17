package com.minhubweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			Player player1 = new Player("Jack Bauer");
			Player player2 = new Player("Chloe O'Brian");
			Player player3 = new Player("Kim Bauer");
			Player player4 = new Player("Tony Almeida");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Game game1 =new Game(LocalDateTime.parse("2018-09-01T15:23:50.411"));
			Game game2 =new Game(LocalDateTime.parse("2018-09-02T15:23:50.411"));
			Game game3 =new Game(LocalDateTime.parse("2018-09-03T15:23:50.411"));
			Game game4 =new Game(LocalDateTime.parse("2018-09-04T15:23:50.411"));
			Game game5 =new Game(LocalDateTime.parse("2018-09-05T15:23:50.411"));
			Game game6 =new Game(LocalDateTime.parse("2018-09-06T15:23:50.411"));
			Game game7 =new Game(LocalDateTime.parse("2018-09-07T15:23:50.411"));
			Game game8 =new Game(LocalDateTime.parse("2018-09-08T15:23:50.411"));
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);
			Set<Ship> shipSet1 = new HashSet<>();
			Set<Ship> shipSet2 = new HashSet<>();
			Set<Ship> shipSet3 = new HashSet<>();
			Set<Ship> shipSet4 = new HashSet<>();
			Set<Ship> shipSet5 = new HashSet<>();
			Set<Ship> shipSet6 = new HashSet<>();
			Set<Ship> shipSet7 = new HashSet<>();
			Set<Ship> shipSet8 = new HashSet<>();
			Set<Ship> shipSet9 = new HashSet<>();
			Set<Ship> shipSet10 = new HashSet<>();
			Set<Ship> shipSet11 = new HashSet<>();
			Set<Ship> shipSet12 = new HashSet<>();
			Set<Ship> shipSet13 = new HashSet<>();

			shipSet1.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("H2", "H3", "H4"))));
			shipSet1.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("E1", "F1", "G1"))));
			shipSet1.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("B4", "B5"))));
			shipSet2.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet2.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("F1", "F2"))));
			shipSet3.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet3.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet4.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4"))));
			shipSet4.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6"))));
			shipSet5.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet5.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet6.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4"))));
			shipSet6.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6"))));
			shipSet7.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet7.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet8.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4"))));
			shipSet8.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6"))));
			shipSet9.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet9.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet10.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4"))));
			shipSet10.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6"))));
			shipSet11.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet11.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet12.add(new Ship ("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5"))));
			shipSet12.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7"))));
			shipSet13.add(new Ship ("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4"))));
			shipSet13.add(new Ship ("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6"))));

			Set<Salvo> salvoSet1 = new HashSet<>();
			Set<Salvo> salvoSet2 = new HashSet<>();
			Set<Salvo> salvoSet3 = new HashSet<>();
			Set<Salvo> salvoSet4 = new HashSet<>();
			Set<Salvo> salvoSet5 = new HashSet<>();
			Set<Salvo> salvoSet6 = new HashSet<>();
			Set<Salvo> salvoSet7 = new HashSet<>();
			Set<Salvo> salvoSet8 = new HashSet<>();
			Set<Salvo> salvoSet9 = new HashSet<>();
			Set<Salvo> salvoSet10 = new HashSet<>();
			salvoSet1.add(new Salvo (1, new ArrayList<>(Arrays.asList("B5", "C5", "F1"))));
			salvoSet1.add(new Salvo (2, new ArrayList<>(Arrays.asList("F2", "D5"))));
			salvoSet3.add(new Salvo (1, new ArrayList<>(Arrays.asList("A2", "A4", "G6"))));
			salvoSet3.add(new Salvo (2, new ArrayList<>(Arrays.asList("A3", "H6"))));
			salvoSet5.add(new Salvo (1, new ArrayList<>(Arrays.asList("G6", "H6", "A4"))));
			salvoSet5.add(new Salvo (2, new ArrayList<>(Arrays.asList("A2", "A3", "D8"))));
			salvoSet7.add(new Salvo (1, new ArrayList<>(Arrays.asList("A3", "A4", "F7"))));
			salvoSet7.add(new Salvo (2, new ArrayList<>(Arrays.asList("A2", "G6", "H6"))));
			salvoSet9.add(new Salvo (1, new ArrayList<>(Arrays.asList("A1", "G6", "H6"))));
			salvoSet9.add(new Salvo (2, new ArrayList<>(Arrays.asList("G6", "G7", "G8"))));
			salvoSet9.add(new Salvo (3, new ArrayList<>()));
			salvoSet2.add(new Salvo (1, new ArrayList<>(Arrays.asList("B4", "B5", "B6"))));
			salvoSet2.add(new Salvo (2, new ArrayList<>(Arrays.asList("E1", "H3", "A2"))));
			salvoSet4.add(new Salvo (1, new ArrayList<>(Arrays.asList("B5", "D5", "C7"))));
			salvoSet4.add(new Salvo (2, new ArrayList<>(Arrays.asList("C5", "C6"))));
			salvoSet6.add(new Salvo (1, new ArrayList<>(Arrays.asList("H1", "H2", "H3"))));
			salvoSet6.add(new Salvo (2, new ArrayList<>(Arrays.asList("E1", "F2", "G3"))));
			salvoSet8.add(new Salvo (1, new ArrayList<>(Arrays.asList("B5", "C6", "H1"))));
			salvoSet8.add(new Salvo (2, new ArrayList<>(Arrays.asList("C5", "C7", "D5"))));
			salvoSet10.add(new Salvo (1, new ArrayList<>(Arrays.asList("B5", "B6", "C7"))));
			salvoSet10.add(new Salvo (2, new ArrayList<>(Arrays.asList("C6", "D6", "E6"))));
			salvoSet10.add(new Salvo (3, new ArrayList<>(Arrays.asList("H1", "H8"))));

			gamePlayerRepository.save(new GamePlayer(game1, player1, LocalDateTime.now(), shipSet1, salvoSet1));
			gamePlayerRepository.save(new GamePlayer(game1, player2, LocalDateTime.now(), shipSet2, salvoSet2));
			gamePlayerRepository.save(new GamePlayer(game2, player1, LocalDateTime.now(), shipSet3, salvoSet3));
			gamePlayerRepository.save(new GamePlayer(game2, player2, LocalDateTime.now(), shipSet4, salvoSet4));
			gamePlayerRepository.save(new GamePlayer(game3, player2, LocalDateTime.now(), shipSet5, salvoSet5));
			gamePlayerRepository.save(new GamePlayer(game3, player4, LocalDateTime.now(), shipSet6, salvoSet6));
			gamePlayerRepository.save(new GamePlayer(game4, player2, LocalDateTime.now(), shipSet7, salvoSet7));
			gamePlayerRepository.save(new GamePlayer(game4, player1, LocalDateTime.now(), shipSet8, salvoSet8));
			gamePlayerRepository.save(new GamePlayer(game5, player4, LocalDateTime.now(), shipSet9, salvoSet9));
			gamePlayerRepository.save(new GamePlayer(game5, player1, LocalDateTime.now(), shipSet10, salvoSet10));
			gamePlayerRepository.save(new GamePlayer(game6, player3, LocalDateTime.now(), shipSet11, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game7, player4, LocalDateTime.now(), new HashSet<>(), new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game8, player3, LocalDateTime.now(), shipSet12, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game8, player4, LocalDateTime.now(), shipSet13, new HashSet<>()));

			scoreRepository.save(new Score(game1, player2, 0, LocalDateTime.now()));
			scoreRepository.save(new Score(game1, player1, 3, LocalDateTime.now()));
			scoreRepository.save(new Score(game2, player1, 2, LocalDateTime.now()));
			scoreRepository.save(new Score(game2, player2, 2, LocalDateTime.now()));
			scoreRepository.save(new Score(game3, player2, 3, LocalDateTime.now()));
			scoreRepository.save(new Score(game3, player4, 0, LocalDateTime.now()));
			scoreRepository.save(new Score(game4, player2, 0, LocalDateTime.now()));
			scoreRepository.save(new Score(game4, player1, 3, LocalDateTime.now()));
			scoreRepository.save(new Score(game5, player4, 0, LocalDateTime.now()));
			scoreRepository.save(new Score(game5, player1, 3, LocalDateTime.now()));
			scoreRepository.save(new Score(game6, player3, 2, LocalDateTime.now()));
			scoreRepository.save(new Score(game7, player4, 2, LocalDateTime.now()));
			scoreRepository.save(new Score(game8, player3, 0, LocalDateTime.now()));
			scoreRepository.save(new Score(game8, player4, 3, LocalDateTime.now()));
		};
	}
}
