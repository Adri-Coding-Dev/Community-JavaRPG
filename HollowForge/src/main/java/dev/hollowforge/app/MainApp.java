package dev.hollowforge.app;

import dev.hollowforge.engine.audio.AudioManager;
import dev.hollowforge.engine.config.AppConstants;
import dev.hollowforge.engine.navigation.SceneManager;
import dev.hollowforge.infrastructure.external.github.GitHubService;
import dev.hollowforge.infrastructure.logging.LogManager;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle(AppConstants.GAME_NAME);
        HostServices hostServices = getHostServices();
        GitHubService gitHubService = new GitHubService();
        AudioManager audioManager = new AudioManager();

        boolean musicStarted = audioManager.playMusic(AppConstants.MUSIC_MENU, 1000);
        if (!musicStarted) {
            LogManager.warning("No se pudo reproducri la musica, verifica el estado del archivo.");
        }

        SceneManager sceneManager = new SceneManager(stage, hostServices, gitHubService, audioManager);
        sceneManager.mostrarMenuPrincipal();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
