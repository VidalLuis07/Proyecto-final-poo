package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Door extends Entidad {

    private boolean abierta;
    private Room salaDestino;
    private String posicionBorde;

    public Door(double x, double y, String posicionBorde, Room salaDestino) {
        super(x, y, 32, 32);
        this.abierta = false;
        this.posicionBorde = posicionBorde;
        this.salaDestino = salaDestino;
    }

    public boolean isAbierta() {
        return abierta;
    }


    public void abrir() {
        this.abierta = true;
    }

    public void cerrar() {
        this.abierta = false;
        // Cambiar la textura a rojo
    }

    public Room getSalaDestino() {
        return salaDestino;
    }

    public String getPosicionBorde() {
        return posicionBorde;
    }
}