package Bird;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BackGround {
	BufferedImage[] images;
	BufferedImage image;
	int x, y;
	int width;
	int height;
	int index;

	public BackGround() throws Exception {
		image = ImageIO.read(getClass().getResource("images//background//background0.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 0;
		y = -768;
		images = new BufferedImage[3];
		for (int i = 0; i < images.length; i++) {
			images[i] = ImageIO.read(getClass().getResource("images//background//background" + i + ".png"));
		}
	}

	public void change(int score) {
		if (score >= 0 && score < 100) {
			this.image = images[0];
		} else if (score >= 100 & score < 200) {
			this.image = images[1];
		} else {
			this.image = images[2];
		}
	}

	public void step() {
		y += 2;

		if (y >= 0) {
			y = -768;
		}
	}

	public int getInt() {
		for (int i = 0; i < 3; i++) {
			if (image == images[i]) {
				index = i;
			}
		}
		return index + 1;
	}
}
