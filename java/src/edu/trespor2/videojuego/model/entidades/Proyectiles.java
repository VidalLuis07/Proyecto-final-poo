package edu.trespor2.videojuego.model.entidades;

public class Proyectiles extends EntidadMovible {

    private int dano;

    public Proyectiles(double x, double y, double width, double height, double velocidad, double dx, double dy, int dano) {

        super(x, y, width, height, velocidad);

        this.dx = dx;
        this.dy = dy;

        this.dano = dano;
    }

    public int getDano() {
        return dano;
    }
}