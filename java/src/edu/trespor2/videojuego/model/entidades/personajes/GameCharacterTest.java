package edu.trespor2.videojuego.model.entidades.personajes;

public class GameCharacterTest {

    private static int passed = 0;
    private static int failed = 0;

    static void assertTrue(String nombre, boolean condicion) {
        if (condicion) {
            System.out.println("  [OK]   " + nombre);
            passed++;
        } else {
            System.out.println("  [FAIL] " + nombre);
            failed++;
        }
    }

    static void assertEquals(String nombre, Object esperado, Object actual) {
        boolean igual = esperado == null ? actual == null : esperado.equals(actual);
        assertTrue(nombre + " (esperado=" + esperado + ", actual=" + actual + ")", igual);
    }

    // Usamos Jugador como implementación concreta de GameCharacter
    static Jugador nuevo() {
        return new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
    }

    // ── tests ────────────────────────────────────────────────────────────────

    static void testRecibirDanoReduceVida() {
        Jugador j = nuevo();
        j.recibirDano(20);
        assertEquals("vida = 80 tras recibir 20 de daño", 80, j.getVidaActual());
    }

    static void testVidaNoBajaDeNegativo() {
        Jugador j = nuevo();
        j.recibirDano(999);
        assertEquals("vida = 0 tras daño masivo", 0, j.getVidaActual());
        assertTrue("vida no puede ser negativa", j.getVidaActual() >= 0);
    }

    static void testEstaMuertoConVidaCero() {
        Jugador j = nuevo();
        j.recibirDano(100);
        assertTrue("estaMuerto() = true con vida 0", j.estaMuerto());
    }

    static void testNoEstaMuertoConVidaPositiva() {
        Jugador j = nuevo();
        assertTrue("estaMuerto() = false recién creado", !j.estaMuerto());
    }

    static void testInvulnerabilidadEvidaSegundoDano() {
        Jugador j = nuevo();
        j.recibirDano(10); // activa invulnerabilidad
        j.recibirDano(10); // debe ignorarse
        assertEquals("segundo golpe ignorado por invulnerabilidad", 90, j.getVidaActual());
    }

    static void testInvulnerabilidadExpiraDespuesDeLosTicks() {
        Jugador j = nuevo();
        j.recibirDano(10); // activa 60 ticks
        for (int i = 0; i < 61; i++) j.update(1);
        j.recibirDano(10); // ahora sí debe aplicarse
        assertEquals("daño aplicado tras expirar invulnerabilidad", 80, j.getVidaActual());
    }

    static void testCurarAumentaVida() {
        Jugador j = nuevo();
        j.recibirDano(30);
        for (int i = 0; i < 61; i++) j.update(1); // esperar fin de invulnerabilidad
        j.curar(20);
        assertEquals("vida = 90 tras curar 20", 90, j.getVidaActual());
    }

    static void testCurarNoSuperaVidaMaxima() {
        Jugador j = nuevo();
        j.curar(999);
        assertEquals("curar no supera vidaMaxima", 100, j.getVidaActual());
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== GameCharacterTest ===");
        testRecibirDanoReduceVida();
        testVidaNoBajaDeNegativo();
        testEstaMuertoConVidaCero();
        testNoEstaMuertoConVidaPositiva();
        testInvulnerabilidadEvidaSegundoDano();
        testInvulnerabilidadExpiraDespuesDeLosTicks();
        testCurarAumentaVida();
        testCurarNoSuperaVidaMaxima();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}