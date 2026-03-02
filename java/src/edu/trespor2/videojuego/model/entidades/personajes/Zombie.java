package edu.trespor2.videojuego.model.entidades.personajes;

public class Zombie extends Enemigo {

    private int ataque;

    /**
     * Construye un zombie con las dimensiones y estadísticas dadas.
     * El daño de ataque base es de 1 punto.
     *
     * @param x          posición inicial en el eje X
     * @param y          posición inicial en el eje Y
     * @param width      ancho del zombie en píxeles
     * @param height     alto del zombie en píxeles
     * @param velocidad  velocidad de movimiento del zombie
     * @param vidaMaxima cantidad máxima de vida del zombie
     */
    public Zombie(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.ataque = 1; // daño de ejemplo
    }

    /**
     * Mueve al zombie en dirección al jugador en cada frame.
     * Calcula la dirección comparando las posiciones de ambos en los ejes X e Y,
     * ajustando {@code dx} y {@code dy} con valores de -1, 0 o 1.
     *
     * @param jugador el jugador al que el zombie persigue
     */
    @Override
    public void perseguir(Jugador jugador) {
        double zombieX = this.getX();
        double zombieY = this.getY();
        double jugadorX = jugador.getX();
        double jugadorY = jugador.getY();

        if (zombieX < jugadorX) {
            this.setDx(1); // El zombie camina a la derecha
        } else if (zombieX > jugadorX) {
            this.setDx(-1); // El zombie camina a la izquierda
        } else {
            this.setDx(0); // No se mueve
        }

        if (zombieY < jugadorY) {
            this.setDy(1); // El zombie va hacia abajo
        } else if (zombieY > jugadorY) {
            this.setDy(-1); // El zombie va hacia arriba
        } else {
            this.setDy(0); // No se mueve
        }
    }

    /**
     * Aplica daño al jugador si las hitboxes de ambos se intersectan.
     * Debe llamarse por cada enemigo presente en la sala.
     *
     * @param jugador el jugador que puede recibir el daño
     */
    //usar for por cada enemigo que haya
    public void atacar(Jugador jugador) {
        if (this.getBounds().intersects(jugador.getBounds())) {
            jugador.recibirDano(this.ataque);
        }
    }
}