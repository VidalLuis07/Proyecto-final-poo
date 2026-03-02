package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Tile extends Entidad {

    // ── Tipo de tile
    public enum TipoTile { VACIO, PARED, PISO }

    private boolean transitable;
    private TipoTile tipo;

    /**
     * Construye un tile de 16x16 píxeles en la posición dada con su tipo y transitabilidad.
     *
     * @param x           posición inicial en el eje X
     * @param y           posición inicial en el eje Y
     * @param transitable {@code true} si el jugador puede moverse sobre este tile; {@code false} si es bloqueante
     * @param tipo        tipo de tile ({@code VACIO}, {@code PARED} o {@code PISO})
     */
    public Tile(double x, double y, boolean transitable, TipoTile tipo) {
        super(x, y, 16, 16);
        this.transitable = transitable;
        this.tipo = tipo;
    }

    /**
     * Indica si el tile puede ser atravesado por el jugador u otras entidades.
     *
     * @return {@code true} si es transitable; {@code false} si es bloqueante
     */
    public boolean isTransitable() {
        return transitable;
    }

    /**
     * Establece si el tile puede ser atravesado por el jugador u otras entidades.
     *
     * @param transitable {@code true} para hacerlo transitable; {@code false} para bloquearlo
     */
    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }

    /**
     * Retorna el tipo de tile.
     *
     * @return tipo de tile ({@code VACIO}, {@code PARED} o {@code PISO})
     */
    public TipoTile getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de tile.
     *
     * @param tipo nuevo tipo de tile ({@code VACIO}, {@code PARED} o {@code PISO})
     */
    public void setTipo(TipoTile tipo) {
        this.tipo = tipo;
    }
}