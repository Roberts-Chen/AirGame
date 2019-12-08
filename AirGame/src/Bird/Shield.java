package Bird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Shield {
	public int x;
	public int y;
	public BufferedImage image;
	public int width;
	public int height;
	public boolean state;
	public BufferedImage[] images;
	public int n;
	public Shield(Warcraft w){
		n = 1;
		state = false;
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			try {
				images[i] = ImageIO.read(getClass().getResource("images//shield//"+i+ ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		image = images[0];
		width = image.getWidth();
		height = image.getHeight();
		this.x = w.x-80;
		this.y = w.y-80;
	}
	public void DrawShield(Graphics g, JPanel j) {
		g.drawImage(image, x, y, j);
	}
	public void change() {
		image = images[n];
		n = 0;
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,width,height);
	}
	public boolean bump(Bullet b) {
		return getRect().intersects(b.getRect());
	}
	public void update(Warcraft w) {
		this.x = w.x-80;
		this.y = w.y-80;
	}
}
