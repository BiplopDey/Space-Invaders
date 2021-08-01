import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ufo extends Element {
	int UfoCount;

	Ufo(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		explosionDuration = 0;
		UfoCount = 0;
		isLive = false;
		points = 50;
	}

	@Override
	void move(int sign) {
		if (UfoCount > Game.frecuenciaOvni) {
			isLive = true;
			x = Window.WIDTH;
			UfoCount = 0;
			explosionDuration = 0;
		}
		if (x + width < -10) {
			isLive = false;
		}
		if (isLive) {
			x += sign * speed;
		}
		UfoCount++;
	}

	@Override
	void paint(Graphics g) {
		if (isLive) {
			g.drawImage(Window.ufo, x, y, width, height, null);
		} else if (explosionDuration < 5) {
			explotar(g);
		}
	}

	void explotar(Graphics g) {
		if (explosionDuration < 5) {
			g.drawImage(Window.explosionOvni, x, y, width, height, null);
			explosionDuration++;
		}
	}

}
