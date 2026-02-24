package edu.trespor2.videojuego.main;

import edu.trespor2.videojuego.controller.CollisionManager;
import edu.trespor2.videojuego.controller.InputHandler;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Dungeon;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.view.GameRenderer;
import edu.trespor2.videojuego.view.HUD;
import edu.trespor2.videojuego.view.SpriteManager;
import edu.trespor2.videojuego.view.screens.GameOverScreen;
import edu.trespor2.videojuego.view.screens.MenuScreen;
import edu.trespor2.videojuego.view.screens.ShopScreen;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class GameLoop extends AnimationTimer {

    // ── Estados posibles del juego
    public enum Estado { MENU, JUGANDO, TIENDA, GAME_OVER }
    private Estado estadoActual = Estado.MENU;

    // ── JavaFX
    private final GraphicsContext gc;
    private final double ancho;
    private final double alto;

    // ── Controladores
    private final InputHandler inputHandler;
    private final CollisionManager collisionManager;

    // ver
    private final GameRenderer renderer;
    private final HUD hud;
    private final MenuScreen menuScreen;
    private final GameOverScreen gameOverScreen;
    private ShopScreen shopScreen;

    // modelos
    private Jugador jugador;
    private Dungeon dungeon;
    private List<Proyectiles> proyectiles;

    // tiempos
    private long tiempoAnterior = -1;
    private long ultimoDisparo = 0;

    //detectar el mouse
    private double mouseX      = 0;
    private double mouseY      = 0;
    private double mouseClickX = -1;
    private double mouseClickY = -1;

    //  constructor
    public GameLoop(Canvas canvas, Scene scene) {
        this.gc    = canvas.getGraphicsContext2D();
        this.ancho = canvas.getWidth();
        this.alto  = canvas.getHeight();

        this.inputHandler     = new InputHandler(scene);
        this.collisionManager = new CollisionManager();

        this.renderer       = new GameRenderer();
        this.hud            = new HUD();
        this.menuScreen     = new MenuScreen();
        this.gameOverScreen = new GameOverScreen();

        SpriteManager.getInstance();

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            mouseClickX = e.getX();
            mouseClickY = e.getY();
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
    }

    // void de actualizar 60 veces por segundo

    @Override
    public void handle(long ahora) {
        if (tiempoAnterior < 0) { tiempoAnterior = ahora; return; }
        double delta = (ahora - tiempoAnterior) / 1_000_000_000.0;
        tiempoAnterior = ahora;

        switch (estadoActual) {
            case MENU      -> manejarMenu();
            case JUGANDO   -> manejarJuego(delta);
            case TIENDA    -> manejarTienda();
            case GAME_OVER -> manejarGameOver();
        }

        mouseClickX = -1;
        mouseClickY = -1;
    }

    //  estado
    private void manejarMenu() {
        menuScreen.actualizarMouse(mouseX, mouseY);
        menuScreen.render(gc, ancho, alto);

        if (mouseClickX >= 0) {
            if (menuScreen.isJugarPresionado(mouseClickX, mouseClickY)) {
                iniciarJuego("carlos");
            } else if (menuScreen.isSalirPresionado(mouseClickX, mouseClickY)) {
                System.exit(0);
            }
        }
    }

    private void manejarJuego(double delta) {
        procesarInputJugador();

        // colisiones antes de moverse
        double[] dirAjustada = collisionManager.resolverColisionParedes(jugador, dungeon.getSalaActual());
        jugador.setDx(dirAjustada[0]);
        jugador.setDy(dirAjustada[1]);
        jugador.update(delta);

        // actualizar Enemigos
        List<Enemigo> enemigosActuales = dungeon.getSalaActual().getEnemigos();
        for (int i = 0; i < enemigosActuales.size(); i++) {
            Enemigo e = enemigosActuales.get(i);

            e.perseguir(jugador);
            e.update(delta);

            // si se murio el enemigo lo quita
            if (e.estaMuerto()) {
                enemigosActuales.remove(i);
                i--;
            }
        }

        // proyectiles
        proyectiles.forEach(p -> p.update(delta));
        proyectiles.removeIf(p -> p.getX() < 0 || p.getX() > ancho || p.getY() < 0 || p.getY() > alto);

        // colision
        collisionManager.checkCollisions(jugador, enemigosActuales, proyectiles);
        dungeon.getSalaActual().actualizarEstadoSala();

        // transiciones
        if (dungeon.getSalaActual().getTipo() == edu.trespor2.videojuego.model.environment.Room.TipoSala.TIENDA) {
            estadoActual = Estado.TIENDA;
        }

        if (jugador.estaMuerto()) {
            estadoActual = Estado.GAME_OVER;
        }

        // mostrarlos
        renderer.render(gc, jugador, dungeon.getSalaActual(), proyectiles, ancho, alto);
        hud.render(gc, jugador, dungeon, ancho, alto);
    }

    private void manejarTienda() {
        shopScreen.render(gc, jugador, ancho, alto);

        if (mouseClickX >= 0) {
            boolean salir = shopScreen.onClick(mouseClickX, mouseClickY);
            if (salir) estadoActual = Estado.JUGANDO;
        }
    }

    private void manejarGameOver() {
        gameOverScreen.render(gc, ancho, alto);

        if (mouseClickX >= 0) {
            if (gameOverScreen.isReiniciarPresionado(mouseClickX, mouseClickY)) {
                iniciarJuego("carlos");
            } else if (gameOverScreen.isMenuPresionado(mouseClickX, mouseClickY)) {
                estadoActual = Estado.MENU;
            }
        }
    }

    //  xd
    private void iniciarJuego(String personaje) {

        int colsSala  = (int)(ancho / Room.TILE_SIZE);
        int filasSala = (int)(alto  / Room.TILE_SIZE);

        Room.inicializarOffset(ancho, alto, colsSala, filasSala);

        dungeon     = new Dungeon(colsSala, filasSala);
        proyectiles = new ArrayList<>();

        Room salaInicial = dungeon.getSalaActual();
        double spawnX = salaInicial.getCentroX() - 48;
        double spawnY = salaInicial.getCentroY() - 48;

        jugador    = new Jugador(spawnX, spawnY, 96, 96, 3.0, 5, personaje);
        shopScreen = new ShopScreen(jugador);

        estadoActual = Estado.JUGANDO;
        System.out.println("¡Juego iniciado con " + personaje + "!");
    }

    private void procesarInputJugador() {
        double dx = 0, dy = 0;

        // moverse con wasd
        if (inputHandler.isKeyPressed(KeyCode.W)) dy = -1;
        if (inputHandler.isKeyPressed(KeyCode.S)) dy =  1;
        if (inputHandler.isKeyPressed(KeyCode.A)) dx = -1;
        if (inputHandler.isKeyPressed(KeyCode.D)) dx =  1;

        jugador.setDx(dx);
        jugador.setDy(dy);

        // disparar con las flechitas
        long ahora = System.currentTimeMillis();

        // velocidad de disparo ms
        if (ahora - ultimoDisparo > 300) {
            double dirDisparoX = 0;
            double dirDisparoY = 0;
            boolean intentarDisparo = false;

            if (inputHandler.isKeyPressed(KeyCode.UP)) {
                dirDisparoY = -1; intentarDisparo = true;
            } else if (inputHandler.isKeyPressed(KeyCode.DOWN)) {
                dirDisparoY = 1; intentarDisparo = true;
            } else if (inputHandler.isKeyPressed(KeyCode.LEFT)) {
                dirDisparoX = -1; intentarDisparo = true;
            } else if (inputHandler.isKeyPressed(KeyCode.RIGHT)) {
                dirDisparoX = 1; intentarDisparo = true;
            }

            // generar disparos con las flechas
            if (intentarDisparo) {
                proyectiles.add(jugador.disparar(dirDisparoX, dirDisparoY));
                ultimoDisparo = ahora;
            }
        }
    }
}