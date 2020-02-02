package com.dotabeans.dota2.controller;


import com.dotabeans.dota2.model.Player;
import com.dotabeans.dota2.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import javax.validation.Valid;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping("/signup")
    public String showSingUpForm(Model model){
        Player player = new Player();
        model.addAttribute("player", player);
        return "player/add-player";
    }

    @GetMapping("/list")
    public String showPlayers(Model model){
        model.addAttribute("players", playerRepository.findAll());
        return "player/index";
    }

    @PostMapping("/add")
    public RedirectView addPlayer(@Valid Player player) {

        playerRepository.save(player);
        return new RedirectView("/players/list");
    }


    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player Id:" + id));
        model.addAttribute("player", player);
        return "player/update-player";
    }

    @PostMapping("/update")
    public RedirectView updatePlayer(@Valid Player player) {

        playerRepository.save(player);
        return new RedirectView("/players/list");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deletePlayer(@PathVariable("id") Long id) {
        Player player = playerRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid player Id:" + id));
        playerRepository.delete(player);
        // I should change all the logic in this function using Optional<Player>
        return new RedirectView("/players/list");
    }
}

