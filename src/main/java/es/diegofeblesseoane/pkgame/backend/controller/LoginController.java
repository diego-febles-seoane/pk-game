package es.diegofeblesseoane.pkgame.backend.controller;

import java.util.ArrayList;
import java.util.List;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.config.ConfigManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    public Button buttonIniciarSesion;

    @FXML
    public Button buttonIniciarSinRegistro;

    @FXML
    private ComboBox<String> comboIdioma;

    @FXML
    public void onIniciarSesionButton(){
        try {
            Stage stage = (Stage) buttonIniciarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("inicioSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Inicio Sesion");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onIncioSinRegistroClick(){
        try {
            Stage stage = (Stage) buttonIniciarSinRegistro.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Menu Principal");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Configurar idiomas en el ComboBox
        List<String> idiomas = new ArrayList<>();
        idiomas.add("es");
        idiomas.add("en");
        idiomas.add("fr");
        comboIdioma.getItems().addAll(idiomas);

        String idiomaActual = ConfigManager.ConfigProperties.getProperty("idiomaActual", "es");
        comboIdioma.setValue(idiomaActual);

        // Add listener for language changes
        comboIdioma.setOnAction(event -> cambiarIdioma());

        // Initial language setup
        cambiarIdioma();
    }

    /**
     * Cambia el idioma de la aplicación según el seleccionado en el ComboBox.
     * Actualiza los textos de la interfaz al idioma seleccionado.
     */
    @FXML
    protected void cambiarIdioma() {
        try {
            String idiomaSeleccionado = comboIdioma.getValue().toString();
            System.out.println("Changing language to: " + idiomaSeleccionado);
            
            String path = "/es/diegofeblesseoane/pkgame/idioma-" + idiomaSeleccionado + ".properties";
            System.out.println("Loading properties file: " + path);
            
            ConfigManager.ConfigProperties.setPath(path);
            ConfigManager.ConfigProperties.setProperty("idiomaActual", idiomaSeleccionado);
            
            // Get properties with debugging
            String buttonLoginText = ConfigManager.ConfigProperties.getProperty("buttonIniciarSesion");
            String buttonPlayText = ConfigManager.ConfigProperties.getProperty("buttonIniciarSinRegistro");
            
            System.out.println("Retrieved properties:");
            System.out.println("  buttonIniciarSesion = " + buttonLoginText);
            System.out.println("  buttonIniciarSinRegistro = " + buttonPlayText);
            
            // Set button text with null checks
            if (buttonLoginText != null) {
                buttonIniciarSesion.setText(buttonLoginText);
            } else {
                System.out.println("WARNING: buttonIniciarSesion property is null");
            }
            
            if (buttonPlayText != null) {
                buttonIniciarSinRegistro.setText(buttonPlayText);
            } else {
                System.out.println("WARNING: buttonIniciarSinRegistro property is null");
            }
        } catch (Exception e) {
            System.out.println("ERROR in cambiarIdioma(): " + e.getMessage());
            e.printStackTrace();
        }
    }
}