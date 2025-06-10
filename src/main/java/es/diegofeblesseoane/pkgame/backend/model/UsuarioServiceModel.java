package es.diegofeblesseoane.pkgame.backend.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.diegofeblesseoane.pkgame.backend.model.abtrastas.Conexion;

public class UsuarioServiceModel extends Conexion {

    public UsuarioServiceModel() {
    }

    /**
     * * Constructor con path de conexion
     * @param unaRutaArchivoBD ruta de la bbdd
     * @throws SQLException error controlado
     */
    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }

    /**
     * * Funcion que devuelve la conexion a la bbdd
     * @return ArrayList<UsuarioEntity> lista de usuarios
     * @throws SQLException error controlado
     */
    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        return obtenerUsuario(sql);
    }

    public ArrayList<UsuarioEntity> obtenerUsuario(String sql) throws SQLException {
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        try (PreparedStatement sentencia = getConnection().prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                String nombreStr = resultado.getString("nombre_usuario");
                String contraseniaStr = resultado.getString("contrasenia");
                String emailStr = resultado.getString("email");
                UsuarioEntity usuarioModel = new UsuarioEntity(emailStr, nombreStr, contraseniaStr);
                usuarios.add(usuarioModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return usuarios;
    }

    /**
     * * Metodo que obtiene las credenciales del usuario
     * @param dato 
     * @return UsuarioEntity usuario con las credenciales
     * @throws SQLException 
     */
    public UsuarioEntity obtenerCredencialesUsuario(String dato) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? OR nombre_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, dato);
            stmt.setString(2, dato);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioEntity usuario = new UsuarioEntity(
                        rs.getString("email"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contrasenia")
                    );
                    // Cargar estadísticas
                    usuario.setVictoriasTotales(rs.getInt("victorias_totales"));
                    usuario.setDerrotasTotales(rs.getInt("derrotas_totales"));
                    usuario.setMayorRacha(rs.getInt("mayor_racha"));
                    usuario.setRachaActual(rs.getInt("racha_actual"));
                    usuario.setDerrotasConsecutivas(rs.getInt("derrotas_consecutivas"));

                    return usuario;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return null;
    }

    /**
     * Verifica si un usuario ya existe por email o nombre de usuario
     * @param email email del usuario
     * @param nombreUsuario nombre de usuario
     * @return true si el usuario ya existe
     * @throws SQLException error controlado
     */
    public boolean usuarioExiste(String email, String nombreUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ? OR nombre_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return false;
    }
    
    /**
     * Valida los datos del usuario antes del registro
     * @param usuario usuario a validar
     * @return mensaje de error o null si es válido
     */
    public String validarUsuario(UsuarioEntity usuario) {
        if (usuario == null) {
            return "Usuario no puede ser nulo";
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            return "El email no puede estar vacío";
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return "El nombre de usuario no puede estar vacío";
        }
        
        if (usuario.getContrasenia() == null || usuario.getContrasenia().trim().isEmpty()) {
            return "La contraseña no puede estar vacía";
        }
        
        // Validar formato de email
        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "El formato del email no es válido";
        }
        
        // Validar longitud del nombre de usuario
        if (usuario.getNombre().length() < 3 || usuario.getNombre().length() > 20) {
            return "El nombre de usuario debe tener entre 3 y 20 caracteres";
        }
        
        // Validar longitud de la contraseña
        if (usuario.getContrasenia().length() < 3) {
            return "La contraseña debe tener al menos 3 caracteres";
        }
        
        return null; // No hay errores
    }
    
    /**
     * Método que agrega un usuario
     * @param usuario usuario a agregar
     * @return boolean true si se ha podido agregar el usuario
     * @throws SQLException error controlado
     */
    public boolean agregarUsuario(UsuarioEntity usuario) throws SQLException {
        // Validar datos del usuario
        String error = validarUsuario(usuario);
        if (error != null) {
            System.err.println("Error de validación: " + error);
            return false;
        }
        
        // Verificar si el usuario ya existe
        if (usuarioExiste(usuario.getEmail(), usuario.getNombre())) {
            System.err.println("El usuario ya existe con ese email o nombre de usuario");
            return false;
        }

        String sql = "INSERT INTO usuario (email, nombre_usuario, contrasenia) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = getConnection().prepareStatement(sql)) {
            sentencia.setString(1, usuario.getEmail().trim());
            sentencia.setString(2, usuario.getNombre().trim());
            sentencia.setString(3, usuario.getContrasenia().trim());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    /**
     * * Metodo que edita el usuario
     * @param usuario
     * @return boolean true si se ha podido editar el usuario
     * @throws SQLException error controlado
     */
    public boolean editarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) {
            return false;
        }

        String sql = "UPDATE usuario SET nombre_usuario = ?, email = ?, contrasenia = ? WHERE email = ?";
        try (PreparedStatement sentencia = getConnection().prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getContrasenia());
            sentencia.setString(4, usuario.getEmail());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    /**
     * * Metodo que edita el usuario
     * @param usuario 
     * @param emailOriginal 
     * @return boolean true si se ha podido editar el usuario
     * @throws SQLException error controlado
     */
    public boolean editarUsuario(UsuarioEntity usuario, String emailOriginal) throws SQLException {
        if (usuario == null || emailOriginal == null || emailOriginal.isEmpty()) {
            return false;
        }

        String sql = "UPDATE usuario SET nombre_usuario = ?, email = ?, contrasenia = ? WHERE email = ?";
        try (PreparedStatement sentencia = getConnection().prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getContrasenia());
            sentencia.setString(4, emailOriginal);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    /**
     * * Metodo que elimina el usuario
     * @param email
     * @return boolean true si se ha podido eliminar el usuario
     * @throws SQLException error controlado
     */
    public boolean eliminarUsuario(String email) throws SQLException {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM usuario WHERE email = ?";
        try (PreparedStatement sentencia = getConnection().prepareStatement(sql)) {
            sentencia.setString(1, email);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    /**
     * Método que actualiza las estadísticas del usuario
     * @param usuario usuario con las nuevas estadísticas
     * @return boolean true si se ha podido actualizar el usuario
     * @throws SQLException error controlado
     */
    public boolean actualizarEstadisticas(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) {
            return false;
        }

        String sql = "UPDATE usuario SET " +
                     "victorias_totales = ?, " +
                     "derrotas_totales = ?, " +
                     "mayor_racha = ?, " +
                     "racha_actual = ?, " +
                     "derrotas_consecutivas = ?, " +
                     "ultimo_acceso = CURRENT_TIMESTAMP " +
                     "WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, usuario.getVictoriasTotales());
            stmt.setInt(2, usuario.getDerrotasTotales());
            stmt.setInt(3, usuario.getMayorRacha());
            stmt.setInt(4, usuario.getRachaActual());
            stmt.setInt(5, usuario.getDerrotasConsecutivas());
            stmt.setString(6, usuario.getEmail());
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Actualizando estadísticas para " + usuario.getEmail() + ": " + rowsUpdated + " filas afectadas");
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }
    
    /**
     * Actualiza la última fecha de acceso del usuario
     * @param email email del usuario
     * @return true si se actualizó correctamente
     * @throws SQLException error controlado
     */
    public boolean actualizarUltimoAcceso(String email) throws SQLException {
        String sql = "UPDATE usuario SET ultimo_acceso = CURRENT_TIMESTAMP WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }
}
