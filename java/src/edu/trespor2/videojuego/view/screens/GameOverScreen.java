package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Pantalla de Game Over.
 * Se muestra cuando jugador.estaMuerto() es true.
 */
public class GameOverScreen {

    private final SpriteManager sprites;

    private static final double BTN_REINICIAR_X = 280;
    private static final double BTN_REINICIAR_Y = 360;
    private static final double BTN_MENU_X      = 280;
    private static final double BTN_MENU_Y      = 430;
    private static final double BTN_ANCHO        = 240;
    private static final double BTN_ALTO         = 50;

    public GameOverScreen() {
        this.sprites = SpriteManager.getInstance();
    }

    public void render(GraphicsContext gc, double anchoCanvas, double altoCanvas) {
        // Fondo
        Image fondo = sprites.getImagen("gameover_fondo");
        if (fondo != null) {
            gc.drawImage(fondo, 0, 0, anchoCanvas, altoCanvas);
        } else {
            gc.setFill(Color.web("#0d0000"));
            gc.fillRect(0, 0, anchoCanvas, altoCanvas);
        }

        gc.setTextAlign(TextAlignment.CENTER);

        // Título
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", anchoCanvas / 2, 240);

        gc.setFont(Font.font("Arial", 22));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Has sido derrotado...", anchoCanvas / 2, 300);

        // Botones
        dibujarBoton(gc, "🔄  REINICIAR", BTN_REINICIAR_X, BTN_REINICIAR_Y,
                BTN_ANCHO, BTN_ALTO, Color.STEELBLUE);
        dibujarBoton(gc, "🏠  MENÚ PRINCIPAL", BTN_MENU_X, BTN_MENU_Y,
                BTN_ANCHO, BTN_ALTO, Color.DIMGRAY);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    public boolean isReiniciarPresionado(double mx, double my) {
        return mx >= BTN_REINICIAR_X && mx <= BTN_REINICIAR_X + BTN_ANCHO
                && my >= BTN_REINICIAR_Y && my <= BTN_REINICIAR_Y + BTN_ALTO;
    }

    public boolean isMenuPresionado(double mx, double my) {
        return mx >= BTN_MENU_X && mx <= BTN_MENU_X + BTN_ANCHO
                && my >= BTN_MENU_Y && my <= BTN_MENU_Y + BTN_ALTO;
    }

    private void dibujarBoton(GraphicsContext gc, String texto,
                              double x, double y, double ancho, double alto, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, ancho, alto, 10, 10);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.fillText(texto, x + 20, y + 32);
    }
}