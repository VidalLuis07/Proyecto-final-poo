package edu.trespor2.videojuego.model.entidades.personajes;

public class ZombieTest {

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

    // ── tests ────────────────────────────────────────────────────────────────

    static void testNoEstaMuertoAlCrearse() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        assertTrue("zombie no está muerto al crearse", !z.estaMuerto());
    }

    static void testMuertoConVidaCero() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        z.recibirDano(30);
        assertTrue("zombie muerto tras recibir todo su daño", z.estaMuerto());
    }

    static void testSinInvulnerabilidad() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        z.recibirDano(5);
        z.recibirDano(5); // debe aplicarse inmediatamente
        assertEquals("vida = 20 (sin invulnerabilidad)", 20, z.getVidaActual());
    }

    static void testPerseguirMueveALaDerecha() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        Jugador j = new Jugador(200, 0, 64, 64, 1.0, 100, "carlos");
        z.perseguir(j);
        assertTrue("dx > 0 cuando jugador está a la derecha", z.getDx() > 0);
    }

    static void testPerseguirMueveALaIzquierda() {
        Zombie z = new Zombie(200, 0, 32, 32, 1.0, 30);
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        z.perseguir(j);
        assertTrue("dx < 0 cuando jugador está a la izquierda", z.getDx() < 0);
    }

    static void testPerseguirMueveHaciaAbajo() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        Jugador j = new Jugador(0, 200, 64, 64, 1.0, 100, "carlos");
        z.perseguir(j);
        assertTrue("dy > 0 cuando jugador está abajo", z.getDy() > 0);
    }

    static void testPerseguirNoCambiaXSiAlineados() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        Jugador j = new Jugador(0, 200, 64, 64, 1.0, 100, "carlos"); // misma X
        z.perseguir(j);
        assertTrue("dx = 0 cuando están alineados en X", z.getDx() == 0.0);
    }

    static void testBoundsReduceHitbox() {
        Zombie z = new Zombie(0, 0, 32, 32, 1.0, 30);
        assertTrue("hitbox ancho < ancho entidad", z.getBounds().getWidth() < z.getWidth());
        assertTrue("hitbox alto  < alto  entidad", z.getBounds().getHeight() < z.getHeight());
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== ZombieTest ===");
        testNoEstaMuertoAlCrearse();
        testMuertoConVidaCero();
        testSinInvulnerabilidad();
        testPerseguirMueveALaDerecha();
        testPerseguirMueveALaIzquierda();
        testPerseguirMueveHaciaAbajo();
        testPerseguirNoCambiaXSiAlineados();
        testBoundsReduceHitbox();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}