package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.model.IdiomaManager;
import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * GameOverScreen — Pantalla que se muestra cuando el jugador muere.
 *
 * SOLID aplicado:
 *  - SRP : solo se encarga de DIBUJAR la pantalla de game over.
 *  - OCP : para cambiar el idioma no se modifica esta clase;
 *           IdiomaManager resuelve la clave del sprite correcto.
 *  - DIP : depende de la abstracción IdiomaManager, no de strings hardcodeados.
 */
public class GameOverScreen {

    private final SpriteManager sprites;
    private final IdiomaManager idioma;

    // ── Posición del botón YES (Sí / reiniciar) ───────────────────────────
    // Canvas 800px ancho. Dos botones de 190px con 60px entre ellos = 440px total.
    // Margen izquierdo: (800 - 440) / 2 = 180  → botón SÍ en X=180, NO en X=430
    // Y=520 para dejar espacio debajo del texto "¿Jugar de nuevo?"
    private static final double BTN_YES_X  = 430;
    private static final double BTN_YES_Y  = 420;
    private static final double BTN_YES_W  = 190;
    private static final double BTN_YES_H  = 90;

    // ── Posición del botón NO ─────────────────────────────────────────────
    private static final double BTN_NO_X   = 630;
    private static final double BTN_NO_Y   = 420;
    private static final double BTN_NO_W   = 190;
    private static final double BTN_NO_H   = 90;

    // ── Efecto hover ──────────────────────────────────────────────────────
    private double mouseX = 0;
    private double mouseY = 0;

    public GameOverScreen() {
        this.sprites = SpriteManager.getInstance();
        this.idioma  = IdiomaManager.getInstance();
    }

    /** Llama esto desde el GameLoop para el efecto hover. */
    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RENDER
    // ══════════════════════════════════════════════════════════════════════
    public void render(GraphicsContext gc, double anchoCanvas, double altoCanvas) {
        gc.setImageSmoothing(false);

        // 1) Imagen de fondo con texto "GAME OVER / PERDISTE" según idioma
        String claveGameOver = idioma.clave("gameover_en", "gameover_es");
        Image fondoGameOver = sprites.getImagen(claveGameOver);

        if (fondoGameOver != null) {
            gc.drawImage(fondoGameOver, 0, 0, anchoCanvas, altoCanvas);
        } else {
            // Fallback si no cargó la imagen
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, anchoCanvas, altoCanvas);
            gc.setFill(Color.WHITE);
            gc.fillText(idioma.esEspanol() ? "PERDISTE" : "GAME OVER",
                    anchoCanvas / 2 - 100, altoCanvas / 2 - 40);
        }

        // 2) Botón YES / SÍ
        String claveYes = idioma.clave("boton_yes", "boton_si");
        Image imgYes = sprites.getImagen(claveYes);
        dibujarBoton(gc, imgYes, BTN_YES_X, BTN_YES_Y, BTN_YES_W, BTN_YES_H);

        // 3) Botón NO (igual en ambos idiomas)
        Image imgNo = sprites.getImagen("boton_no");
        dibujarBoton(gc, imgNo, BTN_NO_X, BTN_NO_Y, BTN_NO_W, BTN_NO_H);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DETECCIÓN DE CLICS — el GameLoop llama estos métodos
    // ══════════════════════════════════════════════════════════════════════

    /** Devuelve true si el clic fue sobre el botón de reiniciar (YES/SÍ). */
    public boolean isReiniciarPresionado(double mx, double my) {
        return isHover(mx, my, BTN_YES_X, BTN_YES_Y, BTN_YES_W, BTN_YES_H);
    }

    /** Devuelve true si el clic fue sobre el botón de salir al menú (NO). */
    public boolean isMenuPresionado(double mx, double my) {
        return isHover(mx, my, BTN_NO_X, BTN_NO_Y, BTN_NO_W, BTN_NO_H);
    }

    // ── Utilidades privadas ───────────────────────────────────────────────

    private void dibujarBoton(GraphicsContext gc, Image img,
                              double x, double y, double w, double h) {
        if (img == null) return;

        if (isHover(mouseX, mouseY, x, y, w, h)) {
            double extra = 6;
            gc.drawImage(img, x - extra / 2, y - extra / 2,
                    w + extra, h + extra);
        } else {
            gc.drawImage(img, x, y, w, h);
        }
    }

    private boolean isHover(double mx, double my,
                            double x, double y, double w, double h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}