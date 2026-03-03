package edu.trespor2.videojuego.model;

/**
 * Interfaz que deben implementar todas las entidades que necesitan
 * actualizarse en cada frame del loop del juego.
 */
public interface IUpdatable {

    /**
     * Actualiza el estado de la entidad en función del tiempo transcurrido.
     *
     * @param delta tiempo en segundos transcurrido desde el último frame
     */
    void update(double delta);
}