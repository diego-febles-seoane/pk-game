package es.diegofeblesseoane.pkgame.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigManager {

    public static class ConfigProperties {

        static String path;

        private static final Properties properties = new Properties();

        /**
         * Obtiene el valor de una propiedad por su clave
         * @param key String con la clave de la propiedad
         * @return String con el valor de la propiedad
         */
        public static String getProperty(String key) {
            return properties.getProperty(key);
        }

        /**
         * Obtiene el valor de una propiedad por su clave
         * @param key String con la clave de la propiedad
         * @param defaultValue String con el valor predeterminado si la clave no existe
         * @return String con el valor de la propiedad o el valor predeterminado
         */
        public static String getProperty(String key, String defaultValue) {
            return properties.getProperty(key, defaultValue);
        }

        /**
         * Establece una propiedad con una clave y un valor
         * @param key String con la clave de la propiedad
         * @param value String con el valor de la propiedad
         */
        public static void setProperty(String key, String value) {
            properties.setProperty(key, value);
        }

        /**
         * Establece la ruta del archivo de propiedades y carga sus valores
         * @param rutaPath String con la ruta del archivo de propiedades
         */
        public static void setPath(String rutaPath) {
            path = rutaPath;
            System.out.println("Attempting to load properties from: " + rutaPath);
            
            // Clear existing properties before loading new ones
            properties.clear();
            
            try {
                // Try to load from classpath resources first
                var resourceStream = ConfigProperties.class.getResourceAsStream(rutaPath);
                
                if (resourceStream != null) {
                    // Resource found on classpath
                    System.out.println("Loading properties from classpath resource: " + rutaPath);
                    try (InputStreamReader isr = new InputStreamReader(resourceStream, "UTF-8")) {
                        properties.load(isr);
                        System.out.println("Successfully loaded " + properties.size() + " properties from classpath");
                        // Print all loaded properties for debugging
                        properties.forEach((key, value) -> System.out.println("Loaded property: " + key + " = " + value));
                    }
                } else {
                    // Fallback to file system
                    System.out.println("Resource not found in classpath, trying filesystem: " + rutaPath);
                    File file = new File(rutaPath);
                    if (!file.exists() || !file.isFile()) {
                        System.out.println("ERROR: Path not found: " + file.getAbsolutePath());
                    } else {
                        try (FileInputStream input = new FileInputStream(path);
                             InputStreamReader isr = new InputStreamReader(input, "UTF-8")) {
                            properties.load(isr);
                            System.out.println("Successfully loaded " + properties.size() + " properties from filesystem");
                            // Print all loaded properties for debugging
                            properties.forEach((key, value) -> System.out.println("Loaded property: " + key + " = " + value));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("ERROR loading properties from " + rutaPath + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
