package dev.hollowforge.presentation.ui;

import dev.hollowforge.engine.config.AppConstants;
import dev.hollowforge.presentation.ui.components.FontLoader;
import dev.hollowforge.presentation.ui.components.UIButtonFactory;
import dev.hollowforge.infrastructure.external.github.GitHubService;
import dev.hollowforge.infrastructure.external.github.dto.Contributor;
import dev.hollowforge.infrastructure.logging.LogManager;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContributorsView {

    private final StackPane root;
    private final FlowPane tarjetasContainer;
    private final HostServices hostServices;
    private final GitHubService gitHubService;
    private final ProgressIndicator progressIndicator;
    private final Runnable onVolver;
    private Task<Void> cargaTask;

    private static final int TARJETA_ANCHO = 480;
    private static final int TARJETA_ALTO = 160;
    private static final int AVATAR_TAMANO = 85;

    private static final int TARJETAS_POR_LOTE = 5;
    private static final long PAUSA_ENTRE_LOTES_MS = 50;

    public ContributorsView(HostServices hostServices, GitHubService gitHubService, Runnable onVolver) {
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
        this.onVolver = onVolver;

        root = new StackPane();

        ImageView fondo = new ImageView();
        String fondoPath = "/assets/ui/background/MainScreen.png";
        try {
            var fondoResource = getClass().getResource(fondoPath);
            if (fondoResource != null) {
                Image fondoImg = new Image(fondoResource.toExternalForm());
                fondo.setImage(fondoImg);
                fondo.setPreserveRatio(false);
                fondo.fitWidthProperty().bind(root.widthProperty());
                fondo.fitHeightProperty().bind(root.heightProperty());

                BoxBlur blur = new BoxBlur(15, 15, 3);
                fondo.setEffect(blur);
            } else {
                System.err.println("No se encontró la imagen de fondo: " + fondoPath);
                root.setStyle("-fx-background-color: #1e1e1e;");
            }
        } catch (Exception e) {
            LogManager.warning("Error al cargar el fondo: " + e.getMessage());
            root.setStyle("-fx-background-color: #1e1e1e;");
        }
        if (fondo.getImage() != null) {
            root.getChildren().add(fondo);
        }

        Button btnVolver = UIButtonFactory.createButtonWithCenteredText(
                300, 50, "← Volver al menú", AppConstants.BUTTON_NEW_GAME_IMAGE, true
        );
        btnVolver.setOnAction(e -> {
            if (cargaTask != null && !cargaTask.isDone()) {
                cargaTask.cancel(true);
            }
            onVolver.run();
        });
        StackPane.setAlignment(btnVolver, Pos.TOP_LEFT);
        StackPane.setMargin(btnVolver, new Insets(15, 0, 0, 15));

        tarjetasContainer = new FlowPane();
        tarjetasContainer.setAlignment(Pos.TOP_LEFT);
        tarjetasContainer.setHgap(25);
        tarjetasContainer.setVgap(25);
        tarjetasContainer.setPadding(new Insets(140, 40, 40, 100));
        tarjetasContainer.setPrefWrapLength(1000);

        ScrollPane scrollPane = new ScrollPane(tarjetasContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StackPane.setMargin(scrollPane, new Insets(60, 0, 0, 0));
        StackPane.setAlignment(scrollPane, Pos.TOP_CENTER);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(60, 60);
        progressIndicator.setVisible(true);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        root.getChildren().addAll(scrollPane, btnVolver, progressIndicator);

        iniciarCarga();
    }

    private List<Contributor> generarContribuidoresFalsos(int cantidad) {
        List<Contributor> falsos = new ArrayList<>();
        String[] nombres = {"Steve", "Alex", "Creeper", "Zombie", "Esqueleto", "Enderman", "Aldeano", "Piglin", "Wither", "Dragon"};
        String[] apellidos = {"Miner", "Builder", "Explorer", "Hunter", "Mage", "Warrior", "Archer", "Thief", "Healer", "Bard"};
        for (int i = 1; i <= cantidad; i++) {
            String nombre = nombres[i % nombres.length] + "_" + apellidos[i % apellidos.length] + i;
            String avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4";
            String htmlUrl = "https://github.com/fakeuser";
            int contribuciones = (int) (Math.random() * 500);
            falsos.add(new Contributor(nombre, avatarUrl, htmlUrl, contribuciones));
        }
        return falsos;
    }

    private void iniciarCargaPrueba() {
        List<Contributor> listaFalsa = generarContribuidoresFalsos(50);
        listaFalsa.sort(Comparator.comparingInt(Contributor::getContribuciones).reversed());
        cargarTarjetasProgresivamente(listaFalsa);
    }

    private void iniciarCarga() {
        Task<List<Contributor>> fetchTask = new Task<>() {
            @Override
            protected List<Contributor> call() throws Exception {
                return gitHubService.obtenerContribuidores();
            }
        };
        fetchTask.setOnSucceeded(event -> {
            List<Contributor> contributors = fetchTask.getValue();
            if (contributors != null && !contributors.isEmpty()) {
                contributors.sort(Comparator.comparingInt(Contributor::getContribuciones).reversed());
                cargarTarjetasProgresivamente(contributors);
            } else {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    Label emptyLabel = new Label("No se encontraron contribuidores.");
                    emptyLabel.setTextFill(Color.WHITE);
                    emptyLabel.setFont(FontLoader.getMinecraftFont(16));
                    tarjetasContainer.getChildren().add(emptyLabel);
                });
            }
        });
        fetchTask.setOnFailed(event -> {
            Throwable e = fetchTask.getException();
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                Label errorLabel = new Label("Error al cargar contribuidores: " + e.getMessage());
                errorLabel.setTextFill(Color.RED);
                errorLabel.setFont(FontLoader.getMinecraftFont(14));
                tarjetasContainer.getChildren().add(errorLabel);
            });
        });
        new Thread(fetchTask).start();
    }

    private void cargarTarjetasProgresivamente(List<Contributor> listaOrdenada) {
        if (cargaTask != null && !cargaTask.isDone()) {
            cargaTask.cancel(true);
        }
        cargaTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int total = listaOrdenada.size();
                int indice = 0;
                while (indice < total) {
                    if (isCancelled()) break;
                    int loteFin = Math.min(indice + TARJETAS_POR_LOTE, total);
                    List<Contributor> lote = listaOrdenada.subList(indice, loteFin);
                    Platform.runLater(() -> {
                        for (Contributor c : lote) {
                            tarjetasContainer.getChildren().add(crearTarjeta(c));
                        }
                    });
                    indice = loteFin;
                    if (indice < total) {
                        Thread.sleep(PAUSA_ENTRE_LOTES_MS);
                    }
                }
                Platform.runLater(() -> progressIndicator.setVisible(false));
                return null;
            }
        };
        tarjetasContainer.getChildren().clear();
        progressIndicator.setVisible(true);
        new Thread(cargaTask).start();
    }

    private VBox crearTarjeta(Contributor c) {
        VBox tarjeta = new VBox();
        tarjeta.setPrefWidth(TARJETA_ANCHO);
        tarjeta.setPrefHeight(TARJETA_ALTO);
        tarjeta.setMaxWidth(TARJETA_ANCHO);
        tarjeta.setMaxHeight(TARJETA_ALTO);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setCursor(javafx.scene.Cursor.HAND);
        tarjeta.setAlignment(Pos.CENTER_LEFT);

        try {
            String imagePath = AppConstants.BUTTON_NEW_GAME_IMAGE;
            var resource = getClass().getResource(imagePath);
            if (resource != null) {
                Image textura = new Image(resource.toExternalForm());
                BackgroundSize size = new BackgroundSize(1.0, 1.0, true, true, false, false);
                BackgroundImage bgImage = new BackgroundImage(
                        textura,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        size
                );
                tarjeta.setBackground(new Background(bgImage));
            } else {
                tarjeta.setStyle("-fx-background-color: #333; -fx-background-radius: 12;");
            }
        } catch (Exception e) {
            tarjeta.setStyle("-fx-background-color: #333; -fx-background-radius: 12;");
        }

        DropShadow shadow = new DropShadow(8, Color.gray(0.3));
        tarjeta.setEffect(shadow);
        tarjeta.setOnMouseClicked(e -> abrirPerfil(c.getHtmlUrl()));

        HBox contenido = new HBox(20);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setPadding(new Insets(8));

        ImageView avatarView = new ImageView();
        avatarView.setFitWidth(AVATAR_TAMANO);
        avatarView.setFitHeight(AVATAR_TAMANO);
        avatarView.setPreserveRatio(true);
        Image avatarImage = new Image(c.getAvatarUrl(), true);
        avatarView.setImage(avatarImage);
        Circle clip = new Circle(AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0);
        avatarView.setClip(clip);

        VBox textos = new VBox(5);
        textos.setPadding(new Insets(8, 0, 0, 0));

        Label nombreLabel = new Label(c.getNombre());
        nombreLabel.setFont(FontLoader.getMinecraftFont(20));
        nombreLabel.setTextFill(Color.web("#3B2A1F"));
        nombreLabel.setEffect(new DropShadow(1, Color.rgb(255, 255, 255, 0.7)));

        Label contribLabel = new Label(c.getContribuciones() + " contribuciones");
        contribLabel.setFont(FontLoader.getMinecraftFont(16));
        contribLabel.setTextFill(Color.web("#3B2A1F"));
        contribLabel.setEffect(new DropShadow(1, Color.rgb(255, 255, 255, 0.7)));

        textos.getChildren().addAll(nombreLabel, contribLabel);
        contenido.getChildren().addAll(avatarView, textos);
        tarjeta.getChildren().add(contenido);

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

    public Parent getRoot() {
        return root;
    }
}
