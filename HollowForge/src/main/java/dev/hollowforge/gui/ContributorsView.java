package dev.hollowforge.gui;

import dev.hollowforge.model.Contributor;
import dev.hollowforge.service.GitHubService;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Vista que muestra los contribuidores oficiales.
 */
public class ContributorsView {

    private final VBox root;
    private final VBox listaContainer; // se llena asíncronamente
    private final HostServices hostServices;
    private final GitHubService gitHubService;

    public ContributorsView(HostServices hostServices,
                            GitHubService gitHubService,
                            Runnable onVolver) {
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;

        Label titulo = new Label("Contribuidores Oficiales");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnVolver = new Button("← Volver al menú");
        btnVolver.setOnAction(e -> onVolver.run());

        listaContainer = new VBox(10);
        listaContainer.setAlignment(Pos.CENTER_LEFT);

        // Indicador de carga inicial
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(32, 32);
        listaContainer.getChildren().add(progress);

        // Cargar datos en segundo plano
        cargarContribuidoresAsync();

        root = new VBox(15, titulo, listaContainer, btnVolver);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-padding: 20;");
    }

    public Parent getRoot() {
        return root;
    }

    private void cargarContribuidoresAsync() {
        Task<List<Contributor>> task = new Task<>() {
            @Override
            protected List<Contributor> call() throws Exception {
                return gitHubService.obtenerContribuidores();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> mostrarLista(task.getValue())));
        task.setOnFailed(e -> Platform.runLater(() -> {
            listaContainer.getChildren().clear();
            listaContainer.getChildren().add(
                    new Label("Error al cargar: " + task.getException().getMessage())
            );
        }));

        new Thread(task).start();
    }

    private void mostrarLista(List<Contributor> contribuidores) {
        listaContainer.getChildren().clear();

        if (contribuidores.isEmpty()) {
            listaContainer.getChildren().add(new Label("No se encontraron contribuidores."));
            return;
        }

        for (Contributor c : contribuidores) {
            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setStyle("-fx-cursor: hand;");

            ImageView avatar = new ImageView();
            avatar.setFitWidth(32);
            avatar.setFitHeight(32);
            try {
                avatar.setImage(new Image(c.getAvatarUrl(), true));
            } catch (Exception ignored) {}

            Label nombre = new Label(c.getNombre());

            fila.getChildren().addAll(avatar, nombre);
            fila.setOnMouseClicked(event -> abrirPerfil(c.getHtmlUrl()));

            listaContainer.getChildren().add(fila);
        }
    }

    private void abrirPerfil(String url) {
        if (hostServices != null) {
            hostServices.showDocument(url);
        } else {
            // Fallback mínimo
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Perfil");
            alert.setHeaderText(null);
            alert.setContentText(url);
            alert.showAndWait();
        }
    }
}