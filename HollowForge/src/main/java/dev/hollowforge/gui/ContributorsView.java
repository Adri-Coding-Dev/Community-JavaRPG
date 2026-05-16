package dev.hollowforge.gui;

import dev.hollowforge.model.Contributor;
import dev.hollowforge.service.GitHubService;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class ContributorsView {

    private final VBox root;
    private final VBox tarjetasContainer;
    private final HostServices hostServices;
    private final GitHubService gitHubService;
    private final ProgressIndicator progressIndicator;
    private final Runnable onVolver;

    private static final int TARJETA_ANCHO_MAX = 350;
    private static final int AVATAR_TAMANO = 64;

    public ContributorsView(HostServices hostServices, GitHubService gitHubService, Runnable onVolver) {
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
        this.onVolver = onVolver;

        Label titulo = new Label("🌟 Contribuidores Oficiales");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.WHITE);

        Button btnVolver = new Button("← Volver al menú");
        btnVolver.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        btnVolver.setOnAction(e -> onVolver.run());

        tarjetasContainer = new VBox(15);
        tarjetasContainer.setAlignment(Pos.TOP_CENTER);
        tarjetasContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tarjetasContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-background-color: #2b2b2b;");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(40, 40);
        progressIndicator.setVisible(true); // Se muestra desde el principio

        VBox topBar = new VBox(10, titulo, progressIndicator);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10));

        root = new VBox(10, topBar, scrollPane, btnVolver);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e1e;");

        iniciarCarga();
    }

    public Parent getRoot() {
        return root;
    }

    private void iniciarCarga() {
        Task<List<Contributor>> fetchTask = new Task<>() {
            @Override
            protected List<Contributor> call() throws Exception {
                System.out.println("[DEBUG] Iniciando petición a GitHub...");
                List<Contributor> lista = gitHubService.obtenerContribuidores();
                System.out.println("[DEBUG] Obtenidos " + lista.size() + " contribuidores.");
                return lista;
            }
        };

        fetchTask.setOnSucceeded(event -> {
            List<Contributor> contributors = fetchTask.getValue();
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                if (contributors == null || contributors.isEmpty()) {
                    tarjetasContainer.getChildren().add(new Label("No se encontraron contribuidores."));
                } else {
                    // Añadir todas las tarjetas de una vez (sin progresividad para simplificar)
                    for (Contributor c : contributors) {
                        tarjetasContainer.getChildren().add(crearTarjeta(c));
                    }
                }
            });
        });

        fetchTask.setOnFailed(event -> {
            Throwable e = fetchTask.getException();
            System.err.println("[ERROR] Falló la carga: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                Label errorLabel = new Label("❌ Error al cargar contribuidores: " + e.getMessage());
                errorLabel.setTextFill(Color.RED);
                tarjetasContainer.getChildren().add(errorLabel);
            });
        });

        new Thread(fetchTask).start();
    }

    private VBox crearTarjeta(Contributor c) {
        VBox tarjeta = new VBox(10);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setMaxWidth(TARJETA_ANCHO_MAX);
        tarjeta.setStyle(
                "-fx-background-color: #333; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-color: #555; " +
                        "-fx-border-width: 1; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0.2, 2, 2);"
        );

        // Avatar
        ImageView avatarView = new ImageView();
        avatarView.setFitWidth(AVATAR_TAMANO);
        avatarView.setFitHeight(AVATAR_TAMANO);
        avatarView.setPreserveRatio(true);
        // Cargar la imagen en segundo plano
        Image avatarImage = new Image(c.getAvatarUrl(), true);
        avatarView.setImage(avatarImage);
        // Clip circular
        Circle clip = new Circle(AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0);
        avatarView.setClip(clip);

        Label nombreLabel = new Label(c.getNombre());
        nombreLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nombreLabel.setTextFill(Color.WHITE);

        Label contribLabel = new Label("🏆 " + c.getContribuciones() + " contribuciones");
        contribLabel.setFont(Font.font("System", 14));
        contribLabel.setTextFill(Color.LIGHTGRAY);

        Button btnPerfil = new Button("🔗 Ver perfil");
        btnPerfil.setStyle("-fx-background-color: #0a66c2; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        btnPerfil.setOnAction(e -> abrirPerfil(c.getHtmlUrl()));

        tarjeta.getChildren().addAll(avatarView, nombreLabel, contribLabel, btnPerfil);

        // Hover
        tarjeta.setOnMouseEntered(e ->
                tarjeta.setStyle(tarjeta.getStyle() + "-fx-background-color: #444;"));
        tarjeta.setOnMouseExited(e ->
                tarjeta.setStyle(tarjeta.getStyle().replace("-fx-background-color: #444;", "-fx-background-color: #333;")));

        return tarjeta;
    }

    private void abrirPerfil(String url) {
        if (hostServices != null) {
            hostServices.showDocument(url);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Perfil");
            alert.setHeaderText(null);
            alert.setContentText(url);
            alert.showAndWait();
        }
    }
}