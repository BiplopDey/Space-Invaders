import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game {
	int velocidadOvni = 20;
	int velocidadOleadaInicial = 8;
	int velocidadNave = 20;
	static int velocidadBala = 25;
	static int frecuenciaOvni = 160;
	static int frecuenciaDisparoNave = 6;
	static int frecuenciaDisparoOleada = 6;
	static int tiempoExplosion = 4;
	int numberUfo = 30;
	int estado = 1;
	Window window;
	Ship miNave;
	Wave wave;
	Ufo ufo;
	Wall muros;
	final int estadoMenu = 1, estadoPlay = 2, estadoGameOver = 3;

	Game(Window window) {
		this.window = window;
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
		while (miNave.isLive && wave.alienExtremoAbajo() < Window.ALTO - 45) {

			if (!wave.isLive) {// si se han matado todos los aliens de la wave, comienza otra wave
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
		int lives = miNave.lives;
		numberUfo += 50;
		inicialitzacio();
		miNave.puntos = puntos;
		miNave.setVidas(lives);
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
		window.g.drawImage(Window.inicio, 0, 0, Window.ANCHO, Window.ALTO, null);
		window.repaint();

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
		window.g.drawImage(Window.gameOver, (int) Window.ANCHO / 4, (int) Window.ALTO / 4, (int) Window.ANCHO / 2,
				(int) Window.ALTO / 2, null);

		if (miNave.puntos == Window.hiScore) {// si se supera el hi-score
			window.g.setColor(Color.WHITE);
			window.g.setFont(new Font("Se rif", Font.PLAIN, 16));
			window.g.drawString("CONGRAJULATIONS NEW HIGH SCORE: " + miNave.puntos, (int) Window.ANCHO / 4 + 40,
					(int) Window.ALTO / 4 + 30);
		}
		window.repaint();
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
		ufo = new Ufo(Window.ANCHO, 50, velocidadOvni, 50, 20);
		wave = new Wave(100, numberUfo, velocidadOleadaInicial, 40, 30);
		miNave = new Ship((int) (Window.ANCHO / 2), Window.ALTO - 70, velocidadNave, 50, 20);
		muros = new Wall(50, 400, 0, 20, 20);
	}

	void ferMoviments() {
		ufo.move(-1);
		miNave.moveNave();
		wave.move(0);
	}

	void detectarXocs() {
		// detectar chocques entre bullets de mi ship y otras cosas
		for (Bullet miBala : miNave.bullets) {// mis bullets vs todos

			for (int j = 0; j < wave.dim; j++) {// vs aliens
				if (wave.aliens[j].isLive && miBala.intersects(wave.aliens[j])) {
					wave.aliens[j].isLive = false;
					// wave.muertos++;
					wave.restarVida();
					miNave.sumarPuntos(wave.aliens[j].puntos);
					miBala.isLive = false;
					Window.crash.start();
					break;
				}
			}

			for (Bullet alienBala : wave.bullets) {// mis bullets vs las bullets del alien
				if (miBala.intersects(alienBala)) {
					miBala.isLive = false;
					alienBala.isLive = false;
					break;
				}
			}

			bulletsVsMuros(miBala);

			if (ufo.isLive && miBala.intersects(ufo)) {// contra el ufo
				miNave.sumarPuntos(ufo.puntos);
				miBala.isLive = false;
				ufo.isLive = false;
			}
		}

		for (Bullet alienBala : wave.bullets) {// bullets del alien

			bulletsVsMuros(alienBala);

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

	void bulletsVsMuros(Bullet bullet) {
		for (int j = 0; j < muros.dim; j++) {
			if (muros.brick[j].isLive && bullet.intersects(muros.brick[j])) {
				muros.brick[j].restarVida();
				bullet.isLive = false;
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
		window.g.setColor(Color.BLACK);
		window.g.fillRect(0, 0, Window.ANCHO, Window.ALTO);
		// pintem
		dibujarScore(window.g);
		miNave.pinta(window.g);
		wave.pinta(window.g);
		ufo.pinta(window.g);
		muros.pinta(window.g);
		window.repaint();// llama a la funcion paint()
	}

}
