package edu.trespor2.videojuego.model.entidades.personajes;

public abstract class Enemigo extends GameCharacter {

    public Enemigo(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);
    }

    public abstract void perseguir (Jugador jugador);
}
