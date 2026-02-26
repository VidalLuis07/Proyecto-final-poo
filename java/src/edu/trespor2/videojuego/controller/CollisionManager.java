package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.EntidadMovible;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Tile;
import javafx.geometry.Rectangle2D;

import java.util.List;

public class CollisionManager {

    public void checkCollisions(Jugador jugador, List<Enemigo> enemigos, List<Proyectiles> proyectilesJugador) {

        for (int i = 0; i < proyectilesJugador.size(); i++) {
            Proyectiles proyectil = proyectilesJugador.get(i);

            for (int j = 0; j < enemigos.size(); j++) {
                Enemigo enemigo = enemigos.get(j);

                // verificar el estado del enemigo al impactar
                if (!enemigo.estaMuerto() && proyectil.getBounds().intersects(enemigo.getBounds())) {
                    enemigo.recibirDano(proyectil.getDano());
                    proyectilesJugador.remove(i);
                    i--;
                    break;
                }
            }
        }
        // si eliminamos al enemigo desaparece
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaMuerto() && enemigo.getBounds().intersects(jugador.getBounds())) {
                if (enemigo instanceof Zombie) {
                    ((Zombie) enemigo).atacar(jugador);
                }
            }
        }
    }

    //COLISIÓN CON PAREDES
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

    // Revisa si un rectángulo choca con algún tile no transitable
    private boolean colisionaConPared(Rectangle2D rect, Tile[][] tiles, int tileSize) {
        // Los 4 tiles que toca el rectángulo
        int colMin = (int)((rect.getMinX() - Room.getOffsetX()) / tileSize);
        int colMax = (int)((rect.getMaxX() - Room.getOffsetX() - 1) / tileSize);
        int filaMin = (int)((rect.getMinY() - Room.getOffsetY()) / tileSize);
        int filaMax = (int)((rect.getMaxY() - Room.getOffsetY() - 1) / tileSize);

        for (int col = colMin; col <= colMax; col++) {
            for (int fila = filaMin; fila <= filaMax; fila++) {
                if (col < 0 || fila < 0 || col >= tiles.length || fila >= tiles[col].length) {
                    return true;
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