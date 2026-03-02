package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Door extends Entidad {

    private boolean abierta;
    private Room salaDestino;
    private String posicionBorde; // Indica si la puerta está al NORTE, SUR, ESTE u OESTE

    /**
     * Construye una puerta de 32x32 píxeles en la posición y borde dados,
     * vinculada a la sala destino. La puerta se inicializa cerrada.
     *
     * @param x            posición inicial en el eje X
     * @param y            posición inicial en el eje Y
     * @param posicionBorde borde donde se ubica la puerta ({@code "NORTE"}, {@code "SUR"},
     *                     {@code "ESTE"} u {@code "OESTE"})
     * @param salaDestino  sala a la que lleva esta puerta al cruzarla
     */
    public Door(double x, double y, String posicionBorde, Room salaDestino) {
        // Hereda de Entidad (x, y, ancho, alto)
        super(x, y, 32, 32);
        this.abierta = false;
        this.posicionBorde = posicionBorde;
        this.salaDestino = salaDestino;
    }

    /**
     * Indica si la puerta está actualmente abierta.
     *
     * @return {@code true} si la puerta está abierta; {@code false} si está cerrada
     */
    public boolean isAbierta() {
        return abierta;
    }

    /**
     * Abre la puerta. Puede extenderse para cambiar el frame del sprite al estado abierto.
     */
    public void abrir() {
        this.abierta = true;
        // Aquí podrías añadir lógica para cambiar el frame del sprite a "abierto"
    }

    /**
     * Cierra la puerta. Puede extenderse para cambiar la textura al estado bloqueado en el GameRenderer.
     */
    public void cerrar() {
        this.abierta = false;
        // Nota: Cambiar la textura a rojo o estado bloqueado en el GameRenderer
    }

    /**
     * Retorna la sala a la que conduce esta puerta.
     *
     * @return la {@code Room} destino de esta puerta
     */
    public Room getSalaDestino() {
        return salaDestino;
    }

    /**
     * Retorna la posición del borde donde se encuentra la puerta.
     *
     * @return cadena con el borde ({@code "NORTE"}, {@code "SUR"}, {@code "ESTE"} u {@code "OESTE"})
     */
    public String getPosicionBorde() {
        return posicionBorde;
    }
}