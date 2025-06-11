package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import es.diegofeblesseoane.pkgame.config.ConfigManager;
import es.diegofeblesseoane.pkgame.config.GlobalLanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
        // Asegurar que el sistema global de idiomas esté inicializado
        GlobalLanguageManager.initialize();
        
        cargarIdiomaActual();
        mostrarInformacionUsuario();
    }
    
    /**
     * Carga el idioma actual configurado globalmente
     */
    private void cargarIdiomaActual() {
        try {
            // Obtener el idioma actual desde el gestor global
            String idiomaActual = GlobalLanguageManager.getCurrentLanguage();
            System.out.println("🌍 Cargando idioma actual en menú: " + idiomaActual);
            
            // El GlobalLanguageManager ya se encarga de cargar las propiedades
            // Solo necesitamos actualizar la interfaz
            actualizarTextosInterfaz();
            
            System.out.println("✅ Idioma cargado exitosamente en menú");
            
        } catch (Exception e) {
            System.out.println("❌ Error al cargar idioma actual: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza todos los textos de la interfaz según el idioma seleccionado
     */
    private void actualizarTextosInterfaz() {
        // Títulos principales
        if (titleMain != null) {
            titleMain.setText(GlobalLanguageManager.getText("title.main", "PK-GAME"));
        }
        
        if (subtitleWelcome != null) {
            subtitleWelcome.setText(GlobalLanguageManager.getText("subtitle.welcome", "Bienvenido al mundo Pokémon"));
        }
        
        // Elementos del menú lateral
        if (labelPerfil != null) {
            labelPerfil.setText(GlobalLanguageManager.getText("label.perfil", "Perfil"));
        }
        
        if (labelInicio != null) {
            labelInicio.setText(GlobalLanguageManager.getText("label.inicio", "Inicio"));
        }
        
        if (labelJugar != null) {
            labelJugar.setText(GlobalLanguageManager.getText("label.jugar", "Jugar"));
        }
        
        if (labelPokemon != null) {
            labelPokemon.setText(GlobalLanguageManager.getText("label.pokemon", "Pokémon"));
        }
        
        if (labelStats != null) {
            labelStats.setText(GlobalLanguageManager.getText("label.stats", "Stats"));
        }
        
        if (labelEventos != null) {
            labelEventos.setText(GlobalLanguageManager.getText("label.eventos", "Eventos"));
        }
        
        if (labelConfig != null) {
            labelConfig.setText(GlobalLanguageManager.getText("label.config", "Config"));
        }
        
        System.out.println("📋 Textos de interfaz del menú actualizados");
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

