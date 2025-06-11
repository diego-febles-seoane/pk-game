package unit;

import es.diegofeblesseoane.pkgame.backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PokemonEntityTest {

    private EstadisticasEntity testEstadisticas;
    private PokemonEntity pokemon;
    private MovimientoEntity movimiento1;
    private MovimientoEntity movimiento2;
    private MovimientoEntity movimiento3;
    private MovimientoEntity movimiento4;
    private MovimientoEntity movimiento5; // Para test de límite

    @BeforeEach
    void setUp() {
        testEstadisticas = new EstadisticasEntity(1, 100, 80, 150, 90);
        pokemon = new PokemonEntity(1, "Pikachu", "Eléctrico", null, testEstadisticas);
        
        movimiento1 = new MovimientoEntity(1, "Rayo", 90, 100, "Eléctrico");
        movimiento2 = new MovimientoEntity(2, "Ataque Rápido", 40, 100, "Normal");
        movimiento3 = new MovimientoEntity(3, "Trueno", 110, 70, "Eléctrico");
        movimiento4 = new MovimientoEntity(4, "Doble Equipo", 0, 100, "Normal");
        movimiento5 = new MovimientoEntity(5, "Voltio Cruel", 120, 100, "Eléctrico");
    }

    @Test
    void testConstructorVacio() {
        // Act
        PokemonEntity pokemonVacio = new PokemonEntity();
        
        // Assert
        assertNotNull(pokemonVacio.getAtaques());
        assertTrue(pokemonVacio.getAtaques().isEmpty());
    }

    @Test
    void testConstructorConParametros() {
        // Act
        PokemonEntity nuevoPokemon = new PokemonEntity("Charizard", "Fuego", "Volador", testEstadisticas);
        
        // Assert
        assertEquals("Charizard", nuevoPokemon.getNombre());
        assertEquals("Fuego", nuevoPokemon.getTipo1());
        assertEquals("Volador", nuevoPokemon.getTipo2());
        assertEquals(testEstadisticas, nuevoPokemon.getEstadisticas());
        assertEquals(testEstadisticas.getVida(), nuevoPokemon.getVidaActual());
        assertNotNull(nuevoPokemon.getAtaques());
        assertTrue(nuevoPokemon.getAtaques().isEmpty());
    }

    @Test
    void testConstructorCompletoConId() {
        // Act
        PokemonEntity nuevoPokemon = new PokemonEntity(99, "Blastoise", "Agua", null, testEstadisticas);
        
        // Assert
        assertEquals(99, nuevoPokemon.getId());
        assertEquals("Blastoise", nuevoPokemon.getNombre());
        assertEquals("Agua", nuevoPokemon.getTipo1());
        assertNull(nuevoPokemon.getTipo2());
        assertEquals(testEstadisticas, nuevoPokemon.getEstadisticas());
        assertEquals(testEstadisticas.getVida(), nuevoPokemon.getVidaActual());
    }

    @Test
    void testAgregarAtaque_Exitoso() {
        // Act
        pokemon.agregarAtaque(movimiento1);
        
        // Assert
        assertEquals(1, pokemon.getAtaques().size());
        assertTrue(pokemon.getAtaques().contains(movimiento1));
    }

    @Test
    void testAgregarMultiplesAtaques() {
        // Act
        pokemon.agregarAtaque(movimiento1);
        pokemon.agregarAtaque(movimiento2);
        pokemon.agregarAtaque(movimiento3);
        pokemon.agregarAtaque(movimiento4);
        
        // Assert
        assertEquals(4, pokemon.getAtaques().size());
        assertTrue(pokemon.getAtaques().contains(movimiento1));
        assertTrue(pokemon.getAtaques().contains(movimiento2));
        assertTrue(pokemon.getAtaques().contains(movimiento3));
        assertTrue(pokemon.getAtaques().contains(movimiento4));
    }

    @Test
    void testAgregarAtaque_ExcedeLimite() {
        // Arrange - Agregar 4 movimientos primero
        pokemon.agregarAtaque(movimiento1);
        pokemon.agregarAtaque(movimiento2);
        pokemon.agregarAtaque(movimiento3);
        pokemon.agregarAtaque(movimiento4);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            pokemon.agregarAtaque(movimiento5);
        });
        
        // Verificar que sigue teniendo solo 4 movimientos
        assertEquals(4, pokemon.getAtaques().size());
    }

    @Test
    void testEliminarAtaque() {
        // Arrange
        pokemon.agregarAtaque(movimiento1);
        pokemon.agregarAtaque(movimiento2);
        
        // Act
        pokemon.eliminarAtaque(movimiento1);
        
        // Assert
        assertEquals(1, pokemon.getAtaques().size());
        assertFalse(pokemon.getAtaques().contains(movimiento1));
        assertTrue(pokemon.getAtaques().contains(movimiento2));
    }

    @Test
    void testTieneDobleTipo_ConSegundoTipo() {
        // Arrange
        PokemonEntity pokemonDoble = new PokemonEntity(2, "Charizard", "Fuego", "Volador", testEstadisticas);
        
        // Act & Assert
        assertTrue(pokemonDoble.tieneDobleTipo());
    }

    @Test
    void testTieneDobleTipo_SinSegundoTipo() {
        // Act & Assert
        assertFalse(pokemon.tieneDobleTipo()); // Pikachu solo tiene tipo Eléctrico
    }

    @Test
    void testTieneDobleTipo_SegundoTipoVacio() {
        // Arrange
        PokemonEntity pokemonTipoVacio = new PokemonEntity(3, "Test", "Normal", "", testEstadisticas);
        
        // Act & Assert
        assertFalse(pokemonTipoVacio.tieneDobleTipo());
    }

    @Test
    void testRecibirDano() {
        // Arrange
        int vidaInicial = pokemon.getVidaActual();
        int dano = 50;
        
        // Act
        pokemon.recibirDaño(dano);
        
        // Assert
        assertEquals(vidaInicial - dano, pokemon.getVidaActual());
    }

    @Test
    void testRecibirDano_NoPermiteVidaNegativa() {
        // Arrange
        int danoExcesivo = pokemon.getVidaActual() + 100;
        
        // Act
        pokemon.recibirDaño(danoExcesivo);
        
        // Assert
        assertEquals(0, pokemon.getVidaActual());
    }

    @Test
    void testCurar() {
        // Arrange
        pokemon.recibirDaño(100); // Reducir vida primero
        int vidaActual = pokemon.getVidaActual();
        int curacion = 30;
        
        // Act
        pokemon.curar(curacion);
        
        // Assert
        assertEquals(vidaActual + curacion, pokemon.getVidaActual());
    }

    @Test
    void testCurar_NoExcedeVidaMaxima() {
        // Arrange
        pokemon.recibirDaño(20); // Reducir vida un poco
        int vidaMaxima = pokemon.getEstadisticas().getVida();
        int curacionExcesiva = 1000;
        
        // Act
        pokemon.curar(curacionExcesiva);
        
        // Assert
        assertEquals(vidaMaxima, pokemon.getVidaActual());
    }

    @Test
    void testRestaurarVida() {
        // Arrange
        pokemon.recibirDaño(100);
        int vidaMaxima = pokemon.getEstadisticas().getVida();
        
        // Act
        pokemon.restaurarVida();
        
        // Assert
        assertEquals(vidaMaxima, pokemon.getVidaActual());
    }

    @Test
    void testEstaDebilitado_VidaCero() {
        // Arrange
        pokemon.recibirDaño(pokemon.getVidaActual());
        
        // Act & Assert
        assertTrue(pokemon.estaDebilitado());
        assertEquals(0, pokemon.getVidaActual());
    }

    @Test
    void testEstaDebilitado_ConVida() {
        // Act & Assert
        assertFalse(pokemon.estaDebilitado());
        assertTrue(pokemon.getVidaActual() > 0);
    }

    @Test
    void testGetPorcentajeVida_VidaCompleta() {
        // Act
        double porcentaje = pokemon.getPorcentajeVida();
        
        // Assert
        assertEquals(100.0, porcentaje, 0.1);
    }

    @Test
    void testGetPorcentajeVida_VidaMedia() {
        // Arrange
        int vidaMaxima = pokemon.getEstadisticas().getVida();
        pokemon.recibirDaño(vidaMaxima / 2); // Reducir vida a la mitad
        
        // Act
        double porcentaje = pokemon.getPorcentajeVida();
        
        // Assert
        assertEquals(50.0, porcentaje, 1.0); // Permitir pequeña variación por división entera
    }

    @Test
    void testGetPorcentajeVida_VidaCero() {
        // Arrange
        pokemon.recibirDaño(pokemon.getVidaActual());
        
        // Act
        double porcentaje = pokemon.getPorcentajeVida();
        
        // Assert
        assertEquals(0.0, porcentaje, 0.1);
    }

    @Test
    void testGetPorcentajeVida_VidaMaximaCero() {
        // Arrange - Crear estadísticas con vida 0 (caso edge)
        EstadisticasEntity estadisticasInvalidas = new EstadisticasEntity(99, 100, 80, 0, 90);
        PokemonEntity pokemonInvalido = new PokemonEntity(99, "Test", "Normal", null, estadisticasInvalidas);
        
        // Act
        double porcentaje = pokemonInvalido.getPorcentajeVida();
        
        // Assert
        assertEquals(0.0, porcentaje, 0.1);
    }

    @Test
    void testSetVidaActual_ValorValido() {
        // Act
        pokemon.setVidaActual(75);
        
        // Assert
        assertEquals(75, pokemon.getVidaActual());
    }

    @Test
    void testSetVidaActual_ValorNegativo() {
        // Act
        pokemon.setVidaActual(-50);
        
        // Assert
        assertEquals(0, pokemon.getVidaActual()); // No permite valores negativos
    }

    @Test
    void testSetAtaques() {
        // Arrange
        List<MovimientoEntity> nuevosAtaques = new ArrayList<>();
        nuevosAtaques.add(movimiento1);
        nuevosAtaques.add(movimiento2);
        
        // Act
        pokemon.setAtaques(nuevosAtaques);
        
        // Assert
        assertEquals(2, pokemon.getAtaques().size());
        assertTrue(pokemon.getAtaques().contains(movimiento1));
        assertTrue(pokemon.getAtaques().contains(movimiento2));
    }

    @Test
    void testEquals_MismoId() {
        // Arrange - Crear Pokémon con mismos datos (no solo ID)
        PokemonEntity otroPokemon = new PokemonEntity(1, "Pikachu", "Eléctrico", null, testEstadisticas);
        
        // Act & Assert
        assertEquals(pokemon, otroPokemon); // Mismo ID y datos
    }

    @Test
    void testEquals_DiferenteId() {
        // Arrange
        PokemonEntity otroPokemon = new PokemonEntity(2, "Pikachu", "Eléctrico", null, testEstadisticas);
        
        // Act & Assert
        assertNotEquals(pokemon, otroPokemon); // Diferente ID
    }

    @Test
    void testEquals_MismoObjeto() {
        // Act & Assert
        assertEquals(pokemon, pokemon);
    }

    @Test
    void testEquals_ObjetoNull() {
        // Act & Assert
        assertNotEquals(pokemon, null);
    }

    @Test
    void testEquals_ClaseDiferente() {
        // Act & Assert
        assertNotEquals(pokemon, "No es un Pokemon");
    }

    @Test
    void testHashCode_ConsistentePorId() {
        // Arrange - Crear Pokémon con mismos datos
        PokemonEntity otroPokemon = new PokemonEntity(1, "Pikachu", "Eléctrico", null, testEstadisticas);
        
        // Act & Assert
        assertEquals(pokemon.hashCode(), otroPokemon.hashCode());
    }

    @Test
    void testToString_ContieneInformacionBasica() {
        // Act
        String resultado = pokemon.toString();
        
        // Assert
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("nombre='Pikachu'"));
        assertTrue(resultado.contains("tipo1='Eléctrico'"));
        assertTrue(resultado.contains("PokemonEntity"));
    }

    @Test
    void testGettersYSetters() {
        // Test todos los getters y setters
        
        // ID
        pokemon.setId(999);
        assertEquals(999, pokemon.getId());
        
        // Nombre
        pokemon.setNombre("Raichu");
        assertEquals("Raichu", pokemon.getNombre());
        
        // Tipo1
        pokemon.setTipo1("Fuego");
        assertEquals("Fuego", pokemon.getTipo1());
        
        // Tipo2
        pokemon.setTipo2("Volador");
        assertEquals("Volador", pokemon.getTipo2());
        
        // Estadísticas
        EstadisticasEntity nuevasStats = new EstadisticasEntity(2, 110, 90, 160, 95);
        pokemon.setEstadisticas(nuevasStats);
        assertEquals(nuevasStats, pokemon.getEstadisticas());
    }

    @Test
    void testCombateSumulado_DanoYCuracion() {
        // Arrange
        int vidaInicial = pokemon.getVidaActual();
        
        // Act - Simular combate
        pokemon.recibirDaño(60);
        assertEquals(vidaInicial - 60, pokemon.getVidaActual());
        
        pokemon.curar(30);
        assertEquals(vidaInicial - 30, pokemon.getVidaActual());
        
        pokemon.recibirDaño(50);
        assertEquals(vidaInicial - 80, pokemon.getVidaActual());
        
        // Assert
        assertFalse(pokemon.estaDebilitado());
        assertTrue(pokemon.getVidaActual() > 0);
    }

    @Test
    void testEstadoInicialVidaActual() {
        // Verify que la vida actual se inicializa correctamente
        assertEquals(testEstadisticas.getVida(), pokemon.getVidaActual());
        assertEquals(150, pokemon.getVidaActual()); // Vida definida en testEstadisticas
    }
}

