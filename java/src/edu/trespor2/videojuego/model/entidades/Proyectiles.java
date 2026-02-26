package edu.trespor2.videojuego.model.entidades;

public class Proyectiles extends EntidadMovible {

    private int dano;
    private boolean isDaga; // true = dibujarse como daga, false = bala normal

    // Constructor original (compatibilidad con enemigos/boss que no usan daga)
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = false;
    }

    // Constructor con flag de daga
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano, boolean isDaga) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = isDaga;
    }

    public int getDano()    { return dano; }
    public boolean isDaga() { return isDaga; }
}