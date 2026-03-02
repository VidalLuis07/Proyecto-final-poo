package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.ICollidable;
import javafx.geometry.Rectangle2D;

public abstract class Entidad implements ICollidable {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    /**
     * Construye una entidad con posición y dimensiones dadas.
     *
     * @param x      posición inicial en el eje X
     * @param y      posición inicial en el eje Y
     * @param width  ancho de la entidad en píxeles
     * @param height alto de la entidad en píxeles
     */
    public Entidad(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Retorna los límites de colisión (hitbox) de la entidad
     * basados en su posición y dimensiones actuales.
     *
     * @return un {@code Rectangle2D} que representa la hitbox de la entidad
     */
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Retorna la posición actual en el eje X.
     *
     * @return coordenada X
     */
    public double getX() { return x; }

    /**
     * Retorna la posición actual en el eje Y.
     *
     * @return coordenada Y
     */
    public double getY() { return y; }

    /**
     * Retorna el ancho de la entidad.
     *
     * @return ancho en píxeles
     */
    public double getWidth() { return width; }

    /**
     * Retorna el alto de la entidad.
     *
     * @return alto en píxeles
     */
    public double getHeight() { return height; }

    /**
     * Establece la posición en el eje X.
     *
     * @param x nueva coordenada X
     */
    public void setX(double x) { this.x = x; }

    /**
     * Establece la posición en el eje Y.
     *
     * @param y nueva coordenada Y
     */
    public void setY(double y) { this.y = y; }
}
