package dev.hollowforge.gui.components;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class WebVideoBackground extends StackPane {

    private final WebView webView;

    /**
     * @param videoUrl    URL completa del video (puede ser local: getClass().getResource(...).toExternalForm())
     * @param blurRadius  píxeles de desenfoque (ej. 15)
     */
    public WebVideoBackground(String videoUrl, double blurRadius) {
        webView = new WebView();
        webView.setStyle("-fx-background-color: transparent;");

        // Cargar página HTML sencilla que reproduce el video en bucle y muteado
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { margin: 0; overflow: hidden; background: black; }
                        video {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }
                    </style>
                </head>
                <body>
                    <video autoplay loop muted playsinline>
                        <source src="%s" type="video/mp4">
                    </video>
                </body>
                </html>
                """.formatted(videoUrl);

        webView.getEngine().loadContent(html);

        // Aplicar desenfoque mediante CSS
        if (blurRadius > 0) {
            webView.setStyle("-fx-effect: blur(" + blurRadius + "px);");
        }

        // Ajustar tamaño al contenedor
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());

        getChildren().add(webView);
    }

    // Métodos para limpiar (opcional, WebView se recolecta solo)
    public void dispose() {
        webView.getEngine().loadContent(""); // descarga la página
    }
}