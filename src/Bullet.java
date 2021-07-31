import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends Elemento {
	Bullet(int x, int y, int velocidad, int width, int height) {
		super(x, y, velocidad, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	void mover(int signo) {
		y += signo * velocidad;
	}

	@Override
	void pinta(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

}
