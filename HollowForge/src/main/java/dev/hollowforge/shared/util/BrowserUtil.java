package dev.hollowforge.shared.util;

import javafx.scene.control.Alert;

public final class BrowserUtil {

    private BrowserUtil() {
    }

    public static void abrirUrl(String url) {
        try {
            if (java.awt.Desktop.isDesktopSupported() &&
                    java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            } else {
                abrirConXdgOpen(url);
            }
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + url);
        }
    }

    private static void abrirConXdgOpen(String url) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
                return;
            }
        } catch (Exception ignored) {}
        mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + url);
    }

    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
