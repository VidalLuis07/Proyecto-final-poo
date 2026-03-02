package edu.trespor2.videojuego.model.entidades;

/**
 * Clase que representa un proyectil dentro del juego.
 * Puede ser disparado por el jugador o por enemigos.
 * Extiende de EntidadMovible para manejar su desplazamiento.
 */
public class Proyectiles extends EntidadMovible {

    /**
     * Cantidad de daño que causa el proyectil.
     */
    private int dano;

    /**
     * Indica si el proyectil se dibuja como daga (true)
     * o como bala normal (false).
     */
    private boolean isDaga;

    /**
     * Constructor principal del proyectil.
     * Se utiliza para proyectiles normales (sin daga).
     *
     * @param x Posición inicial en X.
     * @param y Posición inicial en Y.
     * @param width Ancho del proyectil.
     * @param height Alto del proyectil.
     * @param velocidad Velocidad de desplazamiento.
     * @param dx Dirección horizontal.
     * @param dy Dirección vertical.
     * @param dano Daño que causa el proyectil.
     */
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = false;
    }

    /**
     * Constructor que permite indicar si el proyectil
     * debe representarse como daga.
     *
     * @param x Posición inicial en X.
     * @param y Posición inicial en Y.
     * @param width Ancho del proyectil.
     * @param height Alto del proyectil.
     * @param velocidad Velocidad de desplazamiento.
     * @param dx Dirección horizontal.
     * @param dy Dirección vertical.
     * @param dano Daño que causa el proyectil.
     * @param isDaga true si es una daga, false si es bala normal.
     */
    public Proyectiles(double x, double y, double width, double height,
                       double velocidad, double dx, double dy, int dano, boolean isDaga) {
        super(x, y, width, height, velocidad);
        this.dx = dx;
        this.dy = dy;
        this.dano = dano;
        this.isDaga = isDaga;
    }

    /**
     * Obtiene el daño que causa el proyectil.
     *
     * @return Valor de daño.
     */
    public int getDano() {
        return dano;
    }

    /**
     * Indica si el proyectil es una daga.
     *
     * @return true si es daga, false si es bala normal.
     */
    public boolean isDaga() {
        return isDaga;
    }
}