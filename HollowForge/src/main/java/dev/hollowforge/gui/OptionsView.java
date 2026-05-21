package dev.hollowforge.gui;

import dev.hollowforge.audio.AudioManager;
import dev.hollowforge.gui.components.UIButtonFactory;
import dev.hollowforge.gui.components.VideoBackground;
import dev.hollowforge.util.AppConstants;
import dev.hollowforge.util.FontLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OptionsView extends StackPane {
    public OptionsView(AudioManager audioManager, Runnable onClose, Stage primaryStage) {
        setAlignment(Pos.CENTER);

        // Fondo igual que el menú principal (MainScreen.png)
        VideoBackground videoBackground = new VideoBackground(AppConstants.VIDEO_BACKGROUND, 0);

        // Contenedor de controles (transparente)
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxSize(400, 350); // Tamaño contenido más pequeño, acorde a la ventana
        contentBox.setMinSize(350, 300);
        contentBox.setStyle("-fx-background-color: transparent;");

        // Título
        Label title = new Label("OPCIONES");
        title.setFont(FontLoader.getMinecraftFont(36)); // Reducido ligeramente
        title.setStyle("-fx-text-fill: #3B2A1F;");
        title.setPadding(new Insets(0, 0, 10, 0));

        // Volumen
        Label volumeLabel = new Label("Volumen");
        volumeLabel.setFont(FontLoader.getMinecraftFont(20));
        volumeLabel.setStyle("-fx-text-fill: #3B2A1F;");

        Slider volumeSlider = new Slider(0, 1, audioManager.getVolume());
        volumeSlider.setPrefWidth(240);
        volumeSlider.setMaxWidth(260);
        volumeSlider.setMinWidth(180);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);

        // Fondo del slider con textura (mantenemos el estilo del slider)
        StackPane sliderContainer = new StackPane();
        ImageView trackBg = new ImageView();
        try {
            String trackImagePath = AppConstants.BUTTON_NEW_GAME_IMAGE;
            Image trackImg = new Image(getClass().getResourceAsStream(trackImagePath));
            trackBg.setImage(trackImg);
            trackBg.setPreserveRatio(false);
            trackBg.fitWidthProperty().bind(volumeSlider.widthProperty());
            trackBg.fitHeightProperty().bind(volumeSlider.heightProperty());
        } catch (Exception e) {
            trackBg.setStyle("-fx-background-color: #8b5a2b;");
        }
        volumeSlider.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-track-color: rgba(0,0,0,0.3);");
        sliderContainer.getChildren().addAll(trackBg, volumeSlider);
        sliderContainer.prefWidthProperty().bind(volumeSlider.widthProperty());
        sliderContainer.prefHeightProperty().bind(volumeSlider.heightProperty());

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setVolume(newVal.doubleValue());
        });

        // Botón Volver (más pequeño acorde al tamaño)
        Button btnBack = UIButtonFactory.createButtonWithCenteredText(280, 55, "Volver", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 24);
        btnBack.setOnAction(e -> onClose.run());

        // Botón Salir
        Button btnExit = UIButtonFactory.createButtonWithCenteredText(280, 55, "Salir del juego", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 24);
        btnExit.setOnAction(e -> primaryStage.close());

        contentBox.getChildren().addAll(title, volumeLabel, sliderContainer, btnBack, btnExit);
        VBox.setMargin(sliderContainer, new Insets(5, 0, 15, 0));
        VBox.setMargin(btnBack, new Insets(5, 0, 5, 0));

        // Añadir el video de fondo y el contenido encima
        getChildren().addAll(videoBackground, contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);
    }
}