package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    public Jugador(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);
    }

    public Proyectiles disparar(double direccionX, double direccionY) {
        double balaX = this.x + (this.width / 2);
        double balaY = this.y + (this.height / 2);

        double anchoBala = 10; //ancho de ejemplo
        double altoBala = 10; //alto de ejemplo
        double velocidadBala = 7.0; //velocidad de ejemplo
        int danoBala = 1; // Hace 1 de daño

        return new Proyectiles(balaX, balaY, anchoBala, altoBala, velocidadBala, direccionX, direccionY, danoBala);
    }
}