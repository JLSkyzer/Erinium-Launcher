package fr.eriniumgroup.eriniumlauncher.utils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJsonObjetUrl {
    public static JSONArray getJsonObjet(String urlString){
        try {
            // Spécifiez l'URL du fichier JSON hébergé sur un site
            String jsonUrl = urlString;

            // Créez une URL à partir de l'URL spécifiée
            URL url = new URL(jsonUrl);

            // Ouvrez une connexion HTTP vers l'URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configurez la requête HTTP
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Vérifiez si la réponse HTTP est réussie
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Échec de la requête HTTP : " + conn.getResponseCode());
            }

            // Créez un BufferedReader pour lire la réponse HTTP
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            // Lisez la réponse ligne par ligne
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }

            // Fermez la connexion HTTP et le BufferedReader
            conn.disconnect();
            br.close();

            // Convertissez le contenu JSON en un objet JSONArray
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            return jsonArray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
