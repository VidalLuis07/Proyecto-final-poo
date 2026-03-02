package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.IUpdatable;

/**
 * Clase abstracta que representa una entidad que puede moverse.
 * Extiende de Entidad e implementa la interfaz IUpdatable.
 * Maneja dirección y velocidad de movimiento.
 */
public abstract class EntidadMovible extends Entidad implements IUpdatable {

    /**
     * Velocidad de movimiento de la entidad.
     */
    protected double velocidad;

    /**
     * Dirección horizontal del movimiento.
     */
    protected double dx;

    /**
     * Dirección vertical del movimiento.
     */
    protected double dy;

    /**
     * Constructor de la clase EntidadMovible.
     *
     * @param x Posición inicial en el eje X.
     * @param y Posición inicial en el eje Y.
     * @param width Ancho de la entidad.
     * @param height Alto de la entidad.
     * @param velocidad Velocidad inicial de movimiento.
     */
    public EntidadMovible(double x, double y, double width, double height, double velocidad) {
        super(x, y, width, height);
        this.velocidad = velocidad;
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Actualiza la posición de la entidad según
     * su dirección (dx, dy) y su velocidad.
     */
    public void mover() {
        this.x += dx * velocidad;
        this.y += dy * velocidad;
    }

    /**
     * Método llamado en cada ciclo del juego.
     * Ejecuta el movimiento de la entidad.
     *
     * @param delta Tiempo transcurrido desde la última actualización.
     */
    @Override
    public void update(double delta) {
        mover();
    }

    /**
     * Establece la dirección horizontal del movimiento.
     *
     * @param dx Nuevo valor de dirección en X.
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Establece la dirección vertical del movimiento.
     *
     * @param dy Nuevo valor de dirección en Y.
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * @return Dirección horizontal actual.
     */
    public double getDx() {
        return dx;
    }

    /**
     * @return Dirección vertical actual.
     */
    public double getDy() {
        return dy;
    }

    /**
     * @return Velocidad actual de la entidad.
     */
    public double getVelocidad() {
        return velocidad;
    }

    /**
     * Modifica la velocidad de la entidad.
     *
     * @param velocidad Nueva velocidad.
     */
    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }
}