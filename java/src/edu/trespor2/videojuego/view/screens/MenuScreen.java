package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.model.IdiomaManager;
import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * MenuScreen — Pantalla principal del juego.
 *
 * SOLID aplicado:
 *  - SRP : solo dibuja el menú principal.
 *  - OCP : el cambio de idioma no modifica la lógica de esta clase;
 *           IdiomaManager resuelve qué sprite usar.
 *  - DIP : depende de la abstracción IdiomaManager, no de strings concretos.
 *
 * Lógica del botón de idioma:
 *   - Al iniciar el juego el idioma es ES → se muestra el botón "ESP"
 *   - Al hacer clic en "ESP" → cambia a EN → ahora se muestra "ENG"
 *   - Al hacer clic en "ENG" → vuelve a ES → se muestra "ESP" otra vez
 *   La imagen que se muestra siempre es el idioma ACTUAL
 *   (es decir, le estás diciendo al jugador en qué idioma está jugando).
 */
public class MenuScreen {

    private final SpriteManager sprites;
    private final IdiomaManager idioma;

    // ── Botón START / INICIAR ─────────────────────────────────────────────
    private static final double BTN_START_X = 100;
    private static final double BTN_START_Y = 190;
    private static final double BTN_START_W = 190;
    private static final double BTN_START_H = 90;

    // ── Botón EXIT / SALIR ────────────────────────────────────────────────
    private static final double BTN_EXIT_X  = 80;
    private static final double BTN_EXIT_Y  = 270;
    private static final double BTN_EXIT_W  = 190;
    private static final double BTN_EXIT_H  = 90;

    // ── Botón de idioma (debajo de SALIR) ────────────────────────────────
    // BTN_EXIT_Y(270) + BTN_EXIT_H(90) + 10px de separación = 370
    private static final double BTN_LANG_X  = 80;
    private static final double BTN_LANG_Y  = 370;
    private static final double BTN_LANG_W  = 110;
    private static final double BTN_LANG_H  = 50;

    // ── Hover ──────────────────────────────────────────────────────────────
    private double mouseX = 0;
    private double mouseY = 0;

    public MenuScreen() {
        this.sprites = SpriteManager.getInstance();
        this.idioma  = IdiomaManager.getInstance();
        // Aseguramos que el idioma por defecto sea ES al crear el menú
        idioma.setIdioma(IdiomaManager.Idioma.ES);
    }

    /** El GameLoop llama esto en cada MOUSE_MOVED para el efecto hover. */
    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RENDER
    // ══════════════════════════════════════════════════════════════════════
    public void render(GraphicsContext gc, double anchoCanvas, double altoCanvas) {
        gc.setImageSmoothing(false);

        // 1) Fondo
        Image fondo = sprites.getImagen("menu_fondo");
        if (fondo != null) {
            gc.drawImage(fondo, 0, 0, anchoCanvas, altoCanvas);
        } else {
            gc.setFill(Color.web("#1a0a0a"));
            gc.fillRect(0, 0, anchoCanvas, altoCanvas);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            gc.fillText("CASTILLO'S ADVENTURES", 80, 180);
        }

        // 2) Botón START / INICIAR según idioma activo
        String claveStart = idioma.clave("boton_start", "boton_iniciar");
        Image imgStart = sprites.getImagen(claveStart);
        dibujarBoton(gc, imgStart, BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H);

        // 3) Botón EXIT / SALIR según idioma activo
        String claveExit = idioma.clave("boton_exit", "boton_salir");
        Image imgExit = sprites.getImagen(claveExit);
        dibujarBoton(gc, imgExit, BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H);

        // 4) Botón de idioma:
        //    - Si idioma=ES → muestra "ESP" (indica el idioma actual)
        //    - Si idioma=EN → muestra "ENG"
        String claveLang = idioma.clave("boton_lang_eng", "boton_lang_esp");
        Image imgLang = sprites.getImagen(claveLang);
        dibujarBoton(gc, imgLang, BTN_LANG_X, BTN_LANG_Y, BTN_LANG_W, BTN_LANG_H);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DETECCIÓN DE CLICS — el GameLoop llama estos métodos
    // ══════════════════════════════════════════════════════════════════════

    public boolean isJugarPresionado(double mx, double my) {
        return isHover(mx, my, BTN_START_X, BTN_START_Y, BTN_START_W, BTN_START_H);
    }

    public boolean isSalirPresionado(double mx, double my) {
        return isHover(mx, my, BTN_EXIT_X, BTN_EXIT_Y, BTN_EXIT_W, BTN_EXIT_H);
    }

    /** Devuelve true si se hizo clic en el botón de idioma. */
    public boolean isIdiomaPresionado(double mx, double my) {
        return isHover(mx, my, BTN_LANG_X, BTN_LANG_Y, BTN_LANG_W, BTN_LANG_H);
    }

    // ── Utilidades privadas ───────────────────────────────────────────────

    private void dibujarBoton(GraphicsContext gc, Image img,
                              double x, double y, double w, double h) {
        if (img == null) return;

        if (isHover(mouseX, mouseY, x, y, w, h)) {
            double extra = 6;
            gc.drawImage(img,
                    x - extra / 2, y - extra / 2,
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