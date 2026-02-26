package edu.trespor2.videojuego.view;

import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Tile;
import edu.trespor2.videojuego.model.items.Chest;
import edu.trespor2.videojuego.model.items.Coins;
import edu.trespor2.videojuego.view.SpriteManager.Direccion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class GameRenderer {

    // ── Atributos ──────────────────────────────────────────────────────────
    private final SpriteManager sprites;

    private static final int VELOCIDAD_ANIMACION = 8;
    private static final int FRAMES_JUGADOR = 4;
    private static final int FRAMES_ENEMIGO = 4;
    private static final int TILE_SIZE = 32;

    private int contadorFrames = 0;
    private int frameActualJugador = 0;
    private int frameActualEnemigo = 0;

    // ── Constructor ────────────────────────────────────────────────────────
    public GameRenderer() {
        this.sprites = SpriteManager.getInstance();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MÉTODO PRINCIPAL
    // ══════════════════════════════════════════════════════════════════════
    public void render(GraphicsContext gc,
                       Jugador jugador,
                       Room salaActual,
                       List<Proyectiles> proyectiles,
                       double anchoCanvas,
                       double altoCanvas) {

        gc.setImageSmoothing(false);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, anchoCanvas, altoCanvas);

        if (salaActual == null || jugador == null) return;

        // Avanzar animación
        contadorFrames++;
        if (contadorFrames >= VELOCIDAD_ANIMACION) {
            contadorFrames = 0;
            frameActualJugador = (frameActualJugador + 1) % FRAMES_JUGADOR;
            frameActualEnemigo = (frameActualEnemigo + 1) % FRAMES_ENEMIGO;
        }

        // ── Capas en orden ──────────────────────────────────────────────
        dibujarTiles(gc, salaActual.getMapaTiles(), anchoCanvas, altoCanvas); // ← primero
        dibujarMonedas(gc, salaActual.getMonedasEnSuelo());
        dibujarCofres(gc, salaActual.getCofres());
        dibujarProyectiles(gc, proyectiles);
        dibujarEnemigos(gc, salaActual.getEnemigos());
        dibujarJugador(gc, jugador);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TILES
    //  CAPA 1 → tile_vacio cubre TODA la pantalla (el "espacio" exterior)
    //  CAPA 2 → encima se dibuja la sala: pared en bordes, piso en interior
    // ══════════════════════════════════════════════════════════════════════
    private void dibujarTiles(GraphicsContext gc, Tile[][] tiles,
                              double anchoCanvas, double altoCanvas) {

        Image imgVacio = sprites.getImagen("tile_vacio");
        Image imgPiso  = sprites.getImagen("tile_piso");
        Image imgPared = sprites.getImagen("tile_pared");

        // ── CAPA 1: tile_vacio cubre toda la pantalla ──────────────────
        int colsCanvas  = (int) Math.ceil(anchoCanvas / TILE_SIZE);
        int filasCanvas = (int) Math.ceil(altoCanvas  / TILE_SIZE);

        for (int col = 0; col < colsCanvas; col++) {
            for (int fila = 0; fila < filasCanvas; fila++) {
                double px = col * TILE_SIZE;
                double py = fila * TILE_SIZE;

                if (imgVacio != null) {
                    gc.drawImage(imgVacio, px, py, TILE_SIZE, TILE_SIZE);
                } else {
                    // Fallback: negro si no cargó la imagen
                    gc.setFill(Color.BLACK);
                    gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // ── CAPA 2: sala (pared + piso) encima del vacío ───────────────
        if (tiles == null) return;

        for (int col = 0; col < tiles.length; col++) {
            for (int fila = 0; fila < tiles[col].length; fila++) {
                Tile tile = tiles[col][fila];
                if (tile == null) continue;

                Image img = tile.isTransitable() ? imgPiso : imgPared;

                if (img != null) {
                    gc.drawImage(img, tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);
                } else {
                    // Fallback con colores si no cargaron las imágenes
                    gc.setFill(tile.isTransitable()
                            ? Color.web("#4a3728")   // marrón oscuro = piso
                            : Color.web("#1a1a2e")); // azul muy oscuro = pared
                    gc.fillRect(tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);

                    gc.setStroke(Color.web("#000000", 0.3));
                    gc.strokeRect(tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  CAPAS EXISTENTES (sin cambios)
    // ══════════════════════════════════════════════════════════════════════

    private void dibujarJugador(GraphicsContext gc, Jugador jugador) {
        // Calcular dirección — si está quieto usar la última conocida
        Direccion dir;
        if (jugador.getDx() != 0 || jugador.getDy() != 0) {
            dir = SpriteManager.calcularDireccion(jugador.getDx(), jugador.getDy());
        } else {
            dir = SpriteManager.calcularDireccion(jugador.getUltimaDirX(), jugador.getUltimaDirY());
            if (!jugador.isAtacando()) frameActualJugador = 0;
        }

        Image frameToDraw;

        if (jugador.isAtacando()) {
            // Cuando ataca: usar SOLO el sprite de ataque, nada del sprite normal
            String spriteAtaque = jugador.getNombreSprite() + "_ataque";
            frameToDraw = sprites.getFrame(spriteAtaque, dir, jugador.getFrameAtaque());
            // Si por alguna razón no cargó, fallback al normal
            if (frameToDraw == null) {
                frameToDraw = sprites.getFrame(jugador.getNombreSprite(), dir, 0);
            }
        } else {
            // Cuando camina/está quieto: sprite normal
            frameToDraw = sprites.getFrame(jugador.getNombreSprite(), dir, frameActualJugador);
        }

        if (frameToDraw != null) {
            gc.drawImage(frameToDraw, jugador.getX(), jugador.getY(),
                    jugador.getWidth(), jugador.getHeight());
        } else {
            gc.setFill(Color.CORNFLOWERBLUE);
            gc.fillRect(jugador.getX(), jugador.getY(),
                    jugador.getWidth(), jugador.getHeight());
        }
    }

    private void dibujarEnemigos(GraphicsContext gc, List<Enemigo> enemigos) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaMuerto()) continue;

            Direccion dir = SpriteManager.calcularDireccion(enemigo.getDx(), enemigo.getDy());
            String tipoSprite = enemigo.getClass().getSimpleName().toLowerCase();

            Image frame = sprites.getFrame(tipoSprite, dir, frameActualEnemigo);

            if (frame != null) {
                gc.drawImage(frame, enemigo.getX(), enemigo.getY(),
                        enemigo.getWidth(), enemigo.getHeight());
            } else {
                gc.setFill(Color.CRIMSON);
                gc.fillRect(enemigo.getX(), enemigo.getY(),
                        enemigo.getWidth(), enemigo.getHeight());
            }

            dibujarBarraVida(gc, enemigo.getX(), enemigo.getY(),
                    enemigo.getWidth(), enemigo.getVidaActual(), enemigo.getVidaMaxima());
        }
    }

    private void dibujarProyectiles(GraphicsContext gc, List<Proyectiles> proyectiles) {
        Image imgDaga = sprites.getImagen("daga");

        for (Proyectiles p : proyectiles) {
            if (p.isDaga() && imgDaga != null) {
                // Rotar la daga según su dirección de vuelo
                double angulo = Math.toDegrees(Math.atan2(p.getDy(), p.getDx()));
                double cx = p.getX() + p.getWidth()  / 2;
                double cy = p.getY() + p.getHeight() / 2;

                gc.save();
                gc.translate(cx, cy);
                // La daga apunta hacia arriba en el sprite → restar 90° para alinear con dirección
                gc.rotate(angulo - 90);
                gc.drawImage(imgDaga,
                        -p.getWidth() / 2, -p.getHeight() / 2,
                        p.getWidth(), p.getHeight());
                gc.restore();
            } else {
                // Proyectil normal (bala del boss, etc.)
                gc.setFill(Color.YELLOW);
                gc.fillOval(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }

    private void dibujarCofres(GraphicsContext gc, List<Chest> cofres) {
        for (Chest cofre : cofres) {
            gc.setFill(cofre.isAbierto() ? Color.SADDLEBROWN : Color.GOLDENROD);
            gc.fillRect(cofre.getX(), cofre.getY(), cofre.getWidth(), cofre.getHeight());
        }
    }

    private void dibujarMonedas(GraphicsContext gc, List<Coins> monedas) {
        gc.setFill(Color.GOLD);
        for (Coins moneda : monedas) {
            gc.fillOval(moneda.getX(), moneda.getY(), moneda.getWidth(), moneda.getHeight());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UTILIDADES
    // ══════════════════════════════════════════════════════════════════════
    private void dibujarBarraVida(GraphicsContext gc, double x, double y,
                                  double ancho, int vidaActual, int vidaMaxima) {
        double proporcion = (double) vidaActual / vidaMaxima;
        double yBarra = y - 8;

        gc.setFill(Color.DARKRED);
        gc.fillRect(x, yBarra, ancho, 4);

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x, yBarra, ancho * proporcion, 4);
    }
}