package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.IUpdatable;

public abstract class EntidadMovible extends Entidad implements IUpdatable {


    protected double velocidad;


    protected double dx;
    protected double dy;


    public EntidadMovible(double x, double y, double width, double height, double velocidad) {
        super(x, y, width, height);
        this.velocidad = velocidad;
        this.dx = 0;
        this.dy = 0;
    }

    public void mover() {
        this.x += dx * velocidad;
        this.y += dy * velocidad;
    }


    @Override
    public void update() {
        mover();
    }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }
}