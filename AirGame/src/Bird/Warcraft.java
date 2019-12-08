package Bird;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

//鸟类
public class Warcraft {

	BufferedImage image;
	int x;
	int y;
	int width, height;
	boolean state;
	// 图片的数组，是鸟的动画帧
	BufferedImage[] images;
	int index;

	public Warcraft() throws Exception {
		state = true;
		image = ImageIO.read(getClass().getResource("images//fighter//0.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 256;
		y = 600;
		images = new BufferedImage[9];
		for (int i = 0; i < images.length; i++) {
			images[i] = ImageIO.read(getClass().getResource("images//destroy_fighter//t"+i+ ".png"));
		}
		index = 0;
	}
	public Rectangle getRect() {
		return new Rectangle(x - width/2, y - height/2, width, height);
	}
	public boolean bump(Bullet bullet) {
		return getRect().intersects(bullet.getRect());
	}
	public boolean bump(Prop p) {
		return getRect().intersects(p.getRect());
	}
	public void DrawWarcraft(Graphics g, JPanel j) {
		if(state) {
		g.drawImage(image, x-width/2, y - height/2, j);
		}
		else {
			if(index < 9) {
			g.drawImage(images[index], x-width/2 - 20, y - height/2 - 20, j);
			index++;
			}
		}
	}
}
