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
	Warcraft bird;												//ս����Ķ���
	BufferedImage backGround;									//������Ķ���
	BufferedImage startImage;									//��Ϸ��ʼ��ͼƬ
	BufferedImage gameOverImage;								//��Ϸ������ͼƬ
	int state;													//��Ϸ״̬
	public static final int START = 0;							//��Ϸ״̬������ʼ
	public static final int RUNNING = 1;						//��Ϸ״̬��������
	public static final int GAME_OVER = 2;						//��Ϸ״̬��������
	public boolean b;											//�жϵл��Ƿ񱻻��еı���
	public static MainForm m;									//��������Ķ���
	public BackGround background;								//������Ķ���
	public Vector<Bullet> bullet = new Vector<Bullet>();		// �洢�ҷ����ӵ�
	public Vector<Bullet> ebullet = new Vector<Bullet>();		// �洢�л����ӵ�
	public Vector<Enemy> enemy = new Vector<Enemy>();			// �洢�л�
	public int mSendId = 0;										//�ҷ��ӵ�������
	public long mSendTime = 0L;									//�洢�ӵ����ֹ����е�ʱ����
	public int eSendId = 0;										//�л����ֵ�����
	public long eSendTime = 0L;									//�洢�л����ֹ����е�ʱ����
	private long createEnemyTime = System.currentTimeMillis();	// ��ȡϵͳ��ǰʱ��
	public boolean Strengthen;									//�жϼ�ǿ�����ĵ�ҩ�Ƿ�����ı���
	final static int BULLET_POOL_COUNT = 15;					// ���ͬʱ���ڵ��ӵ���
	final static int ENEMY_POOL_COUNT = 10;						// ���ͬʱ���ڵĵл���
	final static int PLAN_TIME = 200;							// ��200ms�����ӵ�
	final static int PLAN_TIME2 = 500;							// �л���500ms�����ӵ�
	final static int PLAN_TIME3 = 800;							// ��800ms���ֵл�
	public Prop p1, p2;											//����������һ������ǿ������һ���ǻ���ǿ��
	long Time1;
	long Time2 = 0L;
	long Time3;
	long Time4 = 0L;
	public Shield s;
	public int score;											//����
	public int number;											//��ǿ�������ӵ�����
	public int hit;												//��¼���ܱ����еĴ���
	public boolean hit1;										//�жϷɻ��Ƿ񱻵л��ӵ����У������м���ʼ�ֲ�����ͼƬ
	public Skill sk;											//������Ķ���
	public boolean AllKilled;									//�жϴ����Ƿ�ʩ��

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
		
		//����������ɫ
		g.setColor(new Color(255, 255, 0));
		g.drawString("�ؿ�: " + background.getInt(), 40, 30);
		sk.DrawSkill(g, this);
		if (Strengthen) {
			g.drawString("����:���ͼ�����", 40, 90);
			g.drawString("�ӵ���: " + number, 40, 110);
		} else {
			g.drawString("����:С�ͻ���", 40, 90);
			g.drawString("�ӵ���: �� ", 40, 110);
		}
		if (s.state) {
			s.DrawShield(g, this);
			g.drawString("����ǿ�ȣ� " + String.valueOf(20 - hit), 40, 70);
		} else {
			g.drawString("�޻���", 40, 70);
		}
		p1.drawProp(g, this);//������
		p2.drawProp(g, this);//������
		bird.DrawWarcraft(g, this);//���ɻ�
		g.drawString("������ " + score, 40, 50);
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

	// ��Ϸ�Ļ�����º���
	public void action() throws Exception {
		KeyListener k = new KeyAdapter() {// �ü����¼�����������һ������������
			public void keyPressed(KeyEvent e) {
				try {
					switch (state) {
					case GAME_OVER:
						// ��Ϸ����������������
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
						// �ı�״̬
						state = START;
						break;
					case START:
						bird = new Warcraft();
						// �ı�״̬
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
		addKeyListener(k);								// ����ǰ��MainGame������Ӽ����¼�
		this.requestFocus();							// ʹ��ǰ��MainGame�����ý���
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
		this.setTitle("һ���ɻ�");
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