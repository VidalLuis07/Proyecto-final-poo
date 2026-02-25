package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    private String nombreSprite; // "carlos" o "carla"

    // ── Estado de ataque ───────────────────────────────────────────────────
    private boolean atacando = false;
    private int frameAtaque  = 0;          // frame actual de la animación (0,1,2)
    private static final int FRAMES_ATAQUE = 3;
    private static final int DURACION_FRAME = 5; // cuántos ticks dura cada frame
    private int contadorAtaque = 0;

    // Última dirección usada (para saber hacia dónde atacar si está quieto)
    private double ultimaDirX = 0;
    private double ultimaDirY = 1; // por defecto mira al frente

    public Jugador(double x, double y, double width, double height,
                   double velocidad, int vidaMaxima, String nombreSprite) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.nombreSprite = nombreSprite;
    }

    public String getNombreSprite() { return nombreSprite; }

    // ── Actualizar dirección recordada ────────────────────────────────────
    @Override
    public void update(double delta) {
        if (dx != 0 || dy != 0) {
            ultimaDirX = dx;
            ultimaDirY = dy;
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

    // ── Disparo — lanza una daga en la dirección indicada ─────────────────
    public Proyectiles disparar(double direccionX, double direccionY) {
        // Activar animación de ataque
        atacando = true;
        frameAtaque = 0;
        contadorAtaque = 0;

        double balaX = this.x + (this.width  / 2) - 48; // centrar daga (48 = mitad de 96)
        double balaY = this.y + (this.height * 0.3);     // altura del torso (~30% desde arriba)
        // El proyectil lleva flag isDaga=true para que el renderer lo dibuje con sprite
        return new Proyectiles(balaX, balaY, 60, 60, 7.0, direccionX, direccionY, 1, true);
    }

    // ── Getters para el renderer ──────────────────────────────────────────
    public boolean isAtacando()    { return atacando; }
    public int getFrameAtaque()    { return frameAtaque; }
    public double getUltimaDirX()  { return ultimaDirX; }
    public double getUltimaDirY()  { return ultimaDirY; }
}