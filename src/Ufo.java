import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ufo extends Element {
	int UfoCount;

	Ufo(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		contadorExplotar = 0;
		UfoCount = 0;
		isLive = false;
		puntos = 50;
	}

	@Override
	void mover(int signo) {
		if (UfoCount > Game.frecuenciaOvni) {
			isLive = true;
			x = Window.ANCHO;
			UfoCount = 0;
			contadorExplotar = 0;
		}
		if (x + width < -10) {
			isLive = false;
		}
		if (isLive) {
			x += signo * velocidad;
		}
		UfoCount++;
	}

	@Override
	void pinta(Graphics g) {
		if (isLive) {
			g.drawImage(Window.ufo, x, y, width, height, null);
		} else if (contadorExplotar < 5) {
			explotar(g);
		}
	}

	void explotar(Graphics g) {
		if (contadorExplotar < 5) {
			g.drawImage(Window.explosionOvni, x, y, width, height, null);
			contadorExplotar++;
		}
	}

}
