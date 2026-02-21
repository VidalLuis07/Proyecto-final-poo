package edu.trespor2.videojuego.model.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon {

    private Room[][] gridSalas;
    private List<Room> todasLasSalas;
    private Room salaActual;

    private final int FILAS = 5;
    private final int COLUMNAS = 5;

    public Dungeon() {
        this.gridSalas = new Room[COLUMNAS][FILAS];
        this.todasLasSalas = new ArrayList<>();
        generarMapaAleatorio();
    }

    private void generarMapaAleatorio() {
        Random rand = new Random();

        int totalSalasObjetivo = rand.nextInt(3) + 7;
        int salasCreadas = 0;

        Room salaInicial = new Room(15, 10);
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
                    Room nuevaSala = new Room(15, 10);
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

        System.out.println("¡Mazmorra generada con " + totalSalasObjetivo + " salas!");
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