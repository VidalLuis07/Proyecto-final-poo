package edu.trespor2.videojuego.model.entidades.personajes;

import java.util.ArrayList;
import java.util.List;

public class Jefe extends Enemigo {

    private int ataque;
    private int faseActual;
    private int framesPorFase;

    private List<Zombie> zombiesInvocados;

    /**
     * Construye un jefe con las dimensiones y estadísticas dadas.
     * El jefe inicia en la fase 1 con un ataque de 2 puntos de daño.
     *
     * @param x          posición inicial en el eje X
     * @param y          posición inicial en el eje Y
     * @param width      ancho del jefe en píxeles
     * @param height     alto del jefe en píxeles
     * @param velocidad  velocidad de movimiento del jefe
     * @param vidaMaxima cantidad máxima de vida del jefe
     */
    public Jefe(double x, double y, double width, double height, double velocidad, int vidaMaxima) {
        super(x, y, width, height, velocidad, vidaMaxima);

        this.ataque = 2; // Pega el doble que un zombie normal
        this.faseActual = 1;
        this.framesPorFase = 0;

        this.zombiesInvocados = new ArrayList<>();
    }

    /**
     * Define el comportamiento del jefe en cada frame.
     * Alterna entre dos fases cada 180 frames:
     * <ul>
     *   <li><b>Fase 1:</b> persigue al jugador para atacar cuerpo a cuerpo.</li>
     *   <li><b>Fase 2:</b> se detiene e invoca una horda de zombies al inicio de la fase.</li>
     * </ul>
     *
     * @param jugador el jugador al que el jefe persigue o reacciona
     */
    @Override
    public void perseguir(Jugador jugador) {
        framesPorFase++;

        if (framesPorFase >= 180) {
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

    /**
     * Invoca una horda de 3 zombies alrededor de la posición actual del jefe.
     * Los zombies aparecen a los lados y debajo del jefe con estadísticas estándar.
     */
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

    /**
     * Retorna la lista de zombies invocados por el jefe que aún no han sido
     * transferidos a la sala.
     *
     * @return lista de {@code Zombie} invocados pendientes de agregar a la sala
     */
    // La sala usará esto para ver si el jefe invocó algo nuevo
    public List<Zombie> getZombiesInvocados() {
        return zombiesInvocados;
    }

    /**
     * Vacía la lista de zombies invocados del jefe.
     * Debe llamarse después de que la sala haya recogido los zombies para evitar duplicados.
     */
    // La sala usará esto después de sacar a los zombies, para vaciarle los bolsillos al jefe
    public void limpiarZombiesInvocados() {
        zombiesInvocados.clear();
    }

    // --- ATAQUE ---

    /**
     * Aplica daño al jugador igual al valor de ataque del jefe.
     *
     * @param jugador el jugador que recibirá el daño
     */
    public void atacar(Jugador jugador) {
        jugador.recibirDano(this.ataque);
    }
}