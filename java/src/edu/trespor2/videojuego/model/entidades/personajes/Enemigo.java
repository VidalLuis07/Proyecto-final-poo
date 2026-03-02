package edu.trespor2.videojuego.model.entidades.personajes;

public abstract class Enemigo extends GameCharacter {

    /**
     * Construye un enemigo con las dimensiones y estadísticas dadas.
     * Los enemigos no tienen invulnerabilidad, por lo que {@code MAX_TICKS_INVULNERABLES} se establece en 0.
     *
     * @param x          posición inicial en el eje X
     * @param y          posición inicial en el eje Y
     * @param width      ancho del enemigo en píxeles
     * @param height     alto del enemigo en píxeles
     * @param velocidad  velocidad de movimiento del enemigo
     * @param vidaMaxima cantidad máxima de vida del enemigo
     */
    public Enemigo(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);

        // hace que no tengan escudo o invulnerabilidad los enemigos
        this.MAX_TICKS_INVULNERABLES = 0;
    }

    /**
     * Retorna los límites de colisión (hitbox) del enemigo.
     * Se aplica un margen del 10% en cada lado para ajustar la hitbox
     * al tamaño visual real del sprite.
     *
     * @return un {@code Rectangle2D} que representa la hitbox reducida del enemigo
     */
    @Override
    public javafx.geometry.Rectangle2D getBounds() {
        // Reducimos la hitbox un 25% de cada lado por cuestiones de imagen
        double margenX = this.width * 0.10;
        double margenY = this.height * 0.10;

        return new javafx.geometry.Rectangle2D(
                this.x + margenX,
                this.y + margenY,
                this.width - (margenX * 2),
                this.height - (margenY * 2)
        );
    }

    /**
     * Define el comportamiento de persecución del enemigo hacia el jugador.
     * Cada subclase debe implementar su propia lógica de movimiento o ataque.
     *
     * @param jugador el jugador al que el enemigo debe perseguir
     */
    public abstract void perseguir (Jugador jugador);

}