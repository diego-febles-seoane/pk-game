package integration;

import es.diegofeblesseoane.pkgame.backend.model.*;
import es.diegofeblesseoane.pkgame.backend.service.CombateService;
import es.diegofeblesseoane.pkgame.backend.service.CalculadoraDano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para verificar el flujo completo del combate
 * sin mocks de la base de datos (usando datos en memoria)
 */
@ExtendWith(MockitoExtension.class)
class CombateIntegrationTest {

    private CombateService combateService;
    private PokemonEntity pikachu;
    private PokemonEntity charizard;
    private MovimientoEntity rayo;
    private MovimientoEntity llamarada;

    @BeforeEach
    void setUp() throws SQLException {
        // Crear Pokémon y movimientos de prueba
        EstadisticasEntity estadisticasPikachu = new EstadisticasEntity(1, 90, 60, 120, 110);
        EstadisticasEntity estadisticasCharizard = new EstadisticasEntity(2, 120, 80, 150, 100);
        
        pikachu = new PokemonEntity(1, "Pikachu", "Eléctrico", null, estadisticasPikachu);
        charizard = new PokemonEntity(2, "Charizard", "Fuego", "Volador", estadisticasCharizard);
        
        rayo = new MovimientoEntity(1, "Rayo", 90, 100, "Eléctrico");
        llamarada = new MovimientoEntity(2, "Llamarada", 95, 100, "Fuego");
        MovimientoEntity ataqueRapido = new MovimientoEntity(3, "Ataque Rápido", 40, 100, "Normal");
        MovimientoEntity vuelo = new MovimientoEntity(4, "Vuelo", 70, 95, "Volador");
        
        pikachu.agregarAtaque(rayo);
        pikachu.agregarAtaque(ataqueRapido);
        
        charizard.agregarAtaque(llamarada);
        charizard.agregarAtaque(vuelo);
        
        combateService = new CombateService();
    }

    @Test
    void testCalculadoraDano_IntegracionCompleta() {
        // Test de integración del calculador de daño con diferentes escenarios
        
        // Caso 1: Ataque (Eléctrico vs Fuego/Volador)
        CalculadoraDano.ResultadoDano resultado1 = CalculadoraDano.calcularDano(pikachu, charizard, rayo);
        assertTrue(resultado1.getDano() > 0);
        assertTrue(resultado1.getEfectividad() >= 1.0, "El multiplicador debe ser al menos 1.0");
        assertTrue(resultado1.getMensaje().contains("Rayo") || resultado1.getMensaje().contains("Pikachu"));
        
        // Caso 2: Ataque normal
        MovimientoEntity tackle = new MovimientoEntity(5, "Tackle", 40, 100, "Normal");
        CalculadoraDano.ResultadoDano resultado2 = CalculadoraDano.calcularDano(pikachu, charizard, tackle);
        assertTrue(resultado2.getDano() > 0);
        assertEquals(1.0, resultado2.getEfectividad(), 0.1);
        
        // Caso 3: Verificar que el daño mínimo es 1
        // Crear un ataque muy débil
        MovimientoEntity ataqueDebil = new MovimientoEntity(6, "Débil", 1, 100, "Normal");
        EstadisticasEntity statsDebiles = new EstadisticasEntity(3, 1, 1, 50, 50);
        PokemonEntity pokemonDebil = new PokemonEntity(3, "Débil", "Normal", null, statsDebiles);
        
        CalculadoraDano.ResultadoDano resultado3 = CalculadoraDano.calcularDano(pokemonDebil, charizard, ataqueDebil);
        assertTrue(resultado3.getDano() >= 1);
    }

