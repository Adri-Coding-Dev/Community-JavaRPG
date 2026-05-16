// Fichero: MainApp.java
package dev.hollowforge.app;

import dev.hollowforge.navigation.SceneManager;
import dev.hollowforge.service.GitHubService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

/**
 * Punto de entrada de HollowForge.
 * Configura el escenario principal y los servicios compartidos.
 * Extiende Application, por lo que JavaFX lo lanza automáticamente.
 */
public class MainApp extends Application {

    private static final String TITULO_VENTANA = "HollowForge";

    /**
     * Método principal de JavaFX donde se construye la interfaz.
     *
     * @param stage escenario principal proporcionado por JavaFX
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle(TITULO_VENTANA);

        // Servicios compartidos entre las diferentes vistas
        HostServices hostServices = getHostServices(); // Servicio para abrir URLs desde JavaFX
        GitHubService gitHubService = new GitHubService(); // Servicio para llamadas a la API de GitHub

        // Gestor de escenas que controla la navegación
        SceneManager sceneManager = new SceneManager(stage, hostServices, gitHubService);

        // Muestra el menú principal como primera pantalla
        sceneManager.mostrarMenuPrincipal();
        stage.show(); // Hace visible la ventana
    }

    /**
     * Método main tradicional, lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}