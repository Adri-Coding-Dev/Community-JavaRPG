package dev.app;

import dev.gui.ContributorsView;
import dev.gui.MainView;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de HollowForge.
 * Gestiona el escenario principal y la navegación entre vistas.
 */
public class MainApp extends Application {

    private static final String TITULO_VENTANA = "HollowForge";
    private static final int ANCHO_VENTANA = 400;
    private static final int ALTO_VENTANA = 350;

    private Stage escenarioPrincipal;
    private HostServices hostServices;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.escenarioPrincipal = stage;
        this.hostServices = getHostServices();

        escenarioPrincipal.setTitle(TITULO_VENTANA);
        mostrarMenuPrincipal();
        escenarioPrincipal.show();
    }

    /**
     * Muestra la vista del menú principal.
     */
    private void mostrarMenuPrincipal() {
        MainView mainView = new MainView(
                hostServices,
                this::mostrarContribuidores  // callback para navegar a contribuidores
        );
        Scene escena = new Scene(mainView.getRoot(), ANCHO_VENTANA, ALTO_VENTANA);
        escenarioPrincipal.setScene(escena);
    }

    /**
     * Muestra la vista de contribuidores.
     */
    private void mostrarContribuidores() {
        ContributorsView contributorsView = new ContributorsView(
                hostServices,
                this::mostrarMenuPrincipal  // callback para volver al menú
        );
        Scene escena = new Scene(contributorsView.getRoot(), 500, 450);
        escenarioPrincipal.setScene(escena);
    }
}