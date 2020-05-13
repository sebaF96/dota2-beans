package com.dotabeans.dota2.gsonUtils;

import com.dotabeans.dota2.model.MatchTeamData;
import com.dotabeans.dota2.model.ProMatch;
import com.dotabeans.dota2.utils.UtilFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetMatchesTeamData {

    public static List<MatchTeamData> returnTeamMatchesList(Long id) throws IOException {

        String url = "https://api.opendota.com/api/teams/" + id.toString() + "/matches";

        StringBuilder sb = UtilFunctions.getJsonDataFromUrl(url);


        Gson gson = new Gson();
        List<MatchTeamData> matchesList = gson.fromJson(sb.toString(), new TypeToken<List<MatchTeamData>>() {
        }.getType());

        return matchesList.stream()
                .limit(20)
                .collect(Collectors.toList());
    }


    public static List<ProMatch> getProMatches() throws IOException {
        String url = "https://api.opendota.com/api/proMatches/";

        StringBuilder sb = UtilFunctions.getJsonDataFromUrl(url);


        Gson gson = new Gson();
        List<ProMatch> proMatchesList = gson.fromJson(sb.toString(), new TypeToken<List<ProMatch>>() {
        }.getType());

        return proMatchesList
                .stream()
                .filter(m -> m.getDire_name() != null && m.getRadiant_name() != null)
                .filter(m -> m.getDire_team_id() != null && m.getRadiant_team_id() != null)
                .limit(50)
                .collect(Collectors.toList());
    }

}

