package Bird;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Enemy {
	BufferedImage image;
	int x;
	int y;
	int width, height;
	int size;
	boolean state;
	ImageIcon icon;
	BufferedImage[] images;
	int index;
	Bullet b;
	Random ran = new Random();
	public Enemy() throws Exception {
		state = true;
		image = ImageIO.read(getClass().getResource("images//fighter//0.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = ran.nextInt(300)+width*2;
		
		y = ran.nextInt(1)-height+10;
		
		icon = new ImageIcon("µÐ»ú4.gif");
		
		images = new BufferedImage[16];
		for (int i = 0; i < images.length; i++) {
			images[i] = ImageIO.read(getClass().getResource("images//destroy_enemy//±¬Õ¨"+i+ ".png"));
		}
		index = 0;
	}
	public void step() {
		y+=5;
	}
	public void DrawEnemy(Graphics g, JPanel j) {
		if(state) {
		g.drawImage(image, x, y, j);
		}
		else {
			if(index < 16)
			g.drawImage(images[index], x, y, j);
			index++;
		}
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,width,height);
	}
	public boolean bump(Bullet bullet) {
		return getRect().intersects(bullet.getRect());
	}
}
