import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Joc {
	int velocidadOvni=20;
	int velocidadOleadaInicial=8;
	int velocidadShip=20;
	static int velocidadBala=25;
	static int frecuenciaOvni=160;
	static int frecuenciaDisparoShip=6;
	static int frecuenciaDisparoOleada=6;
	static int tiempoExplosion=4;
	int oleadaYinicial=30;
	int estado=1;
	Finestra f;
	Ship miNave;
	Oleada oleada;
	Ovni ovni;
	Bloques muros;
	final int estadoMenu=1,estadoPlay=2,estadoGameOver=3;
	
	Joc(Finestra f){ 
		this.f=f;
	}
	
	void run() {
		
		while(true) {
			if(estado==estadoMenu)
				menuInicio();
			if(estado==estadoPlay)
				play();
			if(estado==estadoGameOver) {
				actualizarScore();
				gameOver();
			}
			
		}
	}
	
	
	void actualizarScore() {
		if(miNave.puntos>Finestra.hiScore) {
			Finestra.hiScore=miNave.puntos;
			escribirHiScore(""+miNave.puntos);
		}
	}
	
	void escribirHiScore(String s) {
		try {
		      FileWriter myWriter = new FileWriter(Finestra.hiScoreTxt);
		      myWriter.write(s);
		      myWriter.close();
		    } catch (IOException e) {}
	}
	
	void play() {
		inicialitzacio();
		while(miNave.isLive && oleada.alienExtremoAbajo()<Finestra.ALTO-45){
			
			if(!oleada.isLive) {// si se han matado todos los aliens de la oleada, comienza otra oleada
				otraOleada();
			}
			
			ferMoviments();
			detectarXocs();
			pintarPantalla();			
			try {
				Thread.sleep(100);
			}catch (InterruptedException e) {}
		
		}
		//game over
		estado=estadoGameOver;
	}
	void otraOleada() {
		int puntos=miNave.puntos;
		int vidas=miNave.vidas;
		oleadaYinicial+=50;
		inicialitzacio();
		miNave.puntos=puntos;
		miNave.setVidas(vidas);
		try {
			Thread.sleep(1500);
		}catch (InterruptedException e) {}
	}
	
	void pausa(int mili) {
			try {
				Thread.sleep(mili);
			}catch (InterruptedException e) {}	
		
	}
	
	void menuInicio() {
		f.g.drawImage(Finestra.inicio, 0, 0, Finestra.ANCHO, Finestra.ALTO, null);
		f.repaint();
		
		while(true) {
			if(Finestra.isClickedSpace) {
				estado=estadoPlay;
				break;
			}
			if(Finestra.isClickedRight) {
				escribirHiScore(""+0);
			}
			pausa(100);
		}
	}
	
	void gameOver() {
		f.g.drawImage(Finestra.gameOver,(int) Finestra.ANCHO/4, (int) Finestra.ALTO/4 , (int) Finestra.ANCHO/2, (int) Finestra.ALTO/2, null);
		
		if(miNave.puntos==Finestra.hiScore) {// si se supera el hi-score
			f.g.setColor(Color.WHITE);
			f.g.setFont(new Font("Se rif", Font.PLAIN, 16));
			f.g.drawString("CONGRAJULATIONS NEW HIGH SCORE: "+miNave.puntos,(int) Finestra.ANCHO/4 +40, (int) Finestra.ALTO/4 +30);
		}
		f.repaint();
		pausa(1000);
		
		while(true) { 
			if(Finestra.isClickedSpace) {
				estado = estadoPlay;
				break;
			}
			if(Finestra.isClickedRight) {//menu
				estado = estadoMenu;
				break;
			}
			pausa(100);
		}
		
	}
	
	void inicialitzacio() {	
		ovni= new Ovni(Finestra.ANCHO,50,velocidadOvni,50,20);
		oleada=new Oleada(100,oleadaYinicial,velocidadOleadaInicial,40,30);
		miNave= new Ship((int)(Finestra.ANCHO/2),Finestra.ALTO-70,velocidadShip,50,20);
		muros = new Bloques(50,400,0,20,20);
	}
	
	void ferMoviments(){
		ovni.mover(-1);
		miNave.moverShip();
		oleada.mover(0); 
	}
	

	void detectarXocs() {
		//detectar chocques entre balas de mi nave y otras cosas
		for(Bala miBala: miNave.balas) {// mis balas vs todos
			
			for(int j=0;j<oleada.dim;j++) {// vs aliens
				if(oleada.aliens[j].isLive && miBala.intersects(oleada.aliens[j])) {
					oleada.aliens[j].isLive=false;
					//oleada.muertos++;
					oleada.restarVida();
					miNave.sumarPuntos(oleada.aliens[j].puntos);
					miBala.isLive=false;
					Finestra.crash.start();
					break;
				}
			}
			
			 
			for(Bala alienBala: oleada.balas) {// mis balas vs las balas del alien
				if(miBala.intersects(alienBala)) {
					miBala.isLive=false;
					alienBala.isLive=false;
					break;
				}
			}
			
			balasVsMuros(miBala);
			
		 	if(ovni.isLive && miBala.intersects(ovni)) {// contra el ovni
				miNave.sumarPuntos(ovni.puntos);
				miBala.isLive=false;
				ovni.isLive=false;
			}
		}
		
		
		for(Bala alienBala: oleada.balas) {//balas de alien

			balasVsMuros(alienBala);
			 
			if(alienBala.intersects(miNave)) {
				miNave.restarVida();
				alienBala.isLive=false;
			}
			
			 
			if(alienBala.y<Finestra.ALTO)
				for(int j=0;j<muros.dimLinea;j++) {//vs Lineas de abajo
					if(muros.lineaAbajo[j].isLive && alienBala.intersects(muros.lineaAbajo[j])) {
						muros.lineaAbajo[j].restarVida();
						alienBala.isLive=false;
						break;
					}
				}
		}
		
	}
	
	void balasVsMuros(Bala bala) {
		for(int j=0;j<muros.dim;j++) {
			if(muros.bloques[j].isLive && bala.intersects(muros.bloques[j])) {
				muros.bloques[j].restarVida();
				bala.isLive=false;
				break;
			}
		}
	}
	
	void dibujarScore(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 26));
		g.drawString("SCORE: "+miNave.puntos,10,50);
		g.drawString("HI-SCORE: "+Finestra.hiScore,(int) Finestra.ANCHO/2-50,50);
	}
		
	void pintarPantalla(){
		// esborrem panatalla
		f.g.setColor(Color.BLACK);
		f.g.fillRect(0, 0, Finestra.ANCHO, Finestra.ALTO);
		// pintem 
		dibujarScore(f.g);
		miNave.pinta(f.g);
		oleada.pinta(f.g);
		ovni.pinta(f.g);
		muros.pinta(f.g);
		f.repaint();// llama a la funcion paint()
	}
	
	
}

