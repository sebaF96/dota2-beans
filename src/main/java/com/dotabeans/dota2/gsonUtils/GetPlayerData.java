package com.dotabeans.dota2.gsonUtils;

import com.dotabeans.dota2.model.PlayerData;
import com.dotabeans.dota2.utils.utilFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetPlayerData {
    public static List<PlayerData> getPlayersByTeamId(Long id) throws IOException {

        String url = "https://api.opendota.com/api/teams/" + id.toString() + "/players";

        StringBuilder sb = utilFunctions.getJsonDataFromUrl(url);
        Gson gson = new Gson();
        List<PlayerData> players = gson.fromJson(sb.toString(), new TypeToken<List<PlayerData>>(){}.getType());

        return players.stream()
                .filter(p -> p.getIs_current_team_member() != null)
                .filter(PlayerData::getIs_current_team_member)
                .collect(Collectors.toList());

    }
}
