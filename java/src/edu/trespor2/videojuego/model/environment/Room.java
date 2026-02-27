package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;
import edu.trespor2.videojuego.model.items.Chest;
import edu.trespor2.videojuego.model.items.Coins;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {

    public enum TipoSala { INICIO, NORMAL, TIENDA, JEFE }

    private TipoSala tipo;
    private Tile[][] mapaTiles;
    private List<Door> puertas;
    private List<Enemigo> enemigos;
    private List<Chest> cofres;
    private List<Coins> monedasEnSuelo;
    private boolean estaLimpia;
    private boolean tiendaVisitada = false;

    private int columnas;
    private int filas;

    public static final int TILE_SIZE = 32;

    private static double offsetX = 0;
    private static double offsetY = 0;

    public Room(int columnas, int filas) {
        this.columnas   = columnas;
        this.filas      = filas;
        this.mapaTiles  = new Tile[columnas][filas];
        this.puertas    = new ArrayList<>();
        this.enemigos   = new ArrayList<>();
        this.cofres     = new ArrayList<>();
        this.monedasEnSuelo = new ArrayList<>();
        this.estaLimpia = false;
        this.tipo       = TipoSala.NORMAL;

        generarTiles(columnas, filas);
        generarEnemigos();
    }

    public static void inicializarOffset(double anchoCanvas, double altoCanvas,
                                         int columnasRoom, int filasRoom) {
        double salaAnchoPx = columnasRoom * TILE_SIZE;
        double salaAltoPx  = filasRoom    * TILE_SIZE;
        offsetX = (anchoCanvas - salaAnchoPx) / 2.0;
        offsetY = (altoCanvas  - salaAltoPx)  / 2.0;
    }

    public static double getOffsetX() { return offsetX; }
    public static double getOffsetY() { return offsetY; }

    private void generarTiles(int columnas, int filas) {
        for (int col = 0; col < columnas; col++) {
            for (int fila = 0; fila < filas; fila++) {
                double pixelX = offsetX + col * TILE_SIZE;
                double pixelY = offsetY + fila * TILE_SIZE;

                Tile.TipoTile tipoTileActual;
                boolean transitable;

                boolean esBordeVacio = (col < 2 || col >= columnas - 2
                        || fila < 2 || fila >= filas - 2);

                boolean esPared = (col == 2 || col == columnas - 3
                        || fila == 2 || fila == filas - 3);

                if (esBordeVacio) {
                    tipoTileActual = Tile.TipoTile.VACIO;
                    transitable = false;
                } else if (esPared) {
                    tipoTileActual = Tile.TipoTile.PARED;
                    transitable = false;
                } else {
                    tipoTileActual = Tile.TipoTile.PISO;
                    transitable = true;
                }

                mapaTiles[col][fila] = new Tile(pixelX, pixelY, transitable, tipoTileActual);
            }
        }
    }

    private void generarEnemigos() {
        if (this.tipo == TipoSala.INICIO || this.tipo == TipoSala.TIENDA) {
            this.estaLimpia = true;
            this.enemigos.clear();
            return;
        }

        Random random = new Random();
        int cantidadZombies = 1;

        int minCol = 3;
        int maxCol = columnas - 4;
        int minFila = 3;
        int maxFila = filas - 4;

        if (maxCol < minCol || maxFila < minFila) return;

        // Lógica de espaciado (del segundo código)
        double distMinimaEntreEnemigos = 100.0;
        double distMinimaAlCentro = 150.0;
        double centroX = getCentroX();
        double centroY = getCentroY();

        for (int i = 0; i < cantidadZombies; i++) {
            double pixelX = 0;
            double pixelY = 0;
            boolean posicionValida = false;
            int intentos = 0;

            while (!posicionValida && intentos < 20) {
                int randomCol = minCol + random.nextInt((maxCol - minCol) + 1);
                int randomFila = minFila + random.nextInt((maxFila - minFila) + 1);

                pixelX = offsetX + (randomCol * TILE_SIZE);
                pixelY = offsetY + (randomFila * TILE_SIZE);
/*
                double distAlCentro = Math.hypot(pixelX - centroX, pixelY - centroY);
                if (distAlCentro < distMinimaAlCentro) {
                    intentos++;
                    continue;
                }
*/
                boolean muyCercaDeOtro = false;
                for (Enemigo e : this.enemigos) {
                    double distAEnemigo = Math.hypot(pixelX - e.getX(), pixelY - e.getY());
                    if (distAEnemigo < distMinimaEntreEnemigos) {
                        muyCercaDeOtro = true;
                        break;
                    }
                }

                if (muyCercaDeOtro) intentos++;
                else posicionValida = true;
            }

            // NUEVA LÓGICA DE TAMAÑOS Y STATS
            if (posicionValida) {
                double tamaño;
                int vida;
                double velocidad;

                // 20% de probabilidad de ser un zombie grande
                if (random.nextDouble() < 0.04) {
                    tamaño = 64;     // Más grande
                    vida = 50;       // Más vida
                    velocidad = 0.2; // Más lento
                } else {
                    tamaño = 32;     // Tamaño normal
                    vida = 30;        // Vida normal
                    velocidad = 0.4; // Velocidad normal
                }

                Zombie nuevoZombie = new Zombie(pixelX, pixelY, tamaño, tamaño, velocidad, vida);
                this.addEnemigo(nuevoZombie);
            }
        }
    }

    public double getCentroX() {
        return offsetX + (columnas / 2.0) * TILE_SIZE;
    }

    public double getCentroY() {
        return offsetY + (filas / 2.0) * TILE_SIZE;
    }

    public TipoSala getTipo() { return tipo; }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
        if(tipo == TipoSala.INICIO || tipo == TipoSala.TIENDA){
            estaLimpia = true;
            this.enemigos.clear(); // Limpieza preventiva para salas seguras
        } else if (tipo == TipoSala.JEFE) {
            this.enemigos.clear(); // Limpiar zombies del constructor antes de añadir el Jefe
        }
    }

    public void addPuerta(Door puerta)   { puertas.add(puerta); }
    public void addEnemigo(Enemigo e)    { enemigos.add(e); }
    public void addCofre(Chest cofre)    { cofres.add(cofre); }
    public void addMoneda(Coins moneda)  { monedasEnSuelo.add(moneda); }

    public void setTile(int x, int y, Tile tile) { mapaTiles[x][y] = tile; }

    public void actualizarEstadoSala() {
        // Si es sala segura, las puertas siempre se abren
        if (tipo == TipoSala.INICIO || tipo == TipoSala.TIENDA) {
            estaLimpia = true;
            for (Door d : puertas) d.abrir();
            return;
        }

        if (estaLimpia) return;

        boolean quedanVivos = false;
        for (Enemigo e : enemigos) {
            if (!e.estaMuerto()) {
                quedanVivos = true;
                break;
            }
        }

        if (!quedanVivos) {
            estaLimpia = true;
            for (Door puerta : puertas) puerta.abrir();

            // Generar cofre de premio (del segundo código)
            Chest cofrePremio = new Chest(getCentroX() - 16, getCentroY() - 16);
            addCofre(cofrePremio);
            System.out.println("¡Sala limpia! Aparece un cofre.");
        } else {
            // Mientras haya enemigos, las puertas se mantienen cerradas
            for (Door puerta : puertas) puerta.cerrar();
        }
    }
    //evita que al entrar a la tienda no puedas continuar
    public boolean isTiendaVisitada() {
        return tiendaVisitada;
    }

    public void setTiendaVisitada(boolean tiendaVisitada) {
        this.tiendaVisitada = tiendaVisitada;
    }

    public boolean isEstaLimpia()             { return estaLimpia; }
    public List<Door> getPuertas()            { return puertas; }
    public List<Enemigo> getEnemigos()        { return enemigos; }
    public List<Chest> getCofres()            { return cofres; }
    public List<Coins> getMonedasEnSuelo()    { return monedasEnSuelo; }
    public Tile[][] getMapaTiles()            { return mapaTiles; }
}