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
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
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

			shipSet1.add(new Ship ("Destroyer", Arrays.asList("H2, H3, H4")));
			shipSet1.add(new Ship ("Submarine", Arrays.asList("E1, F1, G1")));
			shipSet1.add(new Ship ("Patrol Boat", Arrays.asList("B4, B5")));
			shipSet2.add(new Ship ("Destroyer", Arrays.asList("B5, C5, D5")));
			shipSet2.add(new Ship ("Patrol Boat", Arrays.asList("F1, F2")));
			shipSet3.add(new Ship ("Destroyer", Arrays.asList("B5, C5, D5")));
			shipSet3.add(new Ship ("Patrol Boat", Arrays.asList("C6, C7")));
			shipSet4.add(new Ship ("Submarine", Arrays.asList("A2, A3, A4")));
			shipSet4.add(new Ship ("Patrol Boat", Arrays.asList("G6, H6")));

			gamePlayerRepository.save(new GamePlayer(game1, player1, shipSet1));
			gamePlayerRepository.save(new GamePlayer(game1, player2, shipSet2));
			gamePlayerRepository.save(new GamePlayer(game2, player1, shipSet3));
			gamePlayerRepository.save(new GamePlayer(game2, player2, shipSet4));
			gamePlayerRepository.save(new GamePlayer(game3, player2, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game3, player4, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game4, player2, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game4, player1, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game5, player4, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game5, player1, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game6, player3, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game7, player4, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game8, player3, new HashSet<>()));
			gamePlayerRepository.save(new GamePlayer(game8, player4, new HashSet<>()));
		};
	}
}
