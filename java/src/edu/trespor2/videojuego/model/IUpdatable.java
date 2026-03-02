package edu.trespor2.videojuego.model;

/**
 * Define el comportamiento de las entidades que requieren
 * actualización continua dentro del ciclo principal del juego.
 *
 * Las clases que implementen esta interfaz deben definir
 * cómo se actualiza su estado en cada frame.
 */
public interface IUpdatable {

    /**
     * Actualiza el estado de la entidad.
     *
     * @param delta Tiempo transcurrido desde la última actualización
     *              (generalmente en segundos).
     */
    void update(double delta);

}