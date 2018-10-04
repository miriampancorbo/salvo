package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepositor, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {
			playerRepositor.save(new Player("Jack Bauer"));
			playerRepositor.save(new Player("Chloe O'Brian"));
			playerRepositor.save(new Player("Kim Bauer"));
			playerRepositor.save(new Player("Tony Almeida"));
			//GameRepository.save(new Game(LocalDate.now())); //now() es un metodo static por lo que no hay que instanciar el objeto localdate
			gameRepository.save(new Game(LocalDateTime.parse("2018-09-15T15:23:50.411")));
			gameRepository.save(new Game());
			gameRepository.save(new Game());
			gameRepository.save(new Game());
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-01T15:23:50.411")),new Player ("Jack Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-01T15:23:50.411")),new Player ("Chloe O'Brian")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-02T15:23:50.411")),new Player ("Jack Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-02T15:23:50.411")),new Player ("Chloe O'Brian")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-03T15:23:50.411")),new Player ("Chloe O'Brian")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-03T15:23:50.411")),new Player ("Tony Almeida")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-04T15:23:50.411")),new Player ("Chloe O'Brian")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-04T15:23:50.411")),new Player ("Jack Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-05T15:23:50.411")),new Player ("Tony Almeida")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-05T15:23:50.411")),new Player ("Jack Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-06T15:23:50.411")),new Player ("Kim Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-06T15:23:50.411")),new Player ("")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-07T15:23:50.411")),new Player ("Tony Almeida")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-07T15:23:50.411")),new Player ("")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-08T15:23:50.411")),new Player ("Kim Bauer")));
			gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-08T15:23:50.411")),new Player ("Tony Almeida")));
			//gamePlayerRepository.save(new GamePlayer(new Game(LocalDateTime.parse("2018-09-15T15:23:50.411")),new Player ("Chloe O'Brian")));
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
