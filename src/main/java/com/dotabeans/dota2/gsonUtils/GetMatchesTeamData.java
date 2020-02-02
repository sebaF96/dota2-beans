package com.dotabeans.dota2.gsonUtils;

import com.dotabeans.dota2.model.MatchTeamData;
import com.dotabeans.dota2.utils.utilFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetMatchesTeamData {

    public static List<MatchTeamData> returnTeamMatchesList(Long id) throws IOException {

        String url = "https://api.opendota.com/api/teams/" + id.toString() +"/matches";

        StringBuilder sb = utilFunctions.getJsonDataFromUrl(url);


        Gson gson = new Gson();
        List<MatchTeamData> matchesList = gson.fromJson(sb.toString(), new TypeToken<List<MatchTeamData>>(){}.getType());

        return matchesList.stream()
                .limit(20)
                .collect(Collectors.toList());
    }
}
