package edu.trespor2.videojuego.view;

import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.model.environment.Dungeon;
import edu.trespor2.videojuego.model.environment.Room;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Se encarga de renderizar la interfaz gráfica superpuesta (HUD).
 *
 * Muestra información del jugador como:
 * - Vida (corazones)
 * - Monedas
 * - Minimapa del dungeon
 *
 * Debe renderizarse después del GameRenderer para que aparezca
 * por encima del escenario y las entidades.
 */
public class HUD {

    /** Margen general desde los bordes del canvas. */
    private static final int MARGEN = 12;

    /** Tamaño en píxeles de cada sala en el minimapa. */
    private static final int TAMANO_CELDA_MAPA = 8;

    /** Gestor centralizado de sprites. */
    private final SpriteManager sprites;

    /**
     * Constructor del HUD.
     * Inicializa el gestor de sprites.
     */
    public HUD() {
        this.sprites = SpriteManager.getInstance();
    }

    /**
     * Método principal de renderizado del HUD.
     *
     * @param gc Contexto gráfico.
     * @param jugador Jugador actual.
     * @param dungeon Dungeon actual.
     * @param anchoCanvas Ancho del canvas.
     * @param altoCanvas Alto del canvas.
     */
    public void render(GraphicsContext gc,
                       Jugador jugador,
                       Dungeon dungeon,
                       double anchoCanvas,
                       double altoCanvas) {

        if (jugador == null) return;

        dibujarVida(gc, jugador);
        dibujarMonedas(gc, jugador);
        dibujarMiniMapa(gc, dungeon, anchoCanvas, altoCanvas);
    }

    /**
     * Dibuja los corazones de vida del jugador
     * en la esquina superior izquierda.
     *
     * @param gc Contexto gráfico.
     * @param jugador Jugador actual.
     */
    private void dibujarVida(GraphicsContext gc, Jugador jugador) {

        int vidaActual = jugador.getVidaActual();
        int vidaMaxima = jugador.getVidaMaxima();

        Image corazonLleno = sprites.getImagen("corazon_lleno");
        Image corazonVacio = sprites.getImagen("corazon_vacio");

        int CORAZON_SIZE = 55;
        int SEPARACION = -5;

        for (int i = 0; i < vidaMaxima; i++) {

            double x = MARGEN + i * (CORAZON_SIZE + SEPARACION);
            double y = MARGEN;

            Image imgAUsar = (i < vidaActual) ? corazonLleno : corazonVacio;

            if (imgAUsar != null) {
                gc.setImageSmoothing(false);
                gc.drawImage(imgAUsar, x, y, CORAZON_SIZE, CORAZON_SIZE);
            } else {
                gc.setFill(i < vidaActual ? Color.CRIMSON : Color.DARKGRAY);
                gc.fillOval(x, y, CORAZON_SIZE, CORAZON_SIZE);
            }
        }
    }

    /**
     * Dibuja la cantidad de monedas del jugador
     * en la esquina superior derecha.
     *
     * @param gc Contexto gráfico.
     * @param jugador Jugador actual.
     */
    private void dibujarMonedas(GraphicsContext gc, Jugador jugador) {

        int monedas = jugador.getDinero();

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.GOLD);
        gc.fillText("💰 " + monedas, 20, 80);
    }

    /**
     * Dibuja el minimapa del dungeon en la esquina superior derecha.
     * Cada sala se representa como una celda coloreada según su tipo.
     *
     * @param gc Contexto gráfico.
     * @param dungeon Dungeon actual.
     * @param anchoCanvas Ancho del canvas.
     * @param altoCanvas Alto del canvas.
     */
    private void dibujarMiniMapa(GraphicsContext gc,
                                 Dungeon dungeon,
                                 double anchoCanvas,
                                 double altoCanvas) {

        if (dungeon == null) return;

        Room[][] grid = dungeon.getGridSalas();
        Room salaActual = dungeon.getSalaActual();

        int cols = grid.length;
        int filas = grid[0].length;

        double mapaAncho = cols * TAMANO_CELDA_MAPA + (cols - 1);
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

                if (sala == salaActual) {
                    gc.setFill(Color.WHITE);
                } else {
                    switch (sala.getTipo()) {
                        case INICIO -> gc.setFill(Color.LIMEGREEN);
                        case JEFE   -> gc.setFill(Color.CRIMSON);
                        case TIENDA -> gc.setFill(Color.GOLD);
                        default     -> gc.setFill(Color.SLATEGRAY);
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