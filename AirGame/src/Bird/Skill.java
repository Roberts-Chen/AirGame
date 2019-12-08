package Bird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class Skill {
	public int x;
	public int y;
	public BufferedImage image;
	public int width;
	public int height;
	public boolean state;
	public BufferedImage[] images;
	public int index;
	Random ran = new Random();
	public Skill() {
		state = false;
		images = new BufferedImage[17];
		for (int i = 0; i < images.length; i++) {
			try {
				images[i] = ImageIO.read(getClass().getResource("images//skill//"+i+ ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		index = 0;
		image = images[0];
		width = image.getWidth();
		height = image.getHeight();
	}
	public void DrawSkill(Graphics g, JPanel j) {
		if(state) {
			g.drawImage(image,ran.nextInt(400) , ran.nextInt(600) , j);
		}
	}
	public void change() {
		index++;
		image = images[index/10%17];
	}
	public void step() {
		if(y < -200) {
			state = false;
		}
		y-=10;
	}
	public void init(Warcraft w) {
		this.x = w.x;
		this.y = w.y;
	}
}
