
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.imageio.ImageIO;

public class Window extends Frame implements WindowListener, KeyListener {
	final int left = 37;
	final int right = 39;
	final int space = 32;
	static int hiScore;
	final String imageUrl = "assets/image/";
	final String soundUrl = "assets/sound/";
	
	static BufferedImage start, type1open, type2open, type3open, type1close, type2close, type3close,
			explosion, explosionOvni, gameOver, ufo, shipExploited, brickPhase1, brickPhase2, brickPhase3, ship;
	static Clip shoot, crash;
	static Boolean isClickedLeft = false, isClickedRight = false, isClickedSpace = false;
	static String hiScoreTxt = "assets/db/HiScore.txt";
	Game game;

	static int WIDTH = 800, HEIGHT = 600;
	Graphics g;
	Image im;// to load

	public static void main(String[] args) {
		new Window();
	}

	Window() {

		setTitle("SPACE INVADERS");
		setResizable(false);

		hiScore = getHiScoreFromTxt();
		loadSounds();
		loadImages();
		addWindowListener(this);
		addKeyListener(this);

		setSize(WIDTH, HEIGHT);
		setVisible(true);

		im = createImage(WIDTH, HEIGHT);
		g = im.getGraphics();// to paint in memory

		game = new Game(this);
		game.run();

	}

	int getHiScoreFromTxt() {
		try {
			File myObj = new File(hiScoreTxt);
			Scanner myReader = new Scanner(myObj);
			String data = myReader.nextLine();

			myReader.close();
			int i = Integer.parseInt(data);
			return i;

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return -1;
	}

	public void paint(Graphics g) {
		g.drawImage(im, 0, 0, null);
	}

	public void update(Graphics g) {// to improve graphics
		paint(g);
	}

	void loadSounds() {
		try {
			AudioInputStream audioInShoot = AudioSystem.getAudioInputStream(new File(soundUrl+"shoot.wav"));
			shoot = AudioSystem.getClip();
			shoot.open(audioInShoot);

			AudioInputStream audioInCrash = AudioSystem.getAudioInputStream(new File(soundUrl+"invaderkilled.wav"));
			crash = AudioSystem.getClip();
			crash.open(audioInCrash);

		} catch (Exception e) {
		}
	}

	void loadImages() {
		
		try {
			start = ImageIO.read(new File(imageUrl+"ImagenInicio.png"));
			ship = ImageIO.read(new File(imageUrl+"ship.png"));
			gameOver = ImageIO.read(new File(imageUrl+"gameover.png"));
			explosion = ImageIO.read(new File(imageUrl+"explosion.png"));
			explosionOvni = ImageIO.read(new File(imageUrl+"explosionOvni.png"));
			shipExploited = ImageIO.read(new File(imageUrl+"naveExplotado.png"));
			ufo = ImageIO.read(new File(imageUrl+"ovni.png"));
			type1open = ImageIO.read(new File(imageUrl+"tipo1abierto.png"));
			type2open = ImageIO.read(new File(imageUrl+"tipo2abierto.png"));
			type3open = ImageIO.read(new File(imageUrl+"tipo3abierto.png"));
			type1close = ImageIO.read(new File(imageUrl+"tipo1cerrado.png"));
			type2close = ImageIO.read(new File(imageUrl+"tipo2cerrado.png"));
			type3close = ImageIO.read(new File(imageUrl+"tipo3cerrado.png"));
			brickPhase1 = ImageIO.read(new File(imageUrl+"bloqueFase1.png"));
			brickPhase2 = ImageIO.read(new File(imageUrl+"bloqueFase2.png"));
			brickPhase3 = ImageIO.read(new File(imageUrl+"bloqueFase3.png"));
		} catch (IOException ex) {
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		if (arg0.getKeyCode() == space)
			isClickedSpace = true;
		if (arg0.getKeyCode() == left)
			isClickedLeft = true;
		if (arg0.getKeyCode() == right)
			isClickedRight = true;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == left)
			isClickedLeft = false;
		if (arg0.getKeyCode() == right)
			isClickedRight = false;
		if (arg0.getKeyCode() == space)
			isClickedSpace = false;

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
