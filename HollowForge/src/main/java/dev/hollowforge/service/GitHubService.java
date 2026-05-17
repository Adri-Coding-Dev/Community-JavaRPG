package dev.hollowforge.service;

import dev.hollowforge.model.Contributor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servicio para interactuar con la API de GitHub.
 * Obtiene la lista de contribuidores del repositorio oficial.
 * Implementa un caché simple con tiempo de vida (TTL) para reducir
 * peticiones repetitivas y mejorar el rendimiento.
 *
 * <p>El caché se invalida automáticamente después de 5 minutos.
 * También se puede limpiar manualmente con {@link #clearCache()}.</p>
 */
public class GitHubService {

    private static final String API_URL_CONTRIBUTORS =
            "https://api.github.com/repos/Adri-Coding-Dev/Community-JavaRPG/contributors";

    // Tiempo de vida del caché en milisegundos (5 minutos)
    private static final long CACHE_TTL_MS = 5 * 60 * 1000;

    private final HttpClient httpClient;
    private List<Contributor> cachedContributors;
    private long lastFetchTime;

    /**
     * Constructor. Inicializa el cliente HTTP con un pool de hilos
     * y un timeout de conexión de 10 segundos.
     */
    public GitHubService() {
        ExecutorService executor = Executors.newCachedThreadPool();
        this.httpClient = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.cachedContributors = null;
        this.lastFetchTime = 0;
    }

    /**
     * Obtiene la lista de contribuidores del repositorio.
     * Si los datos están en caché y no han expirado, devuelve la copia
     * almacenada. En caso contrario, realiza una petición HTTP a la API
     * de GitHub, actualiza el caché y devuelve la lista.
     *
     * @return lista de contribuidores (nunca null, puede estar vacía)
     * @throws Exception si ocurre un error de red, timeout o la API responde con error
     */
    public List<Contributor> obtenerContribuidores() throws Exception {
        long ahora = System.currentTimeMillis();

        // Si hay caché y no ha expirado, devolver copia defensiva
        if (cachedContributors != null && (ahora - lastFetchTime) < CACHE_TTL_MS) {
            System.out.println("[GitHubService] Usando caché de contribuidores");
            return new ArrayList<>(cachedContributors); // copia inmutable para el exterior
        }

        System.out.println("[GitHubService] Caché expirada o vacía. Realizando petición a GitHub...");
        List<Contributor> contributors = fetchFromGitHub();

        // Actualizar caché
        cachedContributors = new ArrayList<>(contributors);
        lastFetchTime = ahora;

        return contributors;
    }

    /**
     * Realiza la petición HTTP a la API de GitHub y parsea la respuesta JSON.
     *
     * @return lista de contribuidores (vacía si el array está vacío)
     * @throws Exception si la petición falla o el código de respuesta no es 200
     */
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

        System.out.println("[GitHubService] Obtenidos " + contributors.size() + " contribuidores desde la API.");
        return contributors;
    }

    /**
     * Limpia manualmente el caché. Útil si se quiere forzar una recarga
     * en la próxima llamada a {@link #obtenerContribuidores()}.
     */
    public void clearCache() {
        cachedContributors = null;
        lastFetchTime = 0;
        System.out.println("[GitHubService] Caché limpiada manualmente.");
    }

    /**
     * Devuelve el tiempo restante (en milisegundos) hasta que expire el caché.
     * Si no hay caché o ya expiró, devuelve 0.
     *
     * @return tiempo restante en ms, o 0 si no hay caché válida.
     */
    public long getCacheRemainingTime() {
        if (cachedContributors == null) return 0;
        long ahora = System.currentTimeMillis();
        long tiempoVivido = ahora - lastFetchTime;
        return Math.max(0, CACHE_TTL_MS - tiempoVivido);
    }
}