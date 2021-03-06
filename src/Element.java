import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Element extends Rectangle {

	int speed;
	int explosionDuration;

	int points;
	int lives;
	Boolean isLive = true;

	Element(int x, int y, int speed, int width, int height) {
		super(x, y, width, height);
		this.speed = speed;

	}

	void substractLife() {
		lives--;
		if (lives == 0) {
			isLive = false;
		}
	}

	int getLives() {
		return lives;
	}

	void setLives(int lives) {
		this.lives = lives;
	}

	void limits() {
		if (x <= 0) {
			x = 0;
		} else if (x >= Window.WIDTH - width) {
			x = Window.WIDTH - width;
		}
	}

	abstract void move(int sign);

	abstract void paint(Graphics g);

}
