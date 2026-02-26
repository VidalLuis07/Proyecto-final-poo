package edu.trespor2.videojuego.model.items;

import edu.trespor2.videojuego.model.entidades.Entidad;
public class Chest extends Entidad {

    private boolean abierto;

    public Chest(double x, double y) {
        super(x, y, 32, 32);
        this.abierto = false;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public int abrir() {
        if (!abierto) {
            this.abierto = true;
            int monedasGeneradas = (int) (Math.random() * 8) + 8;
            return monedasGeneradas;
        }
        return 0;
    }
}