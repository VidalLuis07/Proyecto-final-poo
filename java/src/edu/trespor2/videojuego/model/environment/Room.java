package edu.trespor2.videojuego.model.environment;

import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.items.Chest;
import edu.trespor2.videojuego.model.items.Coins;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public enum TipoSala { INICIO, NORMAL, TIENDA, JEFE }

    private TipoSala tipo;
    private Tile[][] mapaTiles;
    private List<Door> puertas;
    private List<Enemigo> enemigos;
    private List<Chest> cofres;
    private List<Coins> monedasEnSuelo;
    private boolean estaLimpia;

    private int columnas;
    private int filas;

    // ── Tamaño de cada tile en píxeles ─────────────────────────────────────
    public static final int TILE_SIZE = 32;

    // ── Offset: desplazamiento para centrar la sala en la pantalla ─────────
    // Se calcula en inicializarOffset() una sola vez al arrancar el juego
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
    }

    // ══════════════════════════════════════════════════════════════════════
    //  NUEVO: Llama esto UNA SOLA VEZ desde GameLoop antes de iniciarJuego()
    //  para que la sala quede centrada en la pantalla.
    //
    //  Ejemplo en GameLoop:
    //      Room.inicializarOffset(ancho, alto, 15, 10);
    // ══════════════════════════════════════════════════════════════════════
    public static void inicializarOffset(double anchoCanvas, double altoCanvas,
                                         int columnasRoom, int filasRoom) {
        double salaAnchoPx = columnasRoom * TILE_SIZE;
        double salaAltoPx  = filasRoom    * TILE_SIZE;
        offsetX = (anchoCanvas - salaAnchoPx) / 2.0;
        offsetY = (altoCanvas  - salaAltoPx)  / 2.0;
    }

    public static double getOffsetX() { return offsetX; }
    public static double getOffsetY() { return offsetY; }

    // ══════════════════════════════════════════════════════════════════════
    //  Genera tiles con el offset aplicado para que queden centrados
    // ══════════════════════════════════════════════════════════════════════
    private void generarTiles(int columnas, int filas) {
        for (int col = 0; col < columnas; col++) {
            for (int fila = 0; fila < filas; fila++) {
                boolean esBorde = (col == 0 || col == columnas - 1
                        || fila == 0 || fila == filas - 1);

                double pixelX = offsetX + col * TILE_SIZE;
                double pixelY = offsetY + fila * TILE_SIZE;

                mapaTiles[col][fila] = new Tile(pixelX, pixelY, !esBorde);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  Devuelve el centro de la sala en píxeles (para spawnear al jugador)
    // ══════════════════════════════════════════════════════════════════════
    public double getCentroX() {
        return offsetX + (columnas / 2.0) * TILE_SIZE;
    }

    public double getCentroY() {
        return offsetY + (filas / 2.0) * TILE_SIZE;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MÉTODOS EXISTENTES (sin cambios)
    // ══════════════════════════════════════════════════════════════════════

    public TipoSala getTipo() { return tipo; }
    public void setTipo(TipoSala tipo) { this.tipo = tipo; }

    public void addPuerta(Door puerta)   { puertas.add(puerta); }
    public void addEnemigo(Enemigo e)    { enemigos.add(e); }
    public void addCofre(Chest cofre)    { cofres.add(cofre); }
    public void addMoneda(Coins moneda)  { monedasEnSuelo.add(moneda); }

    public void setTile(int x, int y, Tile tile) { mapaTiles[x][y] = tile; }

    public void actualizarEstadoSala() {
        if (estaLimpia) return;
        boolean quedanVivos = false;
        for (Enemigo e : enemigos) {
            if (!e.estaMuerto()) { quedanVivos = true; break; }
        }
        if (!quedanVivos) {
            estaLimpia = true;
            for (Door puerta : puertas) puerta.abrir();
        }
    }

    public boolean isEstaLimpia()             { return estaLimpia; }
    public List<Door> getPuertas()            { return puertas; }
    public List<Enemigo> getEnemigos()        { return enemigos; }
    public List<Chest> getCofres()            { return cofres; }
    public List<Coins> getMonedasEnSuelo()    { return monedasEnSuelo; }
    public Tile[][] getMapaTiles()            { return mapaTiles; }
}