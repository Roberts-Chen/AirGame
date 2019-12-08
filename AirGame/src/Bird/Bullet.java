package Bird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Bullet {
	BufferedImage image;
	BufferedImage[] images;
	int x, y;
	int width, height;
	Random ran = new Random();
	Warcraft bird;
	boolean state;

	public Bullet(Warcraft bird) throws Exception {
		image = ImageIO.read(getClass().getResource("images//bullet//1.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = bird.x;
		y = bird.y;
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = ImageIO.read(getClass().getResource("images//bullet//" + i + ".png"));
		}
	}

	public Bullet() throws Exception {
		image = ImageIO.read(getClass().getResource("images//bullet//1.png"));
		width = image.getWidth();
		height = image.getHeight();
	}

	public void step() {
		y -= 10;
	}

	public void step(boolean e) {
		y += 8;
	}

	public void init(int x, int y) {
		this.x = x;
		this.y = y;
		state = true;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}

	public void DrawBullet(Graphics g, JPanel j) {
		g.drawImage(image, x, y, j);
	}
}
