package fr.eriniumgroup.eriniumlauncher.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJsonObjetUrl {

    public static StringBuilder getNews() throws IOException {
        URL url = new URL("https://erinium.000webhostapp.com/news.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Échec de la requête HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonContent = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            jsonContent.append(line);
        }

        conn.disconnect();
        br.close();

        return jsonContent;
    }
}
