package es.diegofeblesseoane.pkgame.backend.model;

import java.util.Objects;

public class UsuarioEntity {

    private String email;
    private String nombre;
    private String contrasenia;
    private int nivelActual;
    private int victoriasTotales;
    private int derrotasTotales;
    private int victoriasNivel;
    private int derrotasConsecutivas;
    private int mayorRacha;
    private int rachaActual;

    /**
     * Constructor vacio
     */
    public UsuarioEntity() {
    }

    /**
     * Constructor con parametros
     * @param email
     * @param nombre
     * @param contrasenia
     */
    public UsuarioEntity(String email, String nombre, String contrasenia) {
        this.email = email;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.nivelActual = 1;
        this.victoriasTotales = 0;
        this.derrotasTotales = 0;
        this.victoriasNivel = 0;
        this.derrotasConsecutivas = 0;
        this.mayorRacha = 0;
        this.rachaActual = 0;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return this.contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public int getVictoriasTotales() {
        return victoriasTotales;
    }

    public void setVictoriasTotales(int victoriasTotales) {
        this.victoriasTotales = victoriasTotales;
    }

    public int getDerrotasTotales() {
        return derrotasTotales;
    }

    public void setDerrotasTotales(int derrotasTotales) {
        this.derrotasTotales = derrotasTotales;
    }

    public int getVictoriasNivel() {
        return victoriasNivel;
    }

    public void setVictoriasNivel(int victoriasNivel) {
        this.victoriasNivel = victoriasNivel;
    }

    public int getDerrotasConsecutivas() {
        return derrotasConsecutivas;
    }

    public void setDerrotasConsecutivas(int derrotasConsecutivas) {
        this.derrotasConsecutivas = derrotasConsecutivas;
    }

    public int getMayorRacha() {
        return mayorRacha;
    }

    public void setMayorRacha(int mayorRacha) {
        this.mayorRacha = mayorRacha;
    }

    public int getRachaActual() {
        return rachaActual;
    }

    public void setRachaActual(int rachaActual) {
        this.rachaActual = rachaActual;
    }

    /**
     * Método que actualiza las estadísticas del usuario
     * @param victoria true si el usuario ganó, false si perdió
     */
    public void actualizarEstadisticas(boolean victoria) {
        if (victoria) {
            victoriasTotales++;
            victoriasNivel++;
            derrotasConsecutivas = 0;
            rachaActual++;
            if (rachaActual > mayorRacha) {
                mayorRacha = rachaActual;
            }
            
            // Sistema de niveles mejorado
            if (victoriasNivel >= 3 && nivelActual < 10) {
                nivelActual++;
                victoriasNivel = 0;
            }
        } else {
            derrotasTotales++;
            derrotasConsecutivas++;
            rachaActual = 0;
            
            // Penalización por derrotas consecutivas
            if (derrotasConsecutivas >= 3 && nivelActual > 1) {
                nivelActual = Math.max(1, nivelActual - 1);
                victoriasNivel = 0;
                derrotasConsecutivas = 0;
            }
        }
    }
    
    /**
     * Obtiene el porcentaje de victorias del usuario
     * @return porcentaje de victorias (0-100)
     */
    public double getPorcentajeVictorias() {
        int totalPartidas = victoriasTotales + derrotasTotales;
        if (totalPartidas == 0) return 0.0;
        return (double) victoriasTotales / totalPartidas * 100.0;
    }
    
    /**
     * Obtiene el total de partidas jugadas
     * @return total de partidas
     */
    public int getTotalPartidas() {
        return victoriasTotales + derrotasTotales;
    }
    
    /**
     * Obtiene el título del usuario basado en sus estadísticas
     * @return título del usuario
     */
    public String getTitulo() {
        if (mayorRacha >= 20) return "🏆 Maestro Pokémon";
        if (mayorRacha >= 10) return "⭐ Entrenador Experto";
        if (mayorRacha >= 5) return "🎯 Entrenador Avanzado";
        if (victoriasTotales >= 10) return "🎮 Entrenador";
        if (victoriasTotales >= 3) return "🌟 Novato Prometedor";
        return "🥚 Entrenador Principiante";
    }
    
    /**
     * Método de conveniencia para obtener el nivel
     * @return nivel actual del usuario
     */
    public int getNivel() {
        return nivelActual;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UsuarioEntity)) {
            return false;
        }
        UsuarioEntity usuarioEntity = (UsuarioEntity) o;
        return Objects.equals(email, usuarioEntity.email) && Objects.equals(nombre, usuarioEntity.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
  
    @Override
    public String toString() {
        return String.format("Usuario{email='%s', nombre='%s', nivel=%d, victorias=%d, derrotas=%d, racha=%d}",
                            email, nombre, nivelActual, victoriasTotales, derrotasTotales, rachaActual);
    }
}
