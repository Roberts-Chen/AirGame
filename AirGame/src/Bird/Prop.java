package Bird;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Prop {
	public int x;
	public int y;
	public int width;
	public int height;
	BufferedImage[] images;
	BufferedImage image;
	public boolean state;
	Random ran = new Random();
	public Prop(int n) {
		state = true;
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			try {
				images[i] = ImageIO.read(getClass().getResource("images//prop//"+i+ ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		image = images[n];
		width = image.getWidth();
		height = image.getHeight();
		x = ran.nextInt(512) - width;
		y = - ran.nextInt(50);
	}
	public void step() {
		y += 5;
	}

	public void drawProp(Graphics g,JPanel j) {
		if(state) {
			g.drawImage(image, x, y, j);
		}
	}
	public Rectangle getRect() {
		return new Rectangle(this.x,this.y,this.width,this.height);
	}
}
