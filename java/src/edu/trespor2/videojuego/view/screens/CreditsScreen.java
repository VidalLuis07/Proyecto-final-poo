package edu.trespor2.videojuego.view.screens;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CreditsScreen {

    private Image imagenCreditos;
    private double mouseX = 0;
    private double mouseY = 0;

    public CreditsScreen() {
        // AQUÍ PONES EL NOMBRE DE TU IMAGEN CHIDA
        // Asegúrate de que la ruta coincida con donde guardas tus otras imágenes
        try {
            imagenCreditos = new Image(getClass().getResourceAsStream("/assets/images/creditos_finales.jpeg"));
        } catch (Exception e) {
            System.out.println("No se encontró la imagen de créditos. Verifica la ruta.");
        }
    }

    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public void render(GraphicsContext gc, double ancho, double alto) {
        // Dibujamos la imagen de fondo ocupando toda la pantalla
        if (imagenCreditos != null) {
            gc.drawImage(imagenCreditos, 0, 0, ancho, alto);
        } else {
            // Fondo negro por si falla la imagen
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillRect(0, 0, ancho, alto);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillText("¡VICTORIA! (Falta cargar la imagen)", ancho / 2 - 50, alto / 2);
        }

        // Aquí puedes dibujar algún efecto visual si el mouse pasa sobre el botón de volver a jugar
        // Por ahora lo dejamos limpio para que luzca tu imagen
    }

    // Estas coordenadas dependen de dónde esté pintado el botón en tu imagen.
    // Ajústalas (x, y, ancho, alto) para que coincidan con tu diseño gráfico.
    public boolean isVolverAJugarPresionado(double clickX, double clickY) {
        // Ejemplo: Un botón que está en el centro inferior de la pantalla
        double botonX = 1280 / 2.0 - 100; // Asumiendo que tu ancho es 1280
        double botonY = 600;              // Altura en Y
        double botonAncho = 200;
        double botonAlto = 50;

        return clickX >= botonX && clickX <= botonX + botonAncho &&
                clickY >= botonY && clickY <= botonY + botonAlto;
    }
}