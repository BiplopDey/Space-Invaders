import java.awt.*;
import java.util.*;

public class Ship extends Elemento {

	ArrayList<Bullet> balas = new ArrayList<Bullet>();
	int balasCount;

	Ship(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		balasCount = 0;
		vidas = 4;
		puntos = 0;
	}

	@Override
	void mover(int signo) {
		x += signo * velocidad;
		limites();
	}

	void sumarPuntos(int p) {
		puntos += p;
	}

	@Override
	void pinta(Graphics g) {
		dibujarNave(g, x, y);

		for (int i = 0; i < vidas - 1; i++) {
			dibujarNave(g, width * i + i * 10 + 50, Window.ALTO - height - 10);
		}
		dibujarVida(g);
		if (!isLive) {
			g.drawImage(Window.naveExplotado, x, y, width, height, null);
		}

		for (Bullet b : balas) {
			b.pinta(g);
		}
	}

	void dibujarNave(Graphics g, int x, int y) {
		g.drawImage(Window.nave, x, y, width, height, null);
	}

	void dibujarVida(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("" + vidas, 10, Window.ALTO - 20);
	}

	void dispara() {
		Window.shoot.start();
		balas.add(new Bullet(x + (int) (width * 0.5), y, Juego.velocidadBala, 2, 20));
	}

	void moverBalas() {
		if (Window.isClickedSpace && balasCount > Juego.frecuenciaDisparoNave) {
			dispara();
			balasCount = 0;
		}
		balasCount++;
		balasCount %= 100;// para no tener numero grandes, cuando no dispara

		for (int i = 0; i < balas.size(); i++) {
			balas.get(i).mover(-1);
			if (balas.get(i).y < 30 || balas.get(i).isLive == false) {
				balas.remove(i);
			}
		}
	}

	void moverNave() {
		if (Window.isClickedLeft) {
			mover(-1);
		}
		if (Window.isClickedRight) {
			mover(+1);
		}
		moverBalas();
	}

}
