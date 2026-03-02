/**
 * TestRunner — ejecuta todos los tests del proyecto.
 *
 * Compilar y correr desde la raíz del proyecto:
 *
 *   javac -cp . java/src/**&#47;*.java java/test/**&#47;*.java
 *   java -cp java/src:java/test TestRunner
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          SUITE DE TESTS POO          ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        edu.trespor2.videojuego.model.entidades.personajes.JugadorTest.main(args);
        edu.trespor2.videojuego.model.entidades.personajes.GameCharacterTest.main(args);
        edu.trespor2.videojuego.model.entidades.personajes.ZombieTest.main(args);
        edu.trespor2.videojuego.model.entidades.personajes.JefeTest.main(args);
        edu.trespor2.videojuego.model.items.ItemsTest.main(args);
        edu.trespor2.videojuego.model.entidades.EntidadesTest.main(args);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║            FIN DE LA SUITE           ║");
        System.out.println("╚══════════════════════════════════════╝");
    }
}