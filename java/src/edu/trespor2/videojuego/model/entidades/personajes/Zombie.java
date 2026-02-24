package edu.trespor2.videojuego.model.entidades.personajes;

public class Zombie extends Enemigo {

    private int ataque;

    public Zombie(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);
        this.ataque = 1; // daño de ejemplo
    }

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

    public void atacar(Jugador jugador) {
        if (this.getBounds().intersects(jugador.getBounds())) {
            jugador.recibirDano(this.ataque);

        }
    }
}