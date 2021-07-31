import java.awt.*;
import java.util.*;

public class Nave extends Elemento {
	
	ArrayList<Bala> balas=new ArrayList<Bala>();
	int balasCount;
	
	Nave(int x, int y, int v, int width, int height) {
		super(x, y, v, width, height);
		balasCount=0;
		vidas=4;
		puntos=0;
	}
	
	@Override
	void mover(int signo) {
		x+=signo*velocidad;
		limites();
	}
	
	void sumarPuntos(int p) {
		puntos+=p;
	}
	
	@Override
	void pinta(Graphics g) {
		dibujarNave(g,x,y);
		
		for(int i=0;i<vidas-1;i++) {
			dibujarNave(g,width*i+i*10+50,Ventana.ALTO-height-10);
		}	
		dibujarVida(g);
		if(!isLive) {
			g.drawImage(Ventana.shipExplotado, x, y, width, height, null);
		}

		for(Bala b: balas ) { 
			b.pinta(g);
		}	
	}
	
	void dibujarNave(Graphics g,int x,int y) {
		g.drawImage(Ventana.ship, x, y, width, height, null);
	}
	
	void dibujarVida(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString(""+vidas,10,Ventana.ALTO-20);	
	}
	
	void dispara() {
		Ventana.shoot.start();
		balas.add(new Bala(x+(int)(width*0.5),y,Juego.velocidadBala,2,20));
	}
	
	void moverBalas() {
		if(Ventana.isClickedSpace && balasCount>Juego.frecuenciaDisparoShip){
			dispara();
			balasCount=0;
		}
		balasCount++;
		balasCount%=100;//para no tener numero grandes, cuando no dispara

		for (int i = 0; i < balas.size(); i++) {
			balas.get(i).mover(-1);
			if(balas.get(i).y<30 || balas.get(i).isLive==false) {
				balas.remove(i);
			}
		}
	}

	void moverShip() {
		if(Ventana.isClickedLeft) {
			mover(-1);	
		}if(Ventana.isClickedRight) {
			mover(+1);
		}
		moverBalas();
	}
 
}
