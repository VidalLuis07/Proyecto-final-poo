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

    public Room(int columnas, int filas) {
        this.mapaTiles = new Tile[columnas][filas];
        this.puertas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.cofres = new ArrayList<>();
        this.monedasEnSuelo = new ArrayList<>();
        this.estaLimpia = false;

        this.tipo = TipoSala.NORMAL;
    }

    // 3. ¡Estos son los métodos que quitaban el error "Cannot resolve method setTipo"!
    public TipoSala getTipo() { return tipo; }
    public void setTipo(TipoSala tipo) { this.tipo = tipo; }

    public void addPuerta(Door puerta) { puertas.add(puerta); }
    public void addEnemigo(Enemigo enemigo) { enemigos.add(enemigo); }
    public void addCofre(Chest cofre) { cofres.add(cofre); }
    public void addMoneda(Coins moneda) { monedasEnSuelo.add(moneda); }

    public void setTile(int x, int y, Tile tile) {
        mapaTiles[x][y] = tile;
    }

    public void actualizarEstadoSala() {
        if (estaLimpia) return;

        boolean quedanEnemigosVivos = false;

        for (Enemigo e : enemigos) {
            if (!e.estaMuerto()) {
                quedanEnemigosVivos = true;
                break;
            }
        }

        if (!quedanEnemigosVivos) {
            estaLimpia = true;
            for (Door puerta : puertas) {
                puerta.abrir();
            }
        }
    }

    public boolean isEstaLimpia() { return estaLimpia; }
    public List<Door> getPuertas() { return puertas; }
    public List<Enemigo> getEnemigos() { return enemigos; }
    public List<Chest> getCofres() { return cofres; }
    public List<Coins> getMonedasEnSuelo() { return monedasEnSuelo; }
    public Tile[][] getMapaTiles() { return mapaTiles; }
}