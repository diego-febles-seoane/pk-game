package es.diegofeblesseoane.pkgame.backend.model;

public enum TipoPokemon {
    FUEGO("Fuego", "Charmander"),
    AGUA("Agua", "Squirtle"),
    PLANTA("Planta", "Bulbasaur");
    
    private final String nombre;
    private final String pokemonRepresentativo;
    
    TipoPokemon(String nombre, String pokemonRepresentativo) {
        this.nombre = nombre;
        this.pokemonRepresentativo = pokemonRepresentativo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getPokemonRepresentativo() {
        return pokemonRepresentativo;
    }
    
    /**
     * Determina el resultado del combate entre dos tipos
     * @param oponente el tipo del oponente
     * @return 1 si gana este tipo, 0 si empate, -1 si pierde
     */
    public int combatir(TipoPokemon oponente) {
        if (this == oponente) {
            return 0; // Empate
        }
        
        switch (this) {
            case FUEGO:
                return (oponente == PLANTA) ? 1 : -1; // Fuego vence a Planta, pierde ante Agua
            case AGUA:
                return (oponente == FUEGO) ? 1 : -1;  // Agua vence a Fuego, pierde ante Planta
            case PLANTA:
                return (oponente == AGUA) ? 1 : -1;   // Planta vence a Agua, pierde ante Fuego
            default:
                return 0;
        }
    }
    
    /**
     * Obtiene el tipo basado en el nombre del tipo
     * @param tipoNombre nombre del tipo
     * @return el TipoPokemon correspondiente o null si no se encuentra
     */
    public static TipoPokemon obtenerPorNombre(String tipoNombre) {
        for (TipoPokemon tipo : values()) {
            if (tipo.getNombre().equalsIgnoreCase(tipoNombre)) {
                return tipo;
            }
        }
        return null;
    }
    
    /**
     * Obtiene un tipo aleatorio con distribución equilibrada
     * @return un TipoPokemon aleatorio
     */
    public static TipoPokemon obtenerTipoAleatorio() {
        TipoPokemon[] tipos = values();
        return tipos[(int) (Math.random() * tipos.length)];
    }
    
    /**
     * Obtiene el tipo que es fuerte contra este tipo
     * @return el tipo que vence a este
     */
    public TipoPokemon obtenerTipoContrario() {
        switch (this) {
            case FUEGO:
                return AGUA;   // Agua vence a Fuego
            case AGUA:
                return PLANTA; // Planta vence a Agua
            case PLANTA:
                return FUEGO;  // Fuego vence a Planta
            default:
                return FUEGO;
        }
    }
    
    /**
     * Obtiene el tipo que es débil contra este tipo
     * @return el tipo al que este tipo vence
     */
    public TipoPokemon obtenerTipoVencido() {
        switch (this) {
            case FUEGO:
                return PLANTA; // Fuego vence a Planta
            case AGUA:
                return FUEGO;  // Agua vence a Fuego
            case PLANTA:
                return AGUA;   // Planta vence a Agua
            default:
                return AGUA;
        }
    }
    
    /**
     * Obtiene el emoji correspondiente al tipo
     * @return emoji del tipo
     */
    public String getEmoji() {
        switch (this) {
            case FUEGO:
                return "🔥";
            case AGUA:
                return "💧";
            case PLANTA:
                return "🌿";
            default:
                return "❓";
        }
    }
}

