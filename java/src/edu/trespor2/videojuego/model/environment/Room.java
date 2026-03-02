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

    /**
     * Construye una sala con el número de columnas y filas de tiles dados.
     * Inicializa las listas de puertas, enemigos, cofres y monedas,
     * genera los tiles y spawea enemigos según el tipo de sala.
     *
     * @param columnas número de columnas de tiles de la sala
     * @param filas    número de filas de tiles de la sala
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
     * Calcula y almacena el desplazamiento (offset) necesario para centrar
     * la sala en el canvas. Debe llamarse una vez antes de crear salas.
     *
     * @param anchoCanvas   ancho del canvas en píxeles
     * @param altoCanvas    alto del canvas en píxeles
     * @param columnasRoom  número de columnas de tiles de la sala
     * @param filasRoom     número de filas de tiles de la sala
     */
    public static void inicializarOffset(double anchoCanvas, double altoCanvas,
                                         int columnasRoom, int filasRoom) {
        double salaAnchoPx = columnasRoom * TILE_SIZE;
        double salaAltoPx  = filasRoom    * TILE_SIZE;
        offsetX = (anchoCanvas - salaAnchoPx) / 2.0;
        offsetY = (altoCanvas  - salaAltoPx)  / 2.0;
    }

    /**
     * Retorna el desplazamiento horizontal para centrar la sala en el canvas.
     *
     * @return offset en el eje X en píxeles
     */
    public static double getOffsetX() { return offsetX; }

    /**
     * Retorna el desplazamiento vertical para centrar la sala en el canvas.
     *
     * @return offset en el eje Y en píxeles
     */
    public static double getOffsetY() { return offsetY; }

    /**
     * Genera la matriz de tiles de la sala.
     * Los 2 tiles del borde exterior son VACÍO, el siguiente anillo es PARED,
     * y el resto es PISO transitable.
     *
     * @param columnas número de columnas de tiles
     * @param filas    número de filas de tiles
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
     * Genera enemigos iniciales en la sala según su tipo.
     * En salas INICIO o TIENDA no se generan enemigos y la sala se marca como limpia.
     * En el resto se intenta colocar zombies en posiciones válidas con un mínimo
     * de separación entre ellos. Hay un 4% de probabilidad de generar un zombie grande.
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

    /**
     * Retorna la coordenada X del centro de la sala en píxeles.
     *
     * @return posición X central de la sala
     */
    public double getCentroX() {
        return offsetX + (columnas / 2.0) * TILE_SIZE;
    }

    /**
     * Retorna la coordenada Y del centro de la sala en píxeles.
     *
     * @return posición Y central de la sala
     */
    public double getCentroY() {
        return offsetY + (filas / 2.0) * TILE_SIZE;
    }

    /**
     * Retorna el tipo actual de la sala.
     *
     * @return tipo de sala ({@code INICIO}, {@code NORMAL}, {@code TIENDA} o {@code JEFE})
     */
    public TipoSala getTipo() { return tipo; }

    /**
     * Establece el tipo de la sala y aplica la lógica correspondiente.
     * Las salas INICIO y TIENDA se marcan como limpias y se eliminan sus enemigos.
     * Las salas JEFE limpian los zombies generados por el constructor.
     *
     * @param tipo nuevo tipo de sala
     */
    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
        if(tipo == TipoSala.INICIO || tipo == TipoSala.TIENDA){
            estaLimpia = true;
            this.enemigos.clear(); // Limpieza preventiva para salas seguras
        } else if (tipo == TipoSala.JEFE) {
            this.enemigos.clear(); // Limpiar zombies del constructor antes de añadir el Jefe
        }
    }

    /**
     * Agrega una puerta a la sala.
     *
     * @param puerta la {@code Door} a agregar
     */
    public void addPuerta(Door puerta)   { puertas.add(puerta); }

    /**
     * Agrega un enemigo a la sala.
     *
     * @param e el {@code Enemigo} a agregar
     */
    public void addEnemigo(Enemigo e)    { enemigos.add(e); }

    /**
     * Agrega un cofre a la sala.
     *
     * @param cofre el {@code Chest} a agregar
     */
    public void addCofre(Chest cofre)    { cofres.add(cofre); }

    /**
     * Agrega una moneda al suelo de la sala.
     *
     * @param moneda la {@code Coins} a agregar
     */
    public void addMoneda(Coins moneda)  { monedasEnSuelo.add(moneda); }

    /**
     * Reemplaza el tile en la posición dada del mapa.
     *
     * @param x    columna del tile a reemplazar
     * @param y    fila del tile a reemplazar
     * @param tile nuevo {@code Tile} a colocar
     */
    public void setTile(int x, int y, Tile tile) { mapaTiles[x][y] = tile; }

    /**
     * Evalúa si la sala ha sido limpiada de enemigos y actúa en consecuencia.
     * En salas seguras (INICIO, TIENDA) las puertas siempre permanecen abiertas.
     * En el resto, si todos los enemigos están muertos, marca la sala como limpia,
     * abre las puertas y genera un cofre de premio en el centro. Si quedan enemigos,
     * mantiene las puertas cerradas.
     */
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

    /**
     * Indica si la tienda de esta sala ya fue visitada en la sesión actual.
     * Evita que la pantalla de tienda se vuelva a abrir al regresar a la sala.
     *
     * @return {@code true} si la tienda ya fue visitada; {@code false} en caso contrario
     */
    //evita que al entrar a la tienda no puedas continuar
    public boolean isTiendaVisitada() {
        return tiendaVisitada;
    }

    /**
     * Establece si la tienda de esta sala ha sido visitada.
     *
     * @param tiendaVisitada {@code true} para marcarla como visitada; {@code false} para resetearla
     */
    public void setTiendaVisitada(boolean tiendaVisitada) {
        this.tiendaVisitada = tiendaVisitada;
    }

    /**
     * Indica si la sala está limpia (sin enemigos vivos).
     *
     * @return {@code true} si la sala está limpia; {@code false} en caso contrario
     */
    public boolean isEstaLimpia()             { return estaLimpia; }

    /**
     * Retorna la lista de puertas de la sala.
     *
     * @return lista de {@code Door}
     */
    public List<Door> getPuertas()            { return puertas; }

    /**
     * Retorna la lista de enemigos presentes en la sala.
     *
     * @return lista de {@code Enemigo}
     */
    public List<Enemigo> getEnemigos()        { return enemigos; }

    /**
     * Retorna la lista de cofres presentes en la sala.
     *
     * @return lista de {@code Chest}
     */
    public List<Chest> getCofres()            { return cofres; }

    /**
     * Retorna la lista de monedas en el suelo de la sala.
     *
     * @return lista de {@code Coins}
     */
    public List<Coins> getMonedasEnSuelo()    { return monedasEnSuelo; }

    /**
     * Retorna la matriz de tiles que componen el mapa de la sala.
     *
     * @return matriz de {@code Tile} de dimensiones [columnas][filas]
     */
    public Tile[][] getMapaTiles()            { return mapaTiles; }
}