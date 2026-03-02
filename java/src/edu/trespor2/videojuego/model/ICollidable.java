package edu.trespor2.videojuego.model;
import javafx.geometry.Rectangle2D;

/**
 * Interfaz que deben implementar todas las entidades que participan en el sistema de colisiones.
 * Obliga a definir los límites físicos (hitbox) de la entidad.
 */
public interface ICollidable {

    /**
     * Retorna los límites de colisión (hitbox) de la entidad.
     *
     * @return un {@code Rectangle2D} que representa el área de colisión de la entidad
     */
    Rectangle2D getBounds();
}