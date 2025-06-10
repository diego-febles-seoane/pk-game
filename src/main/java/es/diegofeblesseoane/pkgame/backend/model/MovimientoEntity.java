package es.diegofeblesseoane.pkgame.backend.model;

import java.util.Objects;

public class MovimientoEntity {
    
    private int id;
    private String nombre;
    private int potencia;
    private int precision;
    private String tipo;
    
    /**
     * Constructor vacío
     */
    public MovimientoEntity() {
    }
    
    /**
     * Constructor con parámetros
     * @param nombre nombre del movimiento
     * @param potencia potencia del movimiento
     * @param precision precisión del movimiento
     * @param tipo tipo del movimiento
     */
    public MovimientoEntity(String nombre, int potencia, int precision, String tipo) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.precision = precision;
        this.tipo = tipo;
    }
    
    /**
     * Constructor completo con id
     * @param id id del movimiento
     * @param nombre nombre del movimiento
     * @param potencia potencia del movimiento
     * @param precision precisión del movimiento
     * @param tipo tipo del movimiento
     */
    public MovimientoEntity(int id, String nombre, int potencia, int precision, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.potencia = potencia;
        this.precision = precision;
        this.tipo = tipo;
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
    
    public int getPotencia() {
        return potencia;
    }
    
    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }
    
    public int getPrecision() {
        return precision;
    }
    
    public void setPrecision(int precision) {
        this.precision = precision;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovimientoEntity)) return false;
        MovimientoEntity that = (MovimientoEntity) o;
        return id == that.id && Objects.equals(nombre, that.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
    
    @Override
    public String toString() {
        return "MovimientoEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", potencia=" + potencia +
                ", precision=" + precision +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

