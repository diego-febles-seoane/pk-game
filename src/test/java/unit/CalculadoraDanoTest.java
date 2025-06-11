package unit;

import es.diegofeblesseoane.pkgame.backend.model.*;
import es.diegofeblesseoane.pkgame.backend.service.CalculadoraDano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculadoraDanoTest {

    private PokemonEntity atacante;
    private PokemonEntity defensor;
    private MovimientoEntity movimientoFuego;
    private MovimientoEntity movimientoAgua;
    private MovimientoEntity movimientoNormal;
    private MovimientoEntity movimientoSinPotencia;
    private MovimientoEntity movimientoImpreciso;

    @BeforeEach
    void setUp() {
        // Crear estadísticas de prueba
        EstadisticasEntity estadisticasAtacante = new EstadisticasEntity(1, 120, 80, 180, 100);
        EstadisticasEntity estadisticasDefensor = new EstadisticasEntity(2, 90, 100, 150, 85);
        
        // Crear Pokémon de prueba
        atacante = new PokemonEntity(1, "Charizard", "Fuego", "Volador", estadisticasAtacante);
        defensor = new PokemonEntity(2, "Venusaur", "Planta", "Veneno", estadisticasDefensor);
        
        // Crear movimientos de prueba
        movimientoFuego = new MovimientoEntity(1, "Llamarada", 90, 100, "Fuego");
        movimientoAgua = new MovimientoEntity(2, "Hidrobomba", 110, 80, "Agua");
        movimientoNormal = new MovimientoEntity(3, "Tackle", 40, 100, "Normal");
        movimientoSinPotencia = new MovimientoEntity(4, "Gruñido", 0, 100, "Normal");
        movimientoImpreciso = new MovimientoEntity(5, "Fisura", 999, 30, "Tierra"); // Muy impreciso
    }

    @Test
    void testCalcularDano_MovimientoSuperEficaz() {
        // Arrange - Fuego vs Planta (súper eficaz)
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoFuego);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(2.0, resultado.getEfectividad(), 0.1); // Fuego vs Planta = 2x
        assertTrue(resultado.getMensaje().contains("súper eficaz"));
        assertTrue(resultado.getMensaje().contains("Llamarada"));
    }

    @Test
    void testCalcularDano_MovimientoPocoEficaz() {
        // Arrange - Agua vs Planta (poco eficaz)
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoAgua);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(0.5, resultado.getEfectividad(), 0.1); // Agua vs Planta = 0.5x
        assertTrue(resultado.getMensaje().contains("No es muy eficaz"));
    }

    @Test
    void testCalcularDano_MovimientoNormal() {
        // Arrange - Normal vs cualquier tipo (efectividad normal)
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoNormal);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(1.0, resultado.getEfectividad(), 0.1);
        assertFalse(resultado.getMensaje().contains("súper eficaz"));
        assertFalse(resultado.getMensaje().contains("No es muy eficaz"));
    }

    @Test
    void testCalcularDano_MovimientoSinPotencia() {
        // Arrange - Movimiento de estado sin potencia
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoSinPotencia);
        
        // Assert
        assertEquals(0, resultado.getDano());
        assertEquals(1.0, resultado.getEfectividad(), 0.1);
        assertFalse(resultado.isCritico());
        assertTrue(resultado.getMensaje().contains("Gruñido"));
    }

    @Test
    void testCalcularDano_PokemonDobleTipo() {
        // Arrange - Pokémon con doble tipo (Fuego vs Planta/Veneno)
        // Fuego vs Planta = 2x, Fuego vs Veneno = 1x, Total = 2x
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoFuego);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        // La efectividad debería ser el producto de ambos tipos
        assertEquals(2.0, resultado.getEfectividad(), 0.1); // 2.0 * 1.0 = 2.0
        assertTrue(resultado.getMensaje().contains("súper eficaz"));
    }

    @Test
    void testCalcularDano_PokemonSinSegundoTipo() {
        // Arrange - Pokémon con un solo tipo
        PokemonEntity defensorSimple = new PokemonEntity(3, "Rapidash", "Fuego", null, 
            new EstadisticasEntity(3, 85, 70, 120, 105));
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensorSimple, movimientoFuego);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(0.5, resultado.getEfectividad(), 0.1); // Fuego vs Fuego = 0.5x
        assertTrue(resultado.getMensaje().contains("No es muy eficaz"));
    }

    @RepeatedTest(10)
    void testCalcularDano_FactorAleatorio() {
        // Test para verificar que el factor aleatorio funciona
        // El daño debería variar entre ejecuciones debido al factor aleatorio
        
        // Act
        CalculadoraDano.ResultadoDano resultado1 = CalculadoraDano.calcularDano(atacante, defensor, movimientoNormal);
        CalculadoraDano.ResultadoDano resultado2 = CalculadoraDano.calcularDano(atacante, defensor, movimientoNormal);
        
        // Assert
        assertTrue(resultado1.getDano() > 0);
        assertTrue(resultado2.getDano() > 0);
        // Nota: Debido al factor aleatorio, los resultados pueden variar
        // pero deberían estar en un rango razonable
        assertTrue(resultado1.getDano() >= 1); // Mínimo daño es 1
        assertTrue(resultado2.getDano() >= 1);
    }

    @RepeatedTest(20)
    void testCalcularDano_GolpeCritico() {
        // Test para verificar que los golpes críticos ocurren
        // Con suficientes repeticiones, debería haber al menos un crítico
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoNormal);
        
        // Assert - Solo verificamos que el mecanismo funciona
        assertNotNull(resultado);
        assertTrue(resultado.getDano() > 0);
        // Si es crítico, el mensaje debería contenerlo
        if (resultado.isCritico()) {
            assertTrue(resultado.getMensaje().contains("crítico"));
        }
    }

    @Test
    void testCalcularDano_DanoMinimo() {
        // Test para verificar que el daño mínimo es 1
        
        // Arrange - Atacante muy débil vs defensor muy fuerte
        EstadisticasEntity estadisticasDebiles = new EstadisticasEntity(4, 1, 1, 50, 50);
        EstadisticasEntity estadisticasFuertes = new EstadisticasEntity(5, 50, 255, 200, 100);
        
        PokemonEntity atacanteDebil = new PokemonEntity(4, "Magikarp", "Agua", null, estadisticasDebiles);
        PokemonEntity defensorFuerte = new PokemonEntity(5, "Shuckle", "Bicho", "Roca", estadisticasFuertes);
        
        MovimientoEntity movimientoDebil = new MovimientoEntity(6, "Salpicadura", 1, 100, "Normal");
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacanteDebil, defensorFuerte, movimientoDebil);
        
        // Assert
        assertTrue(resultado.getDano() >= 1); // El daño mínimo siempre debe ser 1
    }

    @Test
    void testCalcularDano_TiposDragon() {
        // Test específico para tipos Dragón
        EstadisticasEntity estadisticas = new EstadisticasEntity(6, 100, 80, 150, 90);
        PokemonEntity dragonAtacante = new PokemonEntity(6, "Dragonite", "Dragón", "Volador", estadisticas);
        PokemonEntity dragonDefensor = new PokemonEntity(7, "Garchomp", "Dragón", "Tierra", estadisticas);
        
        MovimientoEntity movimientoDragon = new MovimientoEntity(7, "Cólera de Dragón", 120, 100, "Dragón");
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(dragonAtacante, dragonDefensor, movimientoDragon);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(2.0, resultado.getEfectividad(), 0.1); // Dragón vs Dragón = 2x
        assertTrue(resultado.getMensaje().contains("súper eficaz"));
    }

    @Test
    void testCalcularDano_TiposVeneno() {
        // Test específico para tipos Veneno
        EstadisticasEntity estadisticas = new EstadisticasEntity(8, 100, 80, 150, 90);
        PokemonEntity venenoAtacante = new PokemonEntity(8, "Crobat", "Veneno", "Volador", estadisticas);
        PokemonEntity plantaDefensor = new PokemonEntity(9, "Tangrowth", "Planta", null, estadisticas);
        
        MovimientoEntity movimientoVeneno = new MovimientoEntity(8, "Bomba Lodo", 90, 100, "Veneno");
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(venenoAtacante, plantaDefensor, movimientoVeneno);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(2.0, resultado.getEfectividad(), 0.1); // Veneno vs Planta = 2x
        assertTrue(resultado.getMensaje().contains("súper eficaz"));
    }

    @Test
    void testCalcularDano_EfectividadCompleja() {
        // Test para efectividad compleja con doble tipo
        // Veneno vs Planta/Veneno = 2x * 0.5x = 1x (normal)
        EstadisticasEntity estadisticas = new EstadisticasEntity(9, 100, 80, 150, 90);
        PokemonEntity venenoAtacante = new PokemonEntity(9, "Nidoking", "Veneno", "Tierra", estadisticas);
        PokemonEntity plantaVenenoDefensor = new PokemonEntity(10, "Vileplume", "Planta", "Veneno", estadisticas);
        
        MovimientoEntity movimientoVeneno = new MovimientoEntity(9, "Púa Venenosa", 80, 100, "Veneno");
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(venenoAtacante, plantaVenenoDefensor, movimientoVeneno);
        
        // Assert
        assertTrue(resultado.getDano() > 0);
        assertEquals(1.0, resultado.getEfectividad(), 0.1); // 2.0 * 0.5 = 1.0
        assertFalse(resultado.getMensaje().contains("súper eficaz"));
        assertFalse(resultado.getMensaje().contains("No es muy eficaz"));
    }

    @Test
    void testResultadoDano_Getters() {
        // Test para verificar que los getters de ResultadoDano funcionan correctamente
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoFuego);
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getDano() >= 0);
        assertTrue(resultado.getEfectividad() > 0);
        assertNotNull(resultado.getMensaje());
        assertFalse(resultado.getMensaje().isEmpty());
        // isCritico puede ser true o false, ambos son válidos
        assertNotNull(resultado.isCritico());
    }

    @Test
    void testCalcularDano_MensajeContieneInformacionRelevante() {
        // Test para verificar que el mensaje contiene información relevante
        
        // Act
        CalculadoraDano.ResultadoDano resultado = CalculadoraDano.calcularDano(atacante, defensor, movimientoFuego);
        
        // Assert
        String mensaje = resultado.getMensaje();
        assertTrue(mensaje.contains(atacante.getNombre())); // Contiene nombre del atacante
        assertTrue(mensaje.contains(movimientoFuego.getNombre())); // Contiene nombre del movimiento
        assertTrue(mensaje.contains("usó")); // Contiene palabra clave del ataque
        
        if (resultado.isCritico()) {
            assertTrue(mensaje.contains("crítico"));
        }
        
        if (resultado.getEfectividad() > 1.0) {
            assertTrue(mensaje.contains("súper eficaz"));
        } else if (resultado.getEfectividad() < 1.0) {
            assertTrue(mensaje.contains("No es muy eficaz"));
        }
    }
}

