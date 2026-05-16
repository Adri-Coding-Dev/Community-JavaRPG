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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * Vista que muestra los contribuidores oficiales con tarjetas atractivas
 * y carga progresiva para evitar bloqueos en la interfaz.
 */
public class ContributorsView {

    private final VBox root;
    private final VBox tarjetasContainer; // Se llenará progresivamente
    private final HostServices hostServices;
    private final GitHubService gitHubService;
    private final ProgressIndicator progressIndicator;

    private static final int TARJETA_ANCHO_MAX = 350;
    private static final int AVATAR_TAMANO = 64;

    public ContributorsView(HostServices hostServices,
                            GitHubService gitHubService,
                            Runnable onVolver) {
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;

        // Título
        Label titulo = new Label("🌟 Contribuidores Oficiales");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.WHITE);

        // Botón volver
        Button btnVolver = new Button("← Volver al menú");
        btnVolver.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        btnVolver.setOnAction(e -> onVolver.run());

        // Contenedor de tarjetas con ScrollPane
        tarjetasContainer = new VBox(15);
        tarjetasContainer.setAlignment(Pos.TOP_CENTER);
        tarjetasContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tarjetasContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-background-color: #2b2b2b;");

        // Indicador de progreso
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(40, 40);
        progressIndicator.setVisible(false);

        // Organización principal
        VBox topBar = new VBox(10, titulo, progressIndicator);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10));

        root = new VBox(10, topBar, scrollPane, btnVolver);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Iniciar carga progresiva
        iniciarCargaProgresiva();
    }

    public Parent getRoot() {
        return root;
    }

    /**
     * Lanza un hilo que obtiene la lista completa y luego,
     * mediante Platform.runLater con pequeños retardos,
     * va añadiendo tarjetas una a una para simular carga progresiva fluida.
     */
    private void iniciarCargaProgresiva() {
        progressIndicator.setVisible(true);

        Task<List<Contributor>> fetchTask = new Task<>() {
            @Override
            protected List<Contributor> call() throws Exception {
                return gitHubService.obtenerContribuidores();
            }
        };

        fetchTask.setOnSucceeded(event -> {
            List<Contributor> contributors = fetchTask.getValue();
            Platform.runLater(() -> progressIndicator.setVisible(false));
            mostrarProgresivamente(contributors, 0);
        });

        fetchTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                tarjetasContainer.getChildren().add(
                        new Label("❌ Error al cargar: " + fetchTask.getException().getMessage())
                );
            });
        });

        new Thread(fetchTask).start();
    }

    /**
     * Muestra los contribuidores uno a uno con un retardo entre cada uno.
     *
     * @param contributors lista completa
     * @param indice       posición actual a mostrar
     */
    private void mostrarProgresivamente(List<Contributor> contributors, int indice) {
        if (indice >= contributors.size()) {
            return; // Fin de la lista
        }

        Contributor c = contributors.get(indice);
        tarjetasContainer.getChildren().add(crearTarjeta(c));

        // Programar el siguiente con un pequeño retardo (80 ms)
        new Thread(() -> {
            try {
                Thread.sleep(80);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> mostrarProgresivamente(contributors, indice + 1));
        }).start();
    }

    /**
     * Construye una tarjeta visual para un contribuidor.
     */
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
        try {
            Image img = new Image(c.getAvatarUrl(), true);
            avatarView.setImage(img);
        } catch (Exception e) {
            // Si falla, se queda el placeholder vacío
        }
        // Hacer el avatar circular con clip
        avatarView.setClip(new javafx.scene.shape.Circle(
                AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0));

        // Nombre
        Label nombreLabel = new Label(c.getNombre());
        nombreLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nombreLabel.setTextFill(Color.WHITE);

        // Contribuciones
        Label contribLabel = new Label("🏆 " + c.getContribuciones() + " contribuciones");
        contribLabel.setFont(Font.font("System", 14));
        contribLabel.setTextFill(Color.LIGHTGRAY);

        // Botón para ver perfil
        Button btnPerfil = new Button("🔗 Ver perfil");
        btnPerfil.setStyle(
                "-fx-background-color: #0a66c2; -fx-text-fill: white; " +
                        "-fx-background-radius: 5; -fx-cursor: hand;");
        btnPerfil.setOnAction(e -> abrirPerfil(c.getHtmlUrl()));

        tarjeta.getChildren().addAll(avatarView, nombreLabel, contribLabel, btnPerfil);

        // Efecto hover para toda la tarjeta
        tarjeta.setOnMouseEntered(e ->
                tarjeta.setStyle(tarjeta.getStyle() + "-fx-background-color: #444;"));
        tarjeta.setOnMouseExited(e ->
                tarjeta.setStyle(tarjeta.getStyle().replace("-fx-background-color: #444;",
                        "-fx-background-color: #333;")));

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