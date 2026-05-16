package dev.hollowforge.app;

import dev.hollowforge.navigation.SceneManager;
import dev.hollowforge.service.GitHubService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

/**
 * Punto de entrada de HollowForge.
 * Configura el escenario principal y los servicios compartidos.
 */
public class MainApp extends Application {

    private static final String TITULO_VENTANA = "HollowForge";

    @Override
    public void start(Stage stage) {
        stage.setTitle(TITULO_VENTANA);

        // Servicios compartidos
        HostServices hostServices = getHostServices();
        GitHubService gitHubService = new GitHubService();

        // Gestor de escenas (controlador principal)
        SceneManager sceneManager = new SceneManager(stage, hostServices, gitHubService);

        // Mostrar menú principal
        sceneManager.mostrarMenuPrincipal();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}