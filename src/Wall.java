import java.awt.Graphics;

public class Wall extends Element {

	Brick[] brick;
	int filas = 3;
	int columnas = 4;
	int dim = filas * columnas * 4;
	int k = filas * columnas;
	Brick[] lineaAbajo;
	int dimLinea = 40;

	Wall(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		brick = new Brick[dim];
		for (int i = 0; i < k; i++)
			brick[i] = new Brick(x + width * (i % columnas), y + height * (i % filas), v, width, height);
		for (int i = k; i < 2 * k; i++)
			brick[i] = new Brick(x + 200 + width * (i % columnas), y + height * (i % filas), v, width, height);
		for (int i = 2 * k; i < 3 * k; i++)
			brick[i] = new Brick(x + 400 + width * (i % columnas), y + height * (i % filas), v, width, height);
		for (int i = 3 * k; i < 4 * k; i++)
			brick[i] = new Brick(x + 600 + width * (i % columnas), y + height * (i % filas), v, width, height);

		lineaAbajo = new Brick[dimLinea];
		for (int i = 0; i < dimLinea; i++)
			lineaAbajo[i] = new Brick(20 * i, Window.HEIGHT - 45, 0, 20, 5);

	}

	@Override
	void move(int sign) {

	}

	@Override
	void pinta(Graphics g) {
		for (int i = 0; i < dim; i++)
			if (brick[i].isLive)
				brick[i].pinta(g);

		for (int i = 0; i < dimLinea; i++)
			if (lineaAbajo[i].isLive)
				lineaAbajo[i].pinta(g);
	}

}
