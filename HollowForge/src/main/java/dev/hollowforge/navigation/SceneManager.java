package dev.hollowforge.navigation;

import dev.hollowforge.service.GitHubService;
import dev.hollowforge.gui.ContributorsView;
import dev.hollowforge.gui.MainMenuView;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Administra la navegación entre las diferentes vistas de la aplicación.
 * Mantiene referencias a los servicios compartidos y al Stage principal.
 */
public class SceneManager {

    private final Stage stage;
    private final HostServices hostServices;
    private final GitHubService gitHubService;

    // Dimensiones base (pueden ajustarse por vista)
    private static final int ANCHO_BASE = 400;
    private static final int ALTO_BASE = 350;

    public SceneManager(Stage stage, HostServices hostServices, GitHubService gitHubService) {
        this.stage = stage;
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
    }

    /**
     * Carga y muestra la vista del menú principal.
     */
    public void mostrarMenuPrincipal() {
        MainMenuView mainMenuView = new MainMenuView(
                hostServices,
                this::mostrarContribuidores   // callback para navegar
        );
        cambiarEscena(mainMenuView.getRoot(), ANCHO_BASE, ALTO_BASE);
    }

    /**
     * Carga y muestra la vista de contribuidores.
     */
    public void mostrarContribuidores() {
        ContributorsView contributorsView = new ContributorsView(
                hostServices,
                gitHubService,
                this::mostrarMenuPrincipal    // callback para volver
        );
        cambiarEscena(contributorsView.getRoot(), 500, 450);
    }

    /**
     * Cambia la escena actual del Stage.
     */
    private void cambiarEscena(javafx.scene.Parent root, int ancho, int alto) {
        Scene scene = new Scene(root, ancho, alto);
        stage.setScene(scene);
        // Opcional: centrar la ventana
        stage.centerOnScreen();
    }
}