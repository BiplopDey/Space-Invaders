import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Oleada extends Elemento {
	int saltosCount;
	int intervaloSaltos = 5;
	int balasCount;
	int muertos;
	int velocidad = 1;
	int filas = 5;
	int columnas = 11;
	int dim = filas * columnas;
	int signo = +1;// la direccion inicial
	Alien[] aliens;
	ArrayList<Bala> balas = new ArrayList<Bala>();

	Random rand;

	Oleada(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		vidas = dim;
		aliens = new Alien[dim];
		muertos = 0;
		balasCount = 0;
		saltosCount = 0;
		for (int i = 0; i < dim; i++) {
			int fila = i % filas + 1;
			int tipo = 1;

			if (fila == 1)
				tipo = Alien.tipo3;
			if (fila == 2 || fila == 3)
				tipo = Alien.tipo2;
			if (fila == 4 || fila == 5)
				tipo = Alien.tipo1;

			aliens[i] = new Alien(tipo, x + (int) (width * (i % columnas) * 1.2), y + height * (i % filas) + 20, v,
					width, height);
		}

		rand = new Random();
	}

	void setDim(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;
		dim = filas * columnas;
	}

	@Override
	void mover(int dy) {// dy=0 siempre
		if (alienExtremoDerecho() >= Ventana.ANCHO - width) {
			signo = -1;
			dy = height;
		} else if (alienExtremoIzquierdo() <= 0) {
			signo = +1;
			dy = height;
		}

		if (saltosCount >= intervaloSaltos) {
			for (int i = 0; i < dim; i++) {
				aliens[i].mover(signo * velocidad);
			}

			if (dy != 0)
				for (int i = 0; i < dim; i++)
					aliens[i].moverVertical(dy);

			saltosCount = 0;
		}
		saltosCount++;

		if (vidas == 1) {
			intervaloSaltos = 0;
			velocidad = 3;
		} else if (vidas <= (int) dim * 0.2) {
			intervaloSaltos = 1;
		} else if (vidas > (int) dim * 0.2 && vidas <= (int) dim * 0.4) {
			intervaloSaltos = 2;
		} else if (vidas > (int) dim * 0.4 && vidas <= (int) dim * 0.6) {
			intervaloSaltos = 3;
		} else if (vidas > (int) dim * 0.6 && vidas <= (int) dim * 0.8) {
			intervaloSaltos = 4;
		}

		// mover balas
		moverBalas();
	}

	int alienExtremoDerecho() {// devuelve la posicion del alien vivo que esta al extremo derecho
		int max = -1;
		for (int i = 0; i < dim; i++) {
			if (aliens[i].isLive && aliens[i].x > max)
				max = aliens[i].x;
		}
		return max;
	}

	int alienExtremoIzquierdo() {
		int min = Ventana.ANCHO + 100;
		for (int i = 0; i < dim; i++) {
			if (aliens[i].isLive && aliens[i].x < min)
				min = aliens[i].x;
		}
		return min;
	}

	int alienExtremoAbajo() {
		int max = -1;
		for (int i = 0; i < dim; i++) {
			if (aliens[i].isLive && aliens[i].y > max)
				max = aliens[i].y;
		}
		return max;
	}

	@Override
	void pinta(Graphics g) {
		for (int i = 0; i < dim; i++)
			if (aliens[i].isLive)
				aliens[i].pinta(g);
			else if (aliens[i].contadorExplotar < 5)
				aliens[i].explotar(g);

		for (Bala b : balas) {
			b.pinta(g);
		}

	}

	void explotar(Graphics g, Alien alien) {
		alien.explotar(g);
	}

	void dispara() {
		ArrayList<Integer> vivo = AliensVivos(aliens);

		int i = vivo.get(rand.nextInt(vivo.size()));// devuelve de 0,...,n-1
		balas.add(new Bala(aliens[i].x + (int) (width * 0.5), aliens[i].y + height, Juego.velocidadBala, 2, 20));

	}

	ArrayList<Integer> AliensVivos(Alien[] a) {
		ArrayList<Integer> vivo = new ArrayList<Integer>();
		for (int i = 0; i < dim; i++) {
			if (a[i].isLive) {
				vivo.add(i);
			}
		}
		return vivo;
	}

	void moverBalas() {
		if (balasCount > Juego.frecuenciaDisparoOleada && isLive) {
			this.dispara();
			balasCount = 0;
		}
		balasCount++;

		for (int i = 0; i < balas.size(); i++) {
			balas.get(i).mover(+1);
			if (balas.get(i).y > Ventana.ALTO || balas.get(i).isLive == false) {
				balas.remove(i);
			}
		}

	}

}
