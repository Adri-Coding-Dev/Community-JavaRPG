package dev.hollowforge.util;

/**
 * Constantes globales del proyecto: rutas de recursos, URLs, etc.
 * IMPORTANTE: AGREGAR AQUI LAS NUEVAS RUTAS, PERO NO MODIFIQUEIS LAS RUTAS ANTERIORES
 *
 * USAD LAS NUEVAS RUTAS COMO LAS QUE VIENEN AQUI
 */
public final class AppConstants {

    private AppConstants() { } // Previene instanciación

    // ========== RUTAS DE RECURSOS ==========
    public static final String FONTS_DIR = "/fonts/";
    public static final String FONT_MINECRAFT = FONTS_DIR + "MinecraftRegular.otf"; // o .ttf

    public static final String VIDEOS_DIR = "/videos/";
    public static final String VIDEO_BACKGROUND = VIDEOS_DIR + "background.mp4";

    public static final String UI_BUTTONS_DIR = "/assets/ui/buttons/";
    public static final String BUTTON_NEW_GAME_IMAGE = UI_BUTTONS_DIR + "ButtonsBeginTexture.png";

    public static final String SOCIAL_IMAGES_DIR = "/assets/ui/socials/";
    public static final String DISCORD_ICON = SOCIAL_IMAGES_DIR + "DiscordLogo.png";
    public static final String YOUTUBE_ICON  = SOCIAL_IMAGES_DIR + "YoutubeLogo.png";
    public static final String GITHUB_ICON   = SOCIAL_IMAGES_DIR + "GitHubLogo.png";

    // ========== URLs EXTERNAS ==========
    public static final String REPO_URL = "https://github.com/Adri-Coding-Dev/Community-JavaRPG";
    public static final String DISCORD_INVITE_URL = "https://discord.gg/RRSpAz6sM9";
    public static final String YOUTUBE_URL = "https://www.youtube.com/@Shadow_Error_Hack";
    public static final String GITHUB_PROJECT_URL = "https://github.com/Adri-Coding-Dev/Community-JavaRPG";

    // ========== CONFIGURACIÓN DE INTERFAZ ==========
    public static final double SOCIAL_BUTTON_SIZE = 80; // tamaño en píxeles
    public static final double TITLE_FONT_SIZE = 48;
    public static final double BUTTON_FONT_SIZE_LARGE = 20;
    public static final double BUTTON_FONT_SIZE_SMALL = 14;
    public static final double NEW_GAME_BUTTON_WIDTH = 400;
    public static final double NEW_GAME_BUTTON_HEIGHT = 80;
    public static final double NORMAL_BUTTON_WIDTH = 200;
    public static final double NORMAL_BUTTON_HEIGHT = 40;

    // ========== TEXTO DE BOTONES (por si se quiere centralizar también) ==========
    public static final String NEW_GAME_TEXT = "Nueva Partida";
    public static final String LOAD_GAME_TEXT = "Cargar Partida";
    public static final String OPTIONS_TEXT = "Opciones";
    public static final String REPOSITORY_TEXT = "Repositorio";
    public static final String CONTRIBUTORS_TEXT = "Contribuidores Oficiales";
}