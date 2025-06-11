package es.diegofeblesseoane.pkgame.backend.controller;

import java.util.ArrayList;
import java.util.List;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.config.ConfigManager;
import es.diegofeblesseoane.pkgame.config.GlobalLanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    public Button buttonIniciarSesion;

    @FXML
    public Button buttonIniciarSinRegistro;

    @FXML
    private ComboBox<String> comboIdioma;
    
    @FXML
    private Label labelIdioma;

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
        configurarComboIdiomas();
        inicializarIdiomaGlobal();
    }
    
    /**
     * Configura el ComboBox de idiomas
     */
    private void configurarComboIdiomas() {
        // Configurar opciones de idioma
        List<String> idiomas = new ArrayList<>();
        idiomas.add("es");
        idiomas.add("en");
        idiomas.add("fr");
        comboIdioma.getItems().addAll(idiomas);

        // Establecer idioma por defecto
        String idiomaActual = GlobalLanguageManager.getCurrentLanguage();
        comboIdioma.setValue(idiomaActual);

        // Añadir listener para cambios de idioma
        comboIdioma.setOnAction(event -> cambiarIdioma());
        
        System.out.println("🎛️ ComboBox de idiomas configurado. Idioma actual: " + idiomaActual);
    }
    
    /**
     * Inicializa el sistema de idioma global y actualiza la interfaz
     */
    private void inicializarIdiomaGlobal() {
        try {
            // Inicializar el sistema de idioma global
            String idiomaInicial = comboIdioma.getValue();
            GlobalLanguageManager.setGlobalLanguage(idiomaInicial);
            
            // Actualizar interfaz con el idioma inicial
            actualizarInterfazLogin();
            
            System.out.println("🚀 Sistema de idioma global inicializado con: " + idiomaInicial);
            
        } catch (Exception e) {
            System.out.println("❌ Error inicializando idioma global: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cambia el idioma de toda la aplicación según el seleccionado en el ComboBox.
     * Este método establece el idioma globalmente para toda la aplicación.
     */
    @FXML
    protected void cambiarIdioma() {
        try {
            if (comboIdioma.getValue() == null) {
                return;
            }
            
            String idiomaSeleccionado = comboIdioma.getValue().toString();
            System.out.println("🌍 Cambiando idioma global a: " + idiomaSeleccionado);
            
            // Establecer idioma globalmente - esto afectará a toda la aplicación
            GlobalLanguageManager.setGlobalLanguage(idiomaSeleccionado);
            
            // Actualizar la interfaz actual
            actualizarInterfazLogin();
            
            System.out.println("✅ Idioma global actualizado exitosamente");
            
        } catch (Exception e) {
            System.out.println("❌ ERROR al cambiar idioma: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza los textos de la interfaz de login según el idioma actual
     */
    private void actualizarInterfazLogin() {
        try {
            // Obtener textos traducidos
            String buttonLoginText = ConfigManager.ConfigProperties.getProperty("buttonIniciarSesion");
            String buttonPlayText = ConfigManager.ConfigProperties.getProperty("buttonIniciarSinRegistro");
            String labelIdiomaText = ConfigManager.ConfigProperties.getProperty("label.language");
            
            System.out.println("📝 Actualizando textos de login:");
            System.out.println("  buttonIniciarSesion = " + buttonLoginText);
            System.out.println("  buttonIniciarSinRegistro = " + buttonPlayText);
            System.out.println("  labelIdioma = " + labelIdiomaText);
            
            // Actualizar textos de botones con verificación
            if (buttonLoginText != null && buttonIniciarSesion != null) {
                buttonIniciarSesion.setText(buttonLoginText);
            }
            
            if (buttonPlayText != null && buttonIniciarSinRegistro != null) {
                buttonIniciarSinRegistro.setText(buttonPlayText);
            }
            
            // Actualizar label de idioma
            if (labelIdiomaText != null && labelIdioma != null) {
                labelIdioma.setText(labelIdiomaText);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error actualizando interfaz de login: " + e.getMessage());
        }
    }
}