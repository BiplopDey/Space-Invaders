import java.awt.Graphics;


public class Bloques extends Nave {

	Bloque[] bloques;
	int filas=3;
	int columnas=4;
	int dim=filas*columnas*4;
	int k = filas*columnas;
	Bloque[] lineaAbajo;
	int dimLinea=40;
	Bloques(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		bloques= new Bloque[dim];
		for (int i = 0; i < k; i++)
			bloques[i] = new Bloque(x + width*(i%columnas), y + height*(i%filas), v, width, height);
		for (int i = k; i < 2*k; i++)
			bloques[i] = new Bloque(x +200 + width*(i%columnas), y + height*(i%filas), v, width, height);
		for (int i = 2*k; i < 3*k; i++)
			bloques[i] = new Bloque(x +400 + width*(i%columnas), y + height*(i%filas), v, width, height);
		for (int i = 3*k; i < 4*k; i++)
			bloques[i] = new Bloque(x +600 + width*(i%columnas), y + height*(i%filas), v, width, height);

		lineaAbajo=new Bloque[dimLinea];
		for(int i=0;i<dimLinea;i++)	
			lineaAbajo[i] = new Bloque( 20*i, Finestra.ALT-45, 0, 20, 5);
		
	}

	@Override
	void mover(int signo) {
		
	}

	@Override
	void pinta(Graphics g) {
		for (int i = 0; i < dim; i++)
			if(bloques[i].isLive)
				bloques[i].pinta(g);	
		
		for(int i=0;i<dimLinea;i++)
			if(lineaAbajo[i].isLive)
				lineaAbajo[i].pinta(g);
	}
	
	
}
