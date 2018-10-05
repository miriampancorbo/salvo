package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepositor, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {
			Player player1 = new Player("Jack Bauer");
			Player player2 = new Player("Chloe O'Brian");
			Player player3 = new Player("Kim Bauer");
			Player player4 = new Player("Tony Almeida");
			Player player5 = new Player("");
			playerRepositor.save(player1);
			playerRepositor.save(player2);
			playerRepositor.save(player3);
			playerRepositor.save(player4);
			playerRepositor.save(player5);

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

			//GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
			//gamePlayerRepository.save(new GamePlayer("2018-09-01T15:23:50.411", "Jack Bauer")));
			gamePlayerRepository.save(new GamePlayer(game1, player1));
			gamePlayerRepository.save(new GamePlayer(game1, player2));
			gamePlayerRepository.save(new GamePlayer(game2, player1));
			gamePlayerRepository.save(new GamePlayer(game2, player2));
			gamePlayerRepository.save(new GamePlayer(game3, player2));
			gamePlayerRepository.save(new GamePlayer(game3, player4));
			gamePlayerRepository.save(new GamePlayer(game4, player2));
			gamePlayerRepository.save(new GamePlayer(game4, player1));
			gamePlayerRepository.save(new GamePlayer(game5, player4));
			gamePlayerRepository.save(new GamePlayer(game5, player1));
			gamePlayerRepository.save(new GamePlayer(game6, player3));
			gamePlayerRepository.save(new GamePlayer(game6, player5));
			gamePlayerRepository.save(new GamePlayer(game7, player4));
			gamePlayerRepository.save(new GamePlayer(game7, player5));
			gamePlayerRepository.save(new GamePlayer(game8, player3));
			gamePlayerRepository.save(new GamePlayer(game8, player4));


			//gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-01T15:23:50.411")),new Player ("Chloe O'Brian")));
			//gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-15T15:23:50.411")),new Player ("Chloe O'Brian")));
			//GameRepository.save(new Game(LocalDate.now())); //now() es un metodo static por lo que no hay que instanciar el objeto localdate
			/*Animal (objeto, clase instanciada, ya tiene asignado un espacio en la memoria) cuerpito (variable) = new Animal();
			cuerpito.setu("toy");
			Sh cuerpo;
			cuerpo.setgjhg(),
			cuerpo.setUserName("maria");
			int aver = 0;
			Integer aver2 = 0;*/

		};
	}
}
