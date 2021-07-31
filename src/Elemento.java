import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Elemento extends Rectangle {

	int velocidad;
	int contadorExplotar;

	int puntos;
	int vidas;
	Boolean isLive = true;

	Elemento(int x, int y, int velocidad, int width, int height) {
		super(x, y, width, height);
		this.velocidad = velocidad;

	}

	void restarVida() {
		vidas--;
		if (vidas == 0) {
			isLive = false;
		}
	}

	int getVidas() {
		return vidas;
	}

	void setVidas(int vidas) {
		this.vidas = vidas;
	}

	void limites() {
		if (x <= 0) {
			x = 0;
		} else if (x >= Ventana.ANCHO - width) {
			x = Ventana.ANCHO - width;
		}
	}

	abstract void mover(int signo);

	abstract void pinta(Graphics g);

}
