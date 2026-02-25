package edu.trespor2.videojuego.model.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;

public class Dungeon {

    private Room[][] gridSalas;
    private List<Room> todasLasSalas;
    private Room salaActual;

    private final int FILAS = 5;
    private final int COLUMNAS = 5;

    // Tamaño de sala en tiles (se recibe desde GameLoop)
    private final int colsSala;
    private final int filasSala;

    public Dungeon(int colsSala, int filasSala) {
        this.colsSala  = colsSala;
        this.filasSala = filasSala;

        this.gridSalas = new Room[COLUMNAS][FILAS];
        this.todasLasSalas = new ArrayList<>();
        generarMapaAleatorio();
    }

    private void generarMapaAleatorio() {
        Random rand = new Random();

        int totalSalasObjetivo = rand.nextInt(3) + 7;
        int salasCreadas = 0;

        Room salaInicial = new Room(colsSala, filasSala);
        salaInicial.setTipo(Room.TipoSala.INICIO);
        gridSalas[2][2] = salaInicial;
        todasLasSalas.add(salaInicial);
        this.salaActual = salaInicial;
        salasCreadas++;

        List<Coordenada> salasDisponibles = new ArrayList<>();
        salasDisponibles.add(new Coordenada(2, 2, salaInicial));

        Room salaParaTienda = null;
        Room salaParaJefe = null;

        while (salasCreadas < totalSalasObjetivo) {
            int indexPadre = rand.nextInt(salasDisponibles.size());
            Coordenada padre = salasDisponibles.get(indexPadre);

            int direccion = rand.nextInt(4);
            int nuevoX = padre.x;
            int nuevoY = padre.y;

            if (direccion == 0) nuevoY--;
            else if (direccion == 1) nuevoY++;
            else if (direccion == 2) nuevoX--;
            else if (direccion == 3) nuevoX++;

            if (nuevoX >= 0 && nuevoX < COLUMNAS && nuevoY >= 0 && nuevoY < FILAS) {
                if (gridSalas[nuevoX][nuevoY] == null) {
                    Room nuevaSala = new Room(colsSala, filasSala);
                    gridSalas[nuevoX][nuevoY] = nuevaSala;
                    todasLasSalas.add(nuevaSala);
                    salasDisponibles.add(new Coordenada(nuevoX, nuevoY, nuevaSala));

                    if (salasCreadas == totalSalasObjetivo - 1) {
                        salaParaJefe = nuevaSala;
                        salaParaTienda = padre.sala;
                    }

                    salasCreadas++;
                }
            }
        }

        if (salaParaJefe != null && salaParaTienda != null) {
            salaParaJefe.setTipo(Room.TipoSala.JEFE);

            if (salaParaTienda.getTipo() != Room.TipoSala.INICIO) {
                salaParaTienda.setTipo(Room.TipoSala.TIENDA);
            } else {
                for (Room r : todasLasSalas) {
                    if (r.getTipo() == Room.TipoSala.NORMAL && r != salaParaJefe) {
                        r.setTipo(Room.TipoSala.TIENDA);
                        break;
                    }
                }
            }
        }

        /*Spawnear zombies en todas las salas excepto MENU y TIENDA
        * Nuevo
         */
        for (Room sala : todasLasSalas) {
            if (sala.getTipo() != Room.TipoSala.INICIO && sala.getTipo() != Room.TipoSala.TIENDA) {
                spawnearEnemigos(sala, rand);
            }
        }

        System.out.println("¡Mazmorra generada con " + totalSalasObjetivo + " salas! (sala: " + colsSala + "x" + filasSala + ")");
    }


    // Spawnea zombies en posiciones aleatorias dentro del area transitable de la sala
    private void spawnearEnemigos(Room sala, Random rand) {
        int cantidad = sala.getTipo() == Room.TipoSala.JEFE ? 1 : rand.nextInt(3) + 2; // 2-4 zombies, 1 boss

        // Area transitable: empieza en tile 3 (después de 2 vacío + 1 pared)
        int tileInicio = 3;
        int tileFinCol  = colsSala  - 4; // deja margen al otro lado
        int tileFinFila = filasSala - 4;

        for (int i = 0; i < cantidad; i++) {
            int col  = tileInicio + rand.nextInt(Math.max(1, tileFinCol  - tileInicio));
            int fila = tileInicio + rand.nextInt(Math.max(1, tileFinFila - tileInicio));

            double spawnX = Room.getOffsetX() + col  * Room.TILE_SIZE;
            double spawnY = Room.getOffsetY() + fila * Room.TILE_SIZE;

            Zombie z = new Zombie(spawnX, spawnY, 48, 48, 1.5, 30);
            sala.addEnemigo(z);
        }
    }

    private class Coordenada {
        int x, y;
        Room sala;
        Coordenada(int x, int y, Room sala) {
            this.x = x;
            this.y = y;
            this.sala = sala;
        }
    }

    public Room[][] getGridSalas() { return gridSalas; }
    public List<Room> getTodasLasSalas() { return todasLasSalas; }
    public Room getSalaActual() { return salaActual; }
    public void setSalaActual(Room salaActual) { this.salaActual = salaActual; }
}