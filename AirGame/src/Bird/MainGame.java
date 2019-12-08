package Bird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGame extends JPanel {
	Warcraft bird;												//战机类的对象
	BufferedImage backGround;									//背景类的对象
	BufferedImage startImage;									//游戏开始的图片
	BufferedImage gameOverImage;								//游戏结束的图片
	int state;													//游戏状态
	public static final int START = 0;							//游戏状态——开始
	public static final int RUNNING = 1;						//游戏状态——运行
	public static final int GAME_OVER = 2;						//游戏状态——结束
	public boolean b;											//判断敌机是否被击中的变量
	public static MainForm m;									//主窗口类的对象
	public BackGround background;								//背景类的对象
	public Vector<Bullet> bullet = new Vector<Bullet>();		// 存储我方的子弹
	public Vector<Bullet> ebullet = new Vector<Bullet>();		// 存储敌机的子弹
	public Vector<Enemy> enemy = new Vector<Enemy>();			// 存储敌机
	public int mSendId = 0;										//我方子弹的数量
	public long mSendTime = 0L;									//存储子弹出现过程中的时间间隔
	public int eSendId = 0;										//敌机出现的数量
	public long eSendTime = 0L;									//存储敌机出现过程中的时间间隔
	private long createEnemyTime = System.currentTimeMillis();	// 获取系统当前时间
	public boolean Strengthen;									//判断加强武器的弹药是否用完的变量
	final static int BULLET_POOL_COUNT = 15;					// 最多同时存在的子弹数
	final static int ENEMY_POOL_COUNT = 10;						// 最多同时存在的敌机数
	final static int PLAN_TIME = 200;							// 隔200ms发射子弹
	final static int PLAN_TIME2 = 500;							// 敌机隔500ms发射子弹
	final static int PLAN_TIME3 = 800;							// 隔800ms出现敌机
	public Prop p1, p2;											//两个法宝，一个武器强化，另一个是护盾强化
	long Time1;
	long Time2 = 0L;
	long Time3;
	long Time4 = 0L;
	public Shield s;
	public int score;											//分数
	public int number;											//加强武器的子弹数量
	public int hit;												//记录护盾被击中的次数
	public boolean hit1;										//判断飞机是否被敌机子弹击中，被击中即开始轮播击毁图片
	public Skill sk;											//护盾类的对象
	public boolean AllKilled;									//判断大招是否施放

	public MainGame() throws Exception {
		AllKilled = false;
		sk = new Skill();
		hit1 = false;
		hit = 0;
		score = 0;
		Strengthen = false;
		bird = new Warcraft();
		s = new Shield(bird);
		p1 = new Prop(0);
		p2 = new Prop(1);
		background = new BackGround();
		bullet.add(new Bullet(bird));
		enemy.add(new Enemy());
		ebullet.add(new Bullet());
		state = START;
		gameOverImage = ImageIO.read(getClass().getResource("images//background//begin.jpg"));
		startImage = ImageIO.read(getClass().getResource("images//background//end.jpg"));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 15);
		g.setFont(f);
		g.drawImage(background.image, background.x, background.y, null);
		for (int i = 0; i < bullet.size(); i++) {
			if (bullet.get(i).state) {
				bullet.get(i).DrawBullet(g, this);
			}
		}
		for (int i = 0; i < enemy.size(); i++) {
			enemy.get(i).DrawEnemy(g, this);
		}
		for (int i = 0; i < ebullet.size(); i++) {
			if (ebullet.get(i).state) {
				ebullet.get(i).DrawBullet(g, this);
			}
		}
		
		//设置字体颜色
		g.setColor(new Color(255, 255, 0));
		g.drawString("关卡: " + background.getInt(), 40, 30);
		sk.DrawSkill(g, this);
		if (Strengthen) {
			g.drawString("武器:大型激光炮", 40, 90);
			g.drawString("子弹数: " + number, 40, 110);
		} else {
			g.drawString("武器:小型机炮", 40, 90);
			g.drawString("子弹数: ∞ ", 40, 110);
		}
		if (s.state) {
			s.DrawShield(g, this);
			g.drawString("护盾强度： " + String.valueOf(20 - hit), 40, 70);
		} else {
			g.drawString("无护盾", 40, 70);
		}
		p1.drawProp(g, this);//画道具
		p2.drawProp(g, this);//画道具
		bird.DrawWarcraft(g, this);//画飞机
		g.drawString("分数： " + score, 40, 50);
		switch (state) {
			case GAME_OVER:
				g.drawImage(gameOverImage, 0, 0, null);
				break;
			case START:
				g.drawImage(startImage, 0, 0, null);
				break;
		}

	}

	public void Update() {
		for (int i = 0; i < ebullet.size(); i++) {
			if (ebullet.get(i).y > 768) {
				ebullet.get(i).state = false;
			}
			if (ebullet.get(i).state) {
				ebullet.get(i).step(true);
			}
		}
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).y >= 768) {
				enemy.get(i).state = false;
				enemy.remove(i);
			}
			enemy.get(i).step();
		}
		long nowTime = System.currentTimeMillis();
		if (nowTime - createEnemyTime >= PLAN_TIME3 && enemy.size() < 5) {
			Enemy tempEnemy = null;
			try {
				tempEnemy = new Enemy();
			} catch (Exception e) {
				e.printStackTrace();
			}
			enemy.add(tempEnemy);
			createEnemyTime = nowTime;
		}
		if (p1.y > 800) {
			Time1 = System.currentTimeMillis();
			if (Time1 - Time2 >= 2000) {
				p1 = new Prop(0);
				Time2 = Time1;
			}
		}
		if (p2.y > 800) {
			Time3 = System.currentTimeMillis();
			if (Time3 - Time4 > 20000) {
				p2 = new Prop(1);
				Time4 = Time1;
			}
		}
		if (eSendId < 10) {
			long now = System.currentTimeMillis();
			if (now - eSendTime >= PLAN_TIME2) {
				Bullet tempeBullet = null;
				try {
					tempeBullet = new Bullet();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < enemy.size(); i++) {
					if (enemy.get(i).state) {
						tempeBullet.init(enemy.get(i).x + 15, enemy.get(i).y);
					}
				}
				ebullet.add(tempeBullet);

				eSendTime = now;
				eSendId++;
			}
		} else {
			eSendId = 0;
		}

		for (int i = 0; i < bullet.size(); i++) {
			if (bullet.get(i).y <= -1500) {
				bullet.get(i).state = false;
				bullet.remove(i);
			}
			bullet.get(i).step();
		}
		if (mSendId < BULLET_POOL_COUNT) {
			long now = System.currentTimeMillis();
			if (now - mSendTime >= PLAN_TIME) {
				Bullet tempBullet = null;
				try {
					tempBullet = new Bullet(bird);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!Strengthen || number == 0) {
					tempBullet.init(bird.x - 15, bird.y);
				}
				if (Strengthen && number > 0) {
					tempBullet.image = tempBullet.images[0];
					tempBullet.width = tempBullet.image.getWidth();
					tempBullet.height = tempBullet.image.getHeight();
					tempBullet.init(bird.x - 50, bird.y);
					number--;
				}
				bullet.add(tempBullet);
				mSendTime = now;
				mSendId++;
			}
		} else {
			mSendId = 0;
		}
		if (bird.index >= 9) {
			state = 2;
		}
		for (int i = 0; i < ebullet.size(); i++) {
			if (bird.bump(ebullet.get(i))) {
				if (ebullet.get(i).state && !s.state) {
					bird.state = false;

				}
			}
		}
		for (int i = 0; i < enemy.size(); i++) {
			for (int j = 0; j < bullet.size(); j++) {
				if (enemy.get(i).bump(bullet.get(j)) && enemy.get(i).state) {
					enemy.get(i).state = false;
					b = !enemy.get(i).state;
					if (b) {
						score += 2;
						b = false;
						background.change(score);
					}
				}
			}
		}
		for (int i = 0; i < ebullet.size(); i++) {
			if (s.bump(ebullet.get(i)) && ebullet.get(i).state && s.state) {
				ebullet.get(i).state = false;
				hit1 = true;
			}

		}
		if (hit1) {
			s.change();
			if (s.image == s.images[0]) {
				s.n = 1;
				hit++;
				hit1 = false;
			}
		}
		if (number <= 0) {
			Strengthen = false;
		}
		if (hit >= 20) {
			s.state = false;
			hit = 0;
		}
		if (sk.state) {
			AllKilled = true;
			sk.change();
			sk.step();
		}
		if (AllKilled) {
			for (int i = 0; i < enemy.size(); i++) {
				enemy.get(i).state = false;
			}
			for (int i = 0; i < ebullet.size(); i++) {
				ebullet.get(i).state = false;
			}
			AllKilled = false;
		}
	}

	public static void main(String[] args) throws Exception {
		m = new MainForm();
	}

	// 游戏的画面更新函数
	public void action() throws Exception {
		KeyListener k = new KeyAdapter() {// 用键盘事件适配器创建一个监听器对象
			public void keyPressed(KeyEvent e) {
				try {
					switch (state) {
					case GAME_OVER:
						// 游戏结束所有内容重置
						bird = new Warcraft();
						for (int i = 0; i < enemy.size(); i++) {
							enemy.get(i).state = false;
						}
						for (int i = 0; i < bullet.size(); i++) {
							bullet.get(i).state = false;
						}
						for (int i = 0; i < ebullet.size(); i++) {
							ebullet.get(i).state = false;
						}
						score = 0;
						number = 0;
						Strengthen = false;
						// 改变状态
						state = START;
						break;
					case START:
						bird = new Warcraft();
						// 改变状态
						state = RUNNING;
					case RUNNING:
						if (e.getKeyCode() == 32) {
							if (!sk.state) {
								sk.state = true;
								sk.init(bird);
							}
						}
						switch (e.getKeyCode()) {
						case 38:
							bird.y -= 50;
							break;
						case 40:
							bird.y += 50;
							break;
						case 37:
							bird.x -= 50;
							break;
						case 39:
							bird.x += 50;
							break;
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 32) {
					s.state = false;
				}
			}
		};
		addKeyListener(k);								// 给当前的MainGame画板添加键盘事件
		this.requestFocus();							// 使当前的MainGame画板获得焦距
		while (true) {
			switch (state) {
			case START:
				bird = new Warcraft();
				s.update(bird);
				background.step();
				break;
			case RUNNING:
				background.step();
				if (bird.bump(p1)) {
					Strengthen = true;
					number = 50;
					p1.state = false;
				}
				if (bird.bump(p2)) {
					p2.state = false;
					hit = 0;
					s.state = true;
				}
				Update();
				p1.step();
				p2.step();
				s.update(bird);
				break;
			}
			repaint();
			Thread.sleep(1000 / 60);
		}
	}
}

class MainForm extends JFrame {
	public MainForm() throws Exception {
		MainGame game = new MainGame();
		this.setTitle("一起打飞机");
		this.add(game);
		this.setSize(512, 768);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.requestFocus();
		this.setVisible(true);
		game.requestFocus();
		game.action();
	}
}