package es.diegofeblesseoane.pkgame.backend.controller.abstracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public abstract class AbstractController {

    private Properties propertiesIdioma;

    /**
     * Establece las propiedades del idioma.
     * @param properties Objeto Properties con las configuraciones del idioma.
     */
    public void setpropertiesIdioma(Properties properties) {
        propertiesIdioma = properties;
    }

    /**
     * Obtiene las propiedades del idioma configuradas.
     * @return Objeto Properties con las configuraciones del idioma.
     */
    public Properties getPropertiesIdioma() {
        return propertiesIdioma;
    }

    /**
     * Carga las propiedades del idioma desde un archivo.
     * @param nombreFichero Nombre base del archivo de propiedades.
     * @param idioma Código del idioma (por ejemplo, "es", "en").
     * @return Objeto Properties con las configuraciones cargadas.
     */
    public Properties loadIdioma(String nombreFichero, String idioma) {
        Properties properties = new Properties();

        if (nombreFichero == null || idioma == null) {
            return properties;
        }

        String path = "src/main/resources/" + nombreFichero + "-" + idioma + ".properties";

        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            System.out.println("Path:" + file.getAbsolutePath());
            return properties;
        }

        try {
            FileInputStream input = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(input, "UTF-8");
            properties.load(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }
}
