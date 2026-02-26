package edu.trespor2.videojuego.view;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Dungeon;
import edu.trespor2.videojuego.model.environment.Room;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;          // ← este
import javafx.scene.paint.Color;          // ← este
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HUD {

    private static final int MARGEN = 12;
    private static final int TAMANO_CELDA_MAPA = 8; // px por sala en el minimapa
    private final SpriteManager sprites;
    public HUD() {
        this.sprites = SpriteManager.getInstance(); // ← agregar esto
    }

    /**
     * Dibuja todo el HUD.
     * Llamar después de GameRenderer.render() para que aparezca encima.
     */
    public void render(GraphicsContext gc, Jugador jugador, Dungeon dungeon,
                       double anchoCanvas, double altoCanvas) {
        if (jugador == null) return;

        dibujarVida(gc, jugador);
        dibujarMonedas(gc, jugador);
        dibujarMiniMapa(gc, dungeon, anchoCanvas, altoCanvas);
    }

    //  VIDA — Corazones en la esquina superior izquierda
    private void dibujarVida(GraphicsContext gc, Jugador jugador) {
        int vidaActual = jugador.getVidaActual();
        int vidaMaxima = jugador.getVidaMaxima();

        Image corazonLleno = sprites.getImagen("corazon_lleno");
        Image corazonVacio = sprites.getImagen("corazon_vacio");

        int CORAZON_SIZE = 55; // tamaño en pantalla, ajusta al gusto
        int SEPARACION   =-5;  // espacio entre corazones

        for (int i = 0; i < vidaMaxima; i++) {
            double x = MARGEN + i * (CORAZON_SIZE + SEPARACION);
            double y = MARGEN;

            Image imgAUsar = (i < vidaActual) ? corazonLleno : corazonVacio;

            if (imgAUsar != null) {
                gc.setImageSmoothing(false);
                gc.drawImage(imgAUsar, x, y, CORAZON_SIZE, CORAZON_SIZE);
            } else {
                // Fallback por si no carga la imagen
                gc.setFill(i < vidaActual ? Color.CRIMSON : Color.DARKGRAY);
                gc.fillOval(x, y, CORAZON_SIZE, CORAZON_SIZE);
            }
        }
    }

    //  MONEDAS — Esquina superior derecha (cuando Jugador tenga getDinero())

    private void dibujarMonedas(GraphicsContext gc, Jugador jugador) {
        int monedas = jugador.getDinero();

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.GOLD);
        gc.fillText("💰 " + monedas, 20, 80);
    }


    //  MINIMAPA — Esquina superior derecha

    private void dibujarMiniMapa(GraphicsContext gc, Dungeon dungeon,
                                 double anchoCanvas, double altoCanvas) {
        if (dungeon == null) return;

        Room[][] grid = dungeon.getGridSalas();
        Room salaActual = dungeon.getSalaActual();

        int cols = grid.length;
        int filas = grid[0].length;

        // Posición del minimapa: esquina superior derecha
        double mapaAncho = cols * TAMANO_CELDA_MAPA + (cols - 1); // incluye gaps
        double mapaAlto  = filas * TAMANO_CELDA_MAPA + (filas - 1);
        double mapaX = anchoCanvas - mapaAncho - MARGEN;
        double mapaY = MARGEN;

        // Fondo semitransparente
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillRect(mapaX - 4, mapaY - 4, mapaAncho + 8, mapaAlto + 8);

        for (int col = 0; col < cols; col++) {
            for (int fila = 0; fila < filas; fila++) {
                Room sala = grid[col][fila];
                if (sala == null) continue;

                double celdaX = mapaX + col * (TAMANO_CELDA_MAPA + 1);
                double celdaY = mapaY + fila * (TAMANO_CELDA_MAPA + 1);

                // Color según tipo de sala
                if (sala == salaActual) {
                    gc.setFill(Color.WHITE);        // Sala donde está el jugador
                } else {
                    switch (sala.getTipo()) {
                        case INICIO   -> gc.setFill(Color.LIMEGREEN);
                        case JEFE     -> gc.setFill(Color.CRIMSON);
                        case TIENDA   -> gc.setFill(Color.GOLD);
                        default       -> gc.setFill(Color.SLATEGRAY);
                    }
                }
                gc.fillRect(celdaX, celdaY, TAMANO_CELDA_MAPA, TAMANO_CELDA_MAPA);
            }
        }

        // Leyenda
        gc.setFont(Font.font("Arial", 9));
        gc.setFill(Color.WHITE);
        gc.fillText("TÚ", mapaX - 4, mapaY + mapaAlto + 14);
    }
}