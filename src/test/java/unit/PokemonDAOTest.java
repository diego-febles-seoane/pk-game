package unit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.diegofeblesseoane.pkgame.backend.model.EstadisticasEntity;
import es.diegofeblesseoane.pkgame.backend.model.MovimientoEntity;
import es.diegofeblesseoane.pkgame.backend.model.PokemonDAO;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEntity;

class PokemonDAOTest {

    private PokemonDAO pokemonDAO;
    private EstadisticasEntity testEstadisticas;
    private PokemonEntity testPokemon;
    private MovimientoEntity testMovimiento;

    @BeforeEach
    void setUp() throws SQLException {
        // Crear objetos de prueba
        testEstadisticas = new EstadisticasEntity(1, 100, 80, 150, 90);
        testPokemon = new PokemonEntity(1, "Pikachu", "Eléctrico", null, testEstadisticas);
        testMovimiento = new MovimientoEntity(1, "Rayo", 90, 100, "Eléctrico");
        testPokemon.agregarAtaque(testMovimiento);

        // Crear archivo temporal para base de datos de test
        String tempDbPath = createTempDatabase();
        pokemonDAO = new PokemonDAO(tempDbPath);
        inicializarEsquemaTest();
    }

    private String createTempDatabase() throws SQLException {
        try {
            java.io.File tempFile = java.io.File.createTempFile("test_pokemon_", ".db");
            tempFile.deleteOnExit(); // Se eliminará al finalizar el programa
            return tempFile.getAbsolutePath();
        } catch (java.io.IOException e) {
            throw new SQLException("No se pudo crear archivo temporal para test", e);
        }
    }
    
    private void inicializarEsquemaTest() throws SQLException {
        Connection conn = pokemonDAO.conectar();
        try (Statement stmt = conn.createStatement()) {
            
            // Crear tablas básicas para test
            stmt.execute("CREATE TABLE IF NOT EXISTS estadisticas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ataque INTEGER, " +
                        "defensa INTEGER, " +
                        "vida INTEGER, " +
                        "velocidad INTEGER)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS pokemon (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nombre TEXT UNIQUE, " +
                        "tipo1 TEXT, " +
                        "tipo2 TEXT, " +
                        "estadisticas_id INTEGER, " +
                        "FOREIGN KEY(estadisticas_id) REFERENCES estadisticas(id))");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS movimientos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nombre TEXT, " +
                        "potencia INTEGER, " +
                        "precision INTEGER, " +
                        "tipo TEXT)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS pokemon_movimientos (" +
                        "pokemon_id INTEGER, " +
                        "movimiento_id INTEGER, " +
                        "FOREIGN KEY(pokemon_id) REFERENCES pokemon(id), " +
                        "FOREIGN KEY(movimiento_id) REFERENCES movimientos(id))");
                        
            // Insertar datos de prueba
            stmt.execute("INSERT INTO estadisticas (id, ataque, defensa, vida, velocidad) " +
                        "VALUES (1, 100, 80, 150, 90)");
            
            stmt.execute("INSERT INTO pokemon (id, nombre, tipo1, tipo2, estadisticas_id) " +
                        "VALUES (1, 'Pikachu', 'Eléctrico', null, 1)");
                        
            stmt.execute("INSERT INTO movimientos (id, nombre, potencia, precision, tipo) " +
                        "VALUES (1, 'Rayo', 90, 100, 'Eléctrico')");
                        
            stmt.execute("INSERT INTO pokemon_movimientos (pokemon_id, movimiento_id) " +
                        "VALUES (1, 1)");
        } finally {
            // No cerrar la conexión aquí para permitir su reutilización
        }
    }

    @AfterEach
    void tearDown() {
        // No hacer nada - dejar que las conexiones try-with-resources se manejen automáticamente
        pokemonDAO = null;
    }

    @Test
    void testObtenerPokemonPorNombre_PokemonExiste() throws SQLException {
        // Act
        PokemonEntity resultado = pokemonDAO.obtenerPokemonPorNombre("Pikachu");

        // Assert
        assertNotNull(resultado);
        assertEquals("Pikachu", resultado.getNombre());
        assertEquals("Eléctrico", resultado.getTipo1());
        assertNull(resultado.getTipo2());
        assertEquals(100, resultado.getEstadisticas().getAtaque());
        assertEquals(80, resultado.getEstadisticas().getDefensa());
        assertEquals(150, resultado.getEstadisticas().getVida());
        assertEquals(90, resultado.getEstadisticas().getVelocidad());
        assertFalse(resultado.getAtaques().isEmpty());
    }

    @Test
    void testObtenerPokemonPorNombre_PokemonNoExiste() throws SQLException {
        // Act
        PokemonEntity resultado = pokemonDAO.obtenerPokemonPorNombre("Inexistente");

        // Assert
        assertNull(resultado);
    }

    @Test
    void testCargarMovimientos() throws SQLException {
        // Arrange
        PokemonEntity pokemon = pokemonDAO.obtenerPokemonPorNombre("Pikachu");
        
        // Act & Assert
        assertNotNull(pokemon);
        assertFalse(pokemon.getAtaques().isEmpty());
        MovimientoEntity movimiento = pokemon.getAtaques().get(0);
        assertEquals("Rayo", movimiento.getNombre());
        assertEquals(90, movimiento.getPotencia());
        assertEquals(100, movimiento.getPrecision());
        assertEquals("Eléctrico", movimiento.getTipo());
    }

    @Test
    void testInsertarPokemon_Exitoso() throws SQLException {
        // Arrange
        EstadisticasEntity nuevasEstadisticas = new EstadisticasEntity(120, 70, 180, 95);
        PokemonEntity nuevoPokemon = new PokemonEntity("Raichu", "Eléctrico", null, nuevasEstadisticas);
        MovimientoEntity nuevoMovimiento = new MovimientoEntity("Trueno", 110, 70, "Eléctrico");
        nuevoPokemon.agregarAtaque(nuevoMovimiento);

        // Act
        boolean resultado = pokemonDAO.insertarPokemon(nuevoPokemon);

        // Assert
        assertTrue(resultado);
        
        // Verificar que se insertó correctamente
        PokemonEntity pokemonInsertado = pokemonDAO.obtenerPokemonPorNombre("Raichu");
        assertNotNull(pokemonInsertado);
        assertEquals("Raichu", pokemonInsertado.getNombre());
        assertEquals("Eléctrico", pokemonInsertado.getTipo1());
    }

    @Test
    void testPokemonConDobleTipo() throws SQLException {
        // Arrange
        EstadisticasEntity estadisticas = new EstadisticasEntity(85, 75, 130, 100);
        PokemonEntity charizard = new PokemonEntity("Charizard", "Fuego", "Volador", estadisticas);

        // Act
        boolean resultado = pokemonDAO.insertarPokemon(charizard);

        // Assert
        assertTrue(resultado);
        
        PokemonEntity pokemonInsertado = pokemonDAO.obtenerPokemonPorNombre("Charizard");
        assertNotNull(pokemonInsertado);
        assertEquals("Fuego", pokemonInsertado.getTipo1());
        assertEquals("Volador", pokemonInsertado.getTipo2());
    }

    @Test
    void testManejoDeExcepcionEnConexion() {
        // Act & Assert - Verificar que la conexión falla apropiadamente
        assertThrows(SQLException.class, () -> {
            // Arrange - Esto debería lanzar SQLException inmediatamente
            PokemonDAO daoConErrorConexion = new PokemonDAO("ruta/inexistente/bd.db");
        }, "Debería lanzar SQLException al crear DAO con ruta inválida");
    }
}

