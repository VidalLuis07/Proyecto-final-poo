package edu.trespor2.videojuego.model.entidades;

import edu.trespor2.videojuego.model.IUpdatable;

public abstract class EntidadMovible extends Entidad implements IUpdatable {

    protected double velocidad;

    protected double dx;
    protected double dy;

    /**
     * Construye una entidad movible con posición, dimensiones y velocidad dadas.
     * Las direcciones {@code dx} y {@code dy} se inicializan en 0.
     *
     * @param x         posición inicial en el eje X
     * @param y         posición inicial en el eje Y
     * @param width     ancho de la entidad en píxeles
     * @param height    alto de la entidad en píxeles
     * @param velocidad velocidad de movimiento de la entidad
     */
    public EntidadMovible(double x, double y, double width, double height, double velocidad) {
        super(x, y, width, height);
        this.velocidad = velocidad;
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Desplaza la entidad sumando a su posición la dirección multiplicada por la velocidad.
     */
    public void mover() {
        this.x += dx * velocidad;
        this.y += dy * velocidad;
    }

    /**
     * Actualiza el estado de la entidad en cada frame llamando a {@code mover()}.
     *
     * @param delta tiempo en segundos transcurrido desde el último frame
     */
    @Override
    public void update(double delta) {
        mover();
    }

    /**
     * Establece la componente X de la dirección de movimiento.
     *
     * @param dx valor de dirección en X (-1, 0 o 1)
     */
    public void setDx(double dx) { this.dx = dx; }

    /**
     * Establece la componente Y de la dirección de movimiento.
     *
     * @param dy valor de dirección en Y (-1, 0 o 1)
     */
    public void setDy(double dy) { this.dy = dy; }

    /**
     * Retorna la componente X actual de la dirección de movimiento.
     *
     * @return valor de {@code dx}
     */
    public double getDx() { return dx; }

    /**
     * Retorna la componente Y actual de la dirección de movimiento.
     *
     * @return valor de {@code dy}
     */
    public double getDy() { return dy; }

    /**
     * Retorna la velocidad actual de la entidad.
     *
     * @return velocidad de movimiento
     */
    public double getVelocidad() { return velocidad; }

    /**
     * Establece una nueva velocidad para la entidad.
     *
     * @param velocidad nueva velocidad de movimiento
     */
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }
}