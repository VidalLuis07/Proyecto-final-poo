package edu.trespor2.videojuego.view;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interfaz que define el contrato para cualquier objeto
 * que pueda ser renderizado (dibujado) en pantalla.
 *
 * <p>Las clases que implementen esta interfaz deberán
 * proporcionar la lógica necesaria para dibujarse
 * utilizando un {@link GraphicsContext} de JavaFX.</p>
 *
 * <p>Forma parte de la capa de vista (view) dentro
 * de la arquitectura del videojuego.</p>
 */
public interface IRenderable {

    /**
     * Renderiza el objeto en el canvas utilizando el
     * contexto gráfico proporcionado.
     *
     * @param gc el {@link GraphicsContext} usado para dibujar
     *           el objeto en pantalla
     */
    void render(GraphicsContext gc);
}