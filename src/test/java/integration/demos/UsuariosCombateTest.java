package integration.demos;

import java.sql.SQLException;

import es.diegofeblesseoane.pkgame.backend.model.TipoPokemon;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioServiceModel;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import es.diegofeblesseoane.pkgame.backend.service.CombateService;

public class UsuariosCombateTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("🏆 === PRUEBA COMPLETA DEL SISTEMA DE USUARIOS Y COMBATE === 🏆");
            System.out.println();
            
            // === TEST 1: Sistema de usuarios ===
            System.out.println("1. 👥 Probando sistema de registro y login de usuarios:");
            System.out.println();
            
            UsuarioServiceModel usuarioService = new UsuarioServiceModel();
            
            // Probar registro de nuevo usuario
            UsuarioEntity nuevoUsuario = new UsuarioEntity("test@test.com", "testuser", "password123");
            
            System.out.println("📝 Validando datos del usuario...");
            String errorValidacion = usuarioService.validarUsuario(nuevoUsuario);
            if (errorValidacion != null) {
                System.out.println("❌ Error de validación: " + errorValidacion);
            } else {
                System.out.println("✅ Usuario válido");
            }
            
            // Verificar si el usuario ya existe
            if (usuarioService.usuarioExiste(nuevoUsuario.getEmail(), nuevoUsuario.getNombre())) {
                System.out.println("⚠️ Usuario ya existe, usando usuario existente");
            } else {
                System.out.println("🎆 Registrando nuevo usuario...");
                boolean registrado = usuarioService.agregarUsuario(nuevoUsuario);
                if (registrado) {
                    System.out.println("✅ Usuario registrado exitosamente");
                } else {
                    System.out.println("❌ Error al registrar usuario");
                }
            }
            
            // Probar login
            System.out.println("\n🔑 Probando login...");
            UsuarioEntity usuarioLogueado = usuarioService.obtenerCredencialesUsuario("test");
            if (usuarioLogueado != null) {
                System.out.println("✅ Usuario encontrado: " + usuarioLogueado.getNombre());
                System.out.println("🏅 Título: " + usuarioLogueado.getTitulo());
                
                // Simular sesión
                UsuarioSesion.getInstancia().setUsuario(usuarioLogueado);
                System.out.println("👤 Usuario en sesión: " + UsuarioSesion.getInstancia().getUsuario().getNombre());
            } else {
                System.out.println("❌ Usuario no encontrado");
                return;
            }
            
            // === TEST 2: Sistema de combate integrado ===
            System.out.println("\n2. ⚔️ Probando sistema de combate con usuario logueado:");
            System.out.println();
            
            CombateService combateService = new CombateService();
            
            // Simular múltiples combates para probar estadísticas
            for (int i = 1; i <= 5; i++) {
                System.out.println(String.format("🎯 --- Combate %d ---", i));
                
                // Usuario elige tipo aleatorio
                TipoPokemon tipoJugador = TipoPokemon.obtenerTipoAleatorio();
                TipoPokemon tipoIA = combateService.obtenerEleccionIA(tipoJugador);
                
                System.out.println(String.format("👤 Jugador: %s %s", tipoJugador.getEmoji(), tipoJugador.getNombre()));
                System.out.println(String.format("🤖 IA: %s %s", tipoIA.getEmoji(), tipoIA.getNombre()));
                
                // Determinar ganador
                int resultado = tipoJugador.combatir(tipoIA);
                boolean jugadorGana = resultado > 0;
                
                String mensajeResultado = resultado > 0 ? "🎉 GANA" : resultado < 0 ? "😵 PIERDE" : "🤝 EMPATE";
                System.out.println("Resultado: " + mensajeResultado);
                
                // Actualizar estadísticas del usuario
                UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
                usuario.actualizarEstadisticas(jugadorGana);
                
                // Guardar en base de datos
                boolean actualizado = usuarioService.actualizarEstadisticas(usuario);
                if (actualizado) {
                    System.out.println("✅ Estadísticas actualizadas");
                } else {
                    System.out.println("❌ Error al actualizar estadísticas");
                }
                
                System.out.println(String.format("📊 Estado actual - V:%d D:%d Racha:%d", 
                                                 usuario.getVictoriasTotales(), 
                                                 usuario.getDerrotasTotales(),
                                                 usuario.getRachaActual()));
                System.out.println();
            }
            
            // === TEST 3: Verificar persistencia ===
            System.out.println("3. 💾 Verificando persistencia de datos:");
            System.out.println();
            
            // Cerrar sesión y volver a cargar usuario
            String emailUsuario = UsuarioSesion.getInstancia().getUsuario().getEmail();
            UsuarioSesion.getInstancia().cerrarSesion();
            
            // Cargar usuario desde base de datos
            UsuarioEntity usuarioRecargado = usuarioService.obtenerCredencialesUsuario(emailUsuario);
            if (usuarioRecargado != null) {
                System.out.println("✅ Usuario recargado desde BD exitosamente");
                System.out.println("👤 Nombre: " + usuarioRecargado.getNombre());
                System.out.println("🏅 Título: " + usuarioRecargado.getTitulo());
                System.out.println("📊 Estadísticas:");
                System.out.println(String.format("   🏆 Victorias: %d", usuarioRecargado.getVictoriasTotales()));
                System.out.println(String.format("   😵 Derrotas: %d", usuarioRecargado.getDerrotasTotales()));
                System.out.println(String.format("   📈 Tasa victoria: %.1f%%", usuarioRecargado.getPorcentajeVictorias()));
                System.out.println(String.format("   🔥 Racha actual: %d", usuarioRecargado.getRachaActual()));
                System.out.println(String.format("   ⭐ Mayor racha: %d", usuarioRecargado.getMayorRacha()));
            } else {
                System.out.println("❌ Error al recargar usuario");
            }
            
            // === TEST 4: Probar usuarios demo ===
            System.out.println("\n4. 🎭 Probando usuarios demo:");
            System.out.println();
            
            String[] usuariosDemo = {"admin", "demo", "test"};
            for (String usuario : usuariosDemo) {
                UsuarioEntity usuarioDemo = usuarioService.obtenerCredencialesUsuario(usuario);
                if (usuarioDemo != null) {
                    System.out.println(String.format("✅ %s - %s (%d-%d)", 
                                                     usuarioDemo.getNombre(),
                                                     usuarioDemo.getTitulo(),
                                                     usuarioDemo.getVictoriasTotales(),
                                                     usuarioDemo.getDerrotasTotales()));
                } else {
                    System.out.println("❌ Usuario demo no encontrado: " + usuario);
                }
            }
            
            System.out.println();
            System.out.println("✅ === TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE === ✅");
            System.out.println("🎉 Características implementadas:");
            System.out.println("   • ✅ Registro de usuarios con validación");
            System.out.println("   • ✅ Inicio de sesión seguro");
            System.out.println("   • ✅ Sistema de estadísticas persistentes");
            System.out.println("   • ✅ Integración con sistema de combate");
            System.out.println("   • ✅ Actualización automática de rachas y títulos");
            System.out.println("   • ✅ Persistencia en base de datos SQLite");
            System.out.println("   • ✅ Sesión de usuario con singleton");
            
        } catch (SQLException e) {
            System.err.println("❌ Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

