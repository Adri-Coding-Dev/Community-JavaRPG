package dev.hollowforge.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;


public final class LogManager {
    private static final Logger logger = Logger.getLogger("HollowForge");

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        @Override
        public String format(LogRecord record){
            return String.format("[%s] [%s] %s%n",
                    dtf.format(LocalDateTime.now()),
                    record.getLevel().getName(),
                    record.getMessage());
        }
        });
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);

        //Crear un fichero con los logs
        try{
            //Directorio de logs en la carpeta del usuario (oculto)
            String userHome = System.getProperty("user.home");
            String logDir = userHome + "/.hollowforge/logs/";
            new java.io.File(logDir).mkdirs();
            //Limitamos 1MB por archivo, 3 ficheros como maximo y se agrega al final del fichero
            FileHandler fileHandler = new FileHandler(logDir + "hollowforge.log", 1_000_000, 3, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
        }catch (IOException e){
            logger.warning("No se pudo crear el archivo de log: " + e.getMessage());
        }
    }

    private LogManager(){}

    public static void info (String msg){
        logger.info(msg);
    }

    public static void warning (String msg){
        logger.warning(msg);
    }

    public static void severe (String msg){
        logger.severe(msg);
    }

    public static void debug (String msg){
        logger.fine(msg);
    }

    public static void setLevel(Level level){
        logger.setLevel(level);
    }
}
