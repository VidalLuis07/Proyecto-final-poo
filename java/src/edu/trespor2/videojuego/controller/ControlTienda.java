package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;

public class ControlTienda {

    private Jugador jugador;

    private final int COSTO_VIDA = 15;
    private final int COSTO_VELOCIDAD = 20;
    private final int COSTO_DANO = 30;

    public ControlTienda(Jugador jugador) {
        this.jugador = jugador;
    }

    public boolean comprarVida() {
        if (/*jugador.getDinero() >= COSTO_VIDA && */ jugador.getVidaActual() < jugador.getVidaMaxima()) {
            jugador.curar(2);
            System.out.println("Vida comprada. Vida actual: " + jugador.getVidaActual());
            return true;
        }
        return false;
    }

    public boolean comprarVelocidad() {
        if (/*jugador.getDinero() >= COSTO_VELOCIDAD*/ true) {
            double nuevaVelocidad = jugador.getVelocidad() + 1.5;
            jugador.setVelocidad(nuevaVelocidad);
            System.out.println("Velocidad mejorada a: " + nuevaVelocidad);
            return true;
        }
        return false;
    }

    public boolean comprarDano() {
        if (/*jugador.getDinero() >= COSTO_DANO*/ true) {
            System.out.println("Daño mejorado.");
            return true;
        }
        return false;
    }
}
// todo lo dentro de los IF es provisional, faltan argumentos en el apartado de jugador