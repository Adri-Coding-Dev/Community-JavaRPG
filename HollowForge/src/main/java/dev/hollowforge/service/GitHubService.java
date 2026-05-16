package dev.hollowforge.service;

import dev.hollowforge.model.Contributor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para interactuar con la API de GitHub.
 */
public class GitHubService {

    private static final String API_URL_CONTRIBUTORS =
            "https://api.github.com/repos/Adri-Coding-Dev/Community-JavaRPG/contributors";

    /**
     * Obtiene la lista completa de contribuidores (se espera una sola página).
     *
     * @return lista de {@link Contributor}
     * @throws Exception si la petición falla o el JSON es inválido
     */
    public List<Contributor> obtenerContribuidores() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_CONTRIBUTORS))
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub respondió con código: " + response.statusCode());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        List<Contributor> contributors = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String login = obj.getString("login");
            String avatarUrl = obj.getString("avatar_url");
            String htmlUrl = obj.getString("html_url");
            int contributions = obj.getInt("contributions");
            contributors.add(new Contributor(login, avatarUrl, htmlUrl, contributions));
        }

        return contributors;
    }
}