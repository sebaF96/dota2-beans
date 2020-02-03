package com.dotabeans.dota2.gsonUtils;

import com.dotabeans.dota2.model.Team;
import com.dotabeans.dota2.model.TeamData;
import com.dotabeans.dota2.utils.utilFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class GetTeamData {

    public static TeamData returnTeamData(Long id) throws IOException {

        String url = "https://api.opendota.com/api/teams/" + id.toString();

        StringBuilder sb = utilFunctions.getJsonDataFromUrl(url);

        Gson gson = new Gson();

        return gson.fromJson(sb.toString(), TeamData.class);

    }

    public static List<TeamData> listAllTeams() throws IOException{
        String url = "https://api.opendota.com/api/teams";

        StringBuilder sb = utilFunctions.getJsonDataFromUrl(url);
        Gson gson = new Gson();

        List<TeamData> teams = gson.fromJson(sb.toString(), new TypeToken<List<TeamData>>(){}.getType());
        teams.sort(Comparator.comparing(TeamData::getWins));
        Collections.reverse(teams);
        return teams.stream().
                filter(t -> t.getLast_match_time() > 1577836800)    // Teams that have played this year (2020)
                .filter(t -> t.getWins() > 49)
                .collect(Collectors.toList());
    }
}
