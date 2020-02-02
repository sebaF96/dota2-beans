package com.dotabeans.dota2.repository;

import com.dotabeans.dota2.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByNameContaining(String name);
}
