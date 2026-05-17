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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import dev.hollowforge.util.FontLoader;

public class MainMenuView {

    private final StackPane root;

    public MainMenuView(HostServices hostServices, Runnable onNuevaPartida, Runnable onContribuidores) {
        // En el constructor de MainMenuView, reemplaza la línea que crea el fondo:
        VideoBackground videoBackground = new VideoBackground(AppConstants.VIDEO_BACKGROUND,0);

        Button btnNuevaPartida = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.NEW_GAME_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE,
                false
        );
        Button btnCargarPartida = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.LOAD_GAME_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE,
                false
        );
        Button btnOpciones = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.OPTIONS_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE,
                false
        );
        Button btnContribuidores = UIButtonFactory.createButtonWithCenteredText(
                AppConstants.NEW_GAME_BUTTON_WIDTH,
                AppConstants.NEW_GAME_BUTTON_HEIGHT,
                AppConstants.CONTRIBUTORS_TEXT,
                AppConstants.BUTTON_NEW_GAME_IMAGE,
                true
        );

        btnNuevaPartida.setOnAction(e -> onNuevaPartida.run());
        btnCargarPartida.setOnAction(e -> System.out.println("Cargar partida - pendiente"));
        btnOpciones.setOnAction(e -> System.out.println("Opciones - pendiente"));
        btnContribuidores.setOnAction(e -> onContribuidores.run());

        VBox mainButtons = new VBox(15);
        mainButtons.setAlignment(Pos.CENTER);
        mainButtons.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        Region spacer = new Region();
        spacer.setPrefHeight(400); // Ajusta la altura según necesites

        mainButtons.getChildren().addAll(spacer, btnNuevaPartida, btnCargarPartida, btnOpciones, btnContribuidores);

        Button btnDiscord = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.DISCORD_ICON,
                "Únete a Discord",
                true
        );
        Button btnTwitter = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.YOUTUBE_ICON,
                "Síguenos en Youtube",
                true
        );
        Button btnGitHub = UIButtonFactory.createImageButton(
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.SOCIAL_BUTTON_SIZE,
                AppConstants.GITHUB_ICON,
                "GitHub del proyecto",
                true
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