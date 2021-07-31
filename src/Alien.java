import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Alien extends Element {

	boolean typeMovimiento = true;
	int type;
	static int type1 = 1, type2 = 2, type3 = 3;

	Alien(int type, int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		contadorExplotar = 0;
		this.type = type;

		if (type == type1)
			puntos = 10;
		if (type == type2)
			puntos = 20;
		if (type == type3)
			puntos = 30;

	}

	@Override
	void mover(int sign) {
		x += sign * velocidad;
		typeMovimiento = !typeMovimiento;
	}

	void moverVertical(int dy) {
		y += dy;
	}

	@Override
	void pinta(Graphics g) {
		if (isLive) {
			if (type == type1) {
				if (typeMovimiento) {
					g.drawImage(Window.type1open, x, y, width, height, null);
				} else {
					g.drawImage(Window.type1close, x, y, width, height, null);
				}
			}

			else if (type == type2) {
				if (typeMovimiento) {
					g.drawImage(Window.type2open, x, y, width, height, null);
				} else {
					g.drawImage(Window.type2close, x, y, width, height, null);
				}
			} else if (type == type3) {
				if (typeMovimiento) {
					g.drawImage(Window.type3open, x, y, width, height, null);
				} else {
					g.drawImage(Window.type3close, x, y, width, height, null);
				}
			}
		}
	}

	void explotar(Graphics g) {// hay que poner un contador
		if (contadorExplotar < 5) {
			g.drawImage(Window.explosion, x, y, width, height, null);
			contadorExplotar++;
		}
	}

}
