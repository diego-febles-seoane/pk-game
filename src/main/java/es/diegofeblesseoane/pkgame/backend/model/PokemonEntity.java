package es.diegofeblesseoane.pkgame.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PokemonEntity {
    
    private int id;
    private String nombre;
    private String tipo1;
    private String tipo2;
    private List<MovimientoEntity> ataques;
    private EstadisticasEntity estadisticas;
    private int vidaActual;
    
    /**
     * Constructor vacío
     */
    public PokemonEntity() {
        this.ataques = new ArrayList<>();
    }
    
    /**
     * Constructor con parámetros
     * @param nombre nombre del Pokémon
     * @param tipo1 tipo principal del Pokémon
     * @param tipo2 tipo secundario del Pokémon (puede ser null)
     * @param estadisticas estadísticas del Pokémon
     */
    public PokemonEntity(String nombre, String tipo1, String tipo2, EstadisticasEntity estadisticas) {
        this.nombre = nombre;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.estadisticas = estadisticas;
        this.ataques = new ArrayList<>();
        this.vidaActual = estadisticas.getVida(); // Inicializar vida actual con vida máxima
    }
    
    /**
     * Constructor completo con id
     * @param id id del Pokémon
     * @param nombre nombre del Pokémon
     * @param tipo1 tipo principal del Pokémon
     * @param tipo2 tipo secundario del Pokémon (puede ser null)
     * @param estadisticas estadísticas del Pokémon
     */
    public PokemonEntity(int id, String nombre, String tipo1, String tipo2, EstadisticasEntity estadisticas) {
        this.id = id;
        this.nombre = nombre;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.estadisticas = estadisticas;
        this.ataques = new ArrayList<>();
        this.vidaActual = estadisticas.getVida(); // Inicializar vida actual con vida máxima
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo1() {
        return tipo1;
    }
    
    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }
    
    public String getTipo2() {
        return tipo2;
    }
    
    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }
    
    public List<MovimientoEntity> getAtaques() {
        return ataques;
    }
    
    public void setAtaques(List<MovimientoEntity> ataques) {
        this.ataques = ataques;
    }
    
    public EstadisticasEntity getEstadisticas() {
        return estadisticas;
    }
    
    public void setEstadisticas(EstadisticasEntity estadisticas) {
        this.estadisticas = estadisticas;
    }
    
    public int getVidaActual() {
        return vidaActual;
    }
    
    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, vidaActual);
    }
    
    /**
     * Añade un ataque a la lista de ataques del Pokémon
     * @param ataque el movimiento a añadir
     */
    public void agregarAtaque(MovimientoEntity ataque) {
        if (this.ataques.size() < 4) {
            this.ataques.add(ataque);
        } else {
            throw new IllegalStateException("Un Pokémon no puede tener más de 4 ataques");
        }
    }
    
    /**
     * Elimina un ataque de la lista de ataques del Pokémon
     * @param ataque el movimiento a eliminar
     */
    public void eliminarAtaque(MovimientoEntity ataque) {
        this.ataques.remove(ataque);
    }
    
    /**
     * Verifica si el Pokémon tiene doble tipo
     * @return true si tiene tipo2, false si solo tiene tipo1
     */
    public boolean tieneDobleTipo() {
        return tipo2 != null && !tipo2.isEmpty();
    }
    
    /**
     * Recibe daño y actualiza la vida actual
     * @param daño cantidad de daño a recibir
     */
    public void recibirDaño(int daño) {
        this.vidaActual = Math.max(0, this.vidaActual - daño);
    }
    
    /**
     * Cura al Pokémon una cantidad específica
     * @param cantidad cantidad de vida a recuperar
     */
    public void curar(int cantidad) {
        this.vidaActual = Math.min(this.estadisticas.getVida(), this.vidaActual + cantidad);
    }
    
    /**
     * Restaura la vida al máximo
     */
    public void restaurarVida() {
        this.vidaActual = this.estadisticas.getVida();
    }
    
    /**
     * Verifica si el Pokémon está debilitado (vida = 0)
     * @return true si está debilitado
     */
    public boolean estaDebilitado() {
        return this.vidaActual <= 0;
    }
    
    /**
     * Obtiene el porcentaje de vida actual
     * @return porcentaje de vida (0-100)
     */
    public double getPorcentajeVida() {
        if (this.estadisticas.getVida() == 0) return 0;
        return (double) this.vidaActual / this.estadisticas.getVida() * 100;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonEntity)) return false;
        PokemonEntity that = (PokemonEntity) o;
        return id == that.id && Objects.equals(nombre, that.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
    
    @Override
    public String toString() {
        return "PokemonEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo1='" + tipo1 + '\'' +
                ", tipo2='" + tipo2 + '\'' +
                ", ataques=" + ataques +
                ", estadisticas=" + estadisticas +
                '}';
    }
}

