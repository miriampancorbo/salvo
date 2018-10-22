package com.minhubweb.salvo.repositories;

import java.util.List;

import com.minhubweb.salvo.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByDate(String date);
    List<Game> findById(long id);
}
