package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.ICollidable;
import javafx.geometry.Rectangle2D;

/**
 * Clase abstracta que representa una entidad básica del juego.
 * Contiene las propiedades comunes como posición y dimensiones.
 * Implementa la interfaz ICollidable para manejar colisiones.
 */
public abstract class Entidad implements ICollidable {

    /**
     * Posición horizontal de la entidad.
     */
    protected double x;

    /**
     * Posición vertical de la entidad.
     */
    protected double y;

    /**
     * Ancho de la entidad.
     */
    protected double width;

    /**
     * Alto de la entidad.
     */
    protected double height;

    /**
     * Constructor de la clase Entidad.
     *
     * @param x Posición inicial en el eje X.
     * @param y Posición inicial en el eje Y.
     * @param width Ancho de la entidad.
     * @param height Alto de la entidad.
     */
    public Entidad(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Obtiene los límites de la entidad en forma de rectángulo.
     * Se utiliza para detectar colisiones.
     *
     * @return Rectángulo que representa el área ocupada por la entidad.
     */
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * @return Posición actual en el eje X.
     */
    public double getX() {
        return x;
    }

    /**
     * @return Posición actual en el eje Y.
     */
    public double getY() {
        return y;
    }

    /**
     * @return Ancho de la entidad.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return Alto de la entidad.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Modifica la posición en el eje X.
     *
     * @param x Nueva posición en X.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Modifica la posición en el eje Y.
     *
     * @param y Nueva posición en Y.
     */
    public void setY(double y) {
        this.y = y;
    }
}