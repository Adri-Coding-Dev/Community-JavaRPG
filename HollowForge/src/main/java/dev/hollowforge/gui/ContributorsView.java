package dev.hollowforge.gui;

import dev.hollowforge.gui.components.UIButtonFactory;
import dev.hollowforge.model.Contributor;
import dev.hollowforge.service.GitHubService;
import dev.hollowforge.util.AppConstants;
import dev.hollowforge.util.FontLoader;
import dev.hollowforge.util.LogManager;
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

/**
 * Vista que muestra la lista de contribuidores oficiales del proyecto.
 * Incluye:
 * - Fondo con imagen y desenfoque (blur).
 * - Botón "Volver" con la misma textura que el menú principal.
 * - Tarjetas personalizadas (textura de botón, avatar circular, nombre y contribuciones).
 * - Carga progresiva en lotes para mantener la interfaz fluida.
 * - Ordenación descendente por número de contribuciones.
 * - Scroll automático si hay muchas tarjetas.
 */
public class ContributorsView {

    // ---------- Componentes principales ----------
    private final StackPane root;                       // Raíz de la vista
    private final FlowPane tarjetasContainer;          // Contenedor tipo grid (wrap automático)
    private final HostServices hostServices;            // Para abrir enlaces web
    private final GitHubService gitHubService;          // Servicio de API de GitHub
    private final ProgressIndicator progressIndicator;  // Indicador de carga
    private final Runnable onVolver;                    // Callback para volver al menú
    private Task<Void> cargaTask;                       // Tarea de carga progresiva (para poder cancelarla)

    // ---------- Dimensiones de las tarjetas ----------
    private static final int TARJETA_ANCHO = 480;       // Ancho fijo de cada tarjeta (ajustado para 4 columnas)
    private static final int TARJETA_ALTO = 160;        // Alto fijo
    private static final int AVATAR_TAMANO = 85;        // Tamaño del avatar circular

    // ---------- Configuración de carga progresiva ----------
    private static final int TARJETAS_POR_LOTE = 5;     // Número de tarjetas añadidas en cada iteración
    private static final long PAUSA_ENTRE_LOTES_MS = 50; // Pausa entre lotes (ms) para no saturar la UI

