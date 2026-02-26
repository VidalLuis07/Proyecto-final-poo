package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.Entidad;

public class Door extends Entidad {

    private boolean abierta;
    private Room salaDestino;
    private String posicionBorde; // Indica si la puerta está al NORTE, SUR, ESTE u OESTE

    public Door(double x, double y, String posicionBorde, Room salaDestino) {
        // Hereda de Entidad (x, y, ancho, alto)
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
        // Aquí podrías añadir lógica para cambiar el frame del sprite a "abierto"
    }

    public void cerrar() {
        this.abierta = false;
        // Nota: Cambiar la textura a rojo o estado bloqueado en el GameRenderer
    }

    public Room getSalaDestino() {
        return salaDestino;
    }

    public String getPosicionBorde() {
        return posicionBorde;
    }
}