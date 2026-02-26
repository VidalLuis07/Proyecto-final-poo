package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    private String nombreSprite; // "carlos" o "carla"

    // Estado de ataque
    private boolean atacando = false;
    private int frameAtaque  = 1;
    private static final int FRAMES_ATAQUE = 5;
    private static final int DURACION_FRAME = 5; // cuántos ticks dura cada frame
    private int contadorAtaque = 0;
    private int cooldownDisparo = 0;
    private static final int MAX_COOLDOWN_DISPARO = 40;
    private int dinero = 0;

    // Última dirección usada
    private double ultimaDirX = 0;
    private double ultimaDirY = 1; // por defecto mira al frente

    public Jugador(double x, double y, double width, double height,
                   double velocidad, int vidaMaxima, String nombreSprite) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.nombreSprite = nombreSprite;
    }

    public String getNombreSprite() { return nombreSprite; }

    // actualizar la direccion
    @Override
    public void update(double delta) {
        // Reducir el tiempo de recarga en cada frame
        if (cooldownDisparo > 0) {
            cooldownDisparo--;
        }

        // Avanzar animación de ataque
        if (atacando) {
            contadorAtaque++;
            if (contadorAtaque >= DURACION_FRAME) {
                contadorAtaque = 0;
                frameAtaque++;
                if (frameAtaque >= FRAMES_ATAQUE) {
                    frameAtaque = 0;
                    atacando = false;
                }
            }
        }

        super.update(delta);
    }
    // metodo para saber si el arma esta lista (cooldown)
    public boolean puedeDisparar() {
        return cooldownDisparo <= 0;
    }

    // lanza una daga en la dirección indicada
    public Proyectiles disparar(double direccionX, double direccionY) {
        // animación de ataque
        atacando = true;
        frameAtaque = 0;
        contadorAtaque = 0;
        cooldownDisparo = MAX_COOLDOWN_DISPARO;


        // Encontrar el centro exacto jugador 96*96 y bala 60*60
        double centroJugadorX = this.x + (this.width / 2);
        double centroJugadorY = this.y + (this.height / 2);

        // centra la bala
        double balaX = centroJugadorX - 30;
        double balaY = centroJugadorY - 30;

        // la bala sale de fuera del cuerpo del jugador
        balaX += direccionX * 40;
        balaY += direccionY * 40;

        // El proyectil lleva 1 de daño y sus respectivos parametros
        return new Proyectiles(balaX, balaY, 60, 60, 2.5, direccionX, direccionY, 100, true);
    }

    public int getDinero() { return dinero; }
    public void sumarDinero(int cantidad) { this.dinero += cantidad; }

    // Getters para el render
    public boolean isAtacando()    { return atacando; }
    public int getFrameAtaque()    { return frameAtaque; }
    public double getUltimaDirX()  { return ultimaDirX; }
    public double getUltimaDirY()  { return ultimaDirY; }
}