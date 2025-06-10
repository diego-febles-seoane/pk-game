package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.model.TipoPokemon;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEvolucion;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioServiceModel;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEntity;
import es.diegofeblesseoane.pkgame.backend.model.MovimientoEntity;
import es.diegofeblesseoane.pkgame.backend.service.CombateService;
import es.diegofeblesseoane.pkgame.backend.service.CalculadoraDano;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombateController {

    @FXML
    private Button buttonFuego;
    
    @FXML
    private Button buttonAgua;
    
    @FXML
    private Button buttonPlanta;
    
    @FXML
    private Button buttonVolverMenu;
    
    @FXML
    private Button buttonNuevoCombate;
    
    // Botones de ataques
    @FXML
    private Button buttonAtaque1;
    
    @FXML
    private Button buttonAtaque2;
    
    @FXML
    private Button buttonAtaque3;
    
    @FXML
    private Button buttonAtaque4;
    
    @FXML
    private Label labelResultado;
    
    @FXML
    private Label labelDetalles;
    
    @FXML
    private ImageView imagenJugador;
    
    @FXML
    private ImageView imagenIA;
    
    @FXML
    private Label labelJugador;
    
    @FXML
    private Label labelIA;
    
    // Barras de vida
    @FXML
    private Label labelVidaJugador;
    
    @FXML
    private Label labelVidaIA;
    
    // Sistema de combate con rondas y evoluciones
    private UsuarioServiceModel usuarioService;
    private Random random = new Random();
    
    // Variables del combate por rondas
    private int rondaActual = 0;
    private int victoriasJugador = 0;
    private int victoriasIA = 0;
    private List<String> resultadosRondas = new ArrayList<>();
    private boolean combateActivo = false;
    private boolean pokemonSeleccionado = false; // Nueva variable de control
    
    // Pokémon actuales
    private PokemonEvolucion pokemonJugador;
    private PokemonEvolucion pokemonIA;
    private TipoPokemon tipoPokemonJugador;  // Pokémon elegido al inicio
    private TipoPokemon tipoPokemonIA;       // Pokémon de la IA
    private TipoPokemon tipoRondaJugador;    // Tipo elegido en cada ronda
    private TipoPokemon tipoRondaIA;         // Tipo de la IA en cada ronda
    
    // Variables para el combate real
    private boolean combateRealActivo = false;
    private PokemonEntity pokemonCombateJugador;
    private PokemonEntity pokemonCombateIA;
    private CombateService combateService;
    private boolean turnoJugador = true;
    
    @FXML
    public void initialize() {
        usuarioService = new UsuarioServiceModel();
        try {
            combateService = new CombateService();
        } catch (Exception e) {
            System.err.println("Error al inicializar CombateService: " + e.getMessage());
        }
        resetearInterfaz();
        mostrarInformacionUsuario();
    }
    
    /**
     * Muestra la información del usuario logueado
     */
    private void mostrarInformacionUsuario() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        if (usuario != null) {
            // Actualizar las estadísticas globales con las del usuario
            victoriasJugador = usuario.getVictoriasTotales();
            victoriasIA = usuario.getDerrotasTotales();
            
            System.out.println("👤 Usuario en combate: " + usuario.getNombre() + " " + usuario.getTitulo());
        }
    }
    
    @FXML
    public void onFuegoClick() {
        if (!pokemonSeleccionado) {
            // FASE 1: Selección de Pokémon inicial
            seleccionarPokemon(TipoPokemon.FUEGO);
        } else if (combateActivo) {
            // FASE 2: Elegir tipo para esta ronda
            elegirTipoRonda(TipoPokemon.FUEGO);
        }
    }
    
    @FXML
    public void onAguaClick() {
        if (!pokemonSeleccionado) {
            // FASE 1: Selección de Pokémon inicial
            seleccionarPokemon(TipoPokemon.AGUA);
        } else if (combateActivo) {
            // FASE 2: Elegir tipo para esta ronda
            elegirTipoRonda(TipoPokemon.AGUA);
        }
    }
    
    @FXML
    public void onPlantaClick() {
        if (!pokemonSeleccionado) {
            // FASE 1: Selección de Pokémon inicial
            seleccionarPokemon(TipoPokemon.PLANTA);
        } else if (combateActivo) {
            // FASE 2: Elegir tipo para esta ronda
            elegirTipoRonda(TipoPokemon.PLANTA);
        }
    }
    
    @FXML
    public void onAtaque1Click() {
        if (combateRealActivo) {
            ejecutarAtaqueReal(0);
        }
    }
    
    @FXML
    public void onAtaque2Click() {
        if (combateRealActivo) {
            ejecutarAtaqueReal(1);
        }
    }
    
    @FXML
    public void onAtaque3Click() {
        if (combateRealActivo) {
            ejecutarAtaqueReal(2);
        }
    }
    
    @FXML
    public void onAtaque4Click() {
        if (combateRealActivo) {
            ejecutarAtaqueReal(3);
        }
    }
    
    @FXML
    public void onVolverMenuClick() {
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
    public void onNuevoCombateClick() {
        resetearInterfaz();
    }
    
    /**
     * FASE 1: Seleccionar Pokémon inicial (solo una vez)
     */
    private void seleccionarPokemon(TipoPokemon tipoElegido) {
        tipoPokemonJugador = tipoElegido;
        tipoPokemonIA = obtenerTipoAleatorioIA();
        
        // Inicializar Pokémon en nivel 1
        pokemonJugador = PokemonEvolucion.getEvolucionInicial(tipoPokemonJugador);
        pokemonIA = PokemonEvolucion.getEvolucionInicial(tipoPokemonIA);
        
        pokemonSeleccionado = true;
        
        // Mostrar mensaje de selección
        String mensaje = String.format("🎯 POKÉMON SELECCIONADOS\n\n" +
                                     "Tu Pokémon: %s\n" +
                                     "Pokémon Rival: %s\n\n" +
                                     "⚡ Ahora comienzan las 3 rondas de combate\n" +
                                     "En cada ronda, elige Fuego/Agua/Planta para atacar\n\n" +
                                     "🎯 Preparándote para la Ronda 1...",
                                     pokemonJugador.getInfoCompleta(),
                                     pokemonIA.getInfoCompleta());
        
        labelResultado.setText(mensaje);
        
        // Configurar interfaz
        configurarInterfazSeleccionCompleta();
        
        // Iniciar primera ronda después de 3 segundos
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> iniciarPrimeraRonda());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * FASE 2: Iniciar las rondas de combate (después de seleccionar Pokémon)
     */
    private void iniciarPrimeraRonda() {
        // Reiniciar contadores de rondas
        rondaActual = 1;
        victoriasJugador = 0;
        victoriasIA = 0;
        resultadosRondas.clear();
        combateActivo = true;
        
        // Mostrar mensaje para primera ronda
        mostrarMensajeRonda();
    }
    
    /**
     * FASE 3: Elegir tipo para la ronda actual
     */
    private void elegirTipoRonda(TipoPokemon tipoElegido) {
        if (!combateActivo || rondaActual > 3) {
            return;
        }
        
        tipoRondaJugador = tipoElegido;
        tipoRondaIA = obtenerTipoAleatorioIA();
        
        // Ejecutar la ronda
        ejecutarRondaIndividual();
    }
    
    /**
     * Muestra el mensaje para que el jugador elija tipo en la ronda
     */
    private void mostrarMensajeRonda() {
        if (rondaActual > 3) {
            finalizarCombateRondas();
            return;
        }
        
        String mensaje = String.format("⚔️ RONDA %d/3\n\n" +
                                     "%s VS %s\n\n" +
                                     "💡 Elige tu tipo de ataque:\n" +
                                     "🔥 Fuego | 💧 Agua | 🌿 Planta\n\n" +
                                     "🎯 Esperando tu elección...",
                                     rondaActual,
                                     pokemonJugador.getInfoCompleta(),
                                     pokemonIA.getInfoCompleta());
        
        labelResultado.setText(mensaje);
    }
    
    /**
     * Configura la interfaz después de seleccionar Pokémon
     */
    private void configurarInterfazSeleccionCompleta() {
        // Mostrar información de Pokémon
        labelJugador.setText(pokemonJugador.getInfoCompleta());
        labelIA.setText(pokemonIA.getInfoCompleta());
        labelJugador.setVisible(true);
        labelIA.setVisible(true);
        
        // Configurar imágenes
        configurarImagenesEvolucion();
        
        // Mantener botones visibles para las rondas
        habilitarBotonesSeleccion(true);
        
        // Ocultar elementos no necesarios
        labelVidaJugador.setVisible(false);
        labelVidaIA.setVisible(false);
        Button[] botonesAtaque = {buttonAtaque1, buttonAtaque2, buttonAtaque3, buttonAtaque4};
        for (Button boton : botonesAtaque) {
            boton.setVisible(false);
        }
    }
    
    /**
     * Obtiene un tipo aleatorio para la IA
     */
    private TipoPokemon obtenerTipoAleatorioIA() {
        TipoPokemon[] tipos = {TipoPokemon.FUEGO, TipoPokemon.AGUA, TipoPokemon.PLANTA};
        return tipos[random.nextInt(tipos.length)];
    }
    
    /**
     * Configura la interfaz para el modo de rondas
     */
    private void configurarInterfazRondas() {
        // Ocultar botones de selección
        habilitarBotonesSeleccion(false);
        
        // Mostrar información de Pokémon
        labelJugador.setText(pokemonJugador.getInfoCompleta());
        labelIA.setText(pokemonIA.getInfoCompleta());
        labelJugador.setVisible(true);
        labelIA.setVisible(true);
        
        // Configurar imágenes
        configurarImagenesEvolucion();
        
        // Ocultar barras de vida (no las necesitamos en este modo)
        labelVidaJugador.setVisible(false);
        labelVidaIA.setVisible(false);
        
        // Ocultar botones de ataque (usamos piedra-papel-tijera)
        Button[] botonesAtaque = {buttonAtaque1, buttonAtaque2, buttonAtaque3, buttonAtaque4};
        for (Button boton : botonesAtaque) {
            boton.setVisible(false);
        }
    }
    
    /**
     * Configura las imágenes según la evolución actual
     */
    private void configurarImagenesEvolucion() {
        try {
            // Imagen del jugador
            Image imagenPokemonJugador = new Image(getClass().getResourceAsStream(pokemonJugador.getRutaImagen()));
            imagenJugador.setImage(imagenPokemonJugador);
            imagenJugador.setVisible(true);
            
            // Imagen de la IA
            Image imagenPokemonIA = new Image(getClass().getResourceAsStream(pokemonIA.getRutaImagen()));
            imagenIA.setImage(imagenPokemonIA);
            imagenIA.setVisible(true);
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar imágenes de evolución: " + e.getMessage());
        }
    }
    
    /**
     * Ejecuta una ronda individual después de que el jugador elija tipo
     */
    private void ejecutarRondaIndividual() {
        if (rondaActual > 3) {
            finalizarCombateRondas();
            return;
        }
        
        // Determinar ganador de la ronda
        String resultadoRonda = determinarGanadorRondaIndividual();
        
        // Mostrar resultado
        String mensaje = String.format("⚔️ RONDA %d/3 - RESULTADO\n\n" +
                                      "%s VS %s\n\n" +
                                      "Tu ataque: %s | Ataque rival: %s\n\n" +
                                      "%s\n\n" +
                                      "Puntuación: Tú %d - %d Rival",
                                      rondaActual,
                                      pokemonJugador.getInfoCompleta(),
                                      pokemonIA.getInfoCompleta(),
                                      tipoRondaJugador.getEmoji() + " " + tipoRondaJugador.getNombre(),
                                      tipoRondaIA.getEmoji() + " " + tipoRondaIA.getNombre(),
                                      resultadoRonda,
                                      victoriasJugador, victoriasIA);
        
        labelResultado.setText(mensaje);
        
        // Actualizar información de Pokémon después de posible evolución
        labelJugador.setText(pokemonJugador.getInfoCompleta());
        labelIA.setText(pokemonIA.getInfoCompleta());
        configurarImagenesEvolucion();
        
        // Continuar a la siguiente ronda o finalizar
        rondaActual++;
        
        new Thread(() -> {
            try {
                Thread.sleep(4000); // Esperar 4 segundos para ver resultado
                javafx.application.Platform.runLater(() -> {
                    if (rondaActual <= 3) {
                        mostrarMensajeRonda(); // Mostrar mensaje para siguiente ronda
                    } else {
                        iniciarCombateReal(); // Iniciar combate real después de las 3 rondas
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Determina el ganador de una ronda individual basado en tipos elegidos
     */
    private String determinarGanadorRondaIndividual() {
        boolean jugadorGana = false;
        String resultado;
        
        // Lógica de tipos: Fuego > Planta > Agua > Fuego
        if (tipoRondaJugador == tipoRondaIA) {
            resultado = "🤝 ¡EMPATE! Ningún Pokémon evoluciona esta ronda";
        } else if ((tipoRondaJugador == TipoPokemon.FUEGO && tipoRondaIA == TipoPokemon.PLANTA) ||
                   (tipoRondaJugador == TipoPokemon.AGUA && tipoRondaIA == TipoPokemon.FUEGO) ||
                   (tipoRondaJugador == TipoPokemon.PLANTA && tipoRondaIA == TipoPokemon.AGUA)) {
            jugadorGana = true;
            victoriasJugador++;
            
            // Evolucionar Pokémon del jugador
            if (pokemonJugador.puedeEvolucionar()) {
                PokemonEvolucion evolucionAnterior = pokemonJugador;
                pokemonJugador = pokemonJugador.getSiguienteEvolucion();
                resultado = String.format("🎉 ¡GANASTE LA RONDA!\n✨ %s evoluciona a %s",
                                         evolucionAnterior.getNombre(), pokemonJugador.getNombre());
            } else {
                resultado = String.format("🎉 ¡GANASTE LA RONDA!\n💎 %s ya está en máxima evolución",
                                         pokemonJugador.getNombre());
            }
        } else {
            victoriasIA++;
            
            // Evolucionar Pokémon de la IA
            if (pokemonIA.puedeEvolucionar()) {
                PokemonEvolucion evolucionAnterior = pokemonIA;
                pokemonIA = pokemonIA.getSiguienteEvolucion();
                resultado = String.format("😞 Rival gana la ronda\n✨ %s evoluciona a %s",
                                         evolucionAnterior.getNombre(), pokemonIA.getNombre());
            } else {
                resultado = String.format("😞 Rival gana la ronda\n💎 %s ya está en máxima evolución",
                                         pokemonIA.getNombre());
            }
        }
        
        // Guardar resultado de la ronda
        String iconoResultado = jugadorGana ? "🏆" : (tipoRondaJugador == tipoRondaIA ? "🤝" : "😞");
        resultadosRondas.add(String.format("%s Ronda %d: %s vs %s", 
                                         iconoResultado, rondaActual,
                                         tipoRondaJugador.getEmoji(), tipoRondaIA.getEmoji()));
        
        return resultado;
    }
    
    /**
     * FASE 4: Iniciar combate real con los Pokémon evolucionados
     */
    private void iniciarCombateReal() {
        combateActivo = false;
        combateRealActivo = true;
        
        try {
            // Convertir PokemonEvolucion a PokemonEntity para el combate real
            pokemonCombateJugador = combateService.obtenerPokemonPorTipo(pokemonJugador.getTipo());
            pokemonCombateIA = combateService.obtenerPokemonPorTipo(pokemonIA.getTipo());
            
            if (pokemonCombateJugador == null || pokemonCombateIA == null) {
                System.err.println("Error: No se pudieron cargar los Pokémon para el combate real");
                finalizarCombateRondas();
                return;
            }
            
            // Ajustar estadísticas según el nivel de evolución alcanzado
            ajustarEstadisticasPorEvolucion(pokemonCombateJugador, pokemonJugador.getNivel());
            ajustarEstadisticasPorEvolucion(pokemonCombateIA, pokemonIA.getNivel());
            
            // Determinar quién ataca primero basado en velocidad
            turnoJugador = pokemonCombateJugador.getEstadisticas().getVelocidad() >= 
                          pokemonCombateIA.getEstadisticas().getVelocidad();
            
            // Configurar interfaz para combate real
            configurarInterfazCombateReal();
            
            String mensaje = String.format("⚔️ COMBATE REAL INICIADO ⚔️\n\n" +
                                         "🏆 Resultados de las rondas: %d - %d\n\n" +
                                         "%s (Nv.%d) VS %s (Nv.%d)\n\n" +
                                         "💡 Ahora es un combate real con ataques, vida y defensa\n" +
                                         "%s",
                                         victoriasJugador, victoriasIA,
                                         pokemonJugador.getNombre(), pokemonJugador.getNivel(),
                                         pokemonIA.getNombre(), pokemonIA.getNivel(),
                                         turnoJugador ? "¡Es tu turno! Elige un ataque" : "El rival atacará primero");
            
            labelResultado.setText(mensaje);
            
            // Si es turno de la IA, que ataque primero
            if (!turnoJugador) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> ejecutarTurnoIA());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            
        } catch (Exception e) {
            System.err.println("Error al iniciar combate real: " + e.getMessage());
            e.printStackTrace();
            finalizarCombateRondas();
        }
    }
    
    /**
     * Ajusta las estadísticas del Pokémon según su nivel de evolución
     */
    private void ajustarEstadisticasPorEvolucion(PokemonEntity pokemon, int nivelEvolucion) {
        // Multiplicadores basados en el nivel de evolución
        double multiplicador = 1.0 + (nivelEvolucion - 1) * 0.3; // +30% por nivel
        
        pokemon.getEstadisticas().setVida((int)(pokemon.getEstadisticas().getVida() * multiplicador));
        pokemon.getEstadisticas().setAtaque((int)(pokemon.getEstadisticas().getAtaque() * multiplicador));
        pokemon.getEstadisticas().setDefensa((int)(pokemon.getEstadisticas().getDefensa() * multiplicador));
        pokemon.getEstadisticas().setVelocidad((int)(pokemon.getEstadisticas().getVelocidad() * multiplicador));
        
        // Restaurar vida completa
        pokemon.restaurarVida();
    }
    
    /**
     * Configura la interfaz para el combate real
     */
    private void configurarInterfazCombateReal() {
        // Ocultar botones de selección de tipo
        habilitarBotonesSeleccion(false);
        
        // Mostrar información actualizada de los Pokémon
        labelJugador.setText(String.format("%s (Nv.%d)\n%s", 
                                         pokemonJugador.getNombre(), 
                                         pokemonJugador.getNivel(),
                                         pokemonJugador.getTipo().getEmoji()));
        labelIA.setText(String.format("%s (Nv.%d)\n%s", 
                                     pokemonIA.getNombre(), 
                                     pokemonIA.getNivel(),
                                     pokemonIA.getTipo().getEmoji()));
        
        // Mostrar barras de vida
        actualizarBarrasVida();
        labelVidaJugador.setVisible(true);
        labelVidaIA.setVisible(true);
        
        // Mostrar botones de ataque
        configurarBotonesAtaque();
        
        // Mantener imágenes visibles
        configurarImagenesEvolucion();
    }
    
    /**
     * Actualiza las barras de vida
     */
    private void actualizarBarrasVida() {
        if (pokemonCombateJugador != null) {
            labelVidaJugador.setText(String.format("💚 %d/%d HP (%.1f%%)", 
                                                 pokemonCombateJugador.getVidaActual(),
                                                 pokemonCombateJugador.getEstadisticas().getVida(),
                                                 pokemonCombateJugador.getPorcentajeVida()));
        }
        
        if (pokemonCombateIA != null) {
            labelVidaIA.setText(String.format("💚 %d/%d HP (%.1f%%)", 
                                            pokemonCombateIA.getVidaActual(),
                                            pokemonCombateIA.getEstadisticas().getVida(),
                                            pokemonCombateIA.getPorcentajeVida()));
        }
    }
    
    /**
     * Configura los botones de ataque con los movimientos del Pokémon
     */
    private void configurarBotonesAtaque() {
        Button[] botonesAtaque = {buttonAtaque1, buttonAtaque2, buttonAtaque3, buttonAtaque4};
        
        if (pokemonCombateJugador != null && pokemonCombateJugador.getAtaques() != null) {
            List<MovimientoEntity> ataques = pokemonCombateJugador.getAtaques();
            
            for (int i = 0; i < botonesAtaque.length; i++) {
                if (i < ataques.size()) {
                    MovimientoEntity ataque = ataques.get(i);
                    botonesAtaque[i].setText(String.format("%s\n%s - %d%% Precisión", 
                                                         ataque.getNombre(),
                                                         ataque.getTipo(),
                                                         ataque.getPrecision()));
                    botonesAtaque[i].setVisible(true);
                    botonesAtaque[i].setDisable(!turnoJugador);
                } else {
                    botonesAtaque[i].setVisible(false);
                }
            }
        }
    }
    
    /**
     * Ejecuta un ataque real del jugador
     */
    private void ejecutarAtaqueReal(int indiceAtaque) {
        if (!combateRealActivo || !turnoJugador || 
            pokemonCombateJugador == null || pokemonCombateIA == null) {
            return;
        }
        
        if (indiceAtaque < 0 || indiceAtaque >= pokemonCombateJugador.getAtaques().size()) {
            return;
        }
        
        MovimientoEntity ataque = pokemonCombateJugador.getAtaques().get(indiceAtaque);
        
        // Ejecutar ataque
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(
            pokemonCombateJugador, pokemonCombateIA, ataque);
        
        // Aplicar daño
        pokemonCombateIA.recibirDaño(resultado.getDano());
        
        // Crear mensaje del ataque
        String mensaje = String.format("⚔️ TURNO DEL JUGADOR\n\n%s\n\n💥 %s recibió %d puntos de daño!",
                                      resultado.getMensaje(),
                                      pokemonCombateIA.getNombre(),
                                      resultado.getDano());
        
        // Verificar si el combate terminó
        if (pokemonCombateIA.estaDebilitado()) {
            mensaje += String.format("\n\n💀 ¡%s se debilitó!\n🎉 ¡HAS GANADO EL COMBATE! 🎉", 
                                    pokemonCombateIA.getNombre());
            labelResultado.setText(mensaje);
            actualizarBarrasVida();
            finalizarCombateReal(true);
            return;
        }
        
        labelResultado.setText(mensaje);
        actualizarBarrasVida();
        
        // Cambiar turno
        turnoJugador = false;
        configurarBotonesAtaque();
        
        // Ejecutar turno de la IA después de una pausa
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> ejecutarTurnoIA());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Ejecuta el turno de la IA
     */
    private void ejecutarTurnoIA() {
        if (!combateRealActivo || turnoJugador || 
            pokemonCombateIA == null || pokemonCombateJugador == null) {
            return;
        }
        
        // IA elige un ataque aleatorio
        List<MovimientoEntity> ataques = pokemonCombateIA.getAtaques();
        if (ataques == null || ataques.isEmpty()) {
            System.err.println("Error: La IA no tiene ataques disponibles");
            return;
        }
        
        MovimientoEntity ataque = ataques.get(random.nextInt(ataques.size()));
        
        // Ejecutar ataque
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(
            pokemonCombateIA, pokemonCombateJugador, ataque);
        
        // Aplicar daño
        pokemonCombateJugador.recibirDaño(resultado.getDano());
        
        // Crear mensaje del ataque
        String mensaje = String.format("🤖 TURNO DEL RIVAL\n\n%s\n\n💥 %s recibió %d puntos de daño!",
                                      resultado.getMensaje(),
                                      pokemonCombateJugador.getNombre(),
                                      resultado.getDano());
        
        // Verificar si el combate terminó
        if (pokemonCombateJugador.estaDebilitado()) {
            mensaje += String.format("\n\n💀 ¡%s se debilitó!\n😞 ¡HAS PERDIDO EL COMBATE! 😞", 
                                    pokemonCombateJugador.getNombre());
            labelResultado.setText(mensaje);
            actualizarBarrasVida();
            finalizarCombateReal(false);
            return;
        }
        
        labelResultado.setText(mensaje + "\n\n💡 Es tu turno, elige un ataque");
        actualizarBarrasVida();
        
        // Cambiar turno
        turnoJugador = true;
        configurarBotonesAtaque();
    }
    
    /**
     * Finaliza el combate real y va a resultados
     */
    private void finalizarCombateReal(boolean jugadorGano) {
        combateRealActivo = false;
        
        // Actualizar estadísticas del usuario
        actualizarEstadisticasUsuario(jugadorGano);
        
        // Mostrar resultados después de una pausa
        new Thread(() -> {
            try {
                Thread.sleep(4000);
                javafx.application.Platform.runLater(() -> finalizarCombateRondas());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Finaliza el combate de 3 rondas y va a la pantalla de resultados
     */
    private void finalizarCombateRondas() {
        combateActivo = false;
        combateRealActivo = false;
        
        // Ir a pantalla de resultados
        try {
            Stage stage = (Stage) buttonVolverMenu.getScene().getWindow();
            if (stage != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("resultados.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
                
                // Pasar datos al controlador de resultados
                ResultadosController controllerResultados = fxmlLoader.getController();
                controllerResultados.setDatosCombate(victoriasJugador, victoriasIA, pokemonJugador, pokemonIA, resultadosRondas);
                
                stage.setTitle("PK-GAME - Resultados");
                stage.setScene(scene);
                stage.show();
            } else {
                throw new RuntimeException("Stage is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: mostrar mensaje en la pantalla actual
            String mensajeResultado = String.format("🏁 COMBATE FINALIZADO\n\nMarcador: %d - %d\n%s",
                                                   victoriasJugador, victoriasIA,
                                                   victoriasJugador > victoriasIA ? "¡GANASTE!" : "¡PERDISTE!");
            labelResultado.setText(mensajeResultado);
            buttonNuevoCombate.setVisible(true);
        }
    }
    
    /**
     * Resetea la interfaz para volver al modo de selección
     */
    private void resetearInterfaz() {
        // Reiniciar todas las variables de control
        combateActivo = false;
        combateRealActivo = false;
        pokemonSeleccionado = false;
        rondaActual = 0;
        victoriasJugador = 0;
        victoriasIA = 0;
        resultadosRondas.clear();
        turnoJugador = true;
        
        // Reiniciar tipos y Pokémon
        tipoPokemonJugador = null;
        tipoPokemonIA = null;
        tipoRondaJugador = null;
        tipoRondaIA = null;
        pokemonJugador = null;
        pokemonIA = null;
        pokemonCombateJugador = null;
        pokemonCombateIA = null;
        
        // Habilitar botones de selección
        habilitarBotonesSeleccion(true);
        
        // Ocultar elementos de combate
        labelJugador.setVisible(false);
        labelIA.setVisible(false);
        labelVidaJugador.setVisible(false);
        labelVidaIA.setVisible(false);
        imagenJugador.setVisible(false);
        imagenIA.setVisible(false);
        buttonNuevoCombate.setVisible(false);
        
        // Ocultar botones de ataque
        Button[] botonesAtaque = {buttonAtaque1, buttonAtaque2, buttonAtaque3, buttonAtaque4};
        for (Button boton : botonesAtaque) {
            boton.setVisible(false);
        }
        
        // Mostrar mensaje inicial
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        String mensajeUsuario = "";
        if (usuario != null) {
            mensajeUsuario = String.format("👋 ¡Hola %s! %s\n", usuario.getNombre(), usuario.getTitulo());
            if (usuario.getTotalPartidas() > 0) {
                mensajeUsuario += String.format("📊 Récord: %d-%d (%.1f%%) | 🔥 Racha: %d\n\n",
                                               usuario.getVictoriasTotales(), usuario.getDerrotasTotales(),
                                               usuario.getPorcentajeVictorias(), usuario.getRachaActual());
            } else {
                mensajeUsuario += "🌟 ¡Primera vez! ¡Buena suerte!\n\n";
            }
        }
        
        labelResultado.setText(mensajeUsuario + "🎮 PASO 1: ¡Elige tu Pokémon! 🎮\n\n" +
                               "🔥 Charmander | 💧 Squirtle | 🌿 Bulbasaur\n\n" +
                               "📝 Flujo del juego:\n" +
                               "1️⃣ Elige tu Pokémon inicial\n" +
                               "2️⃣ Juega 3 rondas eligiendo tipo de ataque\n" +
                               "3️⃣ ¡Gana rondas para evolucionar!\n" +
                               "4️⃣ ¡Combate real con ataques y vida!\n\n" +
                               "⚔️ Tipos (Piedra-Papel-Tijera):\n" +
                               "🔥 Fuego > 🌿 Planta > 💧 Agua > 🔥 Fuego");
    }
    
    private void ejecutarAtaque(int indiceAtaque) {
        // Ya no se usa en el nuevo sistema de rondas
        // Los ataques son automáticos basados en tipos
    }
    
    /**
     * Actualiza las estadísticas del usuario en la base de datos
     * @param victoria true si el usuario ganó, false si perdió
     */
    private void actualizarEstadisticasUsuario(boolean victoria) {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
        if (usuario != null) {
            try {
                // Actualizar estadísticas en el objeto usuario
                usuario.actualizarEstadisticas(victoria);
                
                // Guardar en la base de datos
                boolean actualizado = usuarioService.actualizarEstadisticas(usuario);
                
                if (actualizado) {
                    System.out.println("✅ Estadísticas actualizadas para " + usuario.getNombre());
                } else {
                    System.err.println("❌ Error al actualizar estadísticas en la base de datos");
                }
            } catch (Exception e) {
                System.err.println("Error al actualizar estadísticas: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void habilitarBotonesSeleccion(boolean habilitar) {
        buttonFuego.setDisable(!habilitar);
        buttonAgua.setDisable(!habilitar);
        buttonPlanta.setDisable(!habilitar);
        buttonFuego.setVisible(habilitar);
        buttonAgua.setVisible(habilitar);
        buttonPlanta.setVisible(habilitar);
    }
}

