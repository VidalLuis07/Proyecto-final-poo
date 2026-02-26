package edu.trespor2.videojuego.model.entidades.personajes;

import java.util.ArrayList;
import java.util.List;

public class Jefe extends Enemigo {

    private int ataque;
    private int faseActual;
    private int framesPorFase;

    private List<Zombie> zombiesInvocados;

    public Jefe(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);

        this.ataque = 2; // Pega el doble que un zombie normal
        this.faseActual = 1;
        this.framesPorFase = 0;

        this.zombiesInvocados = new ArrayList<>();
    }

    @Override
    public void perseguir(Jugador jugador) {
        framesPorFase++;

        if (framesPorFase >= 600) {
            if (faseActual == 1) {
                faseActual = 2;
            } else {
                faseActual = 1;
            }
            framesPorFase = 0; // Reiniciamos el reloj
        }

        // 2. LA LÓGICA DE LAS FASES
        if (faseActual == 1) {
            // Fase 1: Perseguir al jugador para hacer daño cuerpo a cuerpo
            if (this.getX() < jugador.getX()) {
                this.setDx(1);
            } else if (this.getX() > jugador.getX()) {
                this.setDx(-1);
            } else {
                this.setDx(0);
            }

            if (this.getY() < jugador.getY()) {
                this.setDy(1);
            } else if (this.getY() > jugador.getY()) {
                this.setDy(-1);
            } else {
                this.setDy(0);
            }

        } else if (faseActual == 2) {
            // Fase 2: Quedarse quieto e invocar zombies
            this.setDx(0);
            this.setDy(0);

            if (framesPorFase == 1) {
                invocarZombiesHorda();
            }
        }
    }


    private void invocarZombiesHorda() {
        // 3 zombies que aparecen alrededor  del jefe
        // Caractisticas normales: 32x32 tamaño, 0.6 vel, 5 vida
        Zombie z1 = new Zombie(this.x + 40, this.y, 32, 32, 0.6, 5);
        Zombie z2 = new Zombie(this.x - 40, this.y, 32, 32, 0.6, 5);
        Zombie z3 = new Zombie(this.x, this.y + 40, 32, 32, 0.6, 5);


        zombiesInvocados.add(z1);
        zombiesInvocados.add(z2);
        zombiesInvocados.add(z3);
    }

    // --- MÉTODOS PARA CONECTAR CON LA SALA/GAMELOOP ---

    // La sala usará esto para ver si el jefe invocó algo nuevo
    public List<Zombie> getZombiesInvocados() {
        return zombiesInvocados;
    }

    // La sala usará esto después de sacar a los zombies, para vaciarle los bolsillos al jefe
    public void limpiarZombiesInvocados() {
        zombiesInvocados.clear();
    }

    // --- ATAQUE ---
    public void atacar(Jugador jugador) {
        if (this.getBounds().intersects(jugador.getBounds())) {
            jugador.recibirDano(this.ataque);
        }
    }
}