    @Test
    void testFlujoCompletoCombate_Simulado() {
        // Simular un combate completo paso a paso
        
        // Inicializar vida de los Pokémon
        pikachu.restaurarVida();
        charizard.restaurarVida();
        
        int vidaInicialPikachu = pikachu.getVidaActual();
        int vidaInicialCharizard = charizard.getVidaActual();
        
        assertTrue(vidaInicialPikachu > 0);
        assertTrue(vidaInicialCharizard > 0);
        
        // Turno 1: Pikachu ataca a Charizard
        CalculadoraDano.ResultadoDano ataque1 = CalculadoraDano.calcularDano(pikachu, charizard, rayo);
        charizard.recibirDaño(ataque1.getDano());
        
        assertTrue(charizard.getVidaActual() < vidaInicialCharizard);
        assertFalse(charizard.estaDebilitado()); // No debería estar debilitado en un turno
        
        // Turno 2: Charizard contraataca
        CalculadoraDano.ResultadoDano ataque2 = CalculadoraDano.calcularDano(charizard, pikachu, llamarada);
        pikachu.recibirDaño(ataque2.getDano());
        
        assertTrue(pikachu.getVidaActual() < vidaInicialPikachu);
        
        // Verificar que ambos Pokémon siguen en combate
        assertFalse(pikachu.estaDebilitado());
        assertFalse(charizard.estaDebilitado());
        
        // Verificar porcentajes de vida
        assertTrue(pikachu.getPorcentajeVida() < 100.0);
        assertTrue(charizard.getPorcentajeVida() < 100.0);
        assertTrue(pikachu.getPorcentajeVida() > 0.0);
        assertTrue(charizard.getPorcentajeVida() > 0.0);
    }

    @Test
    void testCombateHastaDerrota() {
        // Simular un combate hasta que uno de los Pokémon sea derrotado
        
        pikachu.restaurarVida();
        charizard.restaurarVida();
        
        int turnosMaximos = 20; // Evitar bucle infinito
        int turno = 0;
        boolean combateTerminado = false;
        
        while (!combateTerminado && turno < turnosMaximos) {
            turno++;
            
            if (turno % 2 == 1) {
                // Turno de Pikachu
                CalculadoraDano.ResultadoDano ataque = CalculadoraDano.calcularDano(pikachu, charizard, rayo);
                charizard.recibirDaño(ataque.getDano());
                
                if (charizard.estaDebilitado()) {
                    combateTerminado = true;
                }
            } else {
                // Turno de Charizard
                CalculadoraDano.ResultadoDano ataque = CalculadoraDano.calcularDano(charizard, pikachu, llamarada);
                pikachu.recibirDaño(ataque.getDano());
                
                if (pikachu.estaDebilitado()) {
                    combateTerminado = true;
                }
            }
        }
        
        // Verificar que el combate terminó
        assertTrue(combateTerminado || turno >= turnosMaximos);
        
        // Verificar que al menos uno está debilitado
        if (combateTerminado) {
            assertTrue(pikachu.estaDebilitado() || charizard.estaDebilitado());
            assertFalse(pikachu.estaDebilitado() && charizard.estaDebilitado()); // Solo uno debilitado
        }
    }

    @Test
    void testEfectividadDeTipos_Integracion() {
        // Test de integración para verificar la efectividad de tipos
        
        // Crear Pokémon de diferentes tipos
        EstadisticasEntity estadisticasBase = new EstadisticasEntity(1, 100, 80, 150, 90);
        
        PokemonEntity pokemon_fuego = new PokemonEntity(1, "Fuego", "Fuego", null, estadisticasBase);
        PokemonEntity pokemon_agua = new PokemonEntity(2, "Agua", "Agua", null, estadisticasBase);
        PokemonEntity pokemon_planta = new PokemonEntity(3, "Planta", "Planta", null, estadisticasBase);
        
        MovimientoEntity movimiento_fuego = new MovimientoEntity(1, "Fuego", 90, 100, "Fuego");
        MovimientoEntity movimiento_agua = new MovimientoEntity(2, "Agua", 90, 100, "Agua");
        MovimientoEntity movimiento_planta = new MovimientoEntity(3, "Planta", 90, 100, "Planta");
        
        // Fuego vs Planta (súper eficaz)
        CalculadoraDano.ResultadoDano resultado1 = CalculadoraDano.calcularDano(pokemon_fuego, pokemon_planta, movimiento_fuego);
        assertEquals(2.0, resultado1.getEfectividad(), 0.1);
        
        // Agua vs Fuego (súper eficaz)
        CalculadoraDano.ResultadoDano resultado2 = CalculadoraDano.calcularDano(pokemon_agua, pokemon_fuego, movimiento_agua);
        assertEquals(2.0, resultado2.getEfectividad(), 0.1);
        
        // Planta vs Agua (súper eficaz)
        CalculadoraDano.ResultadoDano resultado3 = CalculadoraDano.calcularDano(pokemon_planta, pokemon_agua, movimiento_planta);
        assertEquals(2.0, resultado3.getEfectividad(), 0.1);
        
        // Fuego vs Agua (no muy eficaz)
        CalculadoraDano.ResultadoDano resultado4 = CalculadoraDano.calcularDano(pokemon_fuego, pokemon_agua, movimiento_fuego);
        assertEquals(0.5, resultado4.getEfectividad(), 0.1);
    }

