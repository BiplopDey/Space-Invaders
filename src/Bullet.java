import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends Element {
	
	Bullet(int x, int y, int speed, int width, int height) {
		super(x, y, speed, width, height);
	}

	@Override
	void move(int sign) {
		y += sign * speed;
	}

	@Override
	void pinta(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

}
