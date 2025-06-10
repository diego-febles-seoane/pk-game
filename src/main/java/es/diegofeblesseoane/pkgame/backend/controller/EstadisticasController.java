package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EstadisticasController {

    @FXML
    private Label labelNombreUsuario;
    
    @FXML
    private Label labelTituloUsuario;
    
    @FXML
    private Label labelVictorias;
    
    @FXML
    private Label labelDerrotas;
    
    @FXML
    private Label labelTotalPartidas;
    
    @FXML
    private Label labelPorcentajeVictorias;
    
    @FXML
    private Label labelRachaActual;
    
    @FXML
    private Label labelMayorRacha;
    
    @FXML
    private Label labelNivel;
    
    @FXML
    private Label labelTitulo;
    
    @FXML
    private Label labelEmail;
    
    @FXML
    private Label labelObjetivo1;
    
    @FXML
    private Label labelObjetivo2;
    
    @FXML
    private Label labelObjetivo3;
    
    @FXML
    private BarChart<String, Number> chartEstadisticas;
    
    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private Button buttonVolverMenu;
    
    @FXML
    private Button buttonActualizar;

    @FXML
    public void initialize() {
        cargarEstadisticas();
        configurarGrafico();
    }
    
    /**
     * Carga las estadísticas del usuario en la interfaz
     */
    private void cargarEstadisticas() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        
        if (usuario != null) {
            // Información básica del usuario
            labelNombreUsuario.setText(usuario.getNombre());
            labelTituloUsuario.setText(usuario.getTitulo());
            labelNivel.setText(String.valueOf(usuario.getNivel()));
            labelTitulo.setText(usuario.getTitulo());
            labelEmail.setText(usuario.getEmail());
            
            // Estadísticas de combate
            labelVictorias.setText(String.valueOf(usuario.getVictoriasTotales()));
            labelDerrotas.setText(String.valueOf(usuario.getDerrotasTotales()));
            labelTotalPartidas.setText(String.valueOf(usuario.getTotalPartidas()));
            labelRachaActual.setText(String.valueOf(usuario.getRachaActual()));
            labelMayorRacha.setText(String.valueOf(usuario.getMayorRacha()));
            
            // Porcentaje de victorias
            if (usuario.getTotalPartidas() > 0) {
                labelPorcentajeVictorias.setText(String.format("%.1f%%", usuario.getPorcentajeVictorias()));
            } else {
                labelPorcentajeVictorias.setText("0.0%");
            }
            
            // Actualizar objetivos
            actualizarObjetivos(usuario);
            
            // Actualizar gráfico
            actualizarGrafico(usuario);
            
            System.out.println("📊 Estadísticas cargadas para: " + usuario.getNombre());
        } else {
            // Si no hay usuario, mostrar valores por defecto
            restablecerValoresDefecto();
            System.out.println("⚠️ No hay usuario en sesión para mostrar estadísticas");
        }
    }
    
    /**
     * Configura el gráfico de barras
     */
    private void configurarGrafico() {
        xAxis.setLabel("Tipo de Resultado");
        yAxis.setLabel("Cantidad");
        chartEstadisticas.setTitle("Resumen de Combates");
        chartEstadisticas.setLegendVisible(false);
    }
    
    /**
     * Actualiza el gráfico con los datos del usuario
     */
    private void actualizarGrafico(UsuarioEntity usuario) {
        chartEstadisticas.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("🏆 Victorias", usuario.getVictoriasTotales()));
        series.getData().add(new XYChart.Data<>("😵 Derrotas", usuario.getDerrotasTotales()));
        series.getData().add(new XYChart.Data<>("🔥 Mayor Racha", usuario.getMayorRacha()));
        
        chartEstadisticas.getData().add(series);
    }
    
    /**
     * Actualiza los objetivos basándose en las estadísticas del usuario
     */
    private void actualizarObjetivos(UsuarioEntity usuario) {
        // Objetivo 1: Ganar 10 combates
        String objetivo1;
        if (usuario.getVictoriasTotales() >= 10) {
            objetivo1 = "✅ Ganar 10 combates (Completado)";
        } else {
            objetivo1 = String.format("🎯 Ganar 10 combates (%d/10)", usuario.getVictoriasTotales());
        }
        labelObjetivo1.setText(objetivo1);
        
        // Objetivo 2: Conseguir racha de 5
        String objetivo2;
        if (usuario.getMayorRacha() >= 5) {
            objetivo2 = "✅ Conseguir racha de 5 (Completado)";
        } else {
            objetivo2 = String.format("🔥 Conseguir racha de 5 (Mejor: %d)", usuario.getMayorRacha());
        }
        labelObjetivo2.setText(objetivo2);
        
        // Objetivo 3: Mantener 70% victorias
        String objetivo3;
        if (usuario.getTotalPartidas() >= 5 && usuario.getPorcentajeVictorias() >= 70.0) {
            objetivo3 = "✅ Mantener 70% victorias (Completado)";
        } else if (usuario.getTotalPartidas() >= 5) {
            objetivo3 = String.format("📈 Mantener 70%% victorias (%.1f%%)", usuario.getPorcentajeVictorias());
        } else {
            objetivo3 = "📈 Mantener 70% victorias (Juega 5+ partidas)";
        }
        labelObjetivo3.setText(objetivo3);
    }
    
    /**
     * Restablece los valores por defecto cuando no hay usuario
     */
    private void restablecerValoresDefecto() {
        labelNombreUsuario.setText("Usuario Invitado");
        labelTituloUsuario.setText("Sin título");
        labelNivel.setText("1");
        labelTitulo.setText("Sin título");
        labelEmail.setText("No registrado");
        
        labelVictorias.setText("0");
        labelDerrotas.setText("0");
        labelTotalPartidas.setText("0");
        labelPorcentajeVictorias.setText("0.0%");
        labelRachaActual.setText("0");
        labelMayorRacha.setText("0");
        
        labelObjetivo1.setText("🎯 Ganar 10 combates (0/10)");
        labelObjetivo2.setText("🔥 Conseguir racha de 5 (Mejor: 0)");
        labelObjetivo3.setText("📈 Mantener 70% victorias (Juega 5+ partidas)");
        
        chartEstadisticas.getData().clear();
    }
    
    @FXML
    private void onVolverMenuClick() {
        try {
            Stage stage = (Stage) buttonVolverMenu.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Menu");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onActualizarClick() {
        cargarEstadisticas();
        System.out.println("🔄 Estadísticas actualizadas");
    }
}

