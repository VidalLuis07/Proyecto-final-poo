package edu.trespor2.videojuego.model.entidades.personajes;

public abstract class Enemigo extends GameCharacter {

    public Enemigo(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);

        // hace que no tengan escudo o invulnerabilidad los enemigos
        this.MAX_TICKS_INVULNERABLES = 0;
    }
    public abstract void perseguir (Jugador jugador);
}
