package edu.trespor2.videojuego.model.entidades.personajes;


import edu.trespor2.videojuego.model.IDamageable;
import edu.trespor2.videojuego.model.entidades.EntidadMovible;

public abstract class GameCharacter extends EntidadMovible implements IDamageable {

    protected int vidaMaxima;
    protected int vidaActual;


    public GameCharacter(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad);

        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
    }

    @Override
    public void recibirDano(int cantidad) {
        this.vidaActual -= cantidad;

        if (this.vidaActual < 0) {
            this.vidaActual = 0;
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