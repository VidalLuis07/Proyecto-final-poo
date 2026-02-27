package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;

public class ControlTienda {

    private Jugador jugador;

    // Precios de los power-ups
    private final int COSTO_LLOROS = 5;
    private final int COSTO_VELOCIDAD = 10;
    private final int COSTO_DANO = 15;

    public ControlTienda(Jugador jugador) {
        this.jugador = jugador;
    }

    // los lloros te da un corazón más de vida máxima
    public boolean comprarLloros() {
        if (jugador.getDinero() >= COSTO_LLOROS) {

            jugador.restarDinero(COSTO_LLOROS);

            // Si la vida actual es menor a la MÁXIMA
            if (jugador.getVidaActual() < jugador.getVidaMaxima()) {
                // curar 1 sol corazon
                jugador.curar(1);
                System.out.println("Curaste 1 corazón. Vida actual: " + jugador.getVidaActual());
            }
            // Si la vida ya está al máximo
            else {
                // Te agrega un corazón nuevo extra
                jugador.aumentarVidaMaxima(1);
                System.out.println("¡Corazón extra! Nueva vida máxima: " + jugador.getVidaMaxima());
            }

            return true; // Compra exitosa
        }
        return false; // Sin dinero
    }

    // "Fire Velocity" sube la velocidad
    public boolean comprarVelocidad() {
        if (jugador.getDinero() >= COSTO_VELOCIDAD) {
            jugador.restarDinero(COSTO_VELOCIDAD);
            jugador.setVelocidad(jugador.getVelocidad() + 0.5);
            return true;
        }
        return false;
    }

    // "Mortal Fire" sube el daño
    public boolean comprarDano() {
        if (jugador.getDinero() >= COSTO_DANO) {
            jugador.restarDinero(COSTO_DANO);
            jugador.aumentarDano(5);
            return true;
        }
        return false;
    }
}