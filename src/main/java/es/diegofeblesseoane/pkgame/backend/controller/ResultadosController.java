package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEvolucion;
import es.diegofeblesseoane.pkgame.backend.model.TipoPokemon;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class ResultadosController {

    @FXML
    private Label labelTitulo;
    
    @FXML
    private Label labelResultadoPrincipal;
    
    @FXML
    private Label labelMarcador;
    
    @FXML
    private ImageView imagenPokemonJugador;
    
    @FXML
    private ImageView imagenPokemonIA;
    
    @FXML
    private Label labelNombrePokemonJugador;
    
    @FXML
    private Label labelNombrePokemonIA;
    
    @FXML
    private Label labelNivelPokemonJugador;
    
    @FXML
    private Label labelNivelPokemonIA;
    
    @FXML
    private Label labelEvolucionesPokemonJugador;
    
    @FXML
    private Label labelEvolucionesPokemonIA;
    
    @FXML
    private Label labelRonda1;
    
    @FXML
    private Label labelRonda2;
    
    @FXML
    private Label labelRonda3;
    
    @FXML
    private Label labelEstadisticasUsuario;
    
    @FXML
    private Button buttonNuevoCombate;
    
    @FXML
    private Button buttonVerEstadisticas;
    
    @FXML
    private Button buttonVolverMenu;
    
    // Datos del combate
    private int victoriasJugador;
    private int victoriasIA;
    private PokemonEvolucion pokemonFinalJugador;
    private PokemonEvolucion pokemonFinalIA;
    private List<String> resultadosRondas;
    
    @FXML
    public void initialize() {
        // La inicialización se hará cuando se establezcan los datos del combate
    }
    
    /**
     * Establece los datos del combate terminado
     */
    public void setDatosCombate(int victoriasJugador, int victoriasIA, 
                               PokemonEvolucion pokemonFinalJugador, PokemonEvolucion pokemonFinalIA,
                               List<String> resultadosRondas) {
        this.victoriasJugador = victoriasJugador;
        this.victoriasIA = victoriasIA;
        this.pokemonFinalJugador = pokemonFinalJugador;
        this.pokemonFinalIA = pokemonFinalIA;
        this.resultadosRondas = resultadosRondas;
        
        actualizarInterfaz();
    }
    
    /**
     * Actualiza toda la interfaz con los datos del combate
     */
    private void actualizarInterfaz() {
        // Resultado principal
        if (victoriasJugador > victoriasIA) {
            labelResultadoPrincipal.setText("🏆 ¡VICTORIA! 🏆");
            labelResultadoPrincipal.setStyle("-fx-text-fill: #4CAF50;"); // Verde
        } else if (victoriasIA > victoriasJugador) {
            labelResultadoPrincipal.setText("😞 ¡DERROTA! 😞");
            labelResultadoPrincipal.setStyle("-fx-text-fill: #f44336;"); // Rojo
        } else {
            labelResultadoPrincipal.setText("🤝 ¡EMPATE! 🤝");
            labelResultadoPrincipal.setStyle("-fx-text-fill: #FF9800;"); // Naranja
        }
        
        // Marcador
        labelMarcador.setText(String.format("Marcador Final: %d - %d", victoriasJugador, victoriasIA));
        
        // Pokémon del jugador
        if (pokemonFinalJugador != null) {
            labelNombrePokemonJugador.setText(pokemonFinalJugador.getTipo().getEmoji() + " " + pokemonFinalJugador.getNombre());
            labelNivelPokemonJugador.setText(pokemonFinalJugador.getEmoji() + " Nivel " + pokemonFinalJugador.getNivel() + " - " + pokemonFinalJugador.getNombreNivel());
            labelEvolucionesPokemonJugador.setText("Evoluciones conseguidas: " + (pokemonFinalJugador.getNivel() - 1));
            
            // Cargar imagen
            try {
                Image imagen = new Image(getClass().getResourceAsStream(pokemonFinalJugador.getRutaImagen()));
                imagenPokemonJugador.setImage(imagen);
            } catch (Exception e) {
                System.err.println("⚠️ Error al cargar imagen del Pokémon jugador: " + e.getMessage());
            }
        }
        
        // Pokémon de la IA
        if (pokemonFinalIA != null) {
            labelNombrePokemonIA.setText(pokemonFinalIA.getTipo().getEmoji() + " " + pokemonFinalIA.getNombre());
            labelNivelPokemonIA.setText(pokemonFinalIA.getEmoji() + " Nivel " + pokemonFinalIA.getNivel() + " - " + pokemonFinalIA.getNombreNivel());
            labelEvolucionesPokemonIA.setText("Evoluciones conseguidas: " + (pokemonFinalIA.getNivel() - 1));
            
            // Cargar imagen
            try {
                Image imagen = new Image(getClass().getResourceAsStream(pokemonFinalIA.getRutaImagen()));
                imagenPokemonIA.setImage(imagen);
            } catch (Exception e) {
                System.err.println("⚠️ Error al cargar imagen del Pokémon IA: " + e.getMessage());
            }
        }
        
        // Resultados de rondas
        Label[] labelsRondas = {labelRonda1, labelRonda2, labelRonda3};
        for (int i = 0; i < labelsRondas.length && i < resultadosRondas.size(); i++) {
            labelsRondas[i].setText(resultadosRondas.get(i));
        }
        
        // Estadísticas del usuario
        actualizarEstadisticasUsuario();
    }
    
    /**
     * Actualiza las estadísticas del usuario
     */
    private void actualizarEstadisticasUsuario() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        if (usuario != null) {
            String estadisticas = String.format(
                "📈 Estadísticas Actualizadas:\n" +
                "🏆 Total Victorias: %d | 😵 Total Derrotas: %d\n" +
                "🔥 Racha Actual: %d | ⭐ Mayor Racha: %d | 📈 Tasa Victoria: %.1f%%",
                usuario.getVictoriasTotales(),
                usuario.getDerrotasTotales(),
                usuario.getRachaActual(),
                usuario.getMayorRacha(),
                usuario.getPorcentajeVictorias()
            );
            labelEstadisticasUsuario.setText(estadisticas);
        } else {
            labelEstadisticasUsuario.setText("📈 Sin usuario registrado - Las estadísticas no se guardan");
        }
    }
    
    @FXML
    public void onNuevoCombateClick() {
        try {
            Stage stage = (Stage) buttonNuevoCombate.getScene().getWindow();
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
    public void onVerEstadisticasClick() {
        try {
            Stage stage = (Stage) buttonVerEstadisticas.getScene().getWindow();
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
    public void onVolverMenuClick() {
        try {
            Stage stage = (Stage) buttonVolverMenu.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Menú Principal");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

