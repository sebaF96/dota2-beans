package com.dotabeans.dota2.utils;

import com.dotabeans.dota2.model.MatchTeamData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UtilFunctions {

    public static StringBuilder getJsonDataFromUrl(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:72.0) Gecko/20100101 Firefox/72.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        return sb;
    }

    public static String formatDate(Long timestamp) {

        Long time_now = (System.currentTimeMillis() / 1000);
        long time_ago = time_now - timestamp - 960;
        // Subtracting 960 seconds that is the average time for a draft,
        // since (start_time - duration time) just counts the "juego neto" duration.

        if (time_ago >= 86400) {
            int days = (int) (time_ago / 86400);
            return "" + days + "d ago";
        }

        if (time_ago >= 3600) {
            int hours = (int) (time_ago / 3600);
            return "" + hours + "h ago";
        }

        if (time_ago >= 60) {
            int minutes = (int) (time_ago / 60);
            return "" + minutes + "min ago";
        }

        return "" + time_ago + "sec ago";


    }


    public static List<MatchTeamData> getRecentMatches(Set<MatchTeamData> unordered_matches) {

        ArrayList<MatchTeamData> ordered_matches = new ArrayList<>(unordered_matches);

        ordered_matches.sort(Comparator.comparing(MatchTeamData::getStart_time));
        Collections.reverse(ordered_matches);

        return ordered_matches.stream().limit(50).collect(Collectors.toList());

    }
}
