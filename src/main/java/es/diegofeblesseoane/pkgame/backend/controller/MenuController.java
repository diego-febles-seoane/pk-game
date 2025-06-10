package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
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
        mostrarInformacionUsuario();
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

