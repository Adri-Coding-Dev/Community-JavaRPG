package dev.hollowforge.gui.components;

import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoBackground extends StackPane {
    private MediaPlayer mediaPlayer;

    /**
     * @param videoResourcePath ruta dentro de resources, ej: "/videos/background.mp4"
     * @param blurRadius        radio del desenfoque (píxeles)
     */
    public VideoBackground(String videoResourcePath, double blurRadius) {
        try {
            var resource = getClass().getResource(videoResourcePath);
            if (resource == null) {
                System.err.println("Video no encontrado en recursos: " + videoResourcePath);
                setStyle("-fx-background-color: #1e1e1e;");
                return;
            }
            String uri = resource.toExternalForm();
            Media media = new Media(uri);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);   // silenciado para no molestar
            mediaPlayer.setAutoPlay(true);

            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setPreserveRatio(false);
            mediaView.setSmooth(true);
            if (blurRadius > 0) {
                mediaView.setEffect(new BoxBlur(blurRadius, blurRadius, 3));
            }
            getChildren().add(mediaView);
            widthProperty().addListener((obs, old, newVal) -> mediaView.setFitWidth(getWidth()));
            heightProperty().addListener((obs, old, newVal) -> mediaView.setFitHeight(getHeight()));
            mediaPlayer.setOnReady(() -> {
                mediaView.setFitWidth(getWidth());
                mediaView.setFitHeight(getHeight());
            });
        } catch (Exception e) {
            System.err.println("Error al cargar/reproducir el video: " + e.getMessage());
            e.printStackTrace();
            setStyle("-fx-background-color: #1e1e1e;");
        }
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}