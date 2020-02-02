package com.dotabeans.dota2.controller.rest;

import com.dotabeans.dota2.model.Player;
import com.dotabeans.dota2.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/players")
@RestController
public class PlayerRestController {

    private final PlayerRepository playerRepository;

    public PlayerRestController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> listPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player findById(@PathVariable Long id){
        Optional<Player> player = playerRepository.findById(id);
        return player.orElse(null);
    }

    @GetMapping("/search/{text}")
    public List<Player> searchByName(@PathVariable String text){
        return playerRepository.findByNameContaining(text);
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player){
        playerRepository.save(player);
    }

    @PutMapping
    public void updatePlayer(@RequestBody Player player){
        Optional<Player> actualPlayer = playerRepository.findById(player.getId());
        if(actualPlayer.isPresent()){
            playerRepository.save(player);
        }
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        Optional<Player> player = playerRepository.findById(id);
        player.ifPresent(playerRepository::delete);
    }


}
