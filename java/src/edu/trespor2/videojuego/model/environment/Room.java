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
    //  Llama esto UNA SOLA VEZ desde GameLoop antes de iniciarJuego()
    //  para que la sala quede centrada en la pantalla.
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
    //  Genera tiles con la estructura:
    //    - 2 filas/columnas de borde → VACIO  (negro, no transitable)
    //    - 1 fila/columna de pared   → PARED  (sprite pared, no transitable)
    //    - Todo lo demás             → PISO   (sprite piso, transitable)
    // ══════════════════════════════════════════════════════════════════════
    private void generarTiles(int columnas, int filas) {
        for (int col = 0; col < columnas; col++) {
            for (int fila = 0; fila < filas; fila++) {
                double pixelX = offsetX + col * TILE_SIZE;
                double pixelY = offsetY + fila * TILE_SIZE;

                Tile.TipoTile tipo;
                boolean transitable;

                // Borde exterior de 2 tiles → VACIO
                boolean esBordeVacio = (col < 2 || col >= columnas - 2
                        || fila < 2 || fila >= filas - 2);

                // Una capa de pared justo dentro del borde vacío
                boolean esPared = (col == 2 || col == columnas - 3
                        || fila == 2 || fila == filas - 3);

                if (esBordeVacio) {
                    tipo = Tile.TipoTile.VACIO;
                    transitable = false;
                } else if (esPared) {
                    tipo = Tile.TipoTile.PARED;
                    transitable = false;
                } else {
                    tipo = Tile.TipoTile.PISO;
                    transitable = true;
                }

                mapaTiles[col][fila] = new Tile(pixelX, pixelY, transitable, tipo);
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