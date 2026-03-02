package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

/**
 * Representa una unidad básica del mapa (tile).
 * Un tile define una sección del terreno dentro de una sala,
 * determinando si es transitable y qué tipo visual posee.
 */
public class Tile extends Entidad {

    /**
     * Enumeración que define los tipos posibles de tile.
     */
    public enum TipoTile { VACIO, PARED, PISO }

    private boolean transitable;
    private TipoTile tipo;

    /**
     * Constructor del tile.
     *
     * @param x Coordenada X en píxeles.
     * @param y Coordenada Y en píxeles.
     * @param transitable Indica si el tile puede ser atravesado por entidades.
     * @param tipo Tipo de tile (VACIO, PARED o PISO).
     */
    public Tile(double x, double y, boolean transitable, TipoTile tipo) {
        super(x, y, 16, 16);
        this.transitable = transitable;
        this.tipo = tipo;
    }

    /**
     * Indica si el tile es transitable.
     *
     * @return true si se puede atravesar, false en caso contrario.
     */
    public boolean isTransitable() {
        return transitable;
    }

    /**
     * Define si el tile es transitable.
     *
     * @param transitable Nuevo estado de transitabilidad.
     */
    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }

    /**
     * Obtiene el tipo de tile.
     *
     * @return Tipo actual del tile.
     */
    public TipoTile getTipo() {
        return tipo;
    }

    /**
     * Modifica el tipo del tile.
     *
     * @param tipo Nuevo tipo de tile.
     */
    public void setTipo(TipoTile tipo) {
        this.tipo = tipo;
    }
}