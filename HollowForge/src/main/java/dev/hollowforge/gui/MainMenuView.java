package dev.hollowforge.gui;

import dev.hollowforge.audio.AudioManager;
import dev.hollowforge.gui.components.UIButtonFactory;
import dev.hollowforge.gui.components.VideoBackground;
import dev.hollowforge.util.AppConstants;
import dev.hollowforge.util.BrowserUtil;
import dev.hollowforge.util.FontLoader;
import dev.hollowforge.util.LogManager;
import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainMenuView {
    private final StackPane root;
    private static final double PREF_SPACER_HEIGHT = 350;

    public MainMenuView(HostServices hostServices, Runnable onNuevaPartida, Runnable onContribuidores, Runnable onOpciones, AudioManager audioManager) {
        root = new StackPane();
        try {
            VideoBackground videoBackground = new VideoBackground(AppConstants.VIDEO_BACKGROUND, 0);
            Button btnNuevaPartida = UIButtonFactory.createButtonWithCenteredText(
                    AppConstants.NEW_GAME_BUTTON_WIDTH,
                    AppConstants.NEW_GAME_BUTTON_HEIGHT,
                    AppConstants.NEW_GAME_TEXT,
                    AppConstants.BUTTON_NEW_GAME_IMAGE,
                    true
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
                    true
            );
            Button btnContribuidores = UIButtonFactory.createButtonWithCenteredText(
                    AppConstants.NEW_GAME_BUTTON_WIDTH,
                    AppConstants.NEW_GAME_BUTTON_HEIGHT,
                    AppConstants.CONTRIBUTORS_TEXT,
                    AppConstants.BUTTON_NEW_GAME_IMAGE,
                    true
            );

            btnNuevaPartida.setOnAction(e -> onNuevaPartida.run());
            btnCargarPartida.setOnAction(e -> LogManager.warning("Boton cargar partida accionado - No implementado todavia"));
            btnOpciones.setOnAction(e -> onOpciones.run());  // ahora llama al callback
            btnContribuidores.setOnAction(e -> onContribuidores.run());

            VBox mainButtons = new VBox(15, btnNuevaPartida, btnCargarPartida, btnOpciones, btnContribuidores);
            mainButtons.setAlignment(Pos.CENTER);
            mainButtons.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

            HBox socialBox = new HBox(50);
            socialBox.setAlignment(Pos.CENTER);
            socialBox.setStyle("-fx-padding: 10 0 20 0;");

            Image cartelImg = null;
            try {
                String cartelPath = "/assets/ui/socials/CartelToolTip.png";
                var is = getClass().getResourceAsStream(cartelPath);
                if (is != null) cartelImg = new Image(is);
            } catch (Exception e) {
                System.err.println("Error cargando cartel: " + e.getMessage());
            }

            Object[][] redes = {
                    {AppConstants.DISCORD_ICON, "Únete a Discord", AppConstants.DISCORD_INVITE_URL},
                    {AppConstants.YOUTUBE_ICON, "Síguenos en YouTube", AppConstants.YOUTUBE_URL},
                    {AppConstants.GITHUB_ICON, "GitHub del proyecto", AppConstants.GITHUB_PROJECT_URL}
            };

            for (Object[] red : redes) {
                String icono = (String) red[0];
                String texto = (String) red[1];
                String url = (String) red[2];

                Button btn = UIButtonFactory.createImageButton(
                        AppConstants.SOCIAL_BUTTON_SIZE,
                        AppConstants.SOCIAL_BUTTON_SIZE,
                        icono,
                        null,
                        true
                );
                btn.setOnAction(e -> BrowserUtil.abrirUrl(url));

                StackPane cartel = new StackPane();
                if (cartelImg != null) {
                    ImageView cartelBg = new ImageView(cartelImg);
                    cartelBg.setFitWidth(180);
                    cartelBg.setPreserveRatio(true);
                    cartel.getChildren().add(cartelBg);
                } else {
                    cartel.setStyle("-fx-background-color: #4a4a4a; -fx-background-radius: 8; -fx-padding: 5;");
                }

                Label etiqueta = new Label(texto);
                etiqueta.setFont(FontLoader.getMinecraftFont(12));
                etiqueta.setTextFill(Color.web("#3B2A1F"));
                etiqueta.setStyle("-fx-background-color: transparent; -fx-padding: 0 14 20 14;");
                cartel.getChildren().add(etiqueta);
                StackPane.setAlignment(etiqueta, Pos.CENTER);

                VBox item = new VBox(10, btn, cartel);
                item.setAlignment(Pos.CENTER);
                socialBox.getChildren().add(item);
            }

            Region spacer = new Region();
            spacer.setPrefHeight(PREF_SPACER_HEIGHT);

            VBox uiContainer = new VBox(spacer, mainButtons, socialBox);
            uiContainer.setAlignment(Pos.TOP_CENTER);
            uiContainer.setStyle("-fx-background-color: transparent;");

            root.getChildren().addAll(videoBackground, uiContainer);
            StackPane.setAlignment(uiContainer, Pos.TOP_CENTER);
        } catch (Exception e) {
            System.err.println("Error en MainMenuView: " + e.getMessage());
            e.printStackTrace();
            root.getChildren().clear();
            root.setStyle("-fx-background-color: black;");
            Label errorLabel = new Label("Error al cargar el menú.\nReinicia la aplicación.");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(FontLoader.getMinecraftFont(20));
            root.getChildren().add(errorLabel);
        }
    }

    public Parent getRoot() {
        return root;
    }
}