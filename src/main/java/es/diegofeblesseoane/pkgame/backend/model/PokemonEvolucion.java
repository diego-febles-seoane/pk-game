package es.diegofeblesseoane.pkgame.backend.model;

public enum PokemonEvolucion {
    // Línea evolutiva de Fuego (4 niveles)
    CHARMANDER("Charmander", TipoPokemon.FUEGO, 1, "/images/pokemon/charmander.png"),
    CHARMELEON("Charmeleon", TipoPokemon.FUEGO, 2, "/images/pokemon/charmeleon.png"),
    CHARIZARD("Charizard", TipoPokemon.FUEGO, 3, "/images/pokemon/charizard-normal.png"),
    MEGA_CHARIZARD_Y("Mega Charizard Y", TipoPokemon.FUEGO, 4, "/images/pokemon/charizard-mega-y.png"),
    
    // Línea evolutiva de Agua (4 niveles)
    SQUIRTLE("Squirtle", TipoPokemon.AGUA, 1, "/images/pokemon/squirtle.png"),
    WARTORTLE("Wartortle", TipoPokemon.AGUA, 2, "/images/pokemon/wartortle.png"),
    BLASTOISE("Blastoise", TipoPokemon.AGUA, 3, "/images/pokemon/blastoise.png"),
    MEGA_BLASTOISE("Mega Blastoise", TipoPokemon.AGUA, 4, "/images/pokemon/mega-blastoise.png"),
    
    // Línea evolutiva de Planta (4 niveles)
    BULBASAUR("Bulbasaur", TipoPokemon.PLANTA, 1, "/images/pokemon/bulbasaur.png"),
    IVYSAUR("Ivysaur", TipoPokemon.PLANTA, 2, "/images/pokemon/ivysaur.png"),
    VENUSAUR("Venusaur", TipoPokemon.PLANTA, 3, "/images/pokemon/venusaur.png"),
    MEGA_VENUSAUR("Mega Venusaur", TipoPokemon.PLANTA, 4, "/images/pokemon/mega-venusaur.png");
    
    private final String nombre;
    private final TipoPokemon tipo;
    private final int nivel;
    private final String rutaImagen;
    
    PokemonEvolucion(String nombre, TipoPokemon tipo, int nivel, String rutaImagen) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
        this.rutaImagen = rutaImagen;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public TipoPokemon getTipo() {
        return tipo;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public String getRutaImagen() {
        return rutaImagen;
    }
    
    /**
     * Obtiene la evolución inicial de un tipo
     */
    public static PokemonEvolucion getEvolucionInicial(TipoPokemon tipo) {
        switch (tipo) {
            case FUEGO:
                return CHARMANDER;
            case AGUA:
                return SQUIRTLE;
            case PLANTA:
                return BULBASAUR;
            default:
                return CHARMANDER;
        }
    }
    
    /**
     * Obtiene la evolución por tipo y nivel
     */
    public static PokemonEvolucion getEvolucionPorTipoYNivel(TipoPokemon tipo, int nivel) {
        for (PokemonEvolucion evolucion : values()) {
            if (evolucion.getTipo() == tipo && evolucion.getNivel() == nivel) {
                return evolucion;
            }
        }
        return getEvolucionInicial(tipo);
    }
    
    /**
     * Obtiene la siguiente evolución
     */
    public PokemonEvolucion getSiguienteEvolucion() {
        switch (this) {
            case CHARMANDER:
                return CHARMELEON;
            case CHARMELEON:
                return CHARIZARD;
            case CHARIZARD:
                return MEGA_CHARIZARD_Y;
            case SQUIRTLE:
                return WARTORTLE;
            case WARTORTLE:
                return BLASTOISE;
            case BLASTOISE:
                return MEGA_BLASTOISE;
            case BULBASAUR:
                return IVYSAUR;
            case IVYSAUR:
                return VENUSAUR;
            case VENUSAUR:
                return MEGA_VENUSAUR;
            default:
                return this; // Ya está en máxima evolución
        }
    }
    
    /**
     * Verifica si puede evolucionar
     */
    public boolean puedeEvolucionar() {
        return this.nivel < 4;
    }
    
    /**
     * Obtiene el emoji representativo
     */
    public String getEmoji() {
        switch (this.nivel) {
            case 1:
                return "🥚"; // Forma básica
            case 2:
                return "⭐"; // Primera evolución
            case 3:
                return "👑"; // Segunda evolución
            case 4:
                return "💎"; // Mega evolución
            default:
                return "❓";
        }
    }
    
    /**
     * Obtiene información completa del Pokémon
     */
    public String getInfoCompleta() {
        return String.format("%s %s %s (Nivel %d)", 
                           getEmoji(), tipo.getEmoji(), nombre, nivel);
    }
    
    /**
     * Obtiene el nombre del nivel de evolución
     */
    public String getNombreNivel() {
        switch (this.nivel) {
            case 1:
                return "Básico";
            case 2:
                return "Primera Evolución";
            case 3:
                return "Segunda Evolución";
            case 4:
                return "Mega Evolución";
            default:
                return "Desconocido";
        }
    }
}

