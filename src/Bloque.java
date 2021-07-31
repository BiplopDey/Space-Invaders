import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bloque extends Elemento {

	Bloque(int x, int y, int velocidad, int width, int height) {
		super(x, y, velocidad, width, height);
		// TODO Auto-generated constructor stub
		vidas = 3;
	}

	@Override
	void mover(int signo) {
		// TODO Auto-generated method stub
	}

	@Override
	void pinta(Graphics g) {
		// en funcion de vida detiorar
		switch (vidas) {
		case 3:
			g.setColor(Color.GREEN);
			g.fillRect(x, y, width, height);
			break;
		case 2:
			g.drawImage(Ventana.bloqueFase1, x, y, width, height, null);
			break;
		case 1:
			g.drawImage(Ventana.bloqueFase3, x, y, width, height, null);
			break;
		}

	}

}
