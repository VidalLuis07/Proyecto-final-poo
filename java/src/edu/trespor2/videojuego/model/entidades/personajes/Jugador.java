package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    private String nombreSprite; // "carlos" o "carla"

    // Estado de ataque
    private boolean atacando = false;
    private int frameAtaque  = 1;
    private static final int FRAMES_ATAQUE    = 5;
    private static final int DURACION_FRAME   = 5;
    private int contadorAtaque  = 0;
    private int cooldownDisparo = 0;
    private static final int MAX_COOLDOWN_DISPARO = 40;

    // Última dirección usada
    private double ultimaDirX = 0;
    private double ultimaDirY = 1;

    // ── Power-up de velocidad ──────────────────────────────────────────────
    private int    timerVelocidad   = 0;
    private double multVelocidad    = 1.0;
    private double velocidadBase;        // guardamos la velocidad original

    // ── Power-up de daño ──────────────────────────────────────────────────
    private int timerDano  = 0;
    private int bonusDano  = 0;

    public Jugador(double x, double y, double width, double height,
                   double velocidad, int vidaMaxima, String nombreSprite) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.nombreSprite  = nombreSprite;
        this.velocidadBase = velocidad;
    }

    public String getNombreSprite() { return nombreSprite; }

    @Override
    public void update(double delta) {
        // Cooldown de disparo
        if (cooldownDisparo > 0) cooldownDisparo--;

        // Animación de ataque
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

        // ── Tick power-up velocidad ────────────────────────────────────────
        if (timerVelocidad > 0) {
            timerVelocidad--;
            if (timerVelocidad == 0) {
                // Restaurar velocidad original
                setVelocidad(velocidadBase);
                multVelocidad = 1.0;
            }
        }

        // ── Tick power-up daño ─────────────────────────────────────────────
        if (timerDano > 0) {
            timerDano--;
            if (timerDano == 0) {
                bonusDano = 0;
            }
        }

        super.update(delta);
    }

    public boolean puedeDisparar() {
        return cooldownDisparo <= 0;
    }

    public Proyectiles disparar(double direccionX, double direccionY) {
        atacando       = true;
        frameAtaque    = 0;
        contadorAtaque = 0;
        cooldownDisparo = MAX_COOLDOWN_DISPARO;

        double centroJugadorX = this.x + (this.width  / 2);
        double centroJugadorY = this.y + (this.height / 2);

        double balaX = centroJugadorX - 30;
        double balaY = centroJugadorY - 30;

        balaX += direccionX * 40;
        balaY += direccionY * 40;

        int danoBala = 1 + bonusDano;
        return new Proyectiles(balaX, balaY, 60, 60, 2.5, direccionX, direccionY, danoBala, true);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  POWER-UPS
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Activa el power-up de velocidad.
     * @param duracion  Frames que dura el efecto
     * @param mult      Multiplicador de velocidad (ej. 2.0 = doble)
     */
    public void activarPowerUpVelocidad(int duracion, double mult) {
        // Si ya había uno activo, primero restauramos antes de aplicar el nuevo
        if (timerVelocidad > 0) setVelocidad(velocidadBase);

        timerVelocidad = duracion;
        multVelocidad  = mult;
        setVelocidad(velocidadBase * multVelocidad);
    }

    /**
     * Activa el power-up de daño.
     * @param duracion  Frames que dura el efecto
     * @param bonus     Daño adicional por disparo
     */
    public void activarPowerUpDano(int duracion, int bonus) {
        timerDano = duracion;
        bonusDano = bonus;
    }

    /** @return true si el power-up de velocidad está activo */
    public boolean tienePowerUpVelocidad() { return timerVelocidad > 0; }
    /** @return true si el power-up de daño está activo */
    public boolean tienePowerUpDano()      { return timerDano > 0; }

    // ── Getters para render ────────────────────────────────────────────────
    public boolean isAtacando()    { return atacando; }
    public int getFrameAtaque()    { return frameAtaque; }
    public double getUltimaDirX()  { return ultimaDirX; }
    public double getUltimaDirY()  { return ultimaDirY; }
}