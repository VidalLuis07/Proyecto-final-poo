package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    private long ultimoTiempoDano = 0;
    private final long TIEMPO_INVULNERABILIDAD_MS = 1000;
    private String nombreSprite; // "carlos" o "carla" — para saber qué imagen usar

    // Agrega nombreSprite al final del constructor
    public Jugador(double x, double y, double width, double height,
                   double velocidad, int vidaMaxima, String nombreSprite) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.nombreSprite = nombreSprite;
    }

    public String getNombreSprite() {
        return nombreSprite;
    }

    public Proyectiles disparar(double direccionX, double direccionY) {
        double balaX = this.x + (this.width / 2);
        double balaY = this.y + (this.height / 2);
        return new Proyectiles(balaX, balaY, 10, 10, 7.0, direccionX, direccionY, 1);
    }
}