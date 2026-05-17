package dev.hollowforge.gui.components;

import dev.hollowforge.util.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.io.InputStream;

public class UIButtonFactory {

    // Tamaños de fuente predefinidos
    private static final double FONT_SIZE_LARGE = 20;
    private static final double FONT_SIZE_SMALL = 14;

    public static Button createButtonWithCenteredText(double prefWidth, double prefHeight, String text, String imagePath, boolean hoverActive) {
        Button button = new Button();
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(prefHeight);

        if (imagePath == null || imagePath.isEmpty()) {
            button.setText(text);
            button.setFont(FontLoader.getMinecraftFont(FONT_SIZE_SMALL));
            button.setStyle("-fx-text-fill: #562B05; -fx-background-color: #4a4a4a; -fx-background-radius: 5;");
            return button;
        }

        try (InputStream is = UIButtonFactory.class.getResourceAsStream(imagePath)) {
            if (is == null) {
                System.err.println("[ERROR] No se encontró la imagen: " + imagePath);
                button.setText(text);
                button.setFont(FontLoader.getMinecraftFont(FONT_SIZE_SMALL));
                button.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white;");
                return button;
            }
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(prefWidth);
            imageView.setFitHeight(prefHeight);
            imageView.setPreserveRatio(false);

            Label textLabel = new Label(text);
            textLabel.setWrapText(true);
            textLabel.setAlignment(Pos.CENTER);
            textLabel.setMaxWidth(prefWidth * 0.8);
            textLabel.setFont(FontLoader.getMinecraftFont(FONT_SIZE_LARGE,"/fonts/MinecraftBold-nMK1.otf"));
            textLabel.setStyle("-fx-text-fill: #562B05;");

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(imageView, textLabel);
            stackPane.setPrefSize(prefWidth, prefHeight);

            button.setGraphic(stackPane);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: transparent;");
        } catch (Exception e) {
            e.printStackTrace();
            button.setText(text);
            button.setFont(FontLoader.getMinecraftFont(FONT_SIZE_SMALL));
            button.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white;");
        }

        if(hoverActive) {
            // Crear transiciones de escala suaves
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.setCycleCount(1);
            scaleUp.setAutoReverse(false);

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.setCycleCount(1);
            scaleDown.setAutoReverse(false);

            button.setOnMouseEntered(e -> scaleUp.playFromStart());
            button.setOnMouseExited(e -> scaleDown.playFromStart());
        }
        return button;
    }

    public static Button createButton(double prefWidth, double prefHeight, String text) {
        Button button = new Button(text);
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(prefHeight);
        button.setFont(FontLoader.getMinecraftFont(FONT_SIZE_SMALL));
        button.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white; -fx-background-radius: 5;");
        return button;
    }

    public static Button createImageButton(double width, double height, String imagePath, String tooltipText, boolean hoverActive) {
        Button button = new Button();
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        try (InputStream is = UIButtonFactory.class.getResourceAsStream(imagePath)) {
            if (is == null) {
                System.err.println("No se encontró la imagen: " + imagePath);
                button.setStyle("-fx-background-color: #4a4a4a; -fx-background-radius: 10;");
                return button;
            }
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);

            // Cálculo de viewPort (igual que tenías)
            double imgWidth = image.getWidth();
            double imgHeight = image.getHeight();
            double targetWidth = width;
            double targetHeight = height;
            double scaleW = targetWidth / imgWidth;
            double scaleH = targetHeight / imgHeight;
            double scale = Math.max(scaleW, scaleH);
            double scaledW = imgWidth * scale;
            double scaledH = imgHeight * scale;
            double viewX = (scaledW - targetWidth) / 2 / scale;
            double viewY = (scaledH - targetHeight) / 2 / scale;
            imageView.setViewport(new javafx.geometry.Rectangle2D(viewX, viewY, targetWidth / scale, targetHeight / scale));
            imageView.setFitWidth(targetWidth);
            imageView.setFitHeight(targetHeight);

            button.setGraphic(imageView);
            button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            if (tooltipText != null) {
                Tooltip.install(button, new Tooltip(tooltipText));
            }

            if(hoverActive) {
                // Crear transiciones de escala suaves
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
                scaleUp.setToX(1.1);
                scaleUp.setToY(1.1);
                scaleUp.setCycleCount(1);
                scaleUp.setAutoReverse(false);

                ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);
                scaleDown.setCycleCount(1);
                scaleDown.setAutoReverse(false);

                button.setOnMouseEntered(e -> scaleUp.playFromStart());
                button.setOnMouseExited(e -> scaleDown.playFromStart());
            }

        } catch (Exception e) {
            e.printStackTrace();
            button.setStyle("-fx-background-color: #4a4a4a;");
        }
        return button;
    }
}