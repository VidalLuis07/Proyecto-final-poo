package edu.trespor2.videojuego.main;

import edu.trespor2.videojuego.controller.CollisionManager;
import edu.trespor2.videojuego.controller.InputHandler;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
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

    // ── Estados posibles del juego ─────────────────────────────────────────
    public enum Estado { MENU, JUGANDO, TIENDA, GAME_OVER }
    private Estado estadoActual = Estado.MENU;

    // ── JavaFX ────────────────────────────────────────────────────────────
    private final GraphicsContext gc;
    private final double ancho;
    private final double alto;

    // ── Controladores ─────────────────────────────────────────────────────
    private final InputHandler inputHandler;
    private final CollisionManager collisionManager;

    // ── Vista ─────────────────────────────────────────────────────────────
    private final GameRenderer renderer;
    private final HUD hud;
    private final MenuScreen menuScreen;
    private final GameOverScreen gameOverScreen;
    private ShopScreen shopScreen;

    // ── Modelo ────────────────────────────────────────────────────────────
    private Jugador jugador;
    private Dungeon dungeon;
    private List<Proyectiles> proyectiles;

    // ── Control de tiempo ─────────────────────────────────────────────────
    private long tiempoAnterior = -1;

    // ── Mouse ─────────────────────────────────────────────────────────────
    private double mouseX      = 0;   // posición actual (para hover)
    private double mouseY      = 0;   // posición actual (para hover)
    private double mouseClickX = -1;  // último clic
    private double mouseClickY = -1;  // último clic

    // ══════════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════════
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

        // Clic del mouse → guarda dónde fue el clic
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            mouseClickX = e.getX();
            mouseClickY = e.getY();
        });

        // Movimiento del mouse → guarda posición actual (para efecto hover)
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HANDLE — Se llama ~60 veces por segundo
    // ══════════════════════════════════════════════════════════════════════
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

        // Consumir clic para que no se procese dos veces
        mouseClickX = -1;
        mouseClickY = -1;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ESTADOS
    // ══════════════════════════════════════════════════════════════════════
    private void manejarMenu() {
        // Le pasamos la posición del mouse para el efecto hover en los botones
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
        jugador.update(delta);
        dungeon.getSalaActual().getEnemigos().forEach(e -> e.update(delta));

        proyectiles.forEach(p -> p.update(delta));
        proyectiles.removeIf(p -> p.getX() < 0 || p.getX() > ancho
                || p.getY() < 0 || p.getY() > alto);

        collisionManager.checkCollisions(jugador, dungeon.getSalaActual().getEnemigos(), proyectiles);
        dungeon.getSalaActual().actualizarEstadoSala();

        if (dungeon.getSalaActual().getTipo() == edu.trespor2.videojuego.model.environment.Room.TipoSala.TIENDA) {
            estadoActual = Estado.TIENDA;
        }

        if (jugador.estaMuerto()) {
            estadoActual = Estado.GAME_OVER;
        }

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

    // ══════════════════════════════════════════════════════════════════════
    //  UTILIDADES
    // ══════════════════════════════════════════════════════════════════════
    // DESPUÉS
    private void iniciarJuego(String personaje) {

        // 1) Calcular offset ANTES de crear el Dungeon
        Room.inicializarOffset(ancho, alto, 15, 10);

        dungeon     = new Dungeon();
        proyectiles = new ArrayList<>();

        // 2) Spawnear jugador en el centro de la sala, no de la pantalla
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

        if (inputHandler.isKeyPressed(KeyCode.W) || inputHandler.isKeyPressed(KeyCode.UP))    dy = -1;
        if (inputHandler.isKeyPressed(KeyCode.S) || inputHandler.isKeyPressed(KeyCode.DOWN))  dy =  1;
        if (inputHandler.isKeyPressed(KeyCode.A) || inputHandler.isKeyPressed(KeyCode.LEFT))  dx = -1;
        if (inputHandler.isKeyPressed(KeyCode.D) || inputHandler.isKeyPressed(KeyCode.RIGHT)) dx =  1;

        jugador.setDx(dx);
        jugador.setDy(dy);

        if (inputHandler.isKeyPressed(KeyCode.SPACE)) {
            double dirX = jugador.getDx();
            double dirY = jugador.getDy();
            if (dirX != 0 || dirY != 0) {
                proyectiles.add(jugador.disparar(dirX, dirY));
            }
        }
    }
}