package edu.trespor2.videojuego.model.entidades.personajes;

import edu.trespor2.videojuego.model.entidades.Proyectiles;

public class JugadorTest {

    private static int passed = 0;
    private static int failed = 0;

    // ── utilidades ──────────────────────────────────────────────────────────

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

    static void testConstructorInicializaValores() {
        Jugador j = new Jugador(100, 100, 64, 64, 2.0, 100, "carlos");
        assertEquals("vidaActual inicial = 100",   100,     j.getVidaActual());
        assertEquals("vidaMaxima inicial = 100",   100,     j.getVidaMaxima());
        assertEquals("dinero inicial = 0",           0,     j.getDinero());
        assertEquals("nombreSprite = carlos",  "carlos",    j.getNombreSprite());
    }

    static void testPuedeDispararAlInicio() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        assertTrue("puedeDisparar() = true al inicio", j.puedeDisparar());
    }

    static void testDispararCreaProyectilNoNulo() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        Proyectiles p = j.disparar(1, 0);
        assertTrue("disparar() no retorna null", p != null);
        assertTrue("proyectil es daga", p.isDaga());
    }

    static void testDispararActualizaDireccionVisual() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.disparar(1, 0);
        assertTrue("ultimaDirX = 1 tras disparar a la derecha", j.getUltimaDirX() == 1.0);
        assertTrue("ultimaDirY = 0 tras disparar a la derecha", j.getUltimaDirY() == 0.0);
    }

    static void testDispararPoneEnCooldown() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.disparar(1, 0);
        assertTrue("no puede disparar inmediatamente después", !j.puedeDisparar());
    }

    static void testCooldownBajaConUpdate() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.disparar(1, 0);
        for (int i = 0; i < 40; i++) j.update(1);
        assertTrue("puede disparar tras 40 ticks", j.puedeDisparar());
    }

    static void testSumarDinero() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.sumarDinero(50);
        assertEquals("dinero = 50 tras sumar 50", 50, j.getDinero());
    }

    static void testRestarDinero() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.sumarDinero(100);
        j.restarDinero(40);
        assertEquals("dinero = 60 tras restar 40", 60, j.getDinero());
    }

    static void testAumentarVidaMaxima() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.aumentarVidaMaxima(20);
        assertEquals("vidaMaxima = 120", 120, j.getVidaMaxima());
        assertEquals("vidaActual = 120", 120, j.getVidaActual());
    }

    static void testAumentarDanoAfectaProyectil() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.aumentarDano(5);
        Proyectiles p = j.disparar(0, 1);
        assertTrue("daño del proyectil >= 15 tras aumentar 5", p.getDano() >= 15);
    }

    static void testIsAtacandoTrasDisparar() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        j.disparar(1, 0);
        assertTrue("isAtacando() = true tras disparar", j.isAtacando());
    }

    static void testBoundsReduceHitbox() {
        Jugador j = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        assertTrue("hitbox ancho < ancho entidad", j.getBounds().getWidth() < j.getWidth());
        assertTrue("hitbox alto  < alto  entidad", j.getBounds().getHeight() < j.getHeight());
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== JugadorTest ===");
        testConstructorInicializaValores();
        testPuedeDispararAlInicio();
        testDispararCreaProyectilNoNulo();
        testDispararActualizaDireccionVisual();
        testDispararPoneEnCooldown();
        testCooldownBajaConUpdate();
        testSumarDinero();
        testRestarDinero();
        testAumentarVidaMaxima();
        testAumentarDanoAfectaProyectil();
        testIsAtacandoTrasDisparar();
        testBoundsReduceHitbox();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}