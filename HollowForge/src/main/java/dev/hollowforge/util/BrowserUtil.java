package dev.hollowforge.util;

import javafx.scene.control.Alert;

/**
 * Utilidad para abrir URLs en el navegador del sistema.
 * Combina java.awt.Desktop y xdg-open como fallback.
 */
public final class BrowserUtil {

    private BrowserUtil() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Abre la URL especificada en el navegador predeterminado.
     * Si no es posible, muestra una alerta con la URL.
     *
     * @param url dirección web a abrir
     */
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