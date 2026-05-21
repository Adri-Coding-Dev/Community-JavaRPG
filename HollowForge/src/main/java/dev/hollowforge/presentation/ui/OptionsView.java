package dev.hollowforge.presentation.ui;

import dev.hollowforge.engine.audio.AudioManager;
import dev.hollowforge.engine.config.AppConstants;
import dev.hollowforge.presentation.ui.components.FontLoader;
import dev.hollowforge.presentation.ui.components.ImageBackground;
import dev.hollowforge.presentation.ui.components.UIButtonFactory;
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

        ImageBackground imageBackground = new ImageBackground(AppConstants.VIDEO_BACKGROUND, 0);

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxSize(400, 350);
        contentBox.setMinSize(350, 300);
        contentBox.setStyle("-fx-background-color: transparent;");

        Label title = new Label("OPCIONES");
        title.setFont(FontLoader.getMinecraftFont(36));
        title.setStyle("-fx-text-fill: #3B2A1F;");
        title.setPadding(new Insets(0, 0, 10, 0));

        Label volumeLabel = new Label("Volumen");
        volumeLabel.setFont(FontLoader.getMinecraftFont(20));
        volumeLabel.setStyle("-fx-text-fill: #3B2A1F;");

        Slider volumeSlider = new Slider(0, 1, audioManager.getVolume());
        volumeSlider.setPrefWidth(240);
        volumeSlider.setMaxWidth(260);
        volumeSlider.setMinWidth(180);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);

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

        Button btnBack = UIButtonFactory.createButtonWithCenteredText(280, 55, "Volver", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 24);
        btnBack.setOnAction(e -> onClose.run());

        Button btnExit = UIButtonFactory.createButtonWithCenteredText(280, 55, "Salir del juego", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 24);
        btnExit.setOnAction(e -> primaryStage.close());

        contentBox.getChildren().addAll(title, volumeLabel, sliderContainer, btnBack, btnExit);
        VBox.setMargin(sliderContainer, new Insets(5, 0, 15, 0));
        VBox.setMargin(btnBack, new Insets(5, 0, 5, 0));

        getChildren().addAll(imageBackground, contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);
    }
}
