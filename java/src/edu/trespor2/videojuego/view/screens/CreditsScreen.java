package edu.trespor2.videojuego.view.screens;

import edu.trespor2.videojuego.model.IdiomaManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class CreditsScreen {

    private Image imagenFondo;
    private Image logoExtra1;   // Reiniciar
    private Image logoExtra2;   // Salir
    private Image imgVictoria;  // "VICTORIA" (español)
    private Image imgVictory;   // "VICTORY"  (inglés)

    private double mouseX, mouseY;

    public void actualizarMouse(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public CreditsScreen() {
        try {
            imagenFondo  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/creditos_finales.png")));
            logoExtra1   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/reiniciar.png")));
            logoExtra2   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/salir.png")));
            imgVictoria  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Idioma/victoria.png")));
            imgVictory   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Idioma/victory.png")));

        } catch (Exception e) {
            System.out.println("Error cargando imágenes de créditos: " + e.getMessage());
        }
    }

    public void render(GraphicsContext gc, double ancho, double alto) {

        // ── Fondo de créditos ─────────────────────────────────────────────
        if (imagenFondo != null) {
            gc.drawImage(imagenFondo, 0, 0, ancho, alto);
        }

        // ── Banner VICTORIA / VICTORY arriba al centro ────────────────────
        boolean enEspanol = IdiomaManager.getInstance().esEspanol();
        Image bannerVictoria = enEspanol ? imgVictoria : imgVictory;

        if (bannerVictoria != null) {
            // Ancho = 60% del canvas, centrado horizontalmente
            double bannerAncho  = ancho * 0.60;
            // Mantener proporción original (1920x1080 → ratio ~0.282 de alto/ancho)
            double bannerAlto   = bannerAncho * (bannerVictoria.getHeight() / bannerVictoria.getWidth());
            double bannerX      = (ancho - bannerAncho) / 2.0;
            double bannerY      = 30; // 30px desde arriba

            gc.drawImage(bannerVictoria, bannerX, bannerY, bannerAncho, bannerAlto);
        }

        // ── Botones abajo ─────────────────────────────────────────────────
        double tam    = 120;
        double margen = 40;

        if (logoExtra1 != null) {
            gc.drawImage(logoExtra1, margen, alto - tam - margen, tam, tam);
        }
        if (logoExtra2 != null) {
            gc.drawImage(logoExtra2, ancho - tam - margen, alto - tam - margen, tam, tam);
        }
    }

    // Botón Reiniciar (abajo izquierda)
    public boolean isReiniciarPresionado(double clickX, double clickY, double alto) {
        double tam = 120, margen = 40;
        return clickX >= margen && clickX <= margen + tam &&
                clickY >= (alto - tam - margen) && clickY <= (alto - margen);
    }

    // Botón Salir (abajo derecha)
    public boolean isSalirPresionado(double clickX, double clickY, double ancho, double alto) {
        double tam = 120, margen = 40;
        return clickX >= (ancho - tam - margen) && clickX <= (ancho - margen) &&
                clickY >= (alto - tam - margen) && clickY <= (alto - margen);
    }
}