package edu.trespor2.videojuego.model.entidades;

public class Proyectiles extends EntidadMovible {

    private int dano;
    private boolean isDaga; // true = dibujarse como daga, false = bala normal

    /**
     * Construye un proyectil estándar (no daga) con posición, dimensiones,
     * velocidad, dirección y daño dados.
     *
     * @param x         posición inicial en el eje X
     * @param y         posición inicial en el eje Y
     * @param width     ancho del proyectil en píxeles
     * @param height    alto del proyectil en píxeles
     * @param velocidad velocidad de movimiento del proyectil
     * @param dx        componente X de la dirección de movimiento
     * @param dy        componente Y de la dirección de movimiento
     * @param dano      puntos de daño que aplica el proyectil al impactar
     */
    // Constructor original (compatibilidad con enemigos/boss que no usan daga)
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = false;
    }

    /**
     * Construye un proyectil con posición, dimensiones, velocidad, dirección,
     * daño y un indicador de si debe renderizarse como daga.
     *
     * @param x         posición inicial en el eje X
     * @param y         posición inicial en el eje Y
     * @param width     ancho del proyectil en píxeles
     * @param height    alto del proyectil en píxeles
     * @param velocidad velocidad de movimiento del proyectil
     * @param dx        componente X de la dirección de movimiento
     * @param dy        componente Y de la dirección de movimiento
     * @param dano      puntos de daño que aplica el proyectil al impactar
     * @param isDaga    {@code true} si el proyectil debe dibujarse como daga;
     *                  {@code false} para bala normal
     */
    // Constructor con flag de daga
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano, boolean isDaga) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = isDaga;
    }

    /**
     * Retorna los puntos de daño que aplica este proyectil al impactar.
     *
     * @return daño del proyectil
     */
    public int getDano()    { return dano; }

    /**
     * Indica si el proyectil debe renderizarse como daga.
     *
     * @return {@code true} si es una daga; {@code false} si es una bala normal
     */
    public boolean isDaga() { return isDaga; }
}