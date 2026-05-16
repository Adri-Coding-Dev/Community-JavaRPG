package dev.hollowforge.gui;

import dev.hollowforge.gui.components.UIButtonFactory;
import dev.hollowforge.gui.components.VideoBackground;
import dev.hollowforge.util.AppConstants;
import dev.hollowforge.util.BrowserUtil;
import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import dev.hollowforge.util.FontLoader;

public class MainMenuView {

    private final StackPane root;

    public MainMenuView(HostServices hostServices, Runnable onNuevaPartida, Runnable onContribuidores) {
        VideoBackground videoBackground = new VideoBackground(AppConstants.VIDEO_BACKGROUND, 15);

        Label titulo = new Label(AppConstants.NEW_GAME_TEXT); // No, mejor usar el título fijo
        titulo.setText("HollowForge"); // Título fijo, no es una constante externa
        titulo.setFont(FontLoader.getMinecraftFont(AppConstants.TITLE_FONT_SIZE));
        titulo.setTextFill(Color.WHITE);

        Button btnNuevaPartida = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.NEW_GAME_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE
        );
        Button btnCargarPartida = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.LOAD_GAME_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE
        );
        Button btnOpciones = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.OPTIONS_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE
        );
        Button btnContribuidores = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.CONTRIBUTORS_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE
        );

        btnNuevaPartida.setOnAction(e -> onNuevaPartida.run());
        btnCargarPartida.setOnAction(e -> System.out.println("Cargar partida - pendiente"));
        btnOpciones.setOnAction(e -> System.out.println("Opciones - pendiente"));
        btnContribuidores.setOnAction(e -> onContribuidores.run());

        VBox mainButtons = new VBox(15, titulo, btnNuevaPartida, btnCargarPartida, btnOpciones, btnContribuidores);
        mainButtons.setAlignment(Pos.CENTER);
        mainButtons.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        Button btnDiscord = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.DISCORD_ICON,
                "Únete a Discord"
        );
        Button btnTwitter = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.YOUTUBE_ICON,
                "Síguenos en Twitter"
        );
        Button btnGitHub = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.GITHUB_ICON,
                "GitHub del proyecto"
        );

        btnDiscord.setOnAction(e -> BrowserUtil.abrirUrl(AppConstants.DISCORD_INVITE_URL));
        btnTwitter.setOnAction(e -> BrowserUtil.abrirUrl(AppConstants.YOUTUBE_URL));
        btnGitHub.setOnAction(e -> BrowserUtil.abrirUrl(AppConstants.GITHUB_PROJECT_URL));

        HBox socialBox = new HBox(20, btnDiscord, btnTwitter, btnGitHub);
        socialBox.setAlignment(Pos.CENTER);
        socialBox.setStyle("-fx-padding: 10 0 20 0;");

        VBox uiContainer = new VBox(20, mainButtons, socialBox);
        uiContainer.setAlignment(Pos.CENTER);
        uiContainer.setStyle("-fx-background-color: transparent;");

        root = new StackPane();
        root.getChildren().addAll(videoBackground, uiContainer);
        StackPane.setAlignment(uiContainer, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }
}