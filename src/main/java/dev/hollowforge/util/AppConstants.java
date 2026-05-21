package dev.hollowforge.util;

/**
 * Constantes globales del proyecto: rutas de recursos, URLs, etc.
 * IMPORTANTE: AGREGAR AQUI LAS NUEVAS RUTAS, PERO NO MODIFIQUEIS LAS RUTAS ANTERIORES
 *
 * USAD LAS NUEVAS RUTAS COMO LAS QUE VIENEN AQUI (AppConstants.X)
 */
public final class AppConstants {

    private AppConstants() { } // Previene instanciación

    // ========== RUTAS DE RECURSOS ==========
    public static final String FONTS_DIR = "/fonts/";
    public static final String FONT_MINECRAFT = FONTS_DIR + "HollowForgeMinecraftRegular.otf"; // o .ttf


    public static final String VIDEO_BACKGROUND = "/assets/ui/background/MainScreen.png";

    public static final String UI_BUTTONS_DIR = "/assets/ui/buttons/";
    public static final String BUTTON_NEW_GAME_IMAGE = UI_BUTTONS_DIR + "ButtonsBeginTexture.png";

    public static final String MENU_OPTIONS_IMAGE = UI_BUTTONS_DIR + "OptionsMenu.png";

    public static final String SOCIAL_IMAGES_DIR = "/assets/ui/socials/";
    public static final String DISCORD_ICON = SOCIAL_IMAGES_DIR + "DiscordLogo.png";
    public static final String YOUTUBE_ICON  = SOCIAL_IMAGES_DIR + "YoutubeLogo.png";
    public static final String GITHUB_ICON   = SOCIAL_IMAGES_DIR + "GitHubLogo.png";

    // ========== URLs EXTERNAS ==========
    public static final String DISCORD_INVITE_URL = "https://discord.gg/RRSpAz6sM9";
    public static final String YOUTUBE_URL = "https://www.youtube.com/@Shadow_Error_Hack";
    public static final String GITHUB_PROJECT_URL = "https://github.com/Adri-Coding-Dev/Community-JavaRPG";

    // ========== CONFIGURACIÓN DE INTERFAZ ==========
    public static final double SOCIAL_BUTTON_SIZE = 80; // tamaño en píxeles
    public static final double NEW_GAME_BUTTON_WIDTH = 400;
    public static final double NEW_GAME_BUTTON_HEIGHT = 80;
    public static final int TITLE_SIZE_GAME = 40;

    // ========== TEXTO DE BOTONES (por si se quiere centralizar también) ==========
    public static final String GAME_NAME = "HollowForge";
    public static final String NEW_GAME_TEXT = "Nueva Partida";
    public static final String LOAD_GAME_TEXT = "Cargar Partida";
    public static final String OPTIONS_TEXT = "Opciones";
    public static final String REPOSITORY_TEXT = "Repositorio";
    public static final String CONTRIBUTORS_TEXT = "Contribuidores Oficiales";

    public static final String TOOLTIP_GITHUB_TEXT = "Repositorio del Proyecto";
    public static final String TOOLTIP_DISCORD_TEXT = "Únete a Discord";
    public static final String TOOLTIP_YOUTUBE_TEXT = "Síguenos en Youtube";

    public static final String TOOLTIP_IMAGE = "/assets/ui/socials/CartelToolTip.png";

    // ============= CANCIONES =================== \\
    public static final String AUDIO_DIR = "/assets/audio/";
    public static final String MUSIC_MENU = AUDIO_DIR + "HollowForgeMainTheme.wav";
    public static final String MUSIC_GAME = AUDIO_DIR + "HollowForgeCaveTheme.wav";
}