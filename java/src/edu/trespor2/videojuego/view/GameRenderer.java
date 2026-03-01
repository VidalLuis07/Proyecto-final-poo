package edu.trespor2.videojuego.view;

import edu.trespor2.videojuego.model.entidades.Proyectiles;
import edu.trespor2.videojuego.model.entidades.personajes.Enemigo;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Room;
import edu.trespor2.videojuego.model.environment.Tile;
import edu.trespor2.videojuego.model.environment.Door;
import edu.trespor2.videojuego.model.items.Chest;
import edu.trespor2.videojuego.model.items.Coins;
import edu.trespor2.videojuego.view.SpriteManager.Direccion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class GameRenderer {

    // ── Atributos
    private final SpriteManager sprites;

    private static final int VELOCIDAD_ANIMACION    = 8;
    private static final int FRAMES_JUGADOR         = 4; // caminar
    private static final int FRAMES_JUGADOR_ATAQUE  = 3; // ataque (spritesheet tiene 3 frames)
    private static final int FRAMES_ENEMIGO         = 4;
    private static final int TILE_SIZE = 32;

    private int contadorFrames = 0;
    private int frameActualJugador = 0;
    private int frameActualEnemigo = 0;

    // ── Constructor
    public GameRenderer() {
        this.sprites = SpriteManager.getInstance();
    }

    //  METODO PRINCIPAL
    public void render(GraphicsContext gc,
                       Jugador jugador,
                       Room salaActual,
                       List<Proyectiles> proyectiles,
                       double anchoCanvas,
                       double altoCanvas) {

        gc.setImageSmoothing(false); // Estética Pixel Art

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, anchoCanvas, altoCanvas);

        if (salaActual == null || jugador == null) return;

        // Avanzar animación global
        contadorFrames++;
        if (contadorFrames >= VELOCIDAD_ANIMACION) {
            contadorFrames = 0;
            int maxFramesJugador = jugador.isAtacando() ? FRAMES_JUGADOR_ATAQUE : FRAMES_JUGADOR;
            frameActualJugador = (frameActualJugador + 1) % maxFramesJugador;
            frameActualEnemigo = (frameActualEnemigo + 1) % FRAMES_ENEMIGO;
        }

        // ── Capas en orden de renderizado (Z-Index) ────────────────────────
        dibujarTiles(gc, salaActual.getMapaTiles(), anchoCanvas, altoCanvas);
        dibujarPuertas(gc, salaActual.getPuertas());
        dibujarMonedas(gc, salaActual.getMonedasEnSuelo());
        dibujarCofres(gc, salaActual.getCofres());
        dibujarProyectiles(gc, proyectiles);
        dibujarEnemigos(gc, salaActual.getEnemigos());
        dibujarJugador(gc, jugador);
    }


    //  ESCENARIO (Tiles y Fondo)

    private void dibujarTiles(GraphicsContext gc, Tile[][] tiles,
                              double anchoCanvas, double altoCanvas) {

        Image imgVacio = sprites.getImagen("tile_vacio");
        Image imgPiso  = sprites.getImagen("tile_piso");
        Image imgPared = sprites.getImagen("tile_pared");

        // Capa de fondo infinito (Vacío)
        int colsCanvas  = (int) Math.ceil(anchoCanvas / TILE_SIZE);
        int filasCanvas = (int) Math.ceil(altoCanvas  / TILE_SIZE);

        for (int col = 0; col < colsCanvas; col++) {
            for (int fila = 0; fila < filasCanvas; fila++) {
                double px = col * TILE_SIZE;
                double py = fila * TILE_SIZE;
                if (imgVacio != null) {
                    gc.drawImage(imgVacio, px, py, TILE_SIZE, TILE_SIZE);
                } else {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        if (tiles == null) return;

        // Capa de la Sala (Paredes y Pisos)
        for (int col = 0; col < tiles.length; col++) {
            for (int fila = 0; fila < tiles[col].length; fila++) {
                Tile tile = tiles[col][fila];
                if (tile == null) continue;

                Image img = tile.isTransitable() ? imgPiso : imgPared;
                if (img != null) {
                    gc.drawImage(img, tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);
                } else {
                    gc.setFill(tile.isTransitable() ? Color.web("#4a3728") : Color.web("#1a1a2e"));
                    gc.fillRect(tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }


    //  PERSONAJES Y ENTIDADES

    private void dibujarJugador(GraphicsContext gc, Jugador jugador) {
        // Actualizar última dirección si el jugador se está moviendo
        if (jugador.getDx() != 0 || jugador.getDy() != 0) {
            // La dirección se actualiza en Jugador.update(), aquí solo la leemos
        }

        // Siempre usar ultimaDirX/Y — conserva la dirección al atacar o estar quieto
        Direccion dir = SpriteManager.calcularDireccion(
                jugador.getUltimaDirX(), jugador.getUltimaDirY());

        Image frameToDraw;
        if (jugador.isAtacando()) {
            // Usar sprite de ataque con el frame que maneja el propio Jugador
            String spriteAtaque = jugador.getNombreSprite() + "_ataque";
            frameToDraw = sprites.getFrame(spriteAtaque, dir, jugador.getFrameAtaque());
            if (frameToDraw == null) {
                // Fallback al sprite normal si no existe el de ataque
                frameToDraw = sprites.getFrame(jugador.getNombreSprite(), dir, 0);
            }
        } else {
            // Si está quieto, frame 0; si camina, animar normalmente
            if (jugador.getDx() == 0 && jugador.getDy() == 0) {
                frameActualJugador = 0;
            }
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
                gc.drawImage(frame, enemigo.getX(), enemigo.getY(), enemigo.getWidth(), enemigo.getHeight());
            } else {
                gc.setFill(Color.CRIMSON);
                gc.fillRect(enemigo.getX(), enemigo.getY(), enemigo.getWidth(), enemigo.getHeight());
            }
            dibujarBarraVida(gc, enemigo.getX(), enemigo.getY(), enemigo.getWidth(), enemigo.getVidaActual(), enemigo.getVidaMaxima());
        }
    }

    private void dibujarProyectiles(GraphicsContext gc, List<Proyectiles> proyectiles) {
        Image imgDaga = sprites.getImagen("daga");
        for (Proyectiles p : proyectiles) {
            if (p.isDaga() && imgDaga != null) {
                double angulo = Math.toDegrees(Math.atan2(p.getDy(), p.getDx()));
                gc.save();
                gc.translate(p.getX() + p.getWidth()/2, p.getY() + p.getHeight()/2);
                gc.rotate(angulo - 90);
                gc.drawImage(imgDaga, -p.getWidth()/2, -p.getHeight()/2, p.getWidth(), p.getHeight());
                gc.restore();
            } else {
                gc.setFill(Color.YELLOW);
                gc.fillOval(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }

    private void dibujarCofres(GraphicsContext gc, List<Chest> cofres) {
        Image imgCerrado = sprites.getImagen("cofre_cerrado");
        Image imgAbierto = sprites.getImagen("cofre_abierto");

        for (Chest cofre : cofres) {
            Image imgAUsar = cofre.isAbierto() ? imgAbierto : imgCerrado;
            if (imgAUsar != null) {
                gc.drawImage(imgAUsar, cofre.getX(), cofre.getY(), cofre.getWidth(), cofre.getHeight());
            } else {
                gc.setFill(cofre.isAbierto() ? Color.SADDLEBROWN : Color.GOLDENROD);
                gc.fillRect(cofre.getX(), cofre.getY(), cofre.getWidth(), cofre.getHeight());
            }
        }
    }

    private void dibujarMonedas(GraphicsContext gc, List<Coins> monedas) {
        for (Coins moneda : monedas) {
            moneda.update(); // avanza la animación
            Image frame = sprites.getFrameMoneda("moneda", moneda.getFrameActual());
            if (frame != null) {
                gc.drawImage(frame, moneda.getX(), moneda.getY(), moneda.getWidth(), moneda.getHeight());
            } else {
                // fallback si no cargó el sprite
                gc.setFill(Color.GOLD);
                gc.fillOval(moneda.getX(), moneda.getY(), moneda.getWidth(), moneda.getHeight());
            }
        }
    }

    private void dibujarBarraVida(GraphicsContext gc, double x, double y, double ancho, int vidaActual, int vidaMaxima) {
        double proporcion = (double) vidaActual / vidaMaxima;
        gc.setFill(Color.DARKRED);
        gc.fillRect(x, y - 8, ancho, 4);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x, y - 8, ancho * proporcion, 4);
    }


    //  PUERTAS (Lógica especial para tapar huecos de pared)

    private void dibujarPuertas(GraphicsContext gc, List<Door> puertas) {
        if (puertas == null) return;
        for (Door puerta : puertas) {
            gc.setFill(puerta.isAbierta() ? Color.web("#8B4513") : Color.web("#4A4A4A"));

            double xDibujo = puerta.getX();
            double yDibujo = puerta.getY();
            double ancho = puerta.getWidth();
            double alto = puerta.getHeight();

            // Ajuste visual para cubrir el vano de la puerta de 3 tiles (96px)
            if (puerta.getPosicionBorde().equals("NORTE") || puerta.getPosicionBorde().equals("SUR")) {
                xDibujo -= 32;
                ancho = 96;
            } else {
                yDibujo -= 32;
                alto = 96;
            }

            gc.fillRect(xDibujo, yDibujo, ancho, alto);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(xDibujo, yDibujo, ancho, alto);
        }
    }
}