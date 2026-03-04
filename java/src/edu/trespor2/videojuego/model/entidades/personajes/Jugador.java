package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class Jugador extends GameCharacter {

    private String nombreSprite; // "carlos" o "carla"

    // Estado de ataque
    private boolean atacando = false;
    private int frameAtaque = 1;
    private static final int FRAMES_ATAQUE = 3;
    private static final int DURACION_FRAME = 5; // cuántos ticks dura cada frame
    private int contadorAtaque = 0;
    private int cooldownDisparo = 0;
    private static final int MAX_COOLDOWN_DISPARO = 40;
    private int dinero = 0;
    private int danoBase = 10;

    // Última dirección usada
    private double ultimaDirX = 0;
    private double ultimaDirY = 1; // por defecto mira al frente

    // Destello de daño
    private int hurtTimer = 0;
    private static final int DURACION_HURT = 10; // frames que dura el destello blanco

    /**
     * Construye un jugador con posición, dimensiones, velocidad, vida máxima y sprite dados.
     *
     * @param x             posición inicial en el eje X
     * @param y             posición inicial en el eje Y
     * @param width         ancho del jugador en píxeles
     * @param height        alto del jugador en píxeles
     * @param velocidad     velocidad de movimiento del jugador
     * @param vidaMaxima    cantidad máxima de vida del jugador
     * @param nombreSprite  nombre del sprite a usar ({@code "carlos"} o {@code "carla"})
     */
    public Jugador(double x, double y, double width, double height,
                   double velocidad, int vidaMaxima, String nombreSprite) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.nombreSprite = nombreSprite;
    }

    /**
     * Retorna el nombre del sprite asociado al jugador.
     *
     * @return nombre del sprite ({@code "carlos"} o {@code "carla"})
     */
    public String getNombreSprite() {
        return nombreSprite;
    }

    /**
     * Actualiza el estado del jugador en cada frame.
     * Guarda la última dirección de movimiento, reduce el cooldown de disparo
     * y avanza la animación de ataque si está activa.
     *
     * @param delta tiempo en segundos transcurrido desde el último frame
     */
    // actualizar la direccion
    @Override
    public void update(double delta) {
        // Guardar última dirección de movimiento (solo cuando se mueve)
        if (dx != 0 || dy != 0) {
            ultimaDirX = dx;
            ultimaDirY = dy;
        }

        // Reducir el tiempo de recarga en cada frame
        if (cooldownDisparo > 0) {
            cooldownDisparo--;
        }

        // Reducir el destello de daño
        if (hurtTimer > 0) {
            hurtTimer--;
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

    /**
     * Indica si el jugador puede disparar, es decir, si el cooldown de disparo ha llegado a 0.
     *
     * @return {@code true} si el arma está lista; {@code false} si aún está en recarga
     */
    // metodo para saber si el arma esta lista (cooldown)
    public boolean puedeDisparar() {
        return cooldownDisparo <= 0;
    }

    /**
     * Lanza un proyectil en la dirección indicada.
     * Activa la animación de ataque, reinicia el cooldown y actualiza la dirección visual del sprite.
     * El proyectil se genera centrado respecto al jugador y desplazado en la dirección de disparo.
     *
     * @param direccionX componente X de la dirección de disparo (-1, 0 o 1)
     * @param direccionY componente Y de la dirección de disparo (-1, 0 o 1)
     * @return el {@code Proyectiles} creado con el daño base actual del jugador
     */
    // lanza una daga en la dirección indicada
    public Proyectiles disparar(double direccionX, double direccionY) {
        atacando = true;
        frameAtaque = 0;
        contadorAtaque = 0;
        cooldownDisparo = MAX_COOLDOWN_DISPARO;

        // Actualizar dirección visual para que el sprite mire hacia donde dispara
        if (direccionX != 0 || direccionY != 0) {
            ultimaDirX = direccionX;
            ultimaDirY = direccionY;
        }

        double centroJugadorX = this.x + (this.width / 2);
        double centroJugadorY = this.y + (this.height / 2);
        double balaX = centroJugadorX - 30;
        double balaY = centroJugadorY - 30;

        balaX += direccionX * 40;
        balaY += direccionY * 40;

        // se renplaza el daño por daño base para futuras actualizaciones
        return new Proyectiles(balaX, balaY, 60, 60, 2.5, direccionX, direccionY, danoBase, true);
    }

    /**
     * Resta la cantidad indicada del dinero del jugador.
     *
     * @param cantidad monto a descontar
     */
    public void restarDinero(int cantidad) {
        this.dinero -= cantidad;
    }

    /**
     * Aumenta la vida máxima y la vida actual del jugador en la cantidad indicada.
     *
     * @param cantidad puntos de vida máxima a agregar
     */
    public void aumentarVidaMaxima(int cantidad) {
        this.vidaMaxima += cantidad;
        this.vidaActual += cantidad;
    }

    /**
     * Incrementa el daño base del jugador en la cantidad indicada.
     *
     * @param cantidad puntos de daño a agregar
     */
    public void aumentarDano(int cantidad) {
        this.danoBase += cantidad;
    }

    /**
     * Recibe daño y activa el destello blanco de hurt.
     *
     * @param cantidad puntos de daño a aplicar
     */
    @Override
    public void recibirDano(int cantidad) {
        super.recibirDano(cantidad);
        // Solo activar el destello si realmente recibió daño (no estaba invulnerable)
        if (ticksInvulnerables == MAX_TICKS_INVULNERABLES) {
            hurtTimer = DURACION_HURT;
        }
    }

    /**
     * Retorna los límites de colisión (hitbox) del jugador.
     * Se aplica un margen del 20% en cada lado para ajustar la hitbox
     * al tamaño visual real del sprite.
     *
     * @return un {@code Rectangle2D} que representa la hitbox reducida del jugador
     */
    // al igual que enemigo se actualiza la hitbox porque colisionan sin tocarse
    @Override
    public javafx.geometry.Rectangle2D getBounds() {
        // Le quitamos un 30% del tamaño a la caja de colisión
        double margenX = this.width * 0.20;
        double margenY = this.height * 0.20;

        return new javafx.geometry.Rectangle2D(
                this.x + margenX,
                this.y + margenY,
                this.width - (margenX * 2),
                this.height - (margenY * 2)
        );
    }

    /**
     * Retorna el dinero actual del jugador.
     *
     * @return cantidad de dinero
     */
    public int getDinero() { return dinero; }

    /**
     * Suma la cantidad indicada al dinero del jugador.
     *
     * @param cantidad monto a agregar
     */
    public void sumarDinero(int cantidad) { this.dinero += cantidad; }

    /**
     * Indica si el jugador está ejecutando una animación de ataque.
     *
     * @return {@code true} si está atacando; {@code false} en caso contrario
     */
    public boolean isAtacando()    { return atacando; }

    /**
     * Indica si el jugador debe mostrar el destello blanco de daño recibido.
     * Parpadea en frames pares para dar efecto de invulnerabilidad clásico.
     *
     * @return {@code true} si debe dibujarse el destello blanco
     */
    public boolean isHurt()        { return hurtTimer > 0 && hurtTimer % 2 == 0; }

    /**
     * Retorna el frame actual de la animación de ataque.
     *
     * @return índice del frame de ataque actual
     */
    public int getFrameAtaque()    { return frameAtaque; }

    /**
     * Retorna la última componente X de la dirección de movimiento o disparo del jugador.
     *
     * @return valor X de la última dirección
     */
    public double getUltimaDirX()  { return ultimaDirX; }

    /**
     * Retorna la última componente Y de la dirección de movimiento o disparo del jugador.
     *
     * @return valor Y de la última dirección
     */
    public double getUltimaDirY()  { return ultimaDirY; }
}