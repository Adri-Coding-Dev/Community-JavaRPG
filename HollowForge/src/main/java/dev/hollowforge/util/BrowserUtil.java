// Fichero: BrowserUtil.java
package dev.hollowforge.util;

import javafx.scene.control.Alert;

/**
 * Utilidad para abrir URLs en el navegador del sistema.
 * Combina java.awt.Desktop y, como fallback en Linux, el comando xdg-open.
 * Si todo falla, muestra un diálogo con la URL.
 */
public final class BrowserUtil {

    private BrowserUtil() {
        // Constructor privado para evitar instanciación (clase de utilidad)
    }

    /**
     * Abre la URL especificada en el navegador predeterminado del sistema.
     * Utiliza Desktop.browse() si está disponible; si no, en Linux intenta xdg-open.
     * En caso de error, muestra una alerta.
     *
     * @param url dirección web a abrir
     */
    public static void abrirUrl(String url) {
        try {
            // Comprueba si el sistema soporta Desktop y la acción BROWSE
            if (java.awt.Desktop.isDesktopSupported() &&
                    java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            } else {
                abrirConXdgOpen(url); // Fallback para Linux sin Desktop
            }
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + url);
        }
    }

    /**
     * Intenta abrir la URL usando el comando xdg-open (típico en entornos Linux).
     * Si falla o no es Linux, muestra una alerta.
     *
     * @param url la URL
     */
    private static void abrirConXdgOpen(String url) {
        try {
            // Detecta si el sistema operativo es Linux
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
                return;
            }
        } catch (Exception ignored) {}
        mostrarAlerta("No se pudo abrir el navegador. Visita manualmente:\n" + url);
    }

    /**
     * Muestra un cuadro de diálogo de error con el mensaje proporcionado.
     *
     * @param mensaje texto a mostrar al usuario
     */
    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}