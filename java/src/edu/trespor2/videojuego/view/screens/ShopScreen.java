package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.controller.ControlTienda;
import edu.trespor2.videojuego.model.IdiomaManager; // ¡Importante para el idioma!
import edu.trespor2.videojuego.model.entidades.personajes.Jugador;
import edu.trespor2.videojuego.view.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class ShopScreen {

    private final SpriteManager sprites;
    private final ControlTienda controlTienda;

    // Posiciones en el mostrador de la tienda
    private static final double ITEM_Y       = 300;
    private static final double BTN_LLOROS_X = 180;
    private static final double BTN_VEL_X    = 380;
    private static final double BTN_DANO_X   = 580;
    private static final double ITEM_SIZE    = 80;

    private static final double BTN_SALIR_X  = 340;
    private static final double BTN_SALIR_Y  = 460;

    private String mensajeFeedback = "";
    private int contadorMensaje    = 0;

    public ShopScreen(Jugador jugador) {
        this.sprites       = SpriteManager.getInstance();
        this.controlTienda = new ControlTienda(jugador);
    }

    public void render(GraphicsContext gc, Jugador jugador, double anchoCanvas, double altoCanvas) {

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

        boolean enEspanol = IdiomaManager.getInstance().esEspanol();

        // Mostrar monedas actuales
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.fillText(enEspanol ? "Tus Monedas: " + jugador.getDinero() : "Your Coins: " + jugador.getDinero(), 50, 50);

        // --- DIBUJAR LOS 3 POWER-UPS (Aquí pasamos los precios nuevos) ---
        String claveLloros    = enEspanol ? "powerup_lloros" : "powerup_cries";
        String claveVelocidad = enEspanol ? "powerup_fuego_veloz" : "powerup_fire_velocity";
        String claveDano      = enEspanol ? "powerup_fuego_mortal" : "powerup_mortal_fire";

        dibujarPowerUp(gc, sprites.getImagen(claveLloros), BTN_LLOROS_X, ITEM_Y, "5");
        dibujarPowerUp(gc, sprites.getImagen(claveVelocidad), BTN_VEL_X, ITEM_Y, "10");
        dibujarPowerUp(gc, sprites.getImagen(claveDano), BTN_DANO_X, ITEM_Y, "15");

        // Botón salir
        gc.setFill(Color.DIMGRAY);
        gc.fillRoundRect(BTN_SALIR_X, BTN_SALIR_Y, 120, 40, 8, 8);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.fillText(enEspanol ? "← SALIR" : "← EXIT", BTN_SALIR_X + 25, BTN_SALIR_Y + 25);

        // Feedback
        if (contadorMensaje > 0) {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(mensajeFeedback, anchoCanvas / 2, BTN_SALIR_Y - 30);
            gc.setTextAlign(TextAlignment.LEFT);
            contadorMensaje--;
        }
    }

    public boolean onClick(double mouseX, double mouseY) {
        boolean enEspanol = IdiomaManager.getInstance().esEspanol();

        if (dentroDe(mouseX, mouseY, BTN_LLOROS_X, ITEM_Y, ITEM_SIZE, ITEM_SIZE)) {
            boolean exito = controlTienda.comprarLloros();
            mostrarFeedback(exito ? (enEspanol ? "¡+1 Corazón!" : "+1 Heart!") :
                    (enEspanol ? "Sin fondos" : "Not enough coins"));
        }
        else if (dentroDe(mouseX, mouseY, BTN_VEL_X, ITEM_Y, ITEM_SIZE, ITEM_SIZE)) {
            boolean exito = controlTienda.comprarVelocidad();
            mostrarFeedback(exito ? (enEspanol ? "¡Más rápido!" : "Speed Up!") :
                    (enEspanol ? "Sin fondos" : "Not enough coins"));
        }
        else if (dentroDe(mouseX, mouseY, BTN_DANO_X, ITEM_Y, ITEM_SIZE, ITEM_SIZE)) {
            boolean exito = controlTienda.comprarDano();
            mostrarFeedback(exito ? (enEspanol ? "¡Más daño!" : "Damage Up!") :
                    (enEspanol ? "Sin fondos" : "Not enough coins"));
        }
        else if (dentroDe(mouseX, mouseY, BTN_SALIR_X, BTN_SALIR_Y, 120, 40)) {
            return true;
        }
        return false;
    }

    // dibujar los power ups
    private void dibujarPowerUp(GraphicsContext gc, Image img, double x, double y, String precio) {
        if (img != null) {
            double frameSize = img.getHeight();
            gc.drawImage(img, 0, 0, frameSize, frameSize, x, y, ITEM_SIZE, ITEM_SIZE);
        } else {
            gc.setFill(Color.MAGENTA);
            gc.fillRect(x, y, ITEM_SIZE, ITEM_SIZE);
        }

        // mostrar la moneda (frame 0 del spritesheet animado)
        Image monedaImg = sprites.getFrameMoneda("moneda", 0);
        if (monedaImg != null) {
            gc.drawImage(monedaImg, x + 5, y + ITEM_SIZE + 5, 24, 24);
        }

        // mostrar precio
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.fillText(precio, x + 35, y + ITEM_SIZE + 24);
    }

    private boolean dentroDe(double mx, double my, double x, double y, double ancho, double alto) {
        return mx >= x && mx <= x + ancho && my >= y && my <= y + alto;
    }

    private void mostrarFeedback(String mensaje) {
        this.mensajeFeedback = mensaje;
        this.contadorMensaje = 120;
    }
}