package edu.trespor2.videojuego.view.screens;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CreditsScreen {
    private Image imagenFondo;
    private Image logoExtra1;
    private Image logoExtra2;

    private double mouseX, mouseY; // Para seguimiento del mouse

    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public CreditsScreen() {
        try {
            imagenFondo = new Image(getClass().getResourceAsStream("/assets/images/creditos_finales.jpeg"));
            // Cargamos las dos imágenes pequeñas
            logoExtra1 = new Image(getClass().getResourceAsStream("/assets/images/reiniciar.png"));
            logoExtra2 = new Image(getClass().getResourceAsStream("/assets/images/salir.png"));
        } catch (Exception e) {
            System.out.println("Error cargando imágenes de créditos: " + e.getMessage());
        }
    }
    public void render(GraphicsContext gc, double ancho, double alto) {
        if (imagenFondo != null) gc.drawImage(imagenFondo, 0, 0, ancho, alto);

        double tam = 120;
        double margen = 40;

        // Dibujamos logoExtra1 (Reiniciar)
        if (logoExtra1 != null) {
            gc.drawImage(logoExtra1, margen, alto - tam - margen, tam, tam);
        }

        // Dibujamos logoExtra2 (Salir)
        if (logoExtra2 != null) {
            gc.drawImage(logoExtra2, ancho - tam - margen, alto - tam - margen, tam, tam);
        }
    }

    // Lógica para botón Reiniciar (Abajo Izquierda)
    public boolean isReiniciarPresionado(double clickX, double clickY, double alto) {
        double tam = 120;
        double margen = 40;
        return clickX >= margen && clickX <= margen + tam &&
                clickY >= (alto - tam - margen) && clickY <= (alto - margen);
    }

    // Lógica para botón Salir (Abajo Derecha)
    public boolean isSalirPresionado(double clickX, double clickY, double ancho, double alto) {
        double tam = 120;
        double margen = 40;
        return clickX >= (ancho - tam - margen) && clickX <= (ancho - margen) &&
                clickY >= (alto - tam - margen) && clickY <= (alto - margen);
    }
}