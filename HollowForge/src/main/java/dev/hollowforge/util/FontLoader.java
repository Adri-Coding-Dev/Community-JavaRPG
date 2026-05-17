package dev.hollowforge.util;

import javafx.scene.text.Font;
import java.io.InputStream;

public class FontLoader {
    private static Font minecraftFont;

    public static Font getMinecraftFont(double size) {
        if (minecraftFont == null) {
            try (InputStream is = FontLoader.class.getResourceAsStream(AppConstants.FONT_MINECRAFT)) {
                if (is == null) {
                    System.err.println("Fuente no encontrada, usando fuente por defecto");
                    return Font.font("System", size);
                }
                minecraftFont = Font.loadFont(is, size);
            } catch (Exception e) {
                e.printStackTrace();
                return Font.font("System", size);
            }
        }
        return Font.font(minecraftFont.getFamily(), size);
    }

    public static Font getMinecraftFont(double size, String font) {
        if (minecraftFont == null) {
            try (InputStream is = FontLoader.class.getResourceAsStream(font)) {
                if (is == null) {
                    System.err.println("Fuente no encontrada, usando fuente por defecto");
                    return Font.font("System", size);
                }
                minecraftFont = Font.loadFont(is, size);
            } catch (Exception e) {
                e.printStackTrace();
                return Font.font("System", size);
            }
        }
        return Font.font(minecraftFont.getFamily(), size);
    }
}