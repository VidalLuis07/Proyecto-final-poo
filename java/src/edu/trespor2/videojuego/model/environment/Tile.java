package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Tile extends Entidad {

    // ── Tipo de tile ───────────────────────────────────────────────────────
    public enum TipoTile { VACIO, PARED, PISO }

    private boolean transitable;
    private TipoTile tipo;

    public Tile(double x, double y, boolean transitable, TipoTile tipo) {
        super(x, y, 16, 16);
        this.transitable = transitable;
        this.tipo = tipo;
    }

    public boolean isTransitable() {
        return transitable;
    }

    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }

    public TipoTile getTipo() {
        return tipo;
    }

    public void setTipo(TipoTile tipo) {
        this.tipo = tipo;
    }
}