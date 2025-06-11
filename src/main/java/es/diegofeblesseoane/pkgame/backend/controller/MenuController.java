package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import es.diegofeblesseoane.pkgame.config.ConfigManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

    @FXML
    private Button buttonCombate;
    
    @FXML
    private Button buttonCerrarSesion;
    
    @FXML
    private Button buttonEstadisticas;
    
    @FXML
    private Label labelBienvenida;
    
    @FXML
    private Label labelEstadisticas;
    
    // Elementos de idioma
    @FXML
    private ComboBox<String> comboIdioma;
    
    @FXML
    private Label labelLanguage;
    
    // Elementos del menú para traducir
    @FXML
    private Label titleMain;
    
    @FXML
    private Label subtitleWelcome;
    
    @FXML
    private Label labelPerfil;
    
    @FXML
    private Label labelInicio;
    
    @FXML
    private Label labelJugar;
    
    @FXML
    private Label labelPokemon;
    
    @FXML
    private Label labelStats;
    
    @FXML
    private Label labelEventos;
    
    @FXML
    private Label labelConfig;

    @FXML
    public void onCombateButtonClick() {
        try {
            Stage stage = (Stage) buttonCombate.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("combate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Combate");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onEstadisticasButtonClick() {
        try {
            Stage stage = (Stage) buttonEstadisticas.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("estadisticas.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Estadísticas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onCerrarSesionClick() {
        UsuarioSesion.getInstancia().cerrarSesion();
        try {
            Stage stage = (Stage) buttonCerrarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("inicioSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Inicio de Sesión");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        configurarIdiomas();
        mostrarInformacionUsuario();
    }
    
    /**
     * Configura el ComboBox de idiomas y establece el idioma inicial
     */
    private void configurarIdiomas() {
        if (comboIdioma != null) {
            // Configurar opciones del ComboBox
            List<String> idiomas = new ArrayList<>();
            idiomas.add("es");
            idiomas.add("en");
            comboIdioma.getItems().addAll(idiomas);
            
            // Establecer idioma actual desde configuración
            String idiomaActual = ConfigManager.ConfigProperties.getProperty("idiomaActual", "es");
            comboIdioma.setValue(idiomaActual);
            
            // Añadir listener para cambios de idioma
            comboIdioma.setOnAction(event -> cambiarIdioma());
            
            // Aplicar idioma inicial
            cambiarIdioma();
        }
    }
    
    /**
     * Cambia el idioma de toda la interfaz
     */
    @FXML
    private void cambiarIdioma() {
        try {
            if (comboIdioma == null || comboIdioma.getValue() == null) {
                return;
            }
            
            String idiomaSeleccionado = comboIdioma.getValue();
            System.out.println("Cambiando idioma a: " + idiomaSeleccionado);
            
            // Cargar archivo de propiedades del idioma
            String path = "/es/diegofeblesseoane/pkgame/idioma-" + idiomaSeleccionado + ".properties";
            ConfigManager.ConfigProperties.setPath(path);
            ConfigManager.ConfigProperties.setProperty("idiomaActual", idiomaSeleccionado);
            
            // Actualizar textos de la interfaz
            actualizarTextosInterfaz();
            
        } catch (Exception e) {
            System.out.println("Error al cambiar idioma: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza todos los textos de la interfaz según el idioma seleccionado
     */
    private void actualizarTextosInterfaz() {
        // Selector de idioma
        if (labelLanguage != null) {
            labelLanguage.setText(ConfigManager.ConfigProperties.getProperty("label.language", "Idioma:"));
        }
        
        // Títulos principales
        if (titleMain != null) {
            titleMain.setText(ConfigManager.ConfigProperties.getProperty("title.main", "PK-GAME"));
        }
        
        if (subtitleWelcome != null) {
            subtitleWelcome.setText(ConfigManager.ConfigProperties.getProperty("subtitle.welcome", "Bienvenido al mundo Pokémon"));
        }
        
        // Elementos del menú lateral
        if (labelPerfil != null) {
            labelPerfil.setText(ConfigManager.ConfigProperties.getProperty("label.perfil", "Perfil"));
        }
        
        if (labelInicio != null) {
            labelInicio.setText(ConfigManager.ConfigProperties.getProperty("label.inicio", "Inicio"));
        }
        
        if (labelJugar != null) {
            labelJugar.setText(ConfigManager.ConfigProperties.getProperty("label.jugar", "Jugar"));
        }
        
        if (labelPokemon != null) {
            labelPokemon.setText(ConfigManager.ConfigProperties.getProperty("label.pokemon", "Pokémon"));
        }
        
        if (labelStats != null) {
            labelStats.setText(ConfigManager.ConfigProperties.getProperty("label.stats", "Stats"));
        }
        
        if (labelEventos != null) {
            labelEventos.setText(ConfigManager.ConfigProperties.getProperty("label.eventos", "Eventos"));
        }
        
        if (labelConfig != null) {
            labelConfig.setText(ConfigManager.ConfigProperties.getProperty("label.config", "Config"));
        }
        
        System.out.println("Textos de interfaz actualizados");
    }
    
    /**
     * Muestra la información del usuario logueado
     */
    private void mostrarInformacionUsuario() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        
        if (usuario != null) {
            // Mostrar mensaje de bienvenida
            if (labelBienvenida != null) {
                labelBienvenida.setText(String.format("👋 ¡Bienvenido %s! %s", 
                                                     usuario.getNombre(), usuario.getTitulo()));
            }
            
            // Mostrar estadísticas del usuario
            if (labelEstadisticas != null) {
                String estadisticas;
                if (usuario.getTotalPartidas() > 0) {
                    estadisticas = String.format(
                        "📊 Estadísticas de Combate:\n" +
                        "🏆 Victorias: %d\n" +
                        "😵 Derrotas: %d\n" +
                        "📊 Total: %d partidas\n" +
                        "📈 Tasa de victoria: %.1f%%\n" +
                        "🔥 Racha actual: %d\n" +
                        "⭐ Mayor racha: %d",
                        usuario.getVictoriasTotales(),
                        usuario.getDerrotasTotales(),
                        usuario.getTotalPartidas(),
                        usuario.getPorcentajeVictorias(),
                        usuario.getRachaActual(),
                        usuario.getMayorRacha()
                    );
                } else {
                    estadisticas = "🌟 ¡Aún no has jugado ninguna partida!\n🎮 ¡Ve al combate para empezar tu aventura Pokémon!";
                }
                labelEstadisticas.setText(estadisticas);
            }
            
            System.out.println("👤 Usuario en menú: " + usuario.toString());
        } else {
            // Si no hay usuario logueado, redirigir al login
            System.out.println("⚠️ No hay usuario en sesión, redirigiendo al login");
            try {
                Stage stage = (Stage) buttonCombate.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("inicioSesion.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
                stage.setTitle("PK-GAME - Inicio de Sesión");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

