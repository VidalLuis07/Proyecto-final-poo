package edu.trespor2.videojuego.controller;

import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;

import java.util.List;

public class CollisionManager {

    public void checkCollisions(Jugador jugador, List<Enemigo> enemigos, List<Proyectiles> proyectilesJugador) {

        for (int i = 0; i < proyectilesJugador.size(); i++) {
            Proyectiles proyectil = proyectilesJugador.get(i);

            for (int j = 0; j < enemigos.size(); j++) {
                Enemigo enemigo = enemigos.get(j);

                if (proyectil.getBounds().intersects(enemigo.getBounds())) {
                    enemigo.recibirDano(proyectil.getDano());
                    proyectilesJugador.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (Enemigo enemigo : enemigos) {
            if (enemigo.getBounds().intersects(jugador.getBounds())) {
                // metodo generico de ataque (si se requiere)
                // Si no, hacer un cast owo
                if (enemigo instanceof Zombie) {
                    ((Zombie) enemigo).atacar(jugador);
                }
            }
        }
    }

    //aqui falta agregar un metodo para las colisiones con los muros y otras cosas
}
