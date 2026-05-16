package dev.hollowforge.gui;

import dev.hollowforge.util.BrowserUtil;
import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vista del menú principal con los botones de opciones.
 */
public class MainMenuView {

    private static final String TITULO_JUEGO = "HollowForge";
    private static final String TEXTO_NUEVA_PARTIDA = "Nueva Partida";
    private static final String TEXTO_CARGAR_PARTIDA = "Cargar Partida";
    private static final String TEXTO_OPCIONES = "Opciones";
    private static final String TEXTO_REPOSITORIO = "Repositorio";
    private static final String TEXTO_CONTRIBUIDORES = "Contribuidores Oficiales";
    private static final String REPO_URL = "https://github.com/Adri-Coding-Dev/Community-JavaRPG";

    private final VBox root;

    /**
     * @param hostServices      servicio para abrir enlaces
     * @param onContribuidores  acción al pulsar "Contribuidores Oficiales"
     */
    public MainMenuView(HostServices hostServices, Runnable onContribuidores) {
        Label titulo = new Label(TITULO_JUEGO);
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button btnNuevaPartida    = crearBoton(TEXTO_NUEVA_PARTIDA);
        Button btnCargarPartida   = crearBoton(TEXTO_CARGAR_PARTIDA);
        Button btnOpciones        = crearBoton(TEXTO_OPCIONES);
        Button btnRepositorio     = crearBoton(TEXTO_REPOSITORIO);
        Button btnContribuidores  = crearBoton(TEXTO_CONTRIBUIDORES);

        // Acciones
        btnRepositorio.setOnAction(e -> BrowserUtil.abrirUrl(REPO_URL));
        btnContribuidores.setOnAction(e -> onContribuidores.run());

        root = new VBox(15, titulo,
                btnNuevaPartida, btnCargarPartida, btnOpciones,
                btnRepositorio, btnContribuidores);
        root.setAlignment(Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);
        boton.setMaxWidth(200);
        return boton;
    }
}