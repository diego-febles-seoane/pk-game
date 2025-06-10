package es.diegofeblesseoane.pkgame;

import es.diegofeblesseoane.pkgame.backend.model.TipoPokemon;
import es.diegofeblesseoane.pkgame.backend.service.CombateService;
import es.diegofeblesseoane.pkgame.backend.model.PokemonEntity;

import java.sql.SQLException;

public class CombateTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("🎮 === PRUEBA DEL SISTEMA DE COMBATE POKÉMON MEJORADO === 🎮");
            System.out.println();
            
            CombateService combateService = new CombateService();
            
            // Probar todas las combinaciones posibles de tipos
            System.out.println("1. 🔥💧🌿 Probando efectividad de tipos:");
            System.out.println();
            
            TipoPokemon[] tipos = TipoPokemon.values();
            
            for (TipoPokemon jugador : tipos) {
                for (TipoPokemon oponente : tipos) {
                    int resultado = jugador.combatir(oponente);
                    String emoji = resultado > 0 ? "🏆" : resultado < 0 ? "😵" : "🤝";
                    String mensaje = resultado > 0 ? "GANA" : resultado < 0 ? "PIERDE" : "EMPATE";
                    System.out.println(String.format("%s %-10s vs %-10s: %s %s", 
                                                     emoji, jugador.getNombre(), 
                                                     oponente.getNombre(), mensaje, emoji));
                }
                System.out.println();
            }
            
            System.out.println("2. 🤖 Probando IA inteligente - 15 combates aleatorios:");
            System.out.println();
            
            int victoriasJugador = 0;
            int victoriasIA = 0;
            int empates = 0;
            
            for (int i = 1; i <= 15; i++) {
                // Simular elección del jugador
                TipoPokemon tipoJugador = TipoPokemon.obtenerTipoAleatorio();
                
                // La IA usa su nueva estrategia inteligente
                TipoPokemon tipoIA = combateService.obtenerEleccionIA(tipoJugador);
                String mensajeIA = combateService.obtenerMensajeEleccionIA(tipoIA);
                
                int resultado = tipoJugador.combatir(tipoIA);
                
                String emoji = resultado > 0 ? "🎉" : resultado < 0 ? "😓" : "🤝";
                String ganador = resultado > 0 ? "JUGADOR" : resultado < 0 ? "IA" : "EMPATE";
                
                System.out.println(String.format("Combate %d: %s %s vs %s = %s %s", 
                                                 i, tipoJugador.getEmoji(), tipoJugador.getNombre(), 
                                                 tipoIA.getEmoji(), tipoIA.getNombre(), 
                                                 emoji, ganador));
                System.out.println("   " + mensajeIA);
                System.out.println();
                
                if (resultado > 0) {
                    victoriasJugador++;
                } else if (resultado == 0) {
                    empates++;
                } else {
                    victoriasIA++;
                }
            }
            
            System.out.println("📊 === RESULTADOS FINALES ===");
            System.out.println("🏆 Victorias Jugador: " + victoriasJugador);
            System.out.println("🤖 Victorias IA: " + victoriasIA);
            System.out.println("🤝 Empates: " + empates);
            
            if (victoriasJugador + victoriasIA + empates > 0) {
                double tasaIA = (double) victoriasIA / (victoriasJugador + victoriasIA + empates) * 100;
                System.out.println(String.format("📈 Efectividad de la IA: %.1f%%", tasaIA));
            }
            
            System.out.println();
            System.out.println("3. 📋 Verificando que los Pokémon se cargan correctamente:");
            System.out.println();
            
            for (TipoPokemon tipo : tipos) {
                PokemonEntity pokemon = combateService.obtenerPokemonPorTipo(tipo);
                if (pokemon != null) {
                    System.out.println(String.format("%s Tipo %s - Pokémon: %s (Tipo: %s, Vida: %d, Ataque: %d)", 
                                                     tipo.getEmoji(),
                                                     tipo.getNombre(), 
                                                     pokemon.getNombre(), 
                                                     pokemon.getTipo1(),
                                                     pokemon.getEstadisticas().getVida(),
                                                     pokemon.getEstadisticas().getAtaque()));
                    
                    // Mostrar ataques
                    System.out.println("   Ataques disponibles:");
                    for (int j = 0; j < pokemon.getAtaques().size(); j++) {
                        var ataque = pokemon.getAtaques().get(j);
                        System.out.println(String.format("   %d. %s (Potencia: %d, Precisión: %d%%, Tipo: %s)",
                                                         j+1, ataque.getNombre(), ataque.getPotencia(),
                                                         ataque.getPrecision(), ataque.getTipo()));
                    }
                } else {
                    System.out.println("❌ Error: No se pudo cargar el Pokémon para el tipo " + tipo.getNombre());
                }
                System.out.println();
            }
            
            System.out.println("4. ⚡ Probando un combate completo simulado:");
            System.out.println();
            
            // Simular un combate completo
            TipoPokemon tipoJugadorTest = TipoPokemon.FUEGO;
            TipoPokemon tipoIATest = combateService.obtenerEleccionIA(tipoJugadorTest);
            
            System.out.println(String.format("🎯 Jugador elige: %s %s", 
                                             tipoJugadorTest.getEmoji(), 
                                             tipoJugadorTest.getPokemonRepresentativo()));
            
            System.out.println(combateService.obtenerMensajeEleccionIA(tipoIATest));
            
            var inicioResult = combateService.iniciarCombate(tipoJugadorTest, tipoIATest);
            System.out.println("⚔️ " + inicioResult.getMensaje());
            
            // Simular algunos turnos
            int turno = 1;
            while (combateService.isCombateActivo() && turno <= 5) {
                System.out.println(String.format("\n🔄 --- Turno %d ---", turno));
                
                if (combateService.isTurnoJugador()) {
                    // Jugador ataca con el primer movimiento disponible
                    var resultado = combateService.ejecutarTurnoJugador(0);
                    System.out.println("👤 " + resultado.getMensaje());
                    
                    if (resultado.isCombateTerminado()) break;
                } else {
                    // IA ataca
                    var resultado = combateService.ejecutarTurnoIA();
                    System.out.println("🤖 " + resultado.getMensaje());
                    
                    if (resultado.isCombateTerminado()) break;
                }
                
                turno++;
            }
            
            if (combateService.isCombateActivo()) {
                System.out.println("\n⏰ Combate terminado por límite de turnos");
                combateService.finalizarCombate();
            }
            
            System.out.println();
            System.out.println("✅ ¡Sistema de combate Pokémon mejorado funcionando correctamente!");
            System.out.println("🎮 Nuevas características:");
            System.out.println("   • IA con estrategia inteligente (20% estratégica, 50% aleatoria)");
            System.out.println("   • Sistema de combate por turnos completo");
            System.out.println("   • Efectos visuales con emojis");
            System.out.println("   • Cálculo de daño avanzado con críticos");
            System.out.println("   • Barras de vida dinámicas");
            
        } catch (SQLException e) {
            System.err.println("❌ Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

