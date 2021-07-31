import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game {
	int speedOvni = 20;
	int speedOleadaInicial = 8;
	int speedShip = 20;
	static int speedBullet = 25;
	static int frecuenciaOvni = 160;
	static int frecuenciaDisparoShip = 6;
	static int frecuenciaDisparoOleada = 6;
	static int tiempoExplosion = 4;
	int numberUfo = 30;
	int estado = 1;
	Window window;
	Ship miShip;
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
		if (miShip.points > Window.hiScore) {
			Window.hiScore = miShip.points;
			escribirHiScore("" + miShip.points);
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
		while (miShip.isLive && wave.alienExtremoAbajo() < Window.HEIGHT - 45) {

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
		int points = miShip.points;
		int lives = miShip.lives;
		numberUfo += 50;
		inicialitzacio();
		miShip.points = points;
		miShip.setLives(lives);
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
		window.g.drawImage(Window.inicio, 0, 0, Window.WIDTH, Window.HEIGHT, null);
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
		window.g.drawImage(Window.gameOver, (int) Window.WIDTH / 4, (int) Window.HEIGHT / 4, (int) Window.WIDTH / 2,
				(int) Window.HEIGHT / 2, null);

		if (miShip.points == Window.hiScore) {// si se supera el hi-score
			window.g.setColor(Color.WHITE);
			window.g.setFont(new Font("Se rif", Font.PLAIN, 16));
			window.g.drawString("CONGRAJULATIONS NEW HIGH SCORE: " + miShip.points, (int) Window.WIDTH / 4 + 40,
					(int) Window.HEIGHT / 4 + 30);
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
		ufo = new Ufo(Window.WIDTH, 50, speedOvni, 50, 20);
		wave = new Wave(100, numberUfo, speedOleadaInicial, 40, 30);
		miShip = new Ship((int) (Window.WIDTH / 2), Window.HEIGHT - 70, speedShip, 50, 20);
		muros = new Wall(50, 400, 0, 20, 20);
	}

	void ferMoviments() {
		ufo.move(-1);
		miShip.moveShip();
		wave.move(0);
	}

	void detectarXocs() {
		// detectar chocques entre bullets de mi ship y otras cosas
		for (Bullet miBullet : miShip.bullets) {// mis bullets vs todos

			for (int j = 0; j < wave.dim; j++) {// vs aliens
				if (wave.aliens[j].isLive && miBullet.intersects(wave.aliens[j])) {
					wave.aliens[j].isLive = false;
					// wave.muertos++;
					wave.restarLife();
					miShip.sumarPuntos(wave.aliens[j].points);
					miBullet.isLive = false;
					Window.crash.start();
					break;
				}
			}

			for (Bullet alienBullet : wave.bullets) {// mis bullets vs las bullets del alien
				if (miBullet.intersects(alienBullet)) {
					miBullet.isLive = false;
					alienBullet.isLive = false;
					break;
				}
			}

			bulletsVsMuros(miBullet);

			if (ufo.isLive && miBullet.intersects(ufo)) {// contra el ufo
				miShip.sumarPuntos(ufo.points);
				miBullet.isLive = false;
				ufo.isLive = false;
			}
		}

		for (Bullet alienBullet : wave.bullets) {// bullets del alien

			bulletsVsMuros(alienBullet);

			if (alienBullet.intersects(miShip)) {
				miShip.restarLife();
				alienBullet.isLive = false;
			}

			if (alienBullet.y < Window.HEIGHT)
				for (int j = 0; j < muros.dimLinea; j++) {// vs Lineas de abajo
					if (muros.lineaAbajo[j].isLive && alienBullet.intersects(muros.lineaAbajo[j])) {
						muros.lineaAbajo[j].restarLife();
						alienBullet.isLive = false;
						break;
					}
				}
		}

	}

	void bulletsVsMuros(Bullet bullet) {
		for (int j = 0; j < muros.dim; j++) {
			if (muros.brick[j].isLive && bullet.intersects(muros.brick[j])) {
				muros.brick[j].restarLife();
				bullet.isLive = false;
				break;
			}
		}
	}

	void drawScore(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("SCORE: " + miShip.points, 10, 50);
		g.drawString("HI-SCORE: " + Window.hiScore, (int) Window.WIDTH / 2 - 50, 50);
	}

	void pintarPantalla() {
		// esborrem panatalla
		window.g.setColor(Color.BLACK);
		window.g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		// pintem
		drawScore(window.g);
		miShip.pinta(window.g);
		wave.pinta(window.g);
		ufo.pinta(window.g);
		muros.pinta(window.g);
		window.repaint();// llama a la funcion paint()
	}

}
