package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.IDamageable;
import edu.trespor2.videojuego.model.entidades.EntidadMovible;

public abstract class GameCharacter extends EntidadMovible implements IDamageable {

    protected int vidaMaxima;
    protected int vidaActual;

    // Quitamos la palabra "final" y la cambiamos a minúsculas para poder modificarla
    protected int ticksInvulnerables = 0;
    protected int MAX_TICKS_INVULNERABLES = 60; // Por defecto 60

    public GameCharacter(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad);
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
    }

    // invulnerabilidad del personaje
    @Override
    public void update(double delta) {
        super.update(delta);
        if (ticksInvulnerables > 0) {
            ticksInvulnerables--;
        }
    }

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

    @Override
    public boolean estaMuerto() {
        return this.vidaActual <= 0;
    }

    public int getVidaActual() { return vidaActual; }
    public int getVidaMaxima() { return vidaMaxima; }

    public void curar(int cantidad) {
        this.vidaActual += cantidad;
        if (this.vidaActual > this.vidaMaxima) {
            this.vidaActual = this.vidaMaxima; // Evita curarse más allá del máximo
        }
    }
}