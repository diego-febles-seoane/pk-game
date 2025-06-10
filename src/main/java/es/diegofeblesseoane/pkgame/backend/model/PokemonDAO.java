package es.diegofeblesseoane.pkgame.backend.model;

import es.diegofeblesseoane.pkgame.backend.model.abtrastas.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO extends Conexion {
    
    /**
     * Constructor
     * @param rutaArchivoBD ruta de la base de datos
     * @throws SQLException error controlado
     */
    public PokemonDAO(String rutaArchivoBD) throws SQLException {
        super(rutaArchivoBD);
    }
    
    /**
     * Obtiene un Pokémon por su nombre
     * @param nombre nombre del Pokémon
     * @return PokemonEntity completo con estadísticas y movimientos
     * @throws SQLException error controlado
     */
    public PokemonEntity obtenerPokemonPorNombre(String nombre) throws SQLException {
        String sql = "SELECT p.id, p.nombre, p.tipo1, p.tipo2, " +
                    "e.id as est_id, e.ataque, e.defensa, e.vida, e.velocidad " +
                    "FROM pokemon p " +
                    "JOIN estadisticas e ON p.estadisticas_id = e.id " +
                    "WHERE p.nombre = ?";
        
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                EstadisticasEntity stats = new EstadisticasEntity(
                    rs.getInt("est_id"),
                    rs.getInt("ataque"),
                    rs.getInt("defensa"),
                    rs.getInt("vida"),
                    rs.getInt("velocidad")
                );
                
                PokemonEntity pokemon = new PokemonEntity(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo1"),
                    rs.getString("tipo2"),
                    stats
                );
                
                // Cargar movimientos
                cargarMovimientos(pokemon);
                
                return pokemon;
            }
        }
        return null;
    }
    
    /**
     * Obtiene todos los Pokémon de la base de datos
     * @return Lista de todos los Pokémon
     * @throws SQLException error controlado
     */
    public List<PokemonEntity> obtenerTodosLosPokemon() throws SQLException {
        List<PokemonEntity> pokemonList = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.tipo1, p.tipo2, " +
                    "e.id as est_id, e.ataque, e.defensa, e.vida, e.velocidad " +
                    "FROM pokemon p " +
                    "JOIN estadisticas e ON p.estadisticas_id = e.id";
        
        try (Connection conn = conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                EstadisticasEntity stats = new EstadisticasEntity(
                    rs.getInt("est_id"),
                    rs.getInt("ataque"),
                    rs.getInt("defensa"),
                    rs.getInt("vida"),
                    rs.getInt("velocidad")
                );
                
                PokemonEntity pokemon = new PokemonEntity(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo1"),
                    rs.getString("tipo2"),
                    stats
                );
                
                // Cargar movimientos
                cargarMovimientos(pokemon);
                
                pokemonList.add(pokemon);
            }
        }
        return pokemonList;
    }
    
    /**
     * Inserta un nuevo Pokémon en la base de datos
     * @param pokemon Pokémon a insertar
     * @return true si se insertó correctamente
     * @throws SQLException error controlado
     */
    public boolean insertarPokemon(PokemonEntity pokemon) throws SQLException {
        Connection conn = null;
        try {
            conn = conectar();
            conn.setAutoCommit(false);
            
            // Insertar estadísticas primero
            String sqlStats = "INSERT INTO estadisticas (ataque, defensa, vida, velocidad) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtStats = conn.prepareStatement(sqlStats, Statement.RETURN_GENERATED_KEYS);
            EstadisticasEntity stats = pokemon.getEstadisticas();
            stmtStats.setInt(1, stats.getAtaque());
            stmtStats.setInt(2, stats.getDefensa());
            stmtStats.setInt(3, stats.getVida());
            stmtStats.setInt(4, stats.getVelocidad());
            stmtStats.executeUpdate();
            
            ResultSet rsStats = stmtStats.getGeneratedKeys();
            int statsId = 0;
            if (rsStats.next()) {
                statsId = rsStats.getInt(1);
            }
            
            // Insertar Pokémon
            String sqlPokemon = "INSERT INTO pokemon (nombre, tipo1, tipo2, estadisticas_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtPokemon = conn.prepareStatement(sqlPokemon, Statement.RETURN_GENERATED_KEYS);
            stmtPokemon.setString(1, pokemon.getNombre());
            stmtPokemon.setString(2, pokemon.getTipo1());
            stmtPokemon.setString(3, pokemon.getTipo2());
            stmtPokemon.setInt(4, statsId);
            stmtPokemon.executeUpdate();
            
            ResultSet rsPokemon = stmtPokemon.getGeneratedKeys();
            int pokemonId = 0;
            if (rsPokemon.next()) {
                pokemonId = rsPokemon.getInt(1);
            }
            
            // Insertar movimientos y relaciones
            for (MovimientoEntity movimiento : pokemon.getAtaques()) {
                insertarMovimientoYRelacion(conn, movimiento, pokemonId);
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                cerrar();
            }
        }
    }
    
    /**
     * Carga los movimientos de un Pokémon
     * @param pokemon Pokémon al que cargar los movimientos
     * @throws SQLException error controlado
     */
    private void cargarMovimientos(PokemonEntity pokemon) throws SQLException {
        String sql = "SELECT m.id, m.nombre, m.potencia, m.precision, m.tipo " +
                    "FROM movimientos m " +
                    "JOIN pokemon_movimientos pm ON m.id = pm.movimiento_id " +
                    "WHERE pm.pokemon_id = ?";
        
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pokemon.getId());
            ResultSet rs = stmt.executeQuery();
            
            List<MovimientoEntity> movimientos = new ArrayList<>();
            while (rs.next()) {
                MovimientoEntity movimiento = new MovimientoEntity(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("potencia"),
                    rs.getInt("precision"),
                    rs.getString("tipo")
                );
                movimientos.add(movimiento);
            }
            pokemon.setAtaques(movimientos);
        }
    }
    
    /**
     * Inserta un movimiento y su relación con un Pokémon
     * @param conn conexión a la base de datos
     * @param movimiento movimiento a insertar
     * @param pokemonId ID del Pokémon
     * @throws SQLException error controlado
     */
    private void insertarMovimientoYRelacion(Connection conn, MovimientoEntity movimiento, int pokemonId) throws SQLException {
        // Verificar si el movimiento ya existe
        String sqlCheck = "SELECT id FROM movimientos WHERE nombre = ?";
        PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
        stmtCheck.setString(1, movimiento.getNombre());
        ResultSet rsCheck = stmtCheck.executeQuery();
        
        int movimientoId;
        if (rsCheck.next()) {
            movimientoId = rsCheck.getInt("id");
        } else {
            // Insertar nuevo movimiento
            String sqlMovimiento = "INSERT INTO movimientos (nombre, potencia, precision, tipo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtMovimiento = conn.prepareStatement(sqlMovimiento, Statement.RETURN_GENERATED_KEYS);
            stmtMovimiento.setString(1, movimiento.getNombre());
            stmtMovimiento.setInt(2, movimiento.getPotencia());
            stmtMovimiento.setInt(3, movimiento.getPrecision());
            stmtMovimiento.setString(4, movimiento.getTipo());
            stmtMovimiento.executeUpdate();
            
            ResultSet rsMovimiento = stmtMovimiento.getGeneratedKeys();
            if (rsMovimiento.next()) {
                movimientoId = rsMovimiento.getInt(1);
            } else {
                throw new SQLException("Error al insertar movimiento");
            }
        }
        
        // Insertar relación
        String sqlRelacion = "INSERT INTO pokemon_movimientos (pokemon_id, movimiento_id) VALUES (?, ?)";
        PreparedStatement stmtRelacion = conn.prepareStatement(sqlRelacion);
        stmtRelacion.setInt(1, pokemonId);
        stmtRelacion.setInt(2, movimientoId);
        stmtRelacion.executeUpdate();
    }
}

