package edu.trespor2.videojuego.model.entidades;

public class EntidadesTest {

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

    // Clase concreta mínima para poder instanciar EntidadMovible
    static class TestMovible extends EntidadMovible {
        TestMovible(double x, double y, double w, double h, double vel) {
            super(x, y, w, h, vel);
        }
    }

    // ── Proyectiles ───────────────────────────────────────────────────────────

    static void testProyectilSinFlagIsDagaFalse() {
        Proyectiles p = new Proyectiles(0, 0, 10, 10, 2.0, 1, 0, 5);
        assertTrue("isDaga = false sin flag", !p.isDaga());
    }

    static void testProyectilConFlagIsDagaTrue() {
        Proyectiles p = new Proyectiles(0, 0, 10, 10, 2.0, 1, 0, 5, true);
        assertTrue("isDaga = true con flag", p.isDaga());
    }

    static void testProyectilGetDano() {
        Proyectiles p = new Proyectiles(0, 0, 10, 10, 2.0, 1, 0, 15);
        assertEquals("getDano() = 15", 15, p.getDano());
    }

    static void testProyectilSeMueve() {
        // dx=1, dy=0, velocidad=2.0 → cada update avanza 2 en X
        Proyectiles p = new Proyectiles(0, 0, 10, 10, 2.0, 1, 0, 5);
        p.update(1);
        assertTrue("x = 2.0 tras un update (vel=2, dx=1)", p.getX() == 2.0);
        assertTrue("y = 0.0 sin movimiento vertical",       p.getY() == 0.0);
    }

    // ── EntidadMovible ────────────────────────────────────────────────────────

    static void testMoverActualizaPosicion() {
        TestMovible e = new TestMovible(0, 0, 10, 10, 3.0);
        e.setDx(1);
        e.setDy(1);
        e.mover();
        assertTrue("x = 3.0 tras mover (vel=3, dx=1)", e.getX() == 3.0);
        assertTrue("y = 3.0 tras mover (vel=3, dy=1)", e.getY() == 3.0);
    }

    static void testSinMovimientoNoCambiaPosicion() {
        TestMovible e = new TestMovible(50, 50, 10, 10, 2.0);
        e.mover();
        assertTrue("x sigue en 50 sin movimiento", e.getX() == 50.0);
        assertTrue("y sigue en 50 sin movimiento", e.getY() == 50.0);
    }

    static void testSetVelocidadActualiza() {
        TestMovible e = new TestMovible(0, 0, 10, 10, 1.0);
        e.setVelocidad(5.0);
        assertTrue("velocidad = 5.0 tras setVelocidad(5)", e.getVelocidad() == 5.0);
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== EntidadesTest (Proyectiles + EntidadMovible) ===");
        testProyectilSinFlagIsDagaFalse();
        testProyectilConFlagIsDagaTrue();
        testProyectilGetDano();
        testProyectilSeMueve();
        testMoverActualizaPosicion();
        testSinMovimientoNoCambiaPosicion();
        testSetVelocidadActualiza();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}