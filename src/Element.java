import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Element extends Rectangle {

	int velocidad;
	int contadorExplotar;

	int puntos;
	int lives;
	Boolean isLive = true;

	Element(int x, int y, int velocidad, int width, int height) {
		super(x, y, width, height);
		this.velocidad = velocidad;

	}

	void restarVida() {
		lives--;
		if (lives == 0) {
			isLive = false;
		}
	}

	int getVidas() {
		return lives;
	}

	void setVidas(int lives) {
		this.lives = lives;
	}

	void limites() {
		if (x <= 0) {
			x = 0;
		} else if (x >= Window.ANCHO - width) {
			x = Window.ANCHO - width;
		}
	}

	abstract void mover(int sign);

	abstract void pinta(Graphics g);

}
