package dev.gui;

import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vista del menú principal de HollowForge.
 */
public class MainView {

    private static final String TITULO_JUEGO = "HollowForge";
    private static final String TEXTO_BOTON_NUEVA_PARTIDA = "Nueva Partida";
    private static final String TEXTO_BOTON_CARGAR_PARTIDA = "Cargar Partida";
    private static final String TEXTO_BOTON_OPCIONES = "Opciones";
    private static final String TEXTO_BOTON_REPOSITORIO = "Repositorio";
    private static final String TEXTO_BOTON_CONTRIBUIDORES = "Contribuidores Oficiales";

    private static final String REPO_URL = "https://github.com/Adri-Coding-Dev/Community-JavaRPG";

    private final VBox contenedorPrincipal;
    private final HostServices hostServices;

    /**
     * @param hostServices      servicio para abrir enlaces en el navegador
     * @param onContribuidores  acción al pulsar "Contribuidores Oficiales"
     */
    public MainView(HostServices hostServices, Runnable onContribuidores) {
        this.hostServices = hostServices;

        Label etiquetaTitulo = new Label(TITULO_JUEGO);
        etiquetaTitulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button botonNuevaPartida = crearBotonMenu(TEXTO_BOTON_NUEVA_PARTIDA);
        Button botonCargarPartida = crearBotonMenu(TEXTO_BOTON_CARGAR_PARTIDA);
        Button botonOpciones = crearBotonMenu(TEXTO_BOTON_OPCIONES);
        Button botonRepositorio = crearBotonMenu(TEXTO_BOTON_REPOSITORIO);
        Button botonContribuidores = crearBotonMenu(TEXTO_BOTON_CONTRIBUIDORES);

        botonRepositorio.setOnAction(e -> abrirRepositorio());
        botonContribuidores.setOnAction(e -> onContribuidores.run());

        contenedorPrincipal = new VBox(15);
        contenedorPrincipal.setAlignment(Pos.CENTER);
        contenedorPrincipal.getChildren().addAll(
                etiquetaTitulo,
                botonNuevaPartida,
                botonCargarPartida,
                botonOpciones,
                botonRepositorio,
                botonContribuidores
        );
    }

    public Parent getRoot() {
        return contenedorPrincipal;
    }

    private Button crearBotonMenu(String texto) {
        Button boton = new Button(texto);
        boton.setMaxWidth(200);
        return boton;
    }

    private void abrirRepositorio() {
        try {
            if (java.awt.Desktop.isDesktopSupported() &&
                    java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(REPO_URL));
            } else {
                abrirConXdgOpen(REPO_URL);
            }
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + REPO_URL);
        }
    }

    private void abrirConXdgOpen(String url) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
                return;
            }
        } catch (Exception ignored) {}
        mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + url);
    }

    private void mostrarAlerta(String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}