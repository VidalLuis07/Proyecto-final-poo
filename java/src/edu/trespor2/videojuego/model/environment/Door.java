package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

/**
 * Clase que representa una puerta dentro del juego.
 * Permite la transición entre diferentes salas (Room).
 * Extiende de Entidad para manejar posición y colisiones.
 */
public class Door extends Entidad {

    /**
     * Indica si la puerta está abierta o cerrada.
     */
    private boolean abierta;

    /**
     * Sala a la que se accede al atravesar la puerta.
     */
    private Room salaDestino;

    /**
     * Indica la posición de la puerta en el borde de la sala
     * (NORTE, SUR, ESTE u OESTE).
     */
    private String posicionBorde;

    /**
     * Constructor de la clase Door.
     *
     * @param x Posición en el eje X.
     * @param y Posición en el eje Y.
     * @param posicionBorde Ubicación de la puerta en el borde de la sala.
     * @param salaDestino Sala a la que conecta la puerta.
     */
    public Door(double x, double y, String posicionBorde, Room salaDestino) {
        super(x, y, 32, 32);
        this.abierta = false;
        this.posicionBorde = posicionBorde;
        this.salaDestino = salaDestino;
    }

    /**
     * Indica si la puerta está abierta.
     *
     * @return true si está abierta, false si está cerrada.
     */
    public boolean isAbierta() {
        return abierta;
    }

    /**
     * Abre la puerta.
     * Puede utilizarse para permitir el acceso a la sala destino.
     */
    public void abrir() {
        this.abierta = true;
    }

    /**
     * Cierra la puerta.
     * Impide el acceso a la sala destino.
     */
    public void cerrar() {
        this.abierta = false;
    }

    /**
     * Obtiene la sala a la que conduce la puerta.
     *
     * @return Sala destino.
     */
    public Room getSalaDestino() {
        return salaDestino;
    }

    /**
     * Obtiene la posición de la puerta dentro del borde de la sala.
     *
     * @return Posición del borde (NORTE, SUR, ESTE u OESTE).
     */
    public String getPosicionBorde() {
        return posicionBorde;
    }
}