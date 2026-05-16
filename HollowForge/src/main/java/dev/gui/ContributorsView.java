package dev.gui;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista que muestra los contribuidores oficiales desde la API de GitHub.
 * Permite volver al menú principal y abrir el perfil de cada colaborador.
 */
public class ContributorsView {

    private static final String GITHUB_API_URL =
            "https://api.github.com/repos/Adri-Coding-Dev/Community-JavaRPG/contributors";

    private final VBox contenedorPrincipal;
    private final HostServices hostServices;
    private final Runnable onVolver;
    private final VBox listaContribuidores; // se rellena asíncronamente

    /**
     * @param hostServices servicio para abrir perfiles en el navegador
     * @param onVolver     acción para regresar al menú principal
     */
    public ContributorsView(HostServices hostServices, Runnable onVolver) {
        this.hostServices = hostServices;
        this.onVolver = onVolver;

        Label titulo = new Label("Contribuidores Oficiales");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button botonVolver = new Button("← Volver al menú");
        botonVolver.setOnAction(e -> onVolver.run());

        listaContribuidores = new VBox(10);
        listaContribuidores.setAlignment(Pos.CENTER_LEFT);

        // Indicador de carga
        ProgressIndicator cargando = new ProgressIndicator();
        cargando.setMaxSize(32, 32);
        listaContribuidores.getChildren().add(cargando);

        // Cargar contribuidores en segundo plano
        cargarContribuidores();

        contenedorPrincipal = new VBox(15, titulo, listaContribuidores, botonVolver);
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);
        contenedorPrincipal.setStyle("-fx-padding: 20;");
    }

    public Parent getRoot() {
        return contenedorPrincipal;
    }

    private void cargarContribuidores() {
        Task<List<Contribuidor>> tarea = new Task<>() {
            @Override
            protected List<Contribuidor> call() throws Exception {
                return obtenerContribuidores();
            }
        };

        tarea.setOnSucceeded(e -> {
            List<Contribuidor> contribuidores = tarea.getValue();
            Platform.runLater(() -> mostrarLista(contribuidores));
        });

        tarea.setOnFailed(e -> {
            Platform.runLater(() -> {
                listaContribuidores.getChildren().clear();
                listaContribuidores.getChildren().add(
                        new Label("Error al cargar contribuidores: " + tarea.getException().getMessage())
                );
            });
        });

        new Thread(tarea).start();
    }

    private List<Contribuidor> obtenerContribuidores() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_API_URL))
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub respondió con código: " + response.statusCode());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        List<Contribuidor> lista = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String login = obj.getString("login");
            String avatarUrl = obj.getString("avatar_url");
            String htmlUrl = obj.getString("html_url"); // enlace al perfil
            lista.add(new Contribuidor(login, avatarUrl, htmlUrl));
        }
        return lista;
    }

    private void mostrarLista(List<Contribuidor> contribuidores) {
        listaContribuidores.getChildren().clear();

        if (contribuidores.isEmpty()) {
            listaContribuidores.getChildren().add(new Label("No se encontraron contribuidores."));
            return;
        }

        for (Contribuidor c : contribuidores) {
            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setStyle("-fx-cursor: hand;"); // indica que es clickable

            ImageView avatarView = new ImageView();
            avatarView.setFitWidth(32);
            avatarView.setFitHeight(32);
            try {
                avatarView.setImage(new Image(c.avatarUrl, true));
            } catch (Exception ignored) {
                // si falla la carga, se queda sin imagen
            }

            Label nombreLabel = new Label(c.nombre);

            fila.getChildren().addAll(avatarView, nombreLabel);

            // Al hacer clic en cualquier parte de la fila, abrir perfil
            fila.setOnMouseClicked(event -> {
                if (hostServices != null) {
                    hostServices.showDocument(c.htmlUrl);
                } else {
                    // fallback
                    mostrarAlerta("Perfil: " + c.htmlUrl);
                }
            });

            listaContribuidores.getChildren().add(fila);
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Perfil");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Clase interna para representar un contribuidor
    private static class Contribuidor {
        final String nombre;
        final String avatarUrl;
        final String htmlUrl;

        Contribuidor(String nombre, String avatarUrl, String htmlUrl) {
            this.nombre = nombre;
            this.avatarUrl = avatarUrl;
            this.htmlUrl = htmlUrl;
        }
    }
}