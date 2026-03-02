package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;

/**
 * Clase encargada de gestionar la lógica de la tienda del juego.
 * Permite al jugador comprar diferentes mejoras (power-ups)
 * verificando si tiene el dinero suficiente.
 */
public class ControlTienda {

    /**
     * Referencia al jugador que realiza las compras.
     */
    private Jugador jugador;

    /**
     * Costo del power-up que mejora vida ("Lloros").
     */
    private final int COSTO_LLOROS = 5;

    /**
     * Costo del power-up que aumenta la velocidad.
     */
    private final int COSTO_VELOCIDAD = 10;

    /**
     * Costo del power-up que incrementa el daño.
     */
    private final int COSTO_DANO = 15;

    /**
     * Constructor de la clase ControlTienda.
     *
     * @param jugador Jugador que interactúa con la tienda.
     */
    public ControlTienda(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Permite comprar el power-up "Lloros".
     * <p>
     * Si el jugador tiene suficiente dinero:
     * - Se descuenta el costo.
     * - Si su vida actual es menor a la máxima, se cura 1 corazón.
     * - Si su vida ya está al máximo, se aumenta la vida máxima en 1.
     * </p>
     *
     * @return true si la compra fue exitosa,
     *         false si no tiene dinero suficiente.
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
     * Permite comprar el power-up "Fire Velocity".
     * Aumenta la velocidad del jugador en 0.5 unidades
     * si cuenta con el dinero necesario.
     *
     * @return true si la compra fue exitosa,
     *         false si no tiene dinero suficiente.
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
     * Permite comprar el power-up "Mortal Fire".
     * Incrementa el daño del jugador en 5 puntos
     * si tiene el dinero suficiente.
     *
     * @return true si la compra fue exitosa,
     *         false si no tiene dinero suficiente.
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