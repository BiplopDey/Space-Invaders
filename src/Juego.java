import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Juego {
	int velocidadOvni = 20;
	int velocidadOleadaInicial = 8;
	int velocidadNave = 20;
	static int velocidadBala = 25;
	static int frecuenciaOvni = 160;
	static int frecuenciaDisparoNave = 6;
	static int frecuenciaDisparoOleada = 6;
	static int tiempoExplosion = 4;
	int oleadaYinicial = 30;
	int estado = 1;
	Window ventana;
	Nave miNave;
	Oleada oleada;
	Ovni ovni;
	Wall muros;
	final int estadoMenu = 1, estadoPlay = 2, estadoGameOver = 3;

	Juego(Window ventana) {
		this.ventana = ventana;
	}

	void run() {

		while (true) {
			if (estado == estadoMenu)
				menuInicio();
			if (estado == estadoPlay)
				play();
			if (estado == estadoGameOver) {
				actualizarScore();
				gameOver();
			}

		}
	}

	void actualizarScore() {
		if (miNave.puntos > Window.hiScore) {
			Window.hiScore = miNave.puntos;
			escribirHiScore("" + miNave.puntos);
		}
	}

	void escribirHiScore(String s) {
		try {
			FileWriter myWriter = new FileWriter(Window.hiScoreTxt);
			myWriter.write(s);
			myWriter.close();
		} catch (IOException e) {
		}
	}

	void play() {
		inicialitzacio();
		while (miNave.isLive && oleada.alienExtremoAbajo() < Window.ALTO - 45) {

			if (!oleada.isLive) {// si se han matado todos los aliens de la oleada, comienza otra oleada
				otraOleada();
			}

			ferMoviments();
			detectarXocs();
			pintarPantalla();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

		}
		// game over
		estado = estadoGameOver;
	}

	void otraOleada() {
		int puntos = miNave.puntos;
		int vidas = miNave.vidas;
		oleadaYinicial += 50;
		inicialitzacio();
		miNave.puntos = puntos;
		miNave.setVidas(vidas);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
		}
	}

	void pausa(int mili) {
		try {
			Thread.sleep(mili);
		} catch (InterruptedException e) {
		}

	}

	void menuInicio() {
		ventana.g.drawImage(Window.inicio, 0, 0, Window.ANCHO, Window.ALTO, null);
		ventana.repaint();

		while (true) {
			if (Window.isClickedSpace) {
				estado = estadoPlay;
				break;
			}
			if (Window.isClickedRight) {
				escribirHiScore("" + 0);
			}
			pausa(100);
		}
	}

	void gameOver() {
		ventana.g.drawImage(Window.gameOver, (int) Window.ANCHO / 4, (int) Window.ALTO / 4, (int) Window.ANCHO / 2,
				(int) Window.ALTO / 2, null);

		if (miNave.puntos == Window.hiScore) {// si se supera el hi-score
			ventana.g.setColor(Color.WHITE);
			ventana.g.setFont(new Font("Se rif", Font.PLAIN, 16));
			ventana.g.drawString("CONGRAJULATIONS NEW HIGH SCORE: " + miNave.puntos, (int) Window.ANCHO / 4 + 40,
					(int) Window.ALTO / 4 + 30);
		}
		ventana.repaint();
		pausa(1000);

		while (true) {
			if (Window.isClickedSpace) {
				estado = estadoPlay;
				break;
			}
			if (Window.isClickedRight) {// menu
				estado = estadoMenu;
				break;
			}
			pausa(100);
		}

	}

	void inicialitzacio() {
		ovni = new Ovni(Window.ANCHO, 50, velocidadOvni, 50, 20);
		oleada = new Oleada(100, oleadaYinicial, velocidadOleadaInicial, 40, 30);
		miNave = new Nave((int) (Window.ANCHO / 2), Window.ALTO - 70, velocidadNave, 50, 20);
		muros = new Wall(50, 400, 0, 20, 20);
	}

	void ferMoviments() {
		ovni.mover(-1);
		miNave.moverNave();
		oleada.mover(0);
	}

	void detectarXocs() {
		// detectar chocques entre balas de mi nave y otras cosas
		for (Bullet miBala : miNave.balas) {// mis balas vs todos

			for (int j = 0; j < oleada.dim; j++) {// vs aliens
				if (oleada.aliens[j].isLive && miBala.intersects(oleada.aliens[j])) {
					oleada.aliens[j].isLive = false;
					// oleada.muertos++;
					oleada.restarVida();
					miNave.sumarPuntos(oleada.aliens[j].puntos);
					miBala.isLive = false;
					Window.crash.start();
					break;
				}
			}

			for (Bullet alienBala : oleada.balas) {// mis balas vs las balas del alien
				if (miBala.intersects(alienBala)) {
					miBala.isLive = false;
					alienBala.isLive = false;
					break;
				}
			}

			balasVsMuros(miBala);

			if (ovni.isLive && miBala.intersects(ovni)) {// contra el ovni
				miNave.sumarPuntos(ovni.puntos);
				miBala.isLive = false;
				ovni.isLive = false;
			}
		}

		for (Bullet alienBala : oleada.balas) {// balas del alien

			balasVsMuros(alienBala);

			if (alienBala.intersects(miNave)) {
				miNave.restarVida();
				alienBala.isLive = false;
			}

			if (alienBala.y < Window.ALTO)
				for (int j = 0; j < muros.dimLinea; j++) {// vs Lineas de abajo
					if (muros.lineaAbajo[j].isLive && alienBala.intersects(muros.lineaAbajo[j])) {
						muros.lineaAbajo[j].restarVida();
						alienBala.isLive = false;
						break;
					}
				}
		}

	}

	void balasVsMuros(Bullet bala) {
		for (int j = 0; j < muros.dim; j++) {
			if (muros.brick[j].isLive && bala.intersects(muros.brick[j])) {
				muros.brick[j].restarVida();
				bala.isLive = false;
				break;
			}
		}
	}

	void dibujarScore(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("SCORE: " + miNave.puntos, 10, 50);
		g.drawString("HI-SCORE: " + Window.hiScore, (int) Window.ANCHO / 2 - 50, 50);
	}

	void pintarPantalla() {
		// esborrem panatalla
		ventana.g.setColor(Color.BLACK);
		ventana.g.fillRect(0, 0, Window.ANCHO, Window.ALTO);
		// pintem
		dibujarScore(ventana.g);
		miNave.pinta(ventana.g);
		oleada.pinta(ventana.g);
		ovni.pinta(ventana.g);
		muros.pinta(ventana.g);
		ventana.repaint();// llama a la funcion paint()
	}

}
