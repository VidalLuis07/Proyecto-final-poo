package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.controller.ControlTienda;
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * ShopScreen — Dibuja la tienda y maneja los clics en los botones de compra.
 *
 * SOLID - Single Responsibility: Solo presenta la tienda visualmente.
 * La lógica de compra la delega a ControlTienda (ya existente en tu proyecto).
 *
 * Conexión:
 *   ShopController/GameLoop → llama render() y onClick()
 *   ControlTienda → ejecuta la lógica de compra
 */
public class ShopScreen {

    private final SpriteManager sprites;
    private final ControlTienda controlTienda;

    //Posiciones de los botones de compra
    private static final double BTN_VIDA_X      = 150;
    private static final double BTN_VIDA_Y      = 350;
    private static final double BTN_VELOCIDAD_X = 320;
    private static final double BTN_VELOCIDAD_Y = 350;
    private static final double BTN_DANO_X      = 490;
    private static final double BTN_DANO_Y      = 350;
    private static final double BTN_SALIR_X     = 310;
    private static final double BTN_SALIR_Y     = 460;

    private static final double BTN_ANCHO = 160;
    private static final double BTN_ALTO  = 55;

    // Mensaje de feedback al jugador (ej: "¡Comprado!" o "Sin fondos")
    private String mensajeFeedback = "";
    private int contadorMensaje    = 0;

    public ShopScreen(Jugador jugador) {
        this.sprites       = SpriteManager.getInstance();
        this.controlTienda = new ControlTienda(jugador);
    }


    //  RENDER

    public void render(GraphicsContext gc, Jugador jugador,
                       double anchoCanvas, double altoCanvas) {

        // 1) Fondo — tu imagen de tienda a pantalla completa
        Image fondo = sprites.getImagen("tienda_fondo");
        if (fondo != null) {
            gc.drawImage(fondo, 0, 0, anchoCanvas, altoCanvas);
        } else {
            gc.setFill(Color.web("#1b1028"));
            gc.fillRect(0, 0, anchoCanvas, altoCanvas);
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("🛒 TIENDA", anchoCanvas / 2, 120);
            gc.setTextAlign(TextAlignment.LEFT);
        }

        // 2) Botones de items
        dibujarBotonItem(gc, "❤  VIDA", "15 monedas",
                BTN_VIDA_X, BTN_VIDA_Y, BTN_ANCHO, BTN_ALTO, Color.DARKRED);

        dibujarBotonItem(gc, "⚡ VELOCIDAD", "20 monedas",
                BTN_VELOCIDAD_X, BTN_VELOCIDAD_Y, BTN_ANCHO, BTN_ALTO, Color.DARKBLUE);

        dibujarBotonItem(gc, "⚔  DAÑO", "30 monedas",
                BTN_DANO_X, BTN_DANO_Y, BTN_ANCHO, BTN_ALTO, Color.DARKORANGE);

        // 3) Botón salir
        dibujarBoton(gc, "← SALIR", BTN_SALIR_X, BTN_SALIR_Y,
                120, 40, Color.DIMGRAY);

        // 4) Mensaje de feedback (aparece 2 segundos)
        if (contadorMensaje > 0) {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(mensajeFeedback, anchoCanvas / 2, BTN_SALIR_Y - 20);
            gc.setTextAlign(TextAlignment.LEFT);
            contadorMensaje--;
        }
    }


    //  MANEJO DE CLICS — El GameLoop llama esto cuando hay un clic en modo TIENDA


    /**
     * Procesa un clic del mouse. Delega la lógica a ControlTienda.
     * @return true si el jugador quiere salir de la tienda
     */
    public boolean onClick(double mouseX, double mouseY) {

        if (dentroDe(mouseX, mouseY, BTN_VIDA_X, BTN_VIDA_Y, BTN_ANCHO, BTN_ALTO)) {
            boolean exito = controlTienda.comprarVida();
            mostrarFeedback(exito ? "¡Vida restaurada! ❤" : "No puedes comprar más vida");
        }
        else if (dentroDe(mouseX, mouseY, BTN_VELOCIDAD_X, BTN_VELOCIDAD_Y, BTN_ANCHO, BTN_ALTO)) {
            boolean exito = controlTienda.comprarVelocidad();
            mostrarFeedback(exito ? "¡Velocidad mejorada! ⚡" : "No tienes suficientes monedas");
        }
        else if (dentroDe(mouseX, mouseY, BTN_DANO_X, BTN_DANO_Y, BTN_ANCHO, BTN_ALTO)) {
            boolean exito = controlTienda.comprarDano();
            mostrarFeedback(exito ? "¡Daño mejorado! ⚔" : "No tienes suficientes monedas");
        }
        else if (dentroDe(mouseX, mouseY, BTN_SALIR_X, BTN_SALIR_Y, 120, 40)) {
            return true; // El GameLoop cambiará el estado a JUGANDO
        }

        return false;
    }

    // Utilidades

    private void dibujarBotonItem(GraphicsContext gc, String nombre, String precio,
                                  double x, double y, double ancho, double alto, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, ancho, alto, 10, 10);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText(nombre, x + 10, y + 22);

        gc.setFont(Font.font("Arial", 12));
        gc.setFill(Color.GOLD);
        gc.fillText(precio, x + 10, y + 40);
    }

    private void dibujarBoton(GraphicsContext gc, String texto,
                              double x, double y, double ancho, double alto, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, ancho, alto, 8, 8);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.fillText(texto, x + 12, y + 26);
    }

    private boolean dentroDe(double mx, double my,
                             double x, double y, double ancho, double alto) {
        return mx >= x && mx <= x + ancho && my >= y && my <= y + alto;
    }

    private void mostrarFeedback(String mensaje) {
        this.mensajeFeedback = mensaje;
        this.contadorMensaje = 120; // ~2 segundos a 60fps
    }
}