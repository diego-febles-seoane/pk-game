package es.diegofeblesseoane.pkgame.backend.model;

public class UsuarioSesion {
    private static UsuarioSesion instancia;
    private UsuarioEntity usuario;

    /**
     * Contructor vacio
     */
    private UsuarioSesion() {
    }

    /**
     * * Constructor con parametros
     * @return instancia de la clase
     */
    public static UsuarioSesion getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioSesion();
        }
        return instancia;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    /**
     * * Metodo que cierra la sesion del usuario
     */
    public void cerrarSesion() {
        usuario = null;
    }
}