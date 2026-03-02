package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Zombie;
import edu.trespor2.videojuego.model.items.Chest;
import edu.trespor2.videojuego.model.items.Coins;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa una sala dentro de la mazmorra.
 * Contiene tiles, enemigos, puertas, cofres y monedas.
 * También gestiona su estado (limpia o con enemigos activos).
 */
public class Room {

    /**
     * Tipos posibles de sala dentro del juego.
     */
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

    /**
     * Tamaño en píxeles de cada tile.
     */
    public static final int TILE_SIZE = 32;

    private static double offsetX = 0;
    private static double offsetY = 0;

    /**
     * Constructor de la sala.
     *
     * @param columnas Número de columnas en tiles.
     * @param filas Número de filas en tiles.
     */
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

    /**
     * Inicializa el offset para centrar la sala en el canvas.
     *
     * @param anchoCanvas Ancho del canvas.
     * @param altoCanvas Alto del canvas.
     * @param columnasRoom Columnas de la sala.
     * @param filasRoom Filas de la sala.
     */
    public static void inicializarOffset(double anchoCanvas, double altoCanvas,
                                         int columnasRoom, int filasRoom) {
        double salaAnchoPx = columnasRoom * TILE_SIZE;
        double salaAltoPx  = filasRoom    * TILE_SIZE;
        offsetX = (anchoCanvas - salaAnchoPx) / 2.0;
        offsetY = (altoCanvas  - salaAltoPx)  / 2.0;
    }

    /**
     * @return Offset horizontal de renderizado.
     */
    public static double getOffsetX() { return offsetX; }

    /**
     * @return Offset vertical de renderizado.
     */
    public static double getOffsetY() { return offsetY; }

    /**
     * Genera la estructura base de tiles de la sala
     * (bordes vacíos, paredes y piso transitable).
     */
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

    /**
     * Genera enemigos dentro de la sala si corresponde.
     * No genera enemigos en salas de INICIO o TIENDA.
     */
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

        for (int i = 0; i < cantidadZombies; i++) {

            int randomCol = minCol + random.nextInt((maxCol - minCol) + 1);
            int randomFila = minFila + random.nextInt((maxFila - minFila) + 1);

            double pixelX = offsetX + (randomCol * TILE_SIZE);
            double pixelY = offsetY + (randomFila * TILE_SIZE);

            double tamaño;
            int vida;
            double velocidad;

            if (random.nextDouble() < 0.04) {
                tamaño = 64;
                vida = 50;
                velocidad = 0.2;
            } else {
                tamaño = 32;
                vida = 30;
                velocidad = 0.4;
            }

            Zombie nuevoZombie = new Zombie(pixelX, pixelY, tamaño, tamaño, velocidad, vida);
            this.addEnemigo(nuevoZombie);
        }
    }

    /**
     * @return Coordenada X del centro de la sala.
     */
    public double getCentroX() {
        return offsetX + (columnas / 2.0) * TILE_SIZE;
    }

    /**
     * @return Coordenada Y del centro de la sala.
     */
    public double getCentroY() {
        return offsetY + (filas / 2.0) * TILE_SIZE;
    }

    /**
     * Actualiza el estado de la sala.
     * Abre puertas si todos los enemigos han sido derrotados.
     * Genera un cofre de recompensa al limpiarse.
     */
    public void actualizarEstadoSala() {

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

            Chest cofrePremio = new Chest(getCentroX() - 16, getCentroY() - 16);
            addCofre(cofrePremio);

        } else {
            for (Door puerta : puertas) puerta.cerrar();
        }
    }

    /** @return Tipo de la sala. */
    public TipoSala getTipo() { return tipo; }

    /** @param tipo Nuevo tipo de sala. */
    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
        if(tipo == TipoSala.INICIO || tipo == TipoSala.TIENDA){
            estaLimpia = true;
            this.enemigos.clear();
        } else if (tipo == TipoSala.JEFE) {
            this.enemigos.clear();
        }
    }

    /** @return Indica si la tienda ya fue visitada. */
    public boolean isTiendaVisitada() { return tiendaVisitada; }

    /** @param tiendaVisitada Marca si la tienda fue visitada. */
    public void setTiendaVisitada(boolean tiendaVisitada) {
        this.tiendaVisitada = tiendaVisitada;
    }

    /** @return Indica si la sala está limpia. */
    public boolean isEstaLimpia() { return estaLimpia; }

    /** @return Lista de puertas de la sala. */
    public List<Door> getPuertas() { return puertas; }

    /** @return Lista de enemigos presentes. */
    public List<Enemigo> getEnemigos() { return enemigos; }

    /** @return Lista de cofres en la sala. */
    public List<Chest> getCofres() { return cofres; }

    /** @return Lista de monedas en el suelo. */
    public List<Coins> getMonedasEnSuelo() { return monedasEnSuelo; }

    /** @return Matriz de tiles de la sala. */
    public Tile[][] getMapaTiles() { return mapaTiles; }

    /** Agrega una puerta a la sala. */
    public void addPuerta(Door puerta) { puertas.add(puerta); }

    /** Agrega un enemigo a la sala. */
    public void addEnemigo(Enemigo e) { enemigos.add(e); }

    /** Agrega un cofre a la sala. */
    public void addCofre(Chest cofre) { cofres.add(cofre); }

    /** Agrega una moneda al suelo. */
    public void addMoneda(Coins moneda) { monedasEnSuelo.add(moneda); }

    /** Modifica un tile específico del mapa. */
    public void setTile(int x, int y, Tile tile) { mapaTiles[x][y] = tile; }
}