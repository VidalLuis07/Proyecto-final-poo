package edu.trespor2.videojuego.model.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.trespor2.videojuego.model.entidades.personajes.Zombie;
import edu.trespor2.videojuego.model.entidades.personajes.Jefe;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;

public class Dungeon {

    private Room[][] gridSalas;
    private List<Room> todasLasSalas;
    private Room salaActual;

    private final int FILAS = 5;
    private final int COLUMNAS = 5;

    // Tamaño de sala en tiles (se recibe desde GameLoop)
    private final int colsSala;
    private final int filasSala;

    /**
     * Construye una nueva mazmorra y genera su mapa aleatorio.
     *
     * @param colsSala  número de columnas de tiles que tiene cada sala
     * @param filasSala número de filas de tiles que tiene cada sala
     */
    public Dungeon(int colsSala, int filasSala) {
        this.colsSala  = colsSala;
        this.filasSala = filasSala;

        this.gridSalas = new Room[COLUMNAS][FILAS];
        this.todasLasSalas = new ArrayList<>();

        generarMapaAleatorio();
    }

    /**
     * Genera el mapa de la mazmorra de forma aleatoria.
     * Crea entre 7 y 9 salas conectadas a partir de la sala inicial en el centro del grid,
     * asigna los tipos especiales (TIENDA y JEFE), spawea enemigos y conecta las salas con puertas.
     */
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

        // Asignación de tipos de sala especiales
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

        // Spawnear zombies en todas las salas excepto INICIO y TIENDA
        for (Room sala : todasLasSalas) {
            if (sala.getTipo() != Room.TipoSala.INICIO && sala.getTipo() != Room.TipoSala.TIENDA) {
                spawnearEnemigos(sala, rand);
            }
        }

        // Crear las puertas físicas entre las salas generadas
        conectarSalasConPuertas();

        System.out.println("¡Mazmorra generada con " + totalSalasObjetivo + " salas! (sala: " + colsSala + "x" + filasSala + ")");
    }

    /**
     * Recorre el grid de salas y crea puertas físicas entre cada par de salas adyacentes.
     * Por cada conexión, también rompe 3 tiles de pared para que el jugador pueda pasar.
     */
    private void conectarSalasConPuertas() {
        double tileSize = Room.TILE_SIZE;
        double offsetX = Room.getOffsetX();
        double offsetY = Room.getOffsetY();

        for (int x = 0; x < COLUMNAS; x++) {
            for (int y = 0; y < FILAS; y++) {
                Room actual = gridSalas[x][y];
                if (actual == null) continue;

                int centroCol = colsSala / 2;
                int centroFila = filasSala / 2;

                // NORTE (Pared arriba en fila 2)
                if (y > 0 && gridSalas[x][y - 1] != null) {
                    double px = offsetX + centroCol * tileSize;
                    double py = offsetY + 2 * tileSize;
                    actual.addPuerta(new Door(px, py, "NORTE", gridSalas[x][y - 1]));

                    // Rompemos 3 bloques de pared para que quepa el jugador (96px)
                    actual.setTile(centroCol - 1, 2, new Tile(px - tileSize, py, true, Tile.TipoTile.PISO));
                    actual.setTile(centroCol,     2, new Tile(px,            py, true, Tile.TipoTile.PISO));
                    actual.setTile(centroCol + 1, 2, new Tile(px + tileSize, py, true, Tile.TipoTile.PISO));
                }

                // SUR (Pared abajo en filasSala - 3)
                if (y < FILAS - 1 && gridSalas[x][y + 1] != null) {
                    double px = offsetX + centroCol * tileSize;
                    double py = offsetY + (filasSala - 3) * tileSize;
                    actual.addPuerta(new Door(px, py, "SUR", gridSalas[x][y + 1]));

                    actual.setTile(centroCol - 1, filasSala - 3, new Tile(px - tileSize, py, true, Tile.TipoTile.PISO));
                    actual.setTile(centroCol,     filasSala - 3, new Tile(px,            py, true, Tile.TipoTile.PISO));
                    actual.setTile(centroCol + 1, filasSala - 3, new Tile(px + tileSize, py, true, Tile.TipoTile.PISO));
                }

                // OESTE (Pared izquierda en col 2)
                if (x > 0 && gridSalas[x - 1][y] != null) {
                    double px = offsetX + 2 * tileSize;
                    double py = offsetY + centroFila * tileSize;
                    actual.addPuerta(new Door(px, py, "OESTE", gridSalas[x - 1][y]));

                    actual.setTile(2, centroFila - 1, new Tile(px, py - tileSize, true, Tile.TipoTile.PISO));
                    actual.setTile(2, centroFila,     new Tile(px, py,            true, Tile.TipoTile.PISO));
                    actual.setTile(2, centroFila + 1, new Tile(px, py + tileSize, true, Tile.TipoTile.PISO));
                }

                // ESTE (Pared derecha en colsSala - 3)
                if (x < COLUMNAS - 1 && gridSalas[x + 1][y] != null) {
                    double px = offsetX + (colsSala - 3) * tileSize;
                    double py = offsetY + centroFila * tileSize;
                    actual.addPuerta(new Door(px, py, "ESTE", gridSalas[x + 1][y]));

                    actual.setTile(colsSala - 3, centroFila - 1, new Tile(px, py - tileSize, true, Tile.TipoTile.PISO));
                    actual.setTile(colsSala - 3, centroFila,     new Tile(px, py,            true, Tile.TipoTile.PISO));
                    actual.setTile(colsSala - 3, centroFila + 1, new Tile(px, py + tileSize, true, Tile.TipoTile.PISO));
                }
            }
        }
    }

    /**
     * Spawea enemigos en la sala dada.
     * En salas de tipo JEFE se genera un único {@code Jefe}; en el resto se generan
     * entre 2 y 4 {@code Zombie}, posicionados en el 50% central de la sala.
     *
     * @param sala sala donde se spawnearán los enemigos
     * @param rand instancia de {@code Random} para calcular posiciones y cantidades
     */
    private void spawnearEnemigos(Room sala, Random rand) {
        int cantidad = sala.getTipo() == Room.TipoSala.JEFE ? 1 : rand.nextInt(3) + 2;

        // Limitamos el área de spawn exclusivamente al 50% central de la sala
        int tileInicioCol  = colsSala / 4;
        int tileFinCol     = (colsSala * 3) / 4;
        int tileInicioFila = filasSala / 4;
        int tileFinFila    = (filasSala * 3) / 4;

        for (int i = 0; i < cantidad; i++) {
            // Se calculan las posiciones lejos de las puertas
            int col  = tileInicioCol + rand.nextInt(Math.max(1, tileFinCol - tileInicioCol));
            int fila = tileInicioFila + rand.nextInt(Math.max(1, tileFinFila - tileInicioFila));

            double spawnX = Room.getOffsetX() + col  * Room.TILE_SIZE;
            double spawnY = Room.getOffsetY() + fila * Room.TILE_SIZE;

            if (sala.getTipo() == Room.TipoSala.JEFE) {
                Jefe jefe = new Jefe(spawnX, spawnY, 96, 96, 1.5, 150);
                sala.addEnemigo(jefe);
                break;
            } else {
                Zombie z = new Zombie(spawnX, spawnY, 48, 48, 1, 30);
                sala.addEnemigo(z);
            }
        }
    }

    /**
     * Cambia la sala actual a la sala destino de la puerta cruzada y
     * reposiciona al jugador en el lado opuesto de la entrada según la dirección.
     *
     * @param puertaCruzada la puerta que el jugador ha cruzado
     * @param jugador       el jugador cuya posición será actualizada
     */
    public void cambiarSala(Door puertaCruzada, Jugador jugador) {
        this.salaActual = puertaCruzada.getSalaDestino();
        String direccion = puertaCruzada.getPosicionBorde();

        double tileSize = Room.TILE_SIZE;
        double offsetX = Room.getOffsetX();
        double offsetY = Room.getOffsetY();

        // IMPORTANTE: Usa el ancho real del jugador para centrarlo
        double pW = jugador.getWidth();
        double pH = jugador.getHeight();

        // 1. Calculamos el centro de los ejes para posicionar al jugador en el medio del pasillo
        double centroX = offsetX + (colsSala / 2.0) * tileSize - (pW / 2.0);
        double centroY = offsetY + (filasSala / 2.0) * tileSize - (pH / 2.0);

        if (direccion.equals("NORTE")) {
            // Si entras por el NORTE de una sala, apareces en el SUR (abajo) de la nueva
            jugador.setX(centroX);
            // Te ponemos en la fila de la puerta (filasSala - 3) pero subimos un poco más (- pH - 20)
            jugador.setY(offsetY + (filasSala - 3) * tileSize - pH - 25);

        } else if (direccion.equals("SUR")) {
            // Si entras por el SUR, apareces en el NORTE (arriba)
            jugador.setX(centroX);
            // Te ponemos debajo de la pared norte (fila 2) + un margen de seguridad (+ 25)
            jugador.setY(offsetY + 3 * tileSize + 25);

        } else if (direccion.equals("ESTE")) {
            // Si entras por el ESTE, apareces en el OESTE (izquierda)
            jugador.setX(offsetX + 3 * tileSize + 25);
            jugador.setY(centroY);

        } else if (direccion.equals("OESTE")) {
            // Si entras por el OESTE, apareces en el ESTE (derecha)
            jugador.setX(offsetX + (colsSala - 3) * tileSize - pW - 25);
            jugador.setY(centroY);
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

    /**
     * Retorna el grid bidimensional de salas de la mazmorra.
     *
     * @return matriz de {@code Room} de dimensiones [COLUMNAS][FILAS]
     */
    public Room[][] getGridSalas() { return gridSalas; }

    /**
     * Retorna la lista de todas las salas generadas en la mazmorra.
     *
     * @return lista de {@code Room}
     */
    public List<Room> getTodasLasSalas() { return todasLasSalas; }

    /**
     * Retorna la sala en la que se encuentra actualmente el jugador.
     *
     * @return sala actual
     */
    public Room getSalaActual() { return salaActual; }

    /**
     * Establece la sala actual del jugador.
     *
     * @param salaActual nueva sala actual
     */
    public void setSalaActual(Room salaActual) { this.salaActual = salaActual; }
}