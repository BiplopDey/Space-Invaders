import java.awt.*;
import java.util.*;

public class Ship extends Element {

	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	int bulletsCount;

	Ship(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		bulletsCount = 0;
		lives = 4;
		points = 0;
	}

	@Override
	void move(int sign) {
		x += sign * speed;
		limits();
	}

	void sumarPuntos(int p) {
		points += p;
	}

	@Override
	void pinta(Graphics g) {
		drawShip(g, x, y);

		for (int i = 0; i < lives - 1; i++) {
			drawShip(g, width * i + i * 10 + 50, Window.HEIGHT - height - 10);
		}
		drawLife(g);
		if (!isLive) {
			g.drawImage(Window.shipExplotado, x, y, width, height, null);
		}

		for (Bullet b : bullets) {
			b.pinta(g);
		}
	}

	void drawShip(Graphics g, int x, int y) {
		g.drawImage(Window.ship, x, y, width, height, null);
	}

	void drawLife(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("" + lives, 10, Window.HEIGHT - 20);
	}

	void dispara() {
		Window.shoot.start();
		bullets.add(new Bullet(x + (int) (width * 0.5), y, Game.speedBullet, 2, 20));
	}

	void moveBullets() {
		if (Window.isClickedSpace && bulletsCount > Game.frecuenciaDisparoShip) {
			dispara();
			bulletsCount = 0;
		}
		bulletsCount++;
		bulletsCount %= 100;// not to have large numbers, when he does not shoot

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).move(-1);
			if (bullets.get(i).y < 30 || bullets.get(i).isLive == false) {
				bullets.remove(i);
			}
		}
	}

	void moveShip() {
		if (Window.isClickedLeft) {
			move(-1);
		}
		if (Window.isClickedRight) {
			move(+1);
		}
		moveBullets();
	}

}
