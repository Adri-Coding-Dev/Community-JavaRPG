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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitHubService {

    private static final String API_URL_CONTRIBUTORS =
            "https://api.github.com/repos/Adri-Coding-Dev/Community-JavaRPG/contributors";

    private final HttpClient httpClient;

    public GitHubService() {
        // Usamos un pool de hilos para las peticiones asíncronas (aunque aquí se usa send síncrono)
        ExecutorService executor = Executors.newCachedThreadPool();
        this.httpClient = HttpClient.newBuilder().executor(executor).build();
    }

    public List<Contributor> obtenerContribuidores() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_CONTRIBUTORS))
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

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