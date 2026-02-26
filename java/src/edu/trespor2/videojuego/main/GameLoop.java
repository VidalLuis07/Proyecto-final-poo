package edu.trespor2.videojuego.main;

import edu.trespor2.videojuego.controller.CollisionManager;
import edu.trespor2.videojuego.controller.InputHandler;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Dungeon;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Door; // Importación necesaria para el cambio de sala
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

    // Estados posibles del juego
    public enum Estado { MENU, JUGANDO, TIENDA, GAME_OVER }
    private Estado estadoActual = Estado.MENU;

    // Librería de JavaFX
    private final GraphicsContext gc;
    private final double ancho;
    private final double alto;

    // Controladores
    private final InputHandler inputHandler;
    private final CollisionManager collisionManager;

    // Vistas
    private final GameRenderer renderer;
    private final HUD hud;
    private final MenuScreen menuScreen;
    private final GameOverScreen gameOverScreen;
    private ShopScreen shopScreen;

    // Modelos
    private Jugador jugador;
    private Dungeon dungeon;
    private List<Proyectiles> proyectiles;

    // Control de tiempo
    private long tiempoAnterior = -1;

    // Mouse
    private double mouseX      = 0;
    private double mouseY      = 0;
    private double mouseClickX = -1;
    private double mouseClickY = -1;

    // --- CONSTRUCTOR ---
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

    // --- HANDLE ---
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

    // --- MANEJO DE ESTADOS ---

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

        // Limpiar enemigos muertos
        dungeon.getSalaActual().getEnemigos().removeIf(e -> e.estaMuerto());

        // Colisión con paredes
        double[] dirAjustada = collisionManager.resolverColisionParedes(jugador, dungeon.getSalaActual());
        jugador.setDx(dirAjustada[0]);
        jugador.setDy(dirAjustada[1]);

        jugador.update(delta);

        // Actualizar enemigos
        dungeon.getSalaActual().getEnemigos().forEach(e -> {
            ((edu.trespor2.videojuego.model.entidades.personajes.Enemigo) e).perseguir(jugador);
            e.update(delta);
        });

        // Actualizar proyectiles
        proyectiles.forEach(p -> p.update(delta));
        proyectiles.removeIf(p -> p.getX() < 0 || p.getX() > ancho || p.getY() < 0 || p.getY() > alto);

        // Colisiones generales (Personajes y Proyectiles)
        collisionManager.checkCollisions(jugador, dungeon.getSalaActual().getEnemigos(), proyectiles);

        // --- LÓGICA COMBINADA DE OBJETOS Y SALAS ---

        // 1. Actualizar estado de la sala (abrir puertas si no hay enemigos)
        dungeon.getSalaActual().actualizarEstadoSala();

        // 2. Revisar colisión con COFRES (del primer código)
        collisionManager.checkCofres(jugador, dungeon.getSalaActual().getCofres());

        // 3. Revisar colisión con PUERTAS para cambio de sala (del segundo código)
        Door puertaCruzada = collisionManager.verificarColisionPuerta(jugador, dungeon.getSalaActual().getPuertas());
        if (puertaCruzada != null) {
            dungeon.cambiarSala(puertaCruzada, jugador);
            proyectiles.clear(); // Limpiar proyectiles al cambiar de sala
        }

        // -------------------------------------------

        if (dungeon.getSalaActual().getTipo() == Room.TipoSala.TIENDA) {
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

    // --- UTILIDADES ---

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
        if (inputHandler.isKeyPressed(KeyCode.W)) dy = -1;
        if (inputHandler.isKeyPressed(KeyCode.S)) dy =  1;
        if (inputHandler.isKeyPressed(KeyCode.A)) dx = -1;
        if (inputHandler.isKeyPressed(KeyCode.D)) dx =  1;

        jugador.setDx(dx);

        jugador.setDy(dy);

        double dirDisparoX = 0, dirDisparoY = 0;
        boolean quiereDisparar = false;

        if (inputHandler.isKeyPressed(KeyCode.UP))    { dirDisparoY = -1; quiereDisparar = true; }
        if (inputHandler.isKeyPressed(KeyCode.DOWN))  { dirDisparoY =  1; quiereDisparar = true; }
        if (inputHandler.isKeyPressed(KeyCode.LEFT))  { dirDisparoX = -1; quiereDisparar = true; }
        if (inputHandler.isKeyPressed(KeyCode.RIGHT)) { dirDisparoX =  1; quiereDisparar = true; }

        if (quiereDisparar && !jugador.isAtacando() && jugador.puedeDisparar()) {
            proyectiles.add(jugador.disparar(dirDisparoX, dirDisparoY));
        }
    }
}