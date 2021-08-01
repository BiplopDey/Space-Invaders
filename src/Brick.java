import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Brick extends Element {

	Brick(int x, int y, int speed, int width, int height) {
		super(x, y, speed, width, height);
		lives = 3;
	}

	@Override
	void move(int sign) {
		// TODO Auto-generated method stub
	}

	@Override
	void pinta(Graphics g) {
		
		switch (lives) {// depending on life deteriorate
		case 3:
			g.setColor(Color.GREEN);
			g.fillRect(x, y, width, height);
			break;
		case 2:
			g.drawImage(Window.brickPhase1, x, y, width, height, null);
			break;
		case 1:
			g.drawImage(Window.brickPhase3, x, y, width, height, null);
			break;
		}

	}

}
