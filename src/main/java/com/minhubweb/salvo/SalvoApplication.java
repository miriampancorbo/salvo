package com.minhubweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

			Ship ship1 = new Ship ("Cruiser");
			Ship ship2 = new Ship ("Destroyer");
			Ship ship3 = new Ship ("Battleship");
			Ship ship4 = new Ship ("Chulo");
			Ship ship5 = new Ship ("Fuerte");

			Set<Ship> shipSet1 = new HashSet<>();
			Set<Ship> shipSet2 = new HashSet<>();
			Set<Ship> shipSet3 = new HashSet<>();
			Set<Ship> shipSet4 = new HashSet<>();

			shipSet1.add(ship1);
			shipSet1.add(ship2);
			shipSet1.add(ship3);
			shipSet1.add(ship4);
			shipSet1.add(ship5);

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
