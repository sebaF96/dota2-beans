package com.dotabeans.dota2.controller;

import com.dotabeans.dota2.gsonUtils.GetMatchesTeamData;
import com.dotabeans.dota2.gsonUtils.GetTeamData;
import com.dotabeans.dota2.gsonUtils.GetPlayerData;
import com.dotabeans.dota2.model.MatchTeamData;
import com.dotabeans.dota2.model.Team;
import com.dotabeans.dota2.model.TeamData;
import com.dotabeans.dota2.model.PlayerData;
import com.dotabeans.dota2.repository.TeamRepository;
import com.dotabeans.dota2.utils.UtilFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class TeamController {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping({"", "/"})
    public String showUpdateForm(Model model) throws IOException {
        model.addAttribute("teams", teamRepository.findAll());
        model.addAttribute("proMatches", GetMatchesTeamData.getProMatches());
        return "index";
    }


    @GetMapping("/{id}")
    public String viewTeam(@PathVariable Long id, Model model) throws IOException, NullPointerException {
        try {
            List<MatchTeamData> matches = GetMatchesTeamData.returnTeamMatchesList(id);
            TeamData teamData = GetTeamData.returnTeamData(id);
            List<PlayerData> players = GetPlayerData.getPlayersByTeamId(id);
            teamData.setFollowing(teamRepository.findById(id).isPresent());

            for (MatchTeamData match : matches) {
                match.setActual_team_won(match.getRadiant() == match.getRadiant_win());
                match.setFormattedTime(UtilFunctions.formatDate(match.getStart_time() + match.getDuration()));
            }

            teamData.setWinrate(teamData.getWins() * 100 / (teamData.getWins() + teamData.getLosses()));
            Integer last20victory = (Math.toIntExact(matches.stream().filter(MatchTeamData::getActual_team_won).count()) * 5);


            model.addAttribute("victorys", last20victory);
            model.addAttribute("teamdata", teamData);
            model.addAttribute("matches", matches);
            model.addAttribute("players", players);

            return "view-team";
        } catch (NullPointerException e) {
            return "redirect:";
        }

    }

    @GetMapping("/all")
    public String listAllTeams(Model model) throws IOException {
        List<TeamData> teams = GetTeamData.listAllTeams();

        for (TeamData team : teams) {
            team.setFollowing(teamRepository.findById(team.getTeam_id()).isPresent());
            team.setFormattedDate(UtilFunctions.formatDate(team.getLast_match_time()));
        }

        model.addAttribute("teams", teams);
        return "list-teams";
    }

    @GetMapping("/matches")
    public String getRecentMatches(Model model) throws InterruptedException {

        List<Long> idsMyTeams = teamRepository.findAll().stream().map(Team::getTeam_id).collect(Collectors.toList());
        Set<Long> finishedTeams = Collections.synchronizedSet(new HashSet<>());
        Set<MatchTeamData> matches = Collections.synchronizedSet(new HashSet<>());

        for (Long id : idsMyTeams) {
            Runnable getAndSetMatches = () -> {
                List<MatchTeamData> matchesByTeam = null;
                try {
                    matchesByTeam = GetMatchesTeamData.returnTeamMatchesList(id).stream().limit(10).collect(Collectors.toList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert matchesByTeam != null;
                for (MatchTeamData match : matchesByTeam) {
                    if (matches.contains(match)) continue;
                    match.setActual_team_logo(teamRepository.findById(id).get().getLogo_url());
                    match.setActual_team_name(teamRepository.findById(id).get().getName());
                    match.setFormattedTime(UtilFunctions.formatDate(match.getStart_time() + match.getDuration()));
                    match.setActual_team_won(match.getRadiant_win() == match.getRadiant());
                    match.setActual_team_id(id);
                    matches.add(match);
                }
                finishedTeams.add(id);

            };
            Thread teamThread = new Thread(getAndSetMatches);
            teamThread.start();
        }

        while (finishedTeams.size() != idsMyTeams.size()) {
            Thread.sleep(250);
        }


        List<MatchTeamData> ordered_matches = UtilFunctions.getRecentMatches(matches);
        model.addAttribute("matches", ordered_matches);
        return "recent_matches";
    }

    @GetMapping("/new_team")
    public String showSignUpForm(Model model) {
        Team team = new Team();
        model.addAttribute("team", team);
        return "add-team";
    }

    @GetMapping("/new_team/{id}")
    public String showFormWithId(@PathVariable("id") Long id, Model model) {
        Team team = new Team(id);
        model.addAttribute("team", team);
        return "add-team";
    }

    @PostMapping("add")
    public RedirectView addTeam(@Valid Team team) throws IOException {
        try {
            TeamData teamData = GetTeamData.returnTeamData(team.getTeam_id());
            team.setName(teamData.getName());
            team.setLogo_url(teamData.getLogo_url());

            teamRepository.save(team);
            return new RedirectView("/" + team.getTeam_id());
        } catch (NullPointerException e) {
            return new RedirectView("/");
        }
    }

    @GetMapping("delete/{id}")
    public RedirectView deleteTeam(@PathVariable("id") Long id) {
        Team team = teamRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid team Id:" + id));
        teamRepository.delete(team);

        return new RedirectView("/");
    }
}
