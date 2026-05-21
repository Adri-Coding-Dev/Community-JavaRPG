package dev.hollowforge.game;

import dev.hollowforge.audio.AudioManager;
import dev.hollowforge.gui.components.UIButtonFactory;
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

public class PauseMenu extends VBox {
    private final AudioManager audioManager;
    private final Runnable onResume;
    private final Runnable onReturnToMenu;
    private final Stage primaryStage;

    public PauseMenu(AudioManager audioManager, Runnable onResume, Runnable onReturnToMenu, Stage primaryStage) {
        this.audioManager = audioManager;
        this.onResume = onResume;
        this.onReturnToMenu = onReturnToMenu;
        this.primaryStage = primaryStage;

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Contenedor interno (transparente)
        VBox contentBox = new VBox(15);  // Reducido el espaciado general
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxSize(500, 550);
        contentBox.setMinSize(400, 450);
        contentBox.setStyle("-fx-background-color: transparent;");

        // Imagen de fondo
        ImageView backgroundImage = new ImageView();
        try {
            String imagePath = AppConstants.MENU_OPTIONS_IMAGE;
            var resource = getClass().getResource(imagePath);
            if (resource != null) {
                Image img = new Image(resource.toExternalForm());
                backgroundImage.setImage(img);
                backgroundImage.setPreserveRatio(true);
                backgroundImage.setSmooth(true);
                backgroundImage.setFitWidth(1000); // Según lo que puso el usuario
            } else {
                System.err.println("No se encontró MENU_OPTIONS_IMAGE en " + imagePath);
                contentBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #8b5a2b; -fx-border-width: 4; -fx-border-radius: 15; -fx-background-radius: 15;");
            }
        } catch (Exception e) {
            e.printStackTrace();
            contentBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #8b5a2b; -fx-border-width: 4; -fx-border-radius: 15; -fx-background-radius: 15;");
        }

        // Título con fuente personalizada y tamaño grande
        Label title = new Label("OPCIONES");
        title.setFont(FontLoader.getMinecraftFont(42));  // Tamaño grande
        title.setStyle("-fx-text-fill: #3B2A1F;");
        title.setPadding(new Insets(0, 0, 5, 0));

        // Botones más grandes (350x70) y texto más grande (tamaño 28)
        Button btnResume = UIButtonFactory.createButtonWithCenteredText(350, 70, "Reanudar", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 28);
        btnResume.setOnAction(e -> onResume.run());

        Button btnMenu = UIButtonFactory.createButtonWithCenteredText(350, 70, "Volver al menú", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 28);
        btnMenu.setOnAction(e -> onReturnToMenu.run());

        Button btnExit = UIButtonFactory.createButtonWithCenteredText(350, 70, "Salir del juego", AppConstants.BUTTON_NEW_GAME_IMAGE, true, 28);
        btnExit.setOnAction(e -> primaryStage.close());

        // Etiqueta Volumen con fuente personalizada (tamaño 22)
        Label volumeLabel = new Label("Volumen");
        volumeLabel.setFont(FontLoader.getMinecraftFont(22));
        volumeLabel.setStyle("-fx-text-fill: #3B2A1F;");
        volumeLabel.setPadding(new Insets(10, 0, 0, 0)); // poco espacio arriba

        // Slider
        Slider volumeSlider = new Slider(0, 1, audioManager.getVolume());
        volumeSlider.setPrefWidth(280);
        volumeSlider.setMaxWidth(300);
        volumeSlider.setMinWidth(200);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);

        // Contenedor del slider con fondo
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

        // Añadir elementos al contentBox (sin márgenes extra para reducir espacio)
        contentBox.getChildren().addAll(title, btnResume, btnMenu, volumeLabel, sliderContainer, btnExit);
        // Eliminamos márgenes excesivos, solo dejamos un pequeño margen inferior en el slider
        VBox.setMargin(sliderContainer, new Insets(5, 0, 15, 0));
        VBox.setMargin(btnExit, new Insets(5, 0, 0, 0));

        // Montaje final
        root.getChildren().addAll(backgroundImage, contentBox);
        StackPane.setAlignment(backgroundImage, Pos.CENTER);
        StackPane.setAlignment(contentBox, Pos.CENTER);
        getChildren().add(root);
        setAlignment(Pos.CENTER);
    }
}