package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.ICollidable;
import javafx.geometry.Rectangle2D;

public abstract class Entidad implements ICollidable {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Entidad(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }


    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
