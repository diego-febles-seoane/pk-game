package unit;

import es.diegofeblesseoane.pkgame.backend.model.*;
import es.diegofeblesseoane.pkgame.backend.service.CombateService;
import es.diegofeblesseoane.pkgame.backend.service.CalculadoraDano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockConstruction;

@ExtendWith(MockitoExtension.class)
class CombateServiceTest {

    @Mock
    private PokemonDAO mockPokemonDAO;

    private CombateService combateService;
    private EstadisticasEntity testEstadisticas;
    private PokemonEntity testPokemonJugador;
    private PokemonEntity testPokemonIA;
    private MovimientoEntity testMovimiento;
    private TipoPokemon tipoFuego;
    private TipoPokemon tipoAgua;

    @BeforeEach
    void setUp() throws SQLException {
        combateService = new CombateService();
        
        // Crear objetos de prueba
        testEstadisticas = new EstadisticasEntity(1, 100, 80, 150, 90);
        EstadisticasEntity estadisticasIA = new EstadisticasEntity(2, 95, 85, 140, 85);
        
        testMovimiento = new MovimientoEntity(1, "Llamarada", 90, 100, "Fuego");
        MovimientoEntity movimientoIA = new MovimientoEntity(2, "Hidrobomba", 110, 80, "Agua");
        
        testPokemonJugador = new PokemonEntity(1, "Charizard", "Fuego", "Volador", testEstadisticas);
        testPokemonJugador.agregarAtaque(testMovimiento);
        
        testPokemonIA = new PokemonEntity(2, "Blastoise", "Agua", null, estadisticasIA);
        testPokemonIA.agregarAtaque(movimientoIA);
        
        tipoFuego = TipoPokemon.FUEGO;
        tipoAgua = TipoPokemon.AGUA;
    }
    @Test
    void testObtenerPokemonPorTipo() throws SQLException {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo())).thenReturn(testPokemonJugador);
                })) {
            
            // Act
            PokemonEntity resultado = combateService.obtenerPokemonPorTipo(tipoFuego);
            
            // Assert
            assertNotNull(resultado);
            assertEquals(testPokemonJugador.getNombre(), resultado.getNombre());
            assertEquals(testPokemonJugador.getTipo1(), resultado.getTipo1());
            assertEquals(testPokemonJugador.getEstadisticas().getVida(), resultado.getVidaActual());
        }
    }
    @Test
    void testIniciarCombate_Exitoso() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            // Act
            CombateService.IniciarCombateResult resultado = combateService.iniciarCombate(tipoFuego, tipoAgua);
            
            // Assert
            assertNotNull(resultado);
            assertNotNull(resultado.getPokemonJugador());
            assertNotNull(resultado.getPokemonIA());
            assertTrue(resultado.getMensaje().contains("vs"));
            assertTrue(combateService.isCombateActivo());
        }
    }

    @Test
    void testIniciarCombate_ErrorAlCargarPokemon() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(anyString())).thenReturn(null);
                })) {
            
            // Act
            CombateService.IniciarCombateResult resultado = combateService.iniciarCombate(tipoFuego, tipoAgua);
            
            // Assert
            assertNotNull(resultado);
            assertNull(resultado.getPokemonJugador());
            assertNull(resultado.getPokemonIA());
            assertTrue(resultado.getMensaje().contains("Error"));
            assertFalse(combateService.isCombateActivo());
        }
    }

    @Test
    void testObtenerEleccionIA_SinTipoJugador() {
        // Act
        TipoPokemon eleccionIA = combateService.obtenerEleccionIA();
        
        // Assert
        assertNotNull(eleccionIA);
        assertTrue(eleccionIA instanceof TipoPokemon);
    }

    @Test
    void testObtenerEleccionIA_ConTipoJugador() {
        // Act
        TipoPokemon eleccionIA = combateService.obtenerEleccionIA(tipoFuego);
        
        // Assert
        assertNotNull(eleccionIA);
        assertTrue(eleccionIA instanceof TipoPokemon);
    }

    @Test
    void testObtenerMensajeEleccionIA() {
        // Act
        String mensaje = combateService.obtenerMensajeEleccionIA(tipoFuego);
        
        // Assert
        assertNotNull(mensaje);
        assertFalse(mensaje.isEmpty());
        assertTrue(mensaje.contains(tipoFuego.getPokemonRepresentativo()) || 
                  mensaje.contains(tipoFuego.getEmoji()));
    }

    @Test
    void testEjecutarTurnoJugador_TurnoValido() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            CalculadoraDano.ResultadoDano mockResultado = new CalculadoraDano.ResultadoDano(
                50, 1.0, false, "¡Charizard usó Llamarada!");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(mockResultado);
            
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoJugador(0);
            
            // Assert
            assertTrue(resultado.isExito());
            assertFalse(resultado.isCombateTerminado());
            assertNotNull(resultado.getMensaje());
            assertFalse(combateService.isTurnoJugador()); // Turno cambia a IA
            }
        }
    }

    @Test
    void testEjecutarTurnoJugador_MovimientoInvalido() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoJugador(5); // Índice inválido
            
            // Assert
            assertFalse(resultado.isExito());
            assertTrue(resultado.getMensaje().contains("inválido"));
        }
    }

    @Test
    void testEjecutarTurnoJugador_NoEsTurnoJugador() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            // Simular que no es turno del jugador
            // Ejecutar un turno primero para cambiar el turno
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            CalculadoraDano.ResultadoDano mockResultado = new CalculadoraDano.ResultadoDano(
                50, 1.0, false, "Ataque");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(mockResultado);
            
            combateService.ejecutarTurnoJugador(0); // Cambia turno a IA
            
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoJugador(0);
            
            // Assert
            assertFalse(resultado.isExito());
            assertTrue(resultado.getMensaje().contains("No es tu turno"));
            }
        }
    }

    @Test
    void testEjecutarTurnoIA_TurnoValido() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            // Cambiar turno a IA
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            CalculadoraDano.ResultadoDano mockResultado = new CalculadoraDano.ResultadoDano(
                30, 1.0, false, "¡Blastoise usó Hidrobomba!");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(mockResultado);
            
            // Hacer que sea turno de la IA
            combateService.ejecutarTurnoJugador(0); // Esto cambia el turno a IA
            
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoIA();
            
            // Assert
            assertTrue(resultado.isExito());
            assertFalse(resultado.isCombateTerminado());
            assertNotNull(resultado.getMensaje());
            assertTrue(combateService.isTurnoJugador()); // Turno vuelve al jugador
            }
        }
    }

    @Test
    void testEjecutarTurnoJugador_PokemonIADerrotado() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            // Simular daño que derrote al Pokémon IA
            CalculadoraDano.ResultadoDano mockResultado = new CalculadoraDano.ResultadoDano(
                200, 2.0, true, "¡Golpe crítico súper eficaz!");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(mockResultado);
            
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoJugador(0);
            
            // Assert
            assertTrue(resultado.isExito());
            assertTrue(resultado.isCombateTerminado());
            assertTrue(resultado.getMensaje().contains("ganado"));
            assertFalse(combateService.isCombateActivo());
            }
        }
    }

    @Test
    void testEjecutarTurnoIA_PokemonJugadorDerrotado() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            // Primero cambiar turno a IA
            CalculadoraDano.ResultadoDano resultadoJugador = new CalculadoraDano.ResultadoDano(
                10, 1.0, false, "Ataque débil");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(resultadoJugador);
            
            combateService.ejecutarTurnoJugador(0); // Cambia turno a IA
            
            // Simular daño que derrote al Pokémon jugador
            CalculadoraDano.ResultadoDano resultadoIA = new CalculadoraDano.ResultadoDano(
                200, 2.0, true, "¡Golpe crítico súper eficaz!");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(resultadoIA);
            
            // Act
            CombateService.TurnoResult resultado = combateService.ejecutarTurnoIA();
            
            // Assert
            assertTrue(resultado.isExito());
            assertTrue(resultado.isCombateTerminado());
            assertTrue(resultado.getMensaje().contains("perdido"));
            assertFalse(combateService.isCombateActivo());
            }
        }
    }

    @Test
    void testFinalizarCombate() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
            assertTrue(combateService.isCombateActivo());
            
            // Act
            combateService.finalizarCombate();
            
            // Assert
            assertFalse(combateService.isCombateActivo());
            assertNull(combateService.getPokemonJugador());
            assertNull(combateService.getPokemonIA());
        }
    }


    @Test
    void testMultiplesTurnosAlternando() {
        // Arrange
        try (MockedConstruction<PokemonDAO> mockedConstruction = mockConstruction(PokemonDAO.class,
                (mock, context) -> {
                    when(mock.obtenerPokemonPorNombre(tipoFuego.getPokemonRepresentativo()))
                        .thenReturn(testPokemonJugador);
                    when(mock.obtenerPokemonPorNombre(tipoAgua.getPokemonRepresentativo()))
                        .thenReturn(testPokemonIA);
                })) {
            
            combateService.iniciarCombate(tipoFuego, tipoAgua);
        
            try (MockedStatic<CalculadoraDano> mockedCalculadora = mockStatic(CalculadoraDano.class)) {
            CalculadoraDano.ResultadoDano mockResultado = new CalculadoraDano.ResultadoDano(
                20, 1.0, false, "Ataque normal");
            mockedCalculadora.when(() -> CalculadoraDano.calcularDano(any(), any(), any()))
                .thenReturn(mockResultado);
            
            boolean turnoInicialJugador = combateService.isTurnoJugador();
            
            // Act & Assert - Turno 1
            if (turnoInicialJugador) {
                CombateService.TurnoResult turno1 = combateService.ejecutarTurnoJugador(0);
                assertTrue(turno1.isExito());
                assertFalse(combateService.isTurnoJugador()); // Cambió a IA
                
                CombateService.TurnoResult turno2 = combateService.ejecutarTurnoIA();
                assertTrue(turno2.isExito());
                assertTrue(combateService.isTurnoJugador()); // Volvió al jugador
            } else {
                CombateService.TurnoResult turno1 = combateService.ejecutarTurnoIA();
                assertTrue(turno1.isExito());
                assertTrue(combateService.isTurnoJugador()); // Cambió al jugador
                
                CombateService.TurnoResult turno2 = combateService.ejecutarTurnoJugador(0);
                assertTrue(turno2.isExito());
                assertFalse(combateService.isTurnoJugador()); // Volvió a IA
            }
            }
        }
    }

}

