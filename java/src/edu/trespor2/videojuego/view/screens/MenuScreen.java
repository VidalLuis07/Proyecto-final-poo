package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MenuScreen {

    private final SpriteManager sprites;

    // ── Posición y tamaño de los botones en pantalla ───────────────────────
    private static final double BTN_START_X  = 100;
    private static final double BTN_START_Y  = 190;
    private static final double BTN_START_W  = 190; // ancho al dibujarlo
    private static final double BTN_START_H  = 90;  // alto al dibujarlo

    private static final double BTN_EXIT_X   = 80;
    private static final double BTN_EXIT_Y   = 270;
    private static final double BTN_EXIT_W   = 190;
    private static final double BTN_EXIT_H   = 90;

    // ── Animación de hover (el botón seleccionado brilla) ─────────────────
    private double mouseX = 0;
    private double mouseY = 0;

    public MenuScreen() {
        this.sprites = SpriteManager.getInstance();
    }

    /**
     * Llama esto desde el GameLoop para saber dónde está el mouse
     * y poder hacer el efecto hover en los botones.
     */
    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RENDER
    // ══════════════════════════════════════════════════════════════════════
    public void render(GraphicsContext gc, double anchoCanvas, double altoCanvas) {
        gc.setImageSmoothing(false);

        // 1) Fondo del castillo a pantalla completa
        Image fondo = sprites.getImagen("menu_fondo");
        if (fondo != null) {
            gc.drawImage(fondo, 0, 0, anchoCanvas, altoCanvas);
        } else {
            // Fallback si no cargó
            gc.setFill(Color.web("#1a0a0a"));
            gc.fillRect(0, 0, anchoCanvas, altoCanvas);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            gc.fillText("CASTILLO'S ADVENTURES", 80, 180);
        }

        // 2) Botón START
        Image imgStart = sprites.getImagen("boton_start");
        if (imgStart != null) {
            // Si el mouse está encima, lo dibujamos un poco más grande (efecto hover)
            if (isHover(mouseX, mouseY, BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H)) {
                double extra = 6;
                gc.drawImage(imgStart,
                        BTN_START_X - extra/2, BTN_START_Y - extra/2,
                        BTN_START_W + extra, BTN_START_H + extra);
            } else {
                gc.drawImage(imgStart, BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H);
            }
        } else {
            // Fallback con rectángulo
            gc.setFill(Color.web("#222222"));
            gc.fillRect(BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            gc.fillText("START", BTN_START_X + 30, BTN_START_Y + 44);
        }

        // 3) Botón EXIT
        Image imgExit = sprites.getImagen("boton_exit");
        if (imgExit != null) {
            if (isHover(mouseX, mouseY, BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H)) {
                double extra = 6;
                gc.drawImage(imgExit,
                        BTN_EXIT_X - extra/2, BTN_EXIT_Y - extra/2,
                        BTN_EXIT_W + extra, BTN_EXIT_H + extra);
            } else {
                gc.drawImage(imgExit, BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H);
            }
        } else {
            gc.setFill(Color.web("#222222"));
            gc.fillRect(BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            gc.fillText("EXIT", BTN_EXIT_X + 30, BTN_EXIT_Y + 44);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DETECCIÓN DE CLICS — El GameLoop llama estos métodos
    // ══════════════════════════════════════════════════════════════════════
    public boolean isJugarPresionado(double mx, double my) {
        return isHover(mx, my, BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H);
    }

    public boolean isSalirPresionado(double mx, double my) {
        return isHover(mx, my, BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H);
    }

    // ── Utilidad ───────────────────────────────────────────────────────────
    private boolean isHover(double mx, double my,
                            double x, double y, double w, double h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}