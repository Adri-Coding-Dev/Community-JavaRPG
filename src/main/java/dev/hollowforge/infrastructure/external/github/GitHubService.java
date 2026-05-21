package dev.hollowforge.infrastructure.external.github;

import dev.hollowforge.infrastructure.external.github.dto.Contributor;
import dev.hollowforge.infrastructure.logging.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitHubService {

    private static final String API_URL_CONTRIBUTORS =
            "https://api.github.com/repos/Adri-Coding-Dev/Community-JavaRPG/contributors";

    private static final long CACHE_TTL_MS = 5 * 60 * 1000;

    private final HttpClient httpClient;
    private List<Contributor> cachedContributors;
    private long lastFetchTime;

    public GitHubService() {
        ExecutorService executor = Executors.newCachedThreadPool();
        this.httpClient = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.cachedContributors = null;
        this.lastFetchTime = 0;
    }

    public List<Contributor> obtenerContribuidores() throws Exception {
        long ahora = System.currentTimeMillis();

        if (cachedContributors != null && (ahora - lastFetchTime) < CACHE_TTL_MS) {
            LogManager.info("Usando Caché de contribuidores");
            return new ArrayList<>(cachedContributors);
        }

        LogManager.info("Caché expirada o vacía. Realizando peticion a GitHub");
        List<Contributor> contributors = fetchFromGitHub();

        cachedContributors = new ArrayList<>(contributors);
        lastFetchTime = ahora;

        return contributors;
    }

    private List<Contributor> fetchFromGitHub() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_CONTRIBUTORS))
                .header("Accept", "application/vnd.github.v3+json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub respondió con código: " + response.statusCode() +
                    ", body: " + response.body());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        List<Contributor> contributors = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String login = obj.getString("login");
            String avatarUrl = obj.getString("avatar_url");
            String htmlUrl = obj.getString("html_url");
            int contributions = obj.getInt("contributions");
            contributors.add(new Contributor(login, avatarUrl, htmlUrl, contributions));
        }
        LogManager.info("Obtenidos " + contributors.size() + " contribuidores desde la API");
        return contributors;
    }

    public void clearCache() {
        cachedContributors = null;
        lastFetchTime = 0;
        LogManager.info("Caché limpiada manualmente");
    }

    public long getCacheRemainingTime() {
        if (cachedContributors == null) return 0;
        long ahora = System.currentTimeMillis();
        long tiempoVivido = ahora - lastFetchTime;
        return Math.max(0, CACHE_TTL_MS - tiempoVivido);
    }
}
