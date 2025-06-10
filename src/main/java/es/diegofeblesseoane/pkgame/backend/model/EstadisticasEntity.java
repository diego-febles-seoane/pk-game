package es.diegofeblesseoane.pkgame.backend.model;

import java.util.Objects;

public class EstadisticasEntity {
    
    private int id;
    private int ataque;
    private int defensa;
    private int vida;
    private int velocidad;
    
    /**
     * Constructor vacío
     */
    public EstadisticasEntity() {
    }
    
    /**
     * Constructor con parámetros
     * @param ataque estadística de ataque
     * @param defensa estadística de defensa
     * @param vida estadística de vida
     * @param velocidad estadística de velocidad
     */
    public EstadisticasEntity(int ataque, int defensa, int vida, int velocidad) {
        this.ataque = ataque;
        this.defensa = defensa;
        this.vida = vida;
        this.velocidad = velocidad;
    }
    
    /**
     * Constructor completo con id
     * @param id id de las estadísticas
     * @param ataque estadística de ataque
     * @param defensa estadística de defensa
     * @param vida estadística de vida
     * @param velocidad estadística de velocidad
     */
    public EstadisticasEntity(int id, int ataque, int defensa, int vida, int velocidad) {
        this.id = id;
        this.ataque = ataque;
        this.defensa = defensa;
        this.vida = vida;
        this.velocidad = velocidad;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getAtaque() {
        return ataque;
    }
    
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public int getVida() {
        return vida;
    }
    
    public void setVida(int vida) {
        this.vida = vida;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstadisticasEntity)) return false;
        EstadisticasEntity that = (EstadisticasEntity) o;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "EstadisticasEntity{" +
                "id=" + id +
                ", ataque=" + ataque +
                ", defensa=" + defensa +
                ", vida=" + vida +
                ", velocidad=" + velocidad +
                '}';
    }
}