    /**
     * Constructor. Inicializa la interfaz, configura el fondo, el botón volver,
     * el contenedor de tarjetas y lanza la carga de datos (prueba o real).
     *
     * @param hostServices   servicio para abrir URLs en el navegador
     * @param gitHubService  servicio para obtener datos de GitHub
     * @param onVolver       acción a ejecutar al pulsar "Volver"
     */
    public ContributorsView(HostServices hostServices, GitHubService gitHubService, Runnable onVolver) {
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
        this.onVolver = onVolver;

        root = new StackPane();

        // ---------- Fondo con imagen y desenfoque ----------
        ImageView fondo = new ImageView();
        String fondoPath = "/assets/ui/background/MainScreen.png"; // Ruta de la imagen de fondo
        try {
            var fondoResource = getClass().getResource(fondoPath);
            if (fondoResource != null) {
                Image fondoImg = new Image(fondoResource.toExternalForm());
                fondo.setImage(fondoImg);
                fondo.setPreserveRatio(false);               // Estirar para cubrir toda la pantalla
                fondo.fitWidthProperty().bind(root.widthProperty());
                fondo.fitHeightProperty().bind(root.heightProperty());

                // Desenfoque de 15 píxeles (efecto blur)
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

        // ---------- Botón "Volver" (con textura y estilo consistente) ----------
        Button btnVolver = UIButtonFactory.createButtonWithCenteredText(
                300, 50, "← Volver al menú", AppConstants.BUTTON_NEW_GAME_IMAGE, true
        );
        btnVolver.setOnAction(e -> {
            if (cargaTask != null && !cargaTask.isDone()) {
                cargaTask.cancel(true); // Cancelar la carga si está en curso
            }
            onVolver.run();
        });
        StackPane.setAlignment(btnVolver, Pos.TOP_LEFT);
        StackPane.setMargin(btnVolver, new Insets(15, 0, 0, 15));

        // ---------- Contenedor de tarjetas (FlowPane) ----------
        tarjetasContainer = new FlowPane();
        tarjetasContainer.setAlignment(Pos.TOP_LEFT);          // Alinear a la izquierda
        tarjetasContainer.setHgap(25);                         // Espacio horizontal entre tarjetas
        tarjetasContainer.setVgap(25);                         // Espacio vertical
        tarjetasContainer.setPadding(new Insets(140, 40, 40, 100)); // Margen superior e izquierdo para bajar y separar del borde
        tarjetasContainer.setPrefWrapLength(1000);             // Ancho de referencia para el wrap (no crítico)

        // Envolver en ScrollPane para permitir scroll vertical
        ScrollPane scrollPane = new ScrollPane(tarjetasContainer);
        scrollPane.setFitToWidth(true);                        // El FlowPane se ajusta al ancho del scroll
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StackPane.setMargin(scrollPane, new Insets(60, 0, 0, 0)); // Margen superior adicional
        StackPane.setAlignment(scrollPane, Pos.TOP_CENTER);

        // ---------- Indicador de progreso ----------
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(60, 60);
        progressIndicator.setVisible(true);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        // Añadir todos los elementos al StackPane raíz
        root.getChildren().addAll(scrollPane, btnVolver, progressIndicator);

        // ---------- Iniciar carga (elegir modo: prueba o real) ----------
        //iniciarCargaPrueba();   // <--- MODO PRUEBA: 50 tarjetas falsas
        iniciarCarga();      // <--- MODO REAL: desde GitHub API (descomentar para producción)
    }

    // ===================== GENERACIÓN DE DATOS FALSOS (PRUEBA) =====================
    /**
     * Genera una lista de contribuidores falsos para pruebas de interfaz.
     *
     * @param cantidad número de tarjetas falsas a generar
     * @return lista de objetos Contributor con datos aleatorios
     */
    private List<Contributor> generarContribuidoresFalsos(int cantidad) {
        List<Contributor> falsos = new ArrayList<>();
        String[] nombres = {"Steve", "Alex", "Creeper", "Zombie", "Esqueleto", "Enderman", "Aldeano", "Piglin", "Wither", "Dragon"};
        String[] apellidos = {"Miner", "Builder", "Explorer", "Hunter", "Mage", "Warrior", "Archer", "Thief", "Healer", "Bard"};
        for (int i = 1; i <= cantidad; i++) {
            String nombre = nombres[i % nombres.length] + "_" + apellidos[i % apellidos.length] + i;
            String avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"; // Avatar por defecto
            String htmlUrl = "https://github.com/fakeuser";
            int contribuciones = (int) (Math.random() * 500);
            falsos.add(new Contributor(nombre, avatarUrl, htmlUrl, contribuciones));
        }
        return falsos;
    }

    /**
     * Inicia la carga de datos de prueba (50 tarjetas falsas) con ordenación y carga progresiva.
     */
    private void iniciarCargaPrueba() {
        List<Contributor> listaFalsa = generarContribuidoresFalsos(50);
        // Ordenar de mayor a menor contribución
        listaFalsa.sort(Comparator.comparingInt(Contributor::getContribuciones).reversed());
        cargarTarjetasProgresivamente(listaFalsa);
    }

    // ===================== CARGA REAL DESDE GITHUB API =====================
    /**
     * Inicia la carga real desde GitHub API. Obtiene la lista, la ordena y la envía a carga progresiva.
     */
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
                Label errorLabel = new Label("❌ Error al cargar contribuidores: " + e.getMessage());
                errorLabel.setTextFill(Color.RED);
                errorLabel.setFont(FontLoader.getMinecraftFont(14));
                tarjetasContainer.getChildren().add(errorLabel);
            });
        });
        new Thread(fetchTask).start();
    }

    // ===================== CARGA PROGRESIVA (COMÚN) =====================
    /**
     * Añade las tarjetas al contenedor en lotes, desde un hilo secundario, para no bloquear la UI.
     * Cada lote se añade mediante Platform.runLater() y se pausa entre lotes.
     *
     * @param listaOrdenada lista de contribuidores ya ordenada (mayor a menor contribución)
     */
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
        // Limpiar contenedor y mostrar el progreso
        tarjetasContainer.getChildren().clear();
        progressIndicator.setVisible(true);
        new Thread(cargaTask).start();
    }

    // ===================== CONSTRUCCIÓN DE UNA TARJETA =====================
    /**
     * Crea una tarjeta visual para un contribuidor.
     * La tarjeta tiene fondo de textura, avatar circular, nombre y número de contribuciones.
     * Es clickeable en toda su superficie para abrir el perfil de GitHub.
     *
     * @param c el contribuidor (modelo de datos)
     * @return una VBox que representa la tarjeta
     */
    private VBox crearTarjeta(Contributor c) {
        VBox tarjeta = new VBox();
        // Tamaño fijo para todas las tarjetas
        tarjeta.setPrefWidth(TARJETA_ANCHO);
        tarjeta.setPrefHeight(TARJETA_ALTO);
        tarjeta.setMaxWidth(TARJETA_ANCHO);
        tarjeta.setMaxHeight(TARJETA_ALTO);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setCursor(javafx.scene.Cursor.HAND);
        tarjeta.setAlignment(Pos.CENTER_LEFT);

        // Fondo con textura (estira la imagen para cubrir toda la tarjeta)
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

        // Sombra suave para dar relieve
        DropShadow shadow = new DropShadow(8, Color.gray(0.3));
        tarjeta.setEffect(shadow);
        // Evento click en toda la tarjeta
        tarjeta.setOnMouseClicked(e -> abrirPerfil(c.getHtmlUrl()));

        // Contenedor horizontal: avatar a la izquierda, textos a la derecha
        HBox contenido = new HBox(20);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setPadding(new Insets(8));

        // Avatar circular
        ImageView avatarView = new ImageView();
        avatarView.setFitWidth(AVATAR_TAMANO);
        avatarView.setFitHeight(AVATAR_TAMANO);
        avatarView.setPreserveRatio(true);
        // Carga asíncrona de la imagen (no bloquea)
        Image avatarImage = new Image(c.getAvatarUrl(), true);
        avatarView.setImage(avatarImage);
        Circle clip = new Circle(AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0, AVATAR_TAMANO / 2.0);
        avatarView.setClip(clip);

        // Textos (nombre y contribuciones) con fuente personalizada y color marrón oscuro
        VBox textos = new VBox(5);
        textos.setPadding(new Insets(8, 0, 0, 0)); // Padding superior para bajar el texto

        Label nombreLabel = new Label(c.getNombre());
        nombreLabel.setFont(FontLoader.getMinecraftFont(20)); // Tamaño grande
        nombreLabel.setTextFill(Color.web("#3B2A1F"));
        nombreLabel.setEffect(new DropShadow(1, Color.rgb(255, 255, 255, 0.7))); // Sombra blanca para legibilidad

        Label contribLabel = new Label("🏆 " + c.getContribuciones() + " contribuciones");
        contribLabel.setFont(FontLoader.getMinecraftFont(16));
        contribLabel.setTextFill(Color.web("#3B2A1F"));
        contribLabel.setEffect(new DropShadow(1, Color.rgb(255, 255, 255, 0.7)));

        textos.getChildren().addAll(nombreLabel, contribLabel);
        contenido.getChildren().addAll(avatarView, textos);
        tarjeta.getChildren().add(contenido);

        return tarjeta;
    }

    /**
     * Abre la URL del perfil en el navegador por defecto usando HostServices.
     * Si falla, muestra un diálogo con la URL.
     *
     * @param url perfil de GitHub del contribuidor
     */
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

    /**
     * Devuelve el nodo raíz de esta vista para que SceneManager pueda mostrarlo.
     *
     * @return StackPane raíz
     */
    public Parent getRoot() {
        return root;
    }
}