import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Alien extends Elemento{
	
	boolean tipoMovimiento=true;
	int tipo;
	static int tipo1=1,tipo2=2,tipo3=3;
	Alien(int tipo, int x, int y, int v, int width, int height) {
		super(x, y, v, width, height); 
		contadorExplotar=0;
		this.tipo=tipo;
		
		if(tipo==tipo1) puntos=10;
		if(tipo==tipo2) puntos=20;
		if(tipo==tipo3) puntos=30;
		
	}
		
	
	@Override
	void mover(int signo) {
		x+=signo*velocidad;
		tipoMovimiento=!tipoMovimiento;
	}

	void moverVertical(int dy) {
		y+=dy;
	}
	
	@Override
	void pinta(Graphics g) {
		if(isLive) {
			if(tipo==tipo1) {
				if(tipoMovimiento) {
					g.drawImage(Ventana.tipo1abierto, x, y, width, height, null);
				} else {
					g.drawImage(Ventana.tipo1cerrado, x, y, width, height, null);
				}
			}
			
			else if(tipo==tipo2) {
				if(tipoMovimiento) {
					g.drawImage(Ventana.tipo2abierto, x, y, width, height, null);
				} else {
					g.drawImage(Ventana.tipo2cerrado, x, y, width, height, null);
				}
			}
			else if(tipo==tipo3) {
				if(tipoMovimiento) {
					g.drawImage(Ventana.tipo3abierto, x, y, width, height, null);
				} else {
					g.drawImage(Ventana.tipo3cerrado, x, y, width, height, null);
				}
			}	
		} 
	}
	
	void explotar(Graphics g) {// hay que poner un contador
		if(contadorExplotar<5) {
			g.drawImage(Ventana.explosion, x, y, width, height, null);
			contadorExplotar++;
		}
	}

}
