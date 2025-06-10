package es.diegofeblesseoane.pkgame.backend.service;

import es.diegofeblesseoane.pkgame.backend.model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombateService {
    
    private Random random;
    private PokemonEntity pokemonJugador;
    private PokemonEntity pokemonIA;
    private boolean turnoJugador;
    private boolean combateActivo;
    
    public CombateService() throws SQLException {
        this.random = new Random();
        this.combateActivo = false;
    }
    
    /**
     * Obtiene un Pokémon por tipo
     * @param tipo el tipo de Pokémon a obtener
     * @return el Pokémon correspondiente
     * @throws SQLException error de base de datos
     */
    public PokemonEntity obtenerPokemonPorTipo(TipoPokemon tipo) throws SQLException {
        PokemonDAO pokemonDAO = new PokemonDAO("src/main/resources/db/usuarios.db");
        PokemonEntity pokemon = pokemonDAO.obtenerPokemonPorNombre(tipo.getPokemonRepresentativo());
        // Crear una copia para no afectar la instancia original
        if (pokemon != null) {
            PokemonEntity copia = new PokemonEntity(pokemon.getId(), pokemon.getNombre(), 
                                                 pokemon.getTipo1(), pokemon.getTipo2(), 
                                                 pokemon.getEstadisticas());
            copia.setAtaques(new ArrayList<>(pokemon.getAtaques()));
            copia.restaurarVida(); // Asegurar que tenga vida completa
            return copia;
        }
        return null;
    }
    
    /**
     * Inicia un nuevo combate
     * @param tipoJugador tipo de Pokémon del jugador
     * @param tipoIA tipo de Pokémon de la IA
     * @return información del inicio del combate
     */
    public IniciarCombateResult iniciarCombate(TipoPokemon tipoJugador, TipoPokemon tipoIA) {
        try {
            pokemonJugador = obtenerPokemonPorTipo(tipoJugador);
            pokemonIA = obtenerPokemonPorTipo(tipoIA);
            
            if (pokemonJugador == null || pokemonIA == null) {
                return new IniciarCombateResult(null, null, false, "Error al cargar los Pokémon");
            }
            
            // Determinar quién va primero basado en la velocidad
            turnoJugador = pokemonJugador.getEstadisticas().getVelocidad() >= 
                          pokemonIA.getEstadisticas().getVelocidad();
            
            combateActivo = true;
            
            String mensaje = String.format("¡%s vs %s!\n%s", 
                                          pokemonJugador.getNombre(), 
                                          pokemonIA.getNombre(),
                                          turnoJugador ? "¡Es tu turno!" : "¡" + pokemonIA.getNombre() + " ataca primero!");
            
            return new IniciarCombateResult(pokemonJugador, pokemonIA, turnoJugador, mensaje);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new IniciarCombateResult(null, null, false, "Error al cargar los Pokémon");
        }
    }
    
    /**
     * Genera una elección estratégica para la IA
     * La IA puede elegir aleatoriamente o intentar contraatacar
     * @return un tipo de Pokémon basado en la estrategia de la IA
     */
    public TipoPokemon obtenerEleccionIA() {
        return obtenerEleccionIA(null);
    }
    
    /**
     * Genera una elección estratégica para la IA basada en el tipo del jugador
     * @param tipoJugador el tipo que eligió el jugador (puede ser null)
     * @return un tipo de Pokémon basado en la estrategia de la IA
     */
    public TipoPokemon obtenerEleccionIA(TipoPokemon tipoJugador) {
        TipoPokemon[] tipos = TipoPokemon.values();
        
        // Si no conocemos el tipo del jugador, elección completamente aleatoria
        if (tipoJugador == null) {
            return tipos[random.nextInt(tipos.length)];
        }
        
        // IA con diferentes niveles de dificultad:
        double probabilidad = random.nextDouble();
        
        if (probabilidad < 0.2) {
            // 20% - IA "inteligente": elige el tipo que vence al jugador
            return tipoJugador.obtenerTipoContrario();
        } else if (probabilidad < 0.35) {
            // 15% - IA "defensiva": elige el tipo del jugador (empate)
            return tipoJugador;
        } else if (probabilidad < 0.5) {
            // 15% - IA "vulnerable": elige el tipo débil contra el jugador
            return tipoJugador.obtenerTipoVencido();
        } else {
            // 50% - IA completamente aleatoria
            return tipos[random.nextInt(tipos.length)];
        }
    }
    
    /**
     * Genera un mensaje de elección de la IA
     * @param tipoElegido el tipo que eligió la IA
     * @return mensaje descriptivo
     */
    public String obtenerMensajeEleccionIA(TipoPokemon tipoElegido) {
        String[] mensajes = {
            "%s ¡La IA ha elegido %s!",
            "%s ¡El oponente envía a %s!",
            "%s ¡%s, te elijo a ti!",
            "%s ¡La IA confía en %s!",
            "%s ¡%s aparece en el campo de batalla!",
            "%s ¡El retador envía a %s!",
            "%s ¡%s está listo para el combate!"
        };
        
        String mensaje = mensajes[random.nextInt(mensajes.length)];
        return String.format(mensaje, tipoElegido.getEmoji(), tipoElegido.getPokemonRepresentativo());
    }
    
    /**
     * Ejecuta el turno del jugador
     * @param movimientoIndice índice del movimiento elegido (0-3)
     * @return resultado del turno
     */
    public TurnoResult ejecutarTurnoJugador(int movimientoIndice) {
        if (!combateActivo || !turnoJugador || pokemonJugador.estaDebilitado()) {
            return new TurnoResult(false, "No es tu turno o el combate no está activo", false);
        }
        
        if (movimientoIndice < 0 || movimientoIndice >= pokemonJugador.getAtaques().size()) {
            return new TurnoResult(false, "⚠️ Movimiento inválido", false);
        }
        
        MovimientoEntity movimiento = pokemonJugador.getAtaques().get(movimientoIndice);
        
        // Ejecutar ataque del jugador
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(pokemonJugador, pokemonIA, movimiento);
        pokemonIA.recibirDaño(resultado.getDano());
        
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(resultado.getMensaje());
        
        if (resultado.getDano() > 0) {
            String efectoDano = obtenerEfectoDano(resultado.getDano(), pokemonIA.getEstadisticas().getVida());
            mensaje.append(String.format("\n%s %s recibió %d puntos de daño!", efectoDano, pokemonIA.getNombre(), resultado.getDano()));
        }
        
        // Verificar si la IA fue derrotada
        if (pokemonIA.estaDebilitado()) {
            mensaje.append(String.format("\n💀 ¡%s se debilitó!\n🎊 ¡Has ganado! 🎊", pokemonIA.getNombre()));
            combateActivo = false;
            return new TurnoResult(true, mensaje.toString(), true);
        }
        
        // Cambiar turno
        turnoJugador = false;
        
        return new TurnoResult(true, mensaje.toString(), false);
    }
    
    /**
     * Ejecuta el turno de la IA
     * @return resultado del turno de la IA
     */
    public TurnoResult ejecutarTurnoIA() {
        if (!combateActivo || turnoJugador || pokemonIA.estaDebilitado()) {
            return new TurnoResult(false, "No es el turno de la IA o el combate no está activo", false);
        }
        
        // IA elige un movimiento aleatorio
        List<MovimientoEntity> ataques = pokemonIA.getAtaques();
        MovimientoEntity movimiento = ataques.get(random.nextInt(ataques.size()));
        
        // Ejecutar ataque de la IA
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(pokemonIA, pokemonJugador, movimiento);
        pokemonJugador.recibirDaño(resultado.getDano());
        
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(resultado.getMensaje());
        
        if (resultado.getDano() > 0) {
            String efectoDano = obtenerEfectoDano(resultado.getDano(), pokemonJugador.getEstadisticas().getVida());
            mensaje.append(String.format("\n%s %s recibió %d puntos de daño!", efectoDano, pokemonJugador.getNombre(), resultado.getDano()));
        }
        
        // Verificar si el jugador fue derrotado
        if (pokemonJugador.estaDebilitado()) {
            mensaje.append(String.format("\n💀 ¡%s se debilitó!\n😵 ¡Has perdido! 😵", pokemonJugador.getNombre()));
            combateActivo = false;
            return new TurnoResult(true, mensaje.toString(), true);
        }
        
        // Cambiar turno
        turnoJugador = true;
        
        return new TurnoResult(true, mensaje.toString(), false);
    }
    
    
    // Getters para el estado actual del combate
    public PokemonEntity getPokemonJugador() {
        return pokemonJugador;
    }
    
    public PokemonEntity getPokemonIA() {
        return pokemonIA;
    }
    
    public boolean isTurnoJugador() {
        return turnoJugador;
    }
    
    public boolean isCombateActivo() {
        return combateActivo;
    }
    
    public void finalizarCombate() {
        combateActivo = false;
        pokemonJugador = null;
        pokemonIA = null;
    }
    
    /**
     * Obtiene un emoji de efecto basado en el daño causado
     * @param dano cantidad de daño
     * @param vidaMaxima vida máxima del Pokémon
     * @return emoji representativo del impacto
     */
    private String obtenerEfectoDano(int dano, int vidaMaxima) {
        double porcentajeDano = (double) dano / vidaMaxima * 100;
        
        if (porcentajeDano >= 50) {
            return "💥"; // Daño masivo
        } else if (porcentajeDano >= 30) {
            return "⚡"; // Daño alto
        } else if (porcentajeDano >= 15) {
            return "💢"; // Daño medio
        } else if (porcentajeDano > 0) {
            return "✨"; // Daño bajo
        } else {
            return "🌟"; // Sin daño
        }
    }
    
    /**
     * Clase para el resultado de iniciar combate
     */
    public static class IniciarCombateResult {
        private final PokemonEntity pokemonJugador;
        private final PokemonEntity pokemonIA;
        private final boolean turnoJugador;
        private final String mensaje;
        
        public IniciarCombateResult(PokemonEntity pokemonJugador, PokemonEntity pokemonIA, 
                                   boolean turnoJugador, String mensaje) {
            this.pokemonJugador = pokemonJugador;
            this.pokemonIA = pokemonIA;
            this.turnoJugador = turnoJugador;
            this.mensaje = mensaje;
        }
        
        public PokemonEntity getPokemonJugador() {
            return pokemonJugador;
        }
        
        public PokemonEntity getPokemonIA() {
            return pokemonIA;
        }
        
        public boolean isTurnoJugador() {
            return turnoJugador;
        }
        
        public String getMensaje() {
            return mensaje;
        }
    }
    
    /**
     * Clase para el resultado de un turno
     */
    public static class TurnoResult {
        private final boolean exito;
        private final String mensaje;
        private final boolean combateTerminado;
        
        public TurnoResult(boolean exito, String mensaje, boolean combateTerminado) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.combateTerminado = combateTerminado;
        }
        
        public boolean isExito() {
            return exito;
        }
        
        public String getMensaje() {
            return mensaje;
        }
        
        public boolean isCombateTerminado() {
            return combateTerminado;
        }
    }
}

