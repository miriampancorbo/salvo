package com.minhubweb.salvo.repositories;

import java.util.List;

import com.minhubweb.salvo.models.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

    @RepositoryRestResource
    public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
        List<GamePlayer>findByDate(String date);

    }

