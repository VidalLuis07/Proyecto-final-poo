package edu.trespor2.videojuego.model;

import javafx.geometry.Rectangle2D;

/**
 * Define el comportamiento de los objetos que pueden participar
 * en el sistema de colisiones del juego.
 *
 * Las clases que implementen esta interfaz deben proporcionar
 * los límites de su área de colisión en forma de {@link Rectangle2D}.
 */
public interface ICollidable {

    /**
     * Obtiene los límites rectangulares que representan
     * el área de colisión de la entidad.
     *
     * @return Un {@link Rectangle2D} que define el área de colisión.
     */
    Rectangle2D getBounds();
}