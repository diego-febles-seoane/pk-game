package integration.demos;

import java.sql.SQLException;
import java.util.List;

import es.diegofeblesseoane.pkgame.backend.model.EstadisticasEntity;
import es.diegofeblesseoane.pkgame.backend.model.MovimientoEntity;
import es.diegofeblesseoane.pkgame.backend.model.PokemonDAO;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEntity;

public class PokemonTest {
    
    public static void main(String[] args) {
        try {
            // Crear DAO para acceso a la base de datos
            PokemonDAO pokemonDAO = new PokemonDAO("src/main/resources/db/usuarios.db");
            
            System.out.println("=== PRUEBA DEL SISTEMA POKÉMON ===");
            System.out.println();
            
            // Obtener Charmander de la base de datos
            System.out.println("1. Obteniendo Charmander de la base de datos:");
            PokemonEntity charmander = pokemonDAO.obtenerPokemonPorNombre("Charmander");
            
            if (charmander != null) {
                mostrarPokemon(charmander);
            } else {
                System.out.println("No se encontró Charmander en la base de datos.");
            }
            
            System.out.println();
            System.out.println("2. Listando todos los Pokémon en la base de datos:");
            List<PokemonEntity> todosPokemon = pokemonDAO.obtenerTodosLosPokemon();
            
            if (todosPokemon.isEmpty()) {
                System.out.println("No hay Pokémon en la base de datos.");
            } else {
                for (PokemonEntity pokemon : todosPokemon) {
                    System.out.println("- " + pokemon.getNombre() + " (" + pokemon.getTipo1() + 
                                     (pokemon.getTipo2() != null ? "/" + pokemon.getTipo2() : "") + ")");
                }
            }
            
            System.out.println();
            System.out.println("3. Creando un nuevo Pokémon (Squirtle) programáticamente:");
            
            // Crear estadísticas para Squirtle
            EstadisticasEntity statsSquirtle = new EstadisticasEntity(48, 65, 44, 43);
            
            // Crear Squirtle
            PokemonEntity squirtle = new PokemonEntity("Squirtle", "Agua", null, statsSquirtle);
            
            // Crear movimientos para Squirtle
            MovimientoEntity tackle = new MovimientoEntity("Placaje", 40, 100, "Normal");
            MovimientoEntity waterGun = new MovimientoEntity("Pistola Agua", 40, 100, "Agua");
            MovimientoEntity withdraw = new MovimientoEntity("Refugio", 0, 100, "Agua");
            MovimientoEntity bubble = new MovimientoEntity("Burbuja", 40, 100, "Agua");
            
            // Añadir movimientos a Squirtle
            squirtle.agregarAtaque(tackle);
            squirtle.agregarAtaque(waterGun);
            squirtle.agregarAtaque(withdraw);
            squirtle.agregarAtaque(bubble);
            
            mostrarPokemon(squirtle);
            
            // Insertar Squirtle en la base de datos
            System.out.println();
            System.out.println("4. Insertando Squirtle en la base de datos...");
            boolean insertado = pokemonDAO.insertarPokemon(squirtle);
            
            if (insertado) {
                System.out.println("¡Squirtle ha sido añadido exitosamente a la base de datos!");
                
                // Verificar que se insertó correctamente
                PokemonEntity squirtleDB = pokemonDAO.obtenerPokemonPorNombre("Squirtle");
                if (squirtleDB != null) {
                    System.out.println("Verificación - Squirtle desde la BD:");
                    mostrarPokemon(squirtleDB);
                }
            } else {
                System.out.println("Error al insertar Squirtle.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void mostrarPokemon(PokemonEntity pokemon) {
        System.out.println("🔴 Pokémon: " + pokemon.getNombre());
        System.out.println("   Tipo: " + pokemon.getTipo1() + 
                         (pokemon.getTipo2() != null ? "/" + pokemon.getTipo2() : ""));
        
        EstadisticasEntity stats = pokemon.getEstadisticas();
        System.out.println("   Estadísticas:");
        System.out.println("     - Ataque: " + stats.getAtaque());
        System.out.println("     - Defensa: " + stats.getDefensa());
        System.out.println("     - Vida: " + stats.getVida());
        System.out.println("     - Velocidad: " + stats.getVelocidad());
        
        System.out.println("   Ataques:");
        for (MovimientoEntity ataque : pokemon.getAtaques()) {
            System.out.println("     - " + ataque.getNombre() + 
                             " (Potencia: " + ataque.getPotencia() + 
                             ", Precisión: " + ataque.getPrecision() + 
                             ", Tipo: " + ataque.getTipo() + ")");
        }
    }
}

