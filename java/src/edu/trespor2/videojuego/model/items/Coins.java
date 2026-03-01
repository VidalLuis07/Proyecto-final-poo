package edu.trespor2.videojuego.model.items;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Coins extends Entidad {

    private int valor;

    private static final int TOTAL_FRAMES    = 10;
    private static final int VELOCIDAD_ANIM  = 6; // ticks por frame
    private int frameActual   = 0;
    private int contadorTicks = 0;

    public Coins(double x, double y, int valor) {
        super(x, y, 24, 24);
        this.valor = valor;
    }

    public void update() {
        contadorTicks++;
        if (contadorTicks >= VELOCIDAD_ANIM) {
            contadorTicks = 0;
            frameActual = (frameActual + 1) % TOTAL_FRAMES;
        }
    }

    public int getFrameActual() { return frameActual; }

    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }
}