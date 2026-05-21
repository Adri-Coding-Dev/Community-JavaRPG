package dev.hollowforge.navigation;

import dev.hollowforge.audio.AudioManager;
import dev.hollowforge.game.GameController;
import dev.hollowforge.service.GitHubService;
import dev.hollowforge.gui.ContributorsView;
import dev.hollowforge.gui.MainMenuView;
import dev.hollowforge.gui.OptionsView;
import dev.hollowforge.util.AppConstants;
import dev.hollowforge.util.LogManager;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final HostServices hostServices;
    private final GitHubService gitHubService;
    private final AudioManager audioManager;
    private GameController gameController;
    private String currentMusic;

    private static final int ANCHO_MENU = 400;
    private static final int ALTO_MENU = 350;
    private static final int ANCHO_CONTRIB = 500;
    private static final int ALTO_CONTRIB = 450;
    private static final int ANCHO_JUEGO = 800;
    private static final int ALTO_JUEGO = 600;
    private static final int ANCHO_OPCIONES = 400;   // Igual que el menú principal
    private static final int ALTO_OPCIONES = 350;    // Igual que el menú principal

    public SceneManager(Stage stage, HostServices hostServices, GitHubService gitHubService, AudioManager audioManager) {
        this.stage = stage;
        this.hostServices = hostServices;
        this.gitHubService = gitHubService;
        this.audioManager = audioManager;
        this.currentMusic = AppConstants.MUSIC_MENU;
    }

    public void mostrarMenuPrincipal() {
        if (gameController != null) {
            gameController.shutdown();
            gameController = null;
        }
        if (!AppConstants.MUSIC_MENU.equals(currentMusic)) {
            audioManager.switchMusic(AppConstants.MUSIC_MENU, 300, 500);
            currentMusic = AppConstants.MUSIC_MENU;
        }
        MainMenuView mainMenuView = new MainMenuView(
                hostServices,
                this::iniciarJuego,
                this::mostrarContribuidores,
                this::mostrarOpciones,
                audioManager
        );
        cambiarEscena(mainMenuView.getRoot(), ANCHO_MENU, ALTO_MENU);
    }

    public void mostrarContribuidores() {
        ContributorsView contributorsView = new ContributorsView(
                hostServices,
                gitHubService,
                this::mostrarMenuPrincipal
        );
        cambiarEscena(contributorsView.getRoot(), ANCHO_CONTRIB, ALTO_CONTRIB);
    }

    public void mostrarOpciones() {
        OptionsView optionsView = new OptionsView(audioManager, this::mostrarMenuPrincipal, stage);
        cambiarEscena(optionsView, ANCHO_OPCIONES, ALTO_OPCIONES);
    }

    private void iniciarJuego() {
        LogManager.info("Iniciando nueva partida");
        if (gameController != null) {
            gameController.shutdown();
        }
        currentMusic = AppConstants.MUSIC_GAME;
        gameController = new GameController(audioManager, stage, this::mostrarMenuPrincipal);
        gameController.start();
        Scene scene = new Scene(gameController.getView(), ANCHO_JUEGO, ALTO_JUEGO);
        gameController.setupKeyboard(scene);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void cambiarEscena(javafx.scene.Parent root, int ancho, int alto) {
        Scene scene = new Scene(root, ancho, alto);
        stage.setScene(scene);
        stage.centerOnScreen();
        LogManager.info("Escena Cambiada a: " + root.getClass().getSimpleName());
    }
}