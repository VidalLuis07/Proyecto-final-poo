package edu.trespor2.videojuego.model.items;

import edu.trespor2.videojuego.model.entidades.Entidad;
public class Coins extends Entidad {

    private int valor;

    public Coins(double x, double y, int valor) {
        super(x, y, 16, 16);
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}