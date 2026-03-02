package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.IDamageable;
import edu.trespor2.videojuego.model.entidades.EntidadMovible;

public abstract class GameCharacter extends EntidadMovible implements IDamageable {

    protected int vidaMaxima;
    protected int vidaActual;

    // Quitamos la palabra "final" y la cambiamos a minúsculas para poder modificarla
    protected int ticksInvulnerables = 0;
    protected int MAX_TICKS_INVULNERABLES = 60; // Por defecto 60

    /**
     * Construye un personaje con posición, dimensiones, velocidad y vida máxima dados.
     * La vida actual se inicializa igual a la vida máxima.
     *
     * @param x          posición inicial en el eje X
     * @param y          posición inicial en el eje Y
     * @param width      ancho del personaje en píxeles
     * @param height     alto del personaje en píxeles
     * @param velocidad  velocidad de movimiento del personaje
     * @param vidaMaxima cantidad máxima de vida del personaje
     */
    public GameCharacter(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad);
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
    }

    /**
     * Actualiza el estado del personaje en cada frame.
     * Reduce el contador de invulnerabilidad si está activo.
     *
     * @param delta tiempo en segundos transcurrido desde el último frame
     */
    // invulnerabilidad del personaje
    @Override
    public void update(double delta) {
        super.update(delta);
        if (ticksInvulnerables > 0) {
            ticksInvulnerables--;
        }
    }

    /**
     * Aplica daño al personaje si no está en estado de invulnerabilidad.
     * Tras recibir daño, activa el periodo de invulnerabilidad durante
     * {@code MAX_TICKS_INVULNERABLES} ticks. La vida nunca baja de 0.
     *
     * @param cantidad puntos de daño a aplicar
     */
    @Override
    public void recibirDano(int cantidad) {
        if (ticksInvulnerables <= 0) {
            this.vidaActual -= cantidad;

            if (this.vidaActual < 0) {
                this.vidaActual = 0;
            }

            // Usamos la nueva variable aquí
            ticksInvulnerables = MAX_TICKS_INVULNERABLES;
        }
    }

    /**
     * Indica si el personaje ha muerto, es decir, si su vida actual es 0 o menor.
     *
     * @return {@code true} si el personaje está muerto; {@code false} en caso contrario
     */
    @Override
    public boolean estaMuerto() {
        return this.vidaActual <= 0;
    }

    /**
     * Retorna la vida actual del personaje.
     *
     * @return vida actual
     */
    public int getVidaActual() { return vidaActual; }

    /**
     * Retorna la vida máxima del personaje.
     *
     * @return vida máxima
     */
    public int getVidaMaxima() { return vidaMaxima; }

    /**
     * Cura al personaje la cantidad indicada de puntos de vida.
     * La vida actual no puede superar la vida máxima.
     *
     * @param cantidad puntos de vida a restaurar
     */
    public void curar(int cantidad) {
        this.vidaActual += cantidad;
        if (this.vidaActual > this.vidaMaxima) {
            this.vidaActual = this.vidaMaxima; // Evita curarse más allá del máximo
        }
    }
}