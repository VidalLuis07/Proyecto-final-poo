package edu.trespor2.videojuego.main;

import edu.trespor2.videojuego.controller.CollisionManager;
import edu.trespor2.videojuego.controller.InputHandler;
import edu.trespor2.videojuego.model.IdiomaManager;
import edu.trespor2.videojuego.model.MusicManager;
import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jefe;
import edu.trespor2.videojuego.model.environment.Dungeon;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Door;
import edu.trespor2.videojuego.view.GameRenderer;
import edu.trespor2.videojuego.view.HUD;
import edu.trespor2.videojuego.view.SpriteManager;
import edu.trespor2.videojuego.view.screens.CreditsScreen;
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

    public enum Estado { MENU, JUGANDO, TIENDA, GAME_OVER, CREDITOS }
    private Estado estadoActual = Estado.MENU;

    private final GraphicsContext gc;
    private final double ancho;
    private final double alto;

    private final InputHandler inputHandler;
    private final CollisionManager collisionManager;

    private final GameRenderer renderer;
    private final HUD hud;
    private final MenuScreen menuScreen;
    private final GameOverScreen gameOverScreen;
    private final CreditsScreen creditsScreen;
    private ShopScreen shopScreen;

    private Jugador jugador;
    private Dungeon dungeon;
    private List<Proyectiles> proyectiles;

    private long tiempoAnterior = -1;

    private double mouseX      = 0;
    private double mouseY      = 0;
    private double mouseClickX = -1;
    private double mouseClickY = -1;

    public GameLoop(Canvas canvas, Scene scene) {
        this.gc    = canvas.getGraphicsContext2D();
        this.ancho = canvas.getWidth();
        this.alto  = canvas.getHeight();

        this.inputHandler     = new InputHandler(scene);
        this.collisionManager = new CollisionManager();
        this.renderer         = new GameRenderer();
        this.hud              = new HUD();
        this.menuScreen       = new MenuScreen();
        this.gameOverScreen   = new GameOverScreen();
        this.creditsScreen    = new CreditsScreen();

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
            case CREDITOS  -> manejarCreditos();
        }

        mouseClickX = -1;
        mouseClickY = -1;
    }


    private void manejarCreditos() {
        MusicManager.getInstance().reproducir(MusicManager.Pista.CREDITOS);
        creditsScreen.actualizarMouse(mouseX, mouseY);
        creditsScreen.render(gc, ancho, alto);

        if (mouseClickX >= 0) {
            if (creditsScreen.isReiniciarPresionado(mouseClickX, mouseClickY, alto)) {
                iniciarJuego("carlos"); // Reinicia la partida
            } else if (creditsScreen.isSalirPresionado(mouseClickX, mouseClickY, ancho, alto)) {
                System.exit(0); // Cierra el juego
            }
        }
    }

    private void manejarMenu() {
        MusicManager.getInstance().reproducir(MusicManager.Pista.MENU);
        menuScreen.actualizarMouse(mouseX, mouseY);
        menuScreen.render(gc, ancho, alto);

        if (mouseClickX >= 0) {
            if (menuScreen.isIdiomaPresionado(mouseClickX, mouseClickY)) {
                IdiomaManager.getInstance().toggleIdioma();             // ← TUYO
            } else if (menuScreen.isJugarPresionado(mouseClickX, mouseClickY)) {
                iniciarJuego("carlos");
            } else if (menuScreen.isSalirPresionado(mouseClickX, mouseClickY)) {
                System.exit(0);
            }
        }
    }

    private void manejarJuego(double delta) {
        procesarInputJugador();

        dungeon.getSalaActual().getEnemigos().removeIf(e -> e.estaMuerto());

        double[] dirAjustada = collisionManager.resolverColisionParedes(jugador, dungeon.getSalaActual());
        jugador.setDx(dirAjustada[0]);
        jugador.setDy(dirAjustada[1]);
        jugador.update(delta);

        List<Enemigo> nuevosEnemigos = new ArrayList<>();
        dungeon.getSalaActual().getEnemigos().forEach(e -> {
            Enemigo enemigoActual = (Enemigo) e;
            enemigoActual.perseguir(jugador);
            enemigoActual.update(delta);

            if (enemigoActual instanceof Jefe jefe) {
                // Solo invoca zombies si el jefe sigue vivo
                if (!jefe.estaMuerto() && !jefe.getZombiesInvocados().isEmpty()) {
                    nuevosEnemigos.addAll(jefe.getZombiesInvocados());
                    jefe.limpiarZombiesInvocados();
                } else if (jefe.estaMuerto()) {
                    jefe.limpiarZombiesInvocados();
                }
            }
        });

        if (!nuevosEnemigos.isEmpty()) {
            dungeon.getSalaActual().getEnemigos().addAll(nuevosEnemigos);
        }

        proyectiles.forEach(p -> p.update(delta));
        proyectiles.removeIf(p -> p.getX() < 0 || p.getX() > ancho || p.getY() < 0 || p.getY() > alto);

        collisionManager.checkCollisions(jugador, dungeon.getSalaActual().getEnemigos(), proyectiles);
        collisionManager.checkCofres(jugador, dungeon.getSalaActual().getCofres());

        dungeon.getSalaActual().actualizarEstadoSala();

        Door puertaCruzada = collisionManager.verificarColisionPuerta(jugador, dungeon.getSalaActual().getPuertas());
        if (puertaCruzada != null) {
            dungeon.cambiarSala(puertaCruzada, jugador);
            proyectiles.clear();

            if (dungeon.getSalaActual().getTipo() == Room.TipoSala.TIENDA) {
                dungeon.getSalaActual().setTiendaVisitada(false);
            }

            // Cambiar música según el tipo de sala al entrar
            if (dungeon.getSalaActual().getTipo() == Room.TipoSala.JEFE) {
                MusicManager.getInstance().reproducir(MusicManager.Pista.JEFE);
            } else {
                MusicManager.getInstance().reproducir(MusicManager.Pista.NIVEL);
            }
        }

        if (dungeon.getSalaActual().getTipo() == Room.TipoSala.TIENDA && !dungeon.getSalaActual().isTiendaVisitada()) {
            estadoActual = Estado.TIENDA;
            // si ya se visito la tienda,no se vuelva a abrir al cerrar el menú
            dungeon.getSalaActual().setTiendaVisitada(true);
        }

        if (dungeon.getSalaActual().getTipo() == Room.TipoSala.JEFE && dungeon.getSalaActual().isEstaLimpia()) {
            estadoActual = Estado.CREDITOS;
            MusicManager.getInstance().detener();
        }

        if (jugador.estaMuerto()) {
            estadoActual = Estado.GAME_OVER;
            MusicManager.getInstance().detener();
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
        gameOverScreen.actualizarMouse(mouseX, mouseY);                 // ← TUYO
        gameOverScreen.render(gc, ancho, alto);

        if (mouseClickX >= 0) {
            if (gameOverScreen.isReiniciarPresionado(mouseClickX, mouseClickY)) {
                iniciarJuego("carlos");
            } else if (gameOverScreen.isMenuPresionado(mouseClickX, mouseClickY)) {
                estadoActual = Estado.MENU;
            }
        }
    }


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
        MusicManager.getInstance().reproducir(MusicManager.Pista.NIVEL);
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