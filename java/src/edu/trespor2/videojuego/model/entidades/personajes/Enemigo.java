package edu.trespor2.videojuego.model.entidades.personajes;

public abstract class Enemigo extends GameCharacter {

    public Enemigo(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);

        // hace que no tengan escudo o invulnerabilidad los enemigos
        this.MAX_TICKS_INVULNERABLES = 0;
    }
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
    public abstract void perseguir (Jugador jugador);

}
