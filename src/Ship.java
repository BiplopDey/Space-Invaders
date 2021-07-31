import java.awt.*;
import java.util.*;

public class Ship extends Element {

	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	int bulletsCount;

	Ship(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		bulletsCount = 0;
		lives = 4;
		puntos = 0;
	}

	@Override
	void move(int sign) {
		x += sign * velocidad;
		limites();
	}

	void sumarPuntos(int p) {
		puntos += p;
	}

	@Override
	void pinta(Graphics g) {
		dibujarNave(g, x, y);

		for (int i = 0; i < lives - 1; i++) {
			dibujarNave(g, width * i + i * 10 + 50, Window.ALTO - height - 10);
		}
		dibujarVida(g);
		if (!isLive) {
			g.drawImage(Window.naveExplotado, x, y, width, height, null);
		}

		for (Bullet b : bullets) {
			b.pinta(g);
		}
	}

	void dibujarNave(Graphics g, int x, int y) {
		g.drawImage(Window.nave, x, y, width, height, null);
	}

	void dibujarVida(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("" + lives, 10, Window.ALTO - 20);
	}

	void dispara() {
		Window.shoot.start();
		bullets.add(new Bullet(x + (int) (width * 0.5), y, Game.velocidadBala, 2, 20));
	}

	void moveBalas() {
		if (Window.isClickedSpace && bulletsCount > Game.frecuenciaDisparoNave) {
			dispara();
			bulletsCount = 0;
		}
		bulletsCount++;
		bulletsCount %= 100;// para no tener numero grandes, cuando no dispara

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).move(-1);
			if (bullets.get(i).y < 30 || bullets.get(i).isLive == false) {
				bullets.remove(i);
			}
		}
	}

	void moveNave() {
		if (Window.isClickedLeft) {
			move(-1);
		}
		if (Window.isClickedRight) {
			move(+1);
		}
		moveBalas();
	}

}
