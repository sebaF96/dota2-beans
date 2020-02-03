package com.dotabeans.dota2.controller;

import com.dotabeans.dota2.gsonUtils.GetMatchesTeamData;
import com.dotabeans.dota2.gsonUtils.GetTeamData;
import com.dotabeans.dota2.gsonUtils.GetPlayerData;
import com.dotabeans.dota2.model.MatchTeamData;
import com.dotabeans.dota2.model.Team;
import com.dotabeans.dota2.model.TeamData;
import com.dotabeans.dota2.model.PlayerData;
import com.dotabeans.dota2.repository.TeamRepository;
import com.dotabeans.dota2.utils.utilFunctions;
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
    public String showUpdateForm(Model model) {
        model.addAttribute("teams", teamRepository.findAll());
        return "index";
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @GetMapping("/{id}")
    public String viewTeam(@PathVariable Long id, Model model) throws IOException {
        List<MatchTeamData> matches = GetMatchesTeamData.returnTeamMatchesList(id);
        Optional<Team> team = teamRepository.findById(id);
        TeamData teamData = GetTeamData.returnTeamData(id);
        List<PlayerData> players = GetPlayerData.getPlayersByTeamId(id);

        for (MatchTeamData match : matches) {
            match.setRadiant_win(match.getRadiant() == match.getRadiant_win());
            match.setFormattedTime(utilFunctions.formatDate(match.getStart_time()));
        }

        teamData.setWinrate(teamData.getWins() * 100 / (teamData.getWins() + teamData.getLosses()));

        model.addAttribute("team", team.get());
        model.addAttribute("teamdata", teamData);
        model.addAttribute("matches", matches);
        model.addAttribute("players", players);

        return "view-team";

    }

    @GetMapping("/all")
    public String listAllTeams(Model model) throws IOException {
        List<TeamData> teams = GetTeamData.listAllTeams();
        List<Long> followedTeams = teamRepository.findAll().stream().map(Team::getTeam_id).collect(Collectors.toList());

        for (TeamData team : teams) {
            if (followedTeams.contains(team.getTeam_id())) {
                team.setFollowing(true);
            }
            team.setFormattedDate(utilFunctions.formatDate(team.getLast_match_time()));
        }

        model.addAttribute("teams", teams);
        return "list-teams";
    }

    @GetMapping("/matches")
    public String getRecentMatches(Model model) throws IOException {
        List<Long> idsMyTeams = teamRepository.findAll().stream().map(Team::getTeam_id).collect(Collectors.toList());
        ArrayList<MatchTeamData> matches = new ArrayList<>();

        for (Long id : idsMyTeams) {
            ArrayList<MatchTeamData> matchesByTeam = (ArrayList<MatchTeamData>) GetMatchesTeamData.returnTeamMatchesList(id);
            for(int i = 0; i < 10; i++){
                matchesByTeam.get(i).setActualTeamLogo(teamRepository.findById(id).get().getLogo_url());
                matchesByTeam.get(i).setRadiant_win(matchesByTeam.get(i).getRadiant() && matchesByTeam.get(i).getRadiant_win());
                matchesByTeam.get(i).setFormattedTime(utilFunctions.formatDate(matchesByTeam.get(i).getStart_time()));
                matches.add(matchesByTeam.get(i));
            }
        }

        assert false;
        matches.sort(Comparator.comparing(MatchTeamData::getStart_time));
        List<MatchTeamData> matchesFinal = matches.stream().distinct().collect(Collectors.toList());
        Collections.reverse(matchesFinal);
        matches.clear();

        matches = (ArrayList<MatchTeamData>) matchesFinal.stream().limit(50).collect(Collectors.toList());

        model.addAttribute("matches", matches);
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
        TeamData teamData = GetTeamData.returnTeamData(team.getTeam_id());

        team.setName(teamData.getName());
        team.setLogo_url(teamData.getLogo_url());

        teamRepository.save(team);
        return new RedirectView("/" + team.getTeam_id());
    }

    @GetMapping("delete/{id}")
    public RedirectView deleteTeam(@PathVariable("id") Long id) {
        Team team = teamRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid team Id:" + id));
        teamRepository.delete(team);

        return new RedirectView("/");
    }
}
