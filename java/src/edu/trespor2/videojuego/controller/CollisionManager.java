package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.EntidadMovible;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Tile;
import edu.trespor2.videojuego.model.environment.Door; // Importado de la versión 2
import edu.trespor2.videojuego.model.items.Chest;     // Importado de la versión 1
import javafx.geometry.Rectangle2D;

import java.util.List;

public class CollisionManager {

    /**
     * Revisa colisiones entre Proyectiles-Enemigos y Jugador-Enemigos
     */
    public void checkCollisions(Jugador jugador, List<Enemigo> enemigos, List<Proyectiles> proyectilesJugador) {

        // 1. Colisión Proyectiles -> Enemigos
        for (int i = 0; i < proyectilesJugador.size(); i++) {
            Proyectiles proyectil = proyectilesJugador.get(i);

            for (int j = 0; j < enemigos.size(); j++) {
                Enemigo enemigo = enemigos.get(j);

                // Verificar el estado del enemigo al impactar
                if (!enemigo.estaMuerto() && proyectil.getBounds().intersects(enemigo.getBounds())) {
                    enemigo.recibirDano(proyectil.getDano());
                    proyectilesJugador.remove(i);
                    i--;
                    break;
                }
            }
        }

        // 2. Colisión Enemigo -> Jugador (Ataque)
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaMuerto() && enemigo.getBounds().intersects(jugador.getBounds())) {
                if (enemigo instanceof Zombie) {
                    ((Zombie) enemigo).atacar(jugador);
                }
            }
        }
    }

    /**
     * Resuelve el movimiento de una entidad evitando que atraviese paredes
     */
    public double[] resolverColisionParedes(EntidadMovible entidad, Room sala) {
        Tile[][] tiles = sala.getMapaTiles();
        if (tiles == null) return new double[]{entidad.getDx(), entidad.getDy()};

        int tileSize = Room.TILE_SIZE;
        double velocidad = entidad.getVelocidad();

        double nuevaDx = entidad.getDx();
        double nuevaDy = entidad.getDy();

        double futuroX = entidad.getX() + nuevaDx * velocidad;
        double futuroY = entidad.getY() + nuevaDy * velocidad;

        double w = entidad.getWidth();
        double h = entidad.getHeight();

        // Checar movimiento en X
        Rectangle2D rectX = new Rectangle2D(futuroX, entidad.getY(), w, h);
        if (colisionaConPared(rectX, tiles, tileSize)) {
            nuevaDx = 0;
        }

        // Checar movimiento en Y
        Rectangle2D rectY = new Rectangle2D(entidad.getX(), futuroY, w, h);
        if (colisionaConPared(rectY, tiles, tileSize)) {
            nuevaDy = 0;
        }

        return new double[]{nuevaDx, nuevaDy};
    }

    /**
     * Lógica para abrir COFRES (del primer código)
     */
    public void checkCofres(Jugador jugador, List<Chest> cofres) {
        if (cofres == null) return;
        for (Chest cofre : cofres) {
            if (!cofre.isAbierto() && jugador.getBounds().intersects(cofre.getBounds())) {
                int monedasGeneradas = cofre.abrir();
                jugador.sumarDinero(monedasGeneradas);
                System.out.println("¡Abriste un cofre! Ganaste " + monedasGeneradas + " monedas.");
            }
        }
    }

    /**
     * Lógica para detectar entrada a PUERTAS (del segundo código)
     */
    public Door verificarColisionPuerta(Jugador jugador, List<Door> puertas) {
        if (puertas == null) return null;

        // Hitbox expandida para facilitar la detección al acercarse
        Rectangle2D hitboxExpandida = new Rectangle2D(
                jugador.getX() - 20,
                jugador.getY() - 20,
                jugador.getWidth() + 40,
                jugador.getHeight() + 40
        );

        for (Door puerta : puertas) {
            if (puerta.isAbierta() && hitboxExpandida.intersects(puerta.getBounds())) {
                return puerta;
            }
        }
        return null;
    }

    /**
     * Revisa si un rectángulo choca con algún tile no transitable
     */
    private boolean colisionaConPared(Rectangle2D rect, Tile[][] tiles, int tileSize) {
        int colMin = (int)((rect.getMinX() - Room.getOffsetX()) / tileSize);
        int colMax = (int)((rect.getMaxX() - Room.getOffsetX() - 1) / tileSize);
        int filaMin = (int)((rect.getMinY() - Room.getOffsetY()) / tileSize);
        int filaMax = (int)((rect.getMaxY() - Room.getOffsetY() - 1) / tileSize);

        for (int col = colMin; col <= colMax; col++) {
            for (int fila = filaMin; fila <= filaMax; fila++) {
                if (col < 0 || fila < 0 || col >= tiles.length || fila >= tiles[col].length) {
                    return true; // Fuera del mapa cuenta como pared
                }
                Tile tile = tiles[col][fila];
                if (tile != null && !tile.isTransitable()) {
                    return true;
                }
            }
        }
        return false;
    }
}