package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Tile extends Entidad {

    private boolean transitable;

    public Tile(double x, double y, boolean transitable) {
        super(x, y, 16, 16);
        this.transitable = transitable;
    }

    public boolean isTransitable() {
        return transitable;
    }

    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }
}