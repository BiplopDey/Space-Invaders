import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ovni extends Nave {
	int ovnicount;
	
	Ovni(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		contadorExplotar=0;
		ovnicount=0;
		isLive=false;
		puntos=50;
	}

	@Override
	void mover(int signo){
		if(ovnicount>Joc.frecuenciaOvni) {
			isLive=true;
			x=Finestra.AMPLE;
			ovnicount=0;
			contadorExplotar=0;
		}
		if(x+width<-10) {
			isLive=false;
		}
		if(isLive) {
			x+=signo*velocidad;
		}
		ovnicount++;
	}

	@Override
	void pinta(Graphics g) {
		if(isLive) {
			g.drawImage(Finestra.ovni, x, y, width, height, null);	
		}else if(contadorExplotar<5){
			explotar(g);
		}
	}

	void explotar(Graphics g) {
		if(contadorExplotar<5){
			g.drawImage(Finestra.explosionOvni, x, y, width, height, null);	
			contadorExplotar++;
		}
	}

}