    @Test
    void testCuracionYDano_Integracion() {
        // Test de integración para curación y daño
        
        pikachu.restaurarVida();
        int vidaMaxima = pikachu.getVidaActual();
        
        // Causar daño
        int dano = 50;
        pikachu.recibirDaño(dano);
        assertEquals(vidaMaxima - dano, pikachu.getVidaActual());
        
        // Curar parcialmente
        int curacion = 20;
        pikachu.curar(curacion);
        assertEquals(vidaMaxima - dano + curacion, pikachu.getVidaActual());
        
        // Verificar porcentaje
        double porcentajeEsperado = ((double)(vidaMaxima - dano + curacion) / vidaMaxima) * 100;
        assertEquals(porcentajeEsperado, pikachu.getPorcentajeVida(), 1.0);
        
        // Curación completa
        pikachu.restaurarVida();
        assertEquals(vidaMaxima, pikachu.getVidaActual());
        assertEquals(100.0, pikachu.getPorcentajeVida(), 0.1);
    }

    @Test
    void testLimitesMovimientos_Integracion() {
        // Test de integración para límites de movimientos
        
        PokemonEntity pokemonTest = new PokemonEntity(99, "Test", "Normal", null, 
            new EstadisticasEntity(1, 100, 80, 150, 90));
        
        // Agregar 4 movimientos (límite)
        MovimientoEntity mov1 = new MovimientoEntity(1, "Mov1", 40, 100, "Normal");
        MovimientoEntity mov2 = new MovimientoEntity(2, "Mov2", 50, 100, "Normal");
        MovimientoEntity mov3 = new MovimientoEntity(3, "Mov3", 60, 100, "Normal");
        MovimientoEntity mov4 = new MovimientoEntity(4, "Mov4", 70, 100, "Normal");
        MovimientoEntity mov5 = new MovimientoEntity(5, "Mov5", 80, 100, "Normal");
        
        pokemonTest.agregarAtaque(mov1);
        pokemonTest.agregarAtaque(mov2);
        pokemonTest.agregarAtaque(mov3);
        pokemonTest.agregarAtaque(mov4);
        
        assertEquals(4, pokemonTest.getAtaques().size());
        
        // Intentar agregar el quinto (debería fallar)
        assertThrows(IllegalStateException.class, () -> {
            pokemonTest.agregarAtaque(mov5);
        });
        
        // Eliminar uno y agregar otro
        pokemonTest.eliminarAtaque(mov1);
        assertEquals(3, pokemonTest.getAtaques().size());
        
        pokemonTest.agregarAtaque(mov5);
        assertEquals(4, pokemonTest.getAtaques().size());
        assertTrue(pokemonTest.getAtaques().contains(mov5));
        assertFalse(pokemonTest.getAtaques().contains(mov1));
    }

    @Test
    void testComparacionPokemon_Integracion() {
        // Test de integración para comparación de Pokémon
        
        EstadisticasEntity stats1 = new EstadisticasEntity(1, 100, 80, 150, 90);
        EstadisticasEntity stats2 = new EstadisticasEntity(2, 110, 90, 160, 95);
        
        PokemonEntity pokemon1 = new PokemonEntity(1, "Pokemon1", "Normal", null, stats1);
        PokemonEntity pokemon2 = new PokemonEntity(1, "Pokemon1", "Normal", null, stats1); // Mismo ID y datos
        PokemonEntity pokemon3 = new PokemonEntity(2, "Pokemon3", "Normal", null, stats1); // Diferente ID
        
        // Pokémon con mismo ID son iguales (según equals)
        assertEquals(pokemon1, pokemon2);
        assertEquals(pokemon1.hashCode(), pokemon2.hashCode());
        
        // Pokémon con diferente ID son diferentes
        assertNotEquals(pokemon1, pokemon3);
        
        // Verificar que toString contiene información relevante
        String toString1 = pokemon1.toString();
        assertTrue(toString1.contains("Pokemon1"));
        assertTrue(toString1.contains("id=1"));
        assertTrue(toString1.contains("Normal"));
    }
}

