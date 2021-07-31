import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bala extends Nave {
	Bala(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	void mover(int signo) {
		y+=signo*v;
	}

	@Override
	void pinta(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x,y,width,height);
	}
	
}
