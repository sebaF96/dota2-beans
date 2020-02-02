package com.dotabeans.dota2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class utilFunctions {

    public static StringBuilder getJsonDataFromUrl(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:72.0) Gecko/20100101 Firefox/72.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuilder sb = new StringBuilder();

        while((inputLine = in.readLine()) != null){
            sb.append(inputLine);
        }
        in.close();

        return sb;
    }

    public static String formatDate(Long timestamp){
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.ofEpochDay(timestamp / 86400);
        // ofEpochDay() function waits for DAYS since 1-1-1970 and timestamp is given in
        // SECONDS so we divide by 86400 (seconds in 1 day)

        return FORMATTER.format(localDate);

    }
}
