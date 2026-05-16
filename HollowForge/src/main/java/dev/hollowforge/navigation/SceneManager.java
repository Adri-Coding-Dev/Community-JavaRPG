// Fichero: SceneManager.java
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
 * Es el encargado de cambiar la escena actual de la ventana.
 */
public class SceneManager {

    private final Stage stage;
    private final HostServices hostServices;
    private final GitHubService gitHubService;

    // Dimensiones base de las ventanas (pueden cambiar según la vista)
    private static final int ANCHO_MENU = 400;
    private static final int ALTO_MENU = 350;
    private static final int ANCHO_CONTRIB = 500;
    private static final int ALTO_CONTRIB = 450;

    /**
     * Constructor.
     *
     * @param stage          escenario principal de la aplicación
     * @param hostServices   servicio para abrir enlaces web
     * @param gitHubService  servicio para obtener datos de GitHub
     */
    public SceneManager(Stage stage, HostServices hostServices, GitHubService gitHubService) {
        this.stage = stage;
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
    }

    /**
     * Muestra el menú principal.
     * Crea una instancia de MainMenuView y configura la escena con tamaño base.
     */
    public void mostrarMenuPrincipal() {
        MainMenuView mainMenuView = new MainMenuView(
                hostServices,
                this::iniciarJuego,
                this::mostrarContribuidores   // callback para ir a la vista de contribuidores
        );
        cambiarEscena(mainMenuView.getRoot(), ANCHO_MENU, ALTO_MENU);
    }

    /**
     * Muestra la vista de contribuidores.
     * Crea una instancia de ContributorsView y configura la escena con un tamaño mayor (500x450)
     * para dar más espacio a las tarjetas.
     */
    public void mostrarContribuidores() {
        ContributorsView contributorsView = new ContributorsView(
                hostServices,
                gitHubService,
                this::mostrarMenuPrincipal    // callback para volver al menú
        );
        cambiarEscena(contributorsView.getRoot(), ANCHO_CONTRIB, ALTO_CONTRIB);
    }

    private void iniciarJuego(){
        //TODO -> Implementar logica del juego (crear escena, GameLoop, etc)
        System.out.println("[DEBUG]: Iniciar juego - Pendiente de implementacion");
        //Mostramos el menu para no romper la navegacion (por ahora)
        mostrarMenuPrincipal();
    }

    /**
     * Cambia la escena actual del Stage.
     * Crea una nueva Scene con el nodo raíz y las dimensiones indicadas,
     * la asigna al Stage y centra la ventana en la pantalla.
     *
     * @param root  nodo raíz de la nueva vista
     * @param ancho ancho de la escena
     * @param alto  alto de la escena
     */
    private void cambiarEscena(javafx.scene.Parent root, int ancho, int alto) {
        Scene scene = new Scene(root, ancho, alto);
        stage.setScene(scene);
        stage.centerOnScreen(); // Centra la ventana para mejor experiencia de usuario
    }
}