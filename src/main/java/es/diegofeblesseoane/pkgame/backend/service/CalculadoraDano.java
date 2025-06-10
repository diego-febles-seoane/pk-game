package es.diegofeblesseoane.pkgame.backend.service;

import es.diegofeblesseoane.pkgame.backend.model.MovimientoEntity;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CalculadoraDano {
    
    private static final Random random = new Random();
    
    // Tabla de efectividades de tipos
    private static final Map<String, Map<String, Double>> tablaEfectividad = new HashMap<>();
    
    static {
        // Inicializar tabla de efectividades
        inicializarTablaEfectividad();
    }
    
    private static void inicializarTablaEfectividad() {
        // Fuego
        Map<String, Double> fuego = new HashMap<>();
        fuego.put("Fuego", 0.5);   // Fuego vs Fuego = no muy eficaz
        fuego.put("Agua", 0.5);    // Fuego vs Agua = no muy eficaz
        fuego.put("Planta", 2.0);  // Fuego vs Planta = súper eficaz
        fuego.put("Normal", 1.0);  // Fuego vs Normal = normal
        fuego.put("Dragón", 1.0);  // Fuego vs Dragón = normal
        fuego.put("Veneno", 1.0);  // Fuego vs Veneno = normal
        tablaEfectividad.put("Fuego", fuego);
        
        // Agua
        Map<String, Double> agua = new HashMap<>();
        agua.put("Fuego", 2.0);    // Agua vs Fuego = súper eficaz
        agua.put("Agua", 0.5);     // Agua vs Agua = no muy eficaz
        agua.put("Planta", 0.5);   // Agua vs Planta = no muy eficaz
        agua.put("Normal", 1.0);   // Agua vs Normal = normal
        agua.put("Dragón", 1.0);   // Agua vs Dragón = normal
        agua.put("Veneno", 1.0);   // Agua vs Veneno = normal
        tablaEfectividad.put("Agua", agua);
        
        // Planta
        Map<String, Double> planta = new HashMap<>();
        planta.put("Fuego", 0.5);  // Planta vs Fuego = no muy eficaz
        planta.put("Agua", 2.0);   // Planta vs Agua = súper eficaz
        planta.put("Planta", 0.5); // Planta vs Planta = no muy eficaz
        planta.put("Normal", 1.0); // Planta vs Normal = normal
        planta.put("Dragón", 1.0); // Planta vs Dragón = normal
        planta.put("Veneno", 0.5); // Planta vs Veneno = no muy eficaz
        tablaEfectividad.put("Planta", planta);
        
        // Normal
        Map<String, Double> normal = new HashMap<>();
        normal.put("Fuego", 1.0);  // Normal vs Fuego = normal
        normal.put("Agua", 1.0);   // Normal vs Agua = normal
        normal.put("Planta", 1.0); // Normal vs Planta = normal
        normal.put("Normal", 1.0); // Normal vs Normal = normal
        normal.put("Dragón", 1.0); // Normal vs Dragón = normal
        normal.put("Veneno", 1.0); // Normal vs Veneno = normal
        tablaEfectividad.put("Normal", normal);
        
        // Dragón
        Map<String, Double> dragon = new HashMap<>();
        dragon.put("Fuego", 1.0);  // Dragón vs Fuego = normal
        dragon.put("Agua", 1.0);   // Dragón vs Agua = normal
        dragon.put("Planta", 1.0); // Dragón vs Planta = normal
        dragon.put("Normal", 1.0); // Dragón vs Normal = normal
        dragon.put("Dragón", 2.0); // Dragón vs Dragón = súper eficaz
        dragon.put("Veneno", 1.0); // Dragón vs Veneno = normal
        tablaEfectividad.put("Dragón", dragon);
        
        // Veneno
        Map<String, Double> veneno = new HashMap<>();
        veneno.put("Fuego", 1.0);  // Veneno vs Fuego = normal
        veneno.put("Agua", 1.0);   // Veneno vs Agua = normal
        veneno.put("Planta", 2.0); // Veneno vs Planta = súper eficaz
        veneno.put("Normal", 1.0); // Veneno vs Normal = normal
        veneno.put("Dragón", 1.0); // Veneno vs Dragón = normal
        veneno.put("Veneno", 0.5); // Veneno vs Veneno = no muy eficaz
        tablaEfectividad.put("Veneno", veneno);
    }
    
    /**
     * Calcula el daño de un ataque
     * @param atacante Pokémon que ataca
     * @param defensor Pokémon que defiende
     * @param movimiento Movimiento usado
     * @return información del daño calculado
     */
    public static ResultadoDano calcularDano(PokemonEntity atacante, PokemonEntity defensor, MovimientoEntity movimiento) {
        // Si el movimiento no tiene potencia (movimientos de estado), no hace daño
        if (movimiento.getPotencia() == 0) {
            return new ResultadoDano(0, 1.0, false, "¡" + atacante.getNombre() + " usó " + movimiento.getNombre() + "!");
        }
        
        // Verificar si el ataque acierta
        boolean acierta = random.nextInt(100) < movimiento.getPrecision();
        if (!acierta) {
            return new ResultadoDano(0, 1.0, false, "¡" + atacante.getNombre() + " usó " + movimiento.getNombre() + " pero falló!");
        }
        
        // Calcular efectividad
        double efectividad = calcularEfectividad(movimiento.getTipo(), defensor);
        
        // Fórmula simplificada de daño de Pokémon
        // Daño = ((((2 * Nivel + 10) / 250) * (Ataque/Defensa) * Potencia) + 2) * Efectividad * Crítico * Aleatorio
        // Simplificado: Daño = (Ataque * Potencia / (Defensa * 2)) * Efectividad * Aleatorio
        
        int ataque = atacante.getEstadisticas().getAtaque();
        int defensa = defensor.getEstadisticas().getDefensa();
        int potencia = movimiento.getPotencia();
        
        // Factor aleatorio entre 0.85 y 1.0
        double factorAleatorio = 0.85 + (random.nextDouble() * 0.15);
        
        // Verificar golpe crítico (5% de probabilidad)
        boolean critico = random.nextDouble() < 0.05;
        double factorCritico = critico ? 2.0 : 1.0;
        
        // Calcular daño base
        double danoBase = (ataque * potencia) / (defensa * 2.0);
        
        // Aplicar todos los modificadores
        int danoFinal = (int) Math.max(1, danoBase * efectividad * factorCritico * factorAleatorio);
        
        // Crear mensaje del ataque
        String mensaje = crearMensajeAtaque(atacante, defensor, movimiento, efectividad, critico);
        
        return new ResultadoDano(danoFinal, efectividad, critico, mensaje);
    }
    
    /**
     * Calcula la efectividad del tipo de movimiento contra el Pokémon defensor
     */
    private static double calcularEfectividad(String tipoMovimiento, PokemonEntity defensor) {
        double efectividad = 1.0;
        
        // Efectividad contra el tipo principal
        Map<String, Double> efectividadTipo = tablaEfectividad.get(tipoMovimiento);
        if (efectividadTipo != null) {
            efectividad *= efectividadTipo.getOrDefault(defensor.getTipo1(), 1.0);
            
            // Si tiene doble tipo, aplicar efectividad del segundo tipo
            if (defensor.tieneDobleTipo()) {
                efectividad *= efectividadTipo.getOrDefault(defensor.getTipo2(), 1.0);
            }
        }
        
        return efectividad;
    }
    
    /**
     * Crea un mensaje descriptivo del ataque
     */
    private static String crearMensajeAtaque(PokemonEntity atacante, PokemonEntity defensor, 
                                           MovimientoEntity movimiento, double efectividad, boolean critico) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡").append(atacante.getNombre()).append(" usó ").append(movimiento.getNombre()).append("!");
        
        if (critico) {
            mensaje.append("\n¡Golpe crítico!");
        }
        
        if (efectividad > 1.0) {
            mensaje.append("\n¡Es súper eficaz!");
        } else if (efectividad < 1.0 && efectividad > 0) {
            mensaje.append("\nNo es muy eficaz...");
        } else if (efectividad == 0) {
            mensaje.append("\n¡No afecta a ").append(defensor.getNombre()).append("!");
        }
        
        return mensaje.toString();
    }
    
    /**
     * Clase para representar el resultado del cálculo de daño
     */
    public static class ResultadoDano {
        private final int dano;
        private final double efectividad;
        private final boolean critico;
        private final String mensaje;
        
        public ResultadoDano(int dano, double efectividad, boolean critico, String mensaje) {
            this.dano = dano;
            this.efectividad = efectividad;
            this.critico = critico;
            this.mensaje = mensaje;
        }
        
        public int getDano() {
            return dano;
        }
        
        public double getEfectividad() {
            return efectividad;
        }
        
        public boolean isCritico() {
            return critico;
        }
        
        public String getMensaje() {
            return mensaje;
        }
    }
}

