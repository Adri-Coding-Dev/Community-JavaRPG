package dev.hollowforge.gui.components;

import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class VideoBackground extends StackPane {
    private ImageView imageView;

    public VideoBackground(String imageResourcePath, double blurRadius) {
        try {
            var resource = getClass().getResource(imageResourcePath);
            if (resource == null) throw new RuntimeException("Recurso no encontrado: " + imageResourcePath);
            Image image = new Image(resource.toExternalForm());
            imageView = new ImageView(image);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);
            if (blurRadius > 0) {
                imageView.setEffect(new BoxBlur(blurRadius, blurRadius, 3));
            }
            getChildren().add(imageView);
            // Ajustar al tamaño del contenedor
            widthProperty().addListener((obs, old, newVal) -> imageView.setFitWidth(getWidth()));
            heightProperty().addListener((obs, old, newVal) -> imageView.setFitHeight(getHeight()));
            imageView.setFitWidth(getWidth());
            imageView.setFitHeight(getHeight());
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
            setStyle("-fx-background-color: black;");
        }
    }

    public void dispose() {
        // No es necesario liberar recursos
    }
}