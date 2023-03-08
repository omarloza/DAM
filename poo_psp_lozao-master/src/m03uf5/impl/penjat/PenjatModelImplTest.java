package m03uf5.impl.penjat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PenjatModelImplTest {
	PenjatModelImpl m = new PenjatModelImpl();
	private String fraseRandom = "";
	private String fraseconguiones = "";
	private int turno = 0;

	boolean enPartida;

	private final static String[] letras = new String[] { "B", "U", "E", "N", "O", "S", "D", "I", "A", "M", "G" };
	private final static String[] frase = new String[] { "_____, B_____ ____!", "_____, BU____ ____!",
			"_____, BUE___ ____!", "_____, BUEN__ ____!", "____O, BUENO_ ____!", "____O, BUENOS ___S!",
			"____O, BUENOS D__S!", "__I_O, BUENOS DÍ_S!", // Al pulsar una vocal tambein se muestran las que tiene
															// accentos
			"A_I_O, BUENOS DÍAS!", "AMI_O, BUENOS DÍAS!", "AMIGO, BUENOS DÍAS!" };

	private final static String[] letras2 = new String[] { "B", "U", "X", "E", "N", "O", "Z", "S", "D", "I", "W", "A",
			"M", "G" };
	private final static String[] frase2 = new String[] { "_____, B_____ ____!", "_____, BU____ ____!",
			"_____, BU____ ____!", "_____, BUE___ ____!", "_____, BUEN__ ____!", "____O, BUENO_ ____!",
			"____O, BUENO_ ____!", "____O, BUENOS ___S!", "____O, BUENOS D__S!", "__I_O, BUENOS DÍ_S!",
			"__I_O, BUENOS DÍ_S!", "A_I_O, BUENOS DÍAS!", "AMI_O, BUENOS DÍAS!", "AMIGO, BUENOS DÍAS!" };

	@Test
	void testJugar() {
		fraseRandom = m.jugar("AMIGO, BUENOS DÍAS!");
		assertEquals("_____, ______ ____!", fraseRandom);
		enPartida = m.isEnPartida();

	}

	@Test
	void testComprobar() { /* TEST CON FALLOS + CAMBIOS DE TURNO */

		testJugar();
		String fraseComprobada;

		for (int i = 0; i < letras2.length; i++) {
			fraseconguiones = m.comprobar(letras2[i]);

			fraseComprobada = frase2[i];

			assertEquals(fraseComprobada, fraseconguiones);

			turno = m.getTurno();
			if (turno % 2 == 0) {
				String turn = "Turno: Jugador 2";
				System.out.println(turn);

			} else {
				String turn = "Turno: Jugador 1";

				System.out.println(turn);
			}
			String resultados = m.obtenerPartidas();
			System.out.println(resultados);

		}
	}

	@Test
	void testComprobar2() { /* TEST SIN FALLOS NI CAMBIOS DE TURNO */

		testJugar();
		String fraseComprobada;

		for (int i = 0; i < letras.length; i++) {
			fraseconguiones = m.comprobar(letras[i]);

			fraseComprobada = frase[i];

			assertEquals(fraseComprobada, fraseconguiones);

			turno = m.getTurno();
			if (turno % 2 == 0) {
				String turn = "Turno: Jugador 2";
				System.out.println(turn);

			} else {
				String turn = "Turno: Jugador 1";

				System.out.println(turn);
			}
			String resultados = m.obtenerPartidas();
			System.out.println(resultados);

		}

	}
}
