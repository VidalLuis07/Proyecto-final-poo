package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;

/**
 * Controlador de la tienda del videojuego.
 * <p>
 * Gestiona la compra de power-ups disponibles para el jugador, incluyendo
 * mejoras de vida, velocidad y daño. Cada compra descuenta el costo
 * correspondiente del dinero del jugador.
 * </p>
 *
 * @author Trespor2
 * @version 1.0
 */
public class ControlTienda {

    /** Jugador que realizará las compras en la tienda. */
    private Jugador jugador;

    /** Costo en monedas del power-up "Lloros" (vida). */
    private final int COSTO_LLOROS = 5;

    /** Costo en monedas del power-up "Fire Velocity" (velocidad). */
    private final int COSTO_VELOCIDAD = 10;

    /** Costo en monedas del power-up "Mortal Fire" (daño). */
    private final int COSTO_DANO = 15;

    /**
     * Construye un nuevo {@code ControlTienda} asociado al jugador dado.
     *
     * @param jugador el jugador que interactuará con la tienda; no debe ser {@code null}
     */
    public ControlTienda(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Intenta comprar el power-up "Lloros".
     * <p>
     * Si el jugador tiene suficiente dinero ({@value } monedas):
     * <ul>
     *   <li>Si la vida actual es menor a la máxima, cura 1 corazón.</li>
     *   <li>Si la vida ya está al máximo, aumenta la vida máxima en 1 corazón.</li>
     * </ul>
     * </p>
     *
     * @return {@code true} si la compra fue exitosa; {@code false} si el jugador
     *         no tiene suficiente dinero
     */
    public boolean comprarLloros() {
        if (jugador.getDinero() >= COSTO_LLOROS) {

            jugador.restarDinero(COSTO_LLOROS);

            if (jugador.getVidaActual() < jugador.getVidaMaxima()) {
                jugador.curar(1);
                System.out.println("Curaste 1 corazón. Vida actual: " + jugador.getVidaActual());
            } else {
                jugador.aumentarVidaMaxima(1);
                System.out.println("¡Corazón extra! Nueva vida máxima: " + jugador.getVidaMaxima());
            }

            return true;
        }
        return false;
    }

    /**
     * Intenta comprar el power-up "Fire Velocity".
     * <p>
     * Si el jugador tiene suficiente dinero ({@value } monedas),
     * incrementa la velocidad del jugador en {@code 0.5} unidades.
     * </p>
     *
     * @return {@code true} si la compra fue exitosa; {@code false} si el jugador
     *         no tiene suficiente dinero
     */
    public boolean comprarVelocidad() {
        if (jugador.getDinero() >= COSTO_VELOCIDAD) {
            jugador.restarDinero(COSTO_VELOCIDAD);
            jugador.setVelocidad(jugador.getVelocidad() + 0.5);
            return true;
        }
        return false;
    }

    /**
     * Intenta comprar el power-up "Mortal Fire".
     * <p>
     * Si el jugador tiene suficiente dinero ({@value } monedas),
     * incrementa el daño del jugador en {@code 5} puntos.
     * </p>
     *
     * @return {@code true} si la compra fue exitosa; {@code false} si el jugador
     *         no tiene suficiente dinero
     */
    public boolean comprarDano() {
        if (jugador.getDinero() >= COSTO_DANO) {
            jugador.restarDinero(COSTO_DANO);
            jugador.aumentarDano(5);
            return true;
        }
        return false;
    }
}