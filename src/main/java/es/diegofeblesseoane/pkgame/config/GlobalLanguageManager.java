package es.diegofeblesseoane.pkgame.config;

/**
 * Gestor global de idiomas para toda la aplicación.
 * Esta clase mantiene el estado del idioma actual y lo hace accesible
 * desde cualquier parte de la aplicación.
 */
public class GlobalLanguageManager {
    
    // Idioma actual de la aplicación
    private static String currentLanguage = "es"; // Idioma por defecto
    
    /**
     * Establece el idioma global para toda la aplicación
     * @param language Código del idioma ("es", "en", "fr")
     */
    public static void setGlobalLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            System.out.println("⚠️ Idioma inválido, manteniendo idioma actual: " + currentLanguage);
            return;
        }
        
        currentLanguage = language;
        System.out.println("🌍 Idioma global establecido: " + currentLanguage);
        
        // Cargar el archivo de propiedades correspondiente
        loadLanguageProperties(currentLanguage);
        
        // Guardar en la configuración para persistencia
        ConfigManager.ConfigProperties.setProperty("idiomaActual", currentLanguage);
    }
    
    /**
     * Obtiene el idioma actual de la aplicación
     * @return Código del idioma actual
     */
    public static String getCurrentLanguage() {
        // Intentar obtener desde la configuración primero
        String savedLanguage = ConfigManager.ConfigProperties.getProperty("idiomaActual");
        if (savedLanguage != null && !savedLanguage.trim().isEmpty()) {
            currentLanguage = savedLanguage;
        }
        
        return currentLanguage;
    }
    
    /**
     * Carga las propiedades del idioma especificado
     * @param language Código del idioma a cargar
     */
    private static void loadLanguageProperties(String language) {
        try {
            String path = "/es/diegofeblesseoane/pkgame/idioma-" + language + ".properties";
            System.out.println("📂 Cargando archivo de idioma: " + path);
            
            ConfigManager.ConfigProperties.setPath(path);
            
            System.out.println("✅ Propiedades de idioma cargadas exitosamente");
            
        } catch (Exception e) {
            System.out.println("❌ Error cargando propiedades de idioma: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Inicializa el sistema de idiomas con el idioma por defecto
     */
    public static void initialize() {
        System.out.println("🚀 Inicializando sistema global de idiomas...");
        
        // Cargar idioma desde configuración o usar por defecto
        String savedLanguage = ConfigManager.ConfigProperties.getProperty("idiomaActual", "es");
        setGlobalLanguage(savedLanguage);
        
        System.out.println("✅ Sistema de idiomas inicializado con: " + currentLanguage);
    }
    
    /**
     * Obtiene un texto traducido usando la clave especificada
     * @param key Clave del texto a traducir
     * @param defaultValue Valor por defecto si no se encuentra la clave
     * @return Texto traducido o valor por defecto
     */
    public static String getText(String key, String defaultValue) {
        return ConfigManager.ConfigProperties.getProperty(key, defaultValue);
    }
    
    /**
     * Obtiene un texto traducido usando la clave especificada
     * @param key Clave del texto a traducir
     * @return Texto traducido o la clave si no se encuentra
     */
    public static String getText(String key) {
        return ConfigManager.ConfigProperties.getProperty(key, key);
    }
}

