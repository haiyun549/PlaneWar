import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;
import java.applet.*;
import java.net.*;

import javax.swing.*;

class Flag {
	int f1 = 0, f2 = 0;

	public Flag() {
	}

	public synchronized void putf1begin() {
		while (f1 == 1)
			try {
				wait();
			} catch (Exception e) {
			}
	}

	public synchronized void putf1end() {
		f1 = 1;
		notifyAll();
	}

	public synchronized void getf1begin() {
		while (f1 == 0)
			try {
				wait();
			} catch (Exception e) {
			}
	}

	public synchronized void getf1end() {
		f1 = 0;
		notifyAll();
	}

	public synchronized void putf2begin() {
		while (f2 == 1)
			try {
				wait();
			} catch (Exception e) {
			}
	}

	public synchronized void putf2end() {
		f2 = 1;
		notifyAll();
	}

	public synchronized void getf2begin() {
		while (f2 == 0)
			try {
				wait();
			} catch (Exception e) {
			}
	}

	public synchronized void getf2end() {
		f2 = 0;
		notifyAll();
	}
}

public class Battlefield extends JFrame {
	int level;
	Image OffScreen1, OffScreen2, O2;
	Graphics2D drawOffScreen1, drawOffScreen2, g;// 画框
	Image myplane, eplane1, eplane2, bullet, explode, backgroud, a1, a2, a3, a4, a5, a6, gameoverimage, winimage,
			rockImage;// 对象
	int key;
	Airplane Controlplane;
	ArrayList bulletsList;
	ArrayList planeList;
	ArrayList explodeList;
	ArrayList accessoryList;
	static CopyOnWriteArrayList<Rock> rockList;
	TextField t1, t2, t3, t4, t5;
	Panel p1, p2;
	Button start, save, load;
	Timer timer, timer2, timer3;
	Drawer d1;
	Displayer d2;
	Backgroudmusic m1;
	Scenemusic m2;
	int delay = 1000;
	float backy = 638;
	boolean fire = false;
	boolean goon = true;
	int gameover = 0;
	boolean hasAccessory = false;
	boolean addplane = false;

	Flag flag;

	//////////////////////////// 改了位置，难度选择
	class Startaction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			level = 0;
//			while(level<1||level>10){
			try {
				JFrame frame = new JFrame("Level Choose");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口
				level = Integer.parseInt(JOptionPane.showInputDialog(frame, "Please choose the game level：1-10"));
				goon = true;
				gameover = 0;
				start.disable();
				gamebegin();
			} catch (Exception e) {
//					continue;
			}
//			}
//			goon = true;
//			gameover = 0;
//			start.disable();
//			gamebegin();

		}
	}

	////////////////////////////////////////////////////////////////// 战斗背景，初始化
	public Battlefield() {
		setTitle("～～～～～～～～～～～～ 欢迎收看我们组的面向对象作业：雷霆战机增强版！！～～～～～～～～～～～～～～～～～～～");
//		setSize( 1000, 1900 );
		OffScreen1 = new BufferedImage(1000, 900, BufferedImage.TYPE_INT_RGB);
		drawOffScreen1 = (Graphics2D) OffScreen1.getGraphics();
		OffScreen2 = new BufferedImage(1000, 900, BufferedImage.TYPE_INT_RGB);
		drawOffScreen2 = (Graphics2D) OffScreen2.getGraphics();
		flag = new Flag();
		// 图片变量 飞机、道具、石头
		myplane = getToolkit().getImage("Airplanes/tank.gif");
		eplane1 = getToolkit().getImage("Airplanes/airplane3-1.gif");
		eplane2 = getToolkit().getImage("Airplanes/airplane4.gif");
		a1 = getToolkit().getImage("accessory/lives.gif");
		a2 = getToolkit().getImage("accessory/box1.gif");
		a3 = getToolkit().getImage("accessory/oil.gif");
		a4 = getToolkit().getImage("accessory/Invincible.gif");
		a5 = getToolkit().getImage("accessory/fireLevel.gif");
		a6 = getToolkit().getImage("accessory/cleanscreen.gif");
		rockImage = getToolkit().getImage("rock/Small_Rock_Icon.png");
		// 赋值
		Accessory.aimage1 = a1;
		Accessory.aimage2 = a2;
		Accessory.aimage3 = a3;
		Accessory.aimage4 = a4;
		Accessory.aimage5 = a5;
		Accessory.aimage6 = a6;
		Airplane.eplane1 = eplane1;
		Airplane.eplane2 = eplane2;
		// 图片变量 子弹、爆炸、失败、胜利
		bullet = getToolkit().getImage("Bullets/Bullet2.gif");
		explode = getToolkit().getImage("Bullets/explode.gif");
		// backgroud = getToolkit().getImage("Backgrounds/sandroad.jpg");
		gameoverimage = getToolkit().getImage("accessory/gameover.gif");
		winimage = getToolkit().getImage("accessory/win.gif");

		planeList = new ArrayList();
		bulletsList = new ArrayList();
		explodeList = new ArrayList();
		accessoryList = new ArrayList();
		rockList = new CopyOnWriteArrayList<Rock>();
	}

	public void gameperpare() {
		Controlplane = new Airplane(500, 750, 80, 66);
		//////////////////////////////////////////////// 飞机速度
		p2.addKeyListener(new MultiKeyPressListener());
		m2 = new Scenemusic();
	}

	public void gamebegin() {
		// 音乐
		TimerTask task = new TimerTask() {
			public void run() {
				hasAccessory = true;
				m2.beepclip.loop();
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, delay);
		// 油量
		TimerTask task2 = new TimerTask() {
			public void run() {
				Controlplane.oil -= 1;
				t3.setText(Controlplane.oil + "");
			}
		};
		timer2 = new Timer();
		timer2.schedule(task2, 3000, 3000);
		// 加敌机
		TimerTask task3 = new TimerTask() {
			public void run() {
				addplane = true;
			}
		};
		timer3 = new Timer();
		timer3.schedule(task3, 2000, 40000);

		////////// 控制初始状态与选择level的关系
		Controlplane.pX = 480;
		Controlplane.pY = 700;
		Controlplane.life = 100 * level;
		Controlplane.bulletnum = 100 * level;
		Controlplane.oil = 100 * level;
		Controlplane.speed = 15;

		switch (level) {
		case 1:
			backgroud = getToolkit().getImage("Backgrounds/sea2.jpg");
			break;
		case 2:
			backgroud = getToolkit().getImage("Backgrounds/sandroad.jpg");
			break;
		case 3:
			backgroud = getToolkit().getImage("Backgrounds/color.jpg");
			break;
		case 4:
			backgroud = getToolkit().getImage("Backgrounds/nightsky.jpg");
			break;
		case 5:
			backgroud = getToolkit().getImage("Backgrounds/beach.jpg");
			break;
		case 6:
			backgroud = getToolkit().getImage("Backgrounds/sky.jpg");
			break;
		case 7:
			backgroud = getToolkit().getImage("Backgrounds/city.jpg");
			break;
		case 8:
			backgroud = getToolkit().getImage("Backgrounds/sea.jpg");
			break;
		case 9:
			backgroud = getToolkit().getImage("Backgrounds/universe.jpg");
			break;
		case 10:
			backgroud = getToolkit().getImage("Backgrounds/color.jpg");
			break;
		default:
			backgroud = getToolkit().getImage("Backgrounds/sea2.jpg");
			break;
		}

		g = (Graphics2D) this.p2.getGraphics();
		planeList.clear();
		bulletsList.clear();
		explodeList.clear();
		accessoryList.clear();
		// 加敌机
		for (int i = 1; i <= 8; i++) {
			Airplane p1 = new Airplane(90 * i, 50, 78, 68);
			planeList.add(p1);
			p1.intervel = p1.getRandomIntNum(0, 6);
			p1.eplane = 1;
		}
		// 加石头
		for (int i = 1; i <= 4; i++) {
			Rock rock = new Rock(200 * i, 0, 100, 100);
			rockList.add(rock);
		}
		p2.requestFocus();
		m1 = new Backgroudmusic();
		m1.run();
		d1 = new Drawer();
		d2 = new Displayer();
		d1.start();
		d2.start();
	}

	public void gameContrl(Graphics2D drawOffScreen) {
		// 控制

		// drawOffScreen.fillRect(0, 0, 1000, 900);
		drawOffScreen.drawImage(backgroud, 0, 0, 1000, 900, 0, (int) backy, 360, 320 + (int) backy, null);
		// 石头运动、消失
		for (Rock rock : rockList) {
			rock.y += rock.speed;
			if (rock.y > 900) {// 控制rock消失
				rockList.remove(rock);
			}
//			rockList.add(new Rock(new Random().nextInt(1000), 0, 100, 100));
		}
		if (rockList.size() == 0) {
			for (int i = 0; i < 4; i++) {
				rockList.add(new Rock(new Random().nextInt(1000), 0, 100, 100));
			}
		}
		for (Rock rock : rockList) {
			drawOffScreen.drawImage(rockImage, rock.x, rock.y, null);
		}
		backy -= .2;
		// System.out.println((int)backy+"");
		if (backy < 0)
			backy = 638;
		// drawOffScreen.drawImage(backgroud,0,0,1000,900,null);
		if (addplane) {
			if (planeList.size() < 8)
				planeList.add(new Airplane());
			addplane = false;
		}
		// 加敌机皮肤
		Iterator pnums = planeList.iterator();
		while (pnums.hasNext()) {
			Airplane p = (Airplane) pnums.next();
			p.fly();
			if (p.eplane == 1)
				drawOffScreen.drawImage(p.eplane1, p.pX, p.pY, null);
			if (p.eplane == 2)
				drawOffScreen.drawImage(p.eplane2, p.pX, p.pY, null);

			// 发射子弹
			if ((p.getRandomIntNum(0, 300)) == 2) {
				Bullet b2 = new Bullet(p.pX + p.pWidth / 2 - 3, p.pY + p.pHeight, 13, 13);
				b2.speed = -3;
				bulletsList.add(b2);
			}
			// 判断是否被击中?
			Iterator bnums = bulletsList.iterator();
			while (bnums.hasNext()) {
				Bullet b = (Bullet) bnums.next();
				if (p.hit(b)) {
					b = null;
					bnums.remove();
					m2.hitclip.play();
				}
				;
				// 判断是否撞击控制飞机
				if (p.hit(Controlplane))
					m2.explodeclip.play();
			}
			// 判断是否撞击附件
			Iterator anums = accessoryList.iterator();
			while (anums.hasNext()) {
				Accessory a = (Accessory) anums.next();
				if (p.hit(a)) {
					a = null;
					anums.remove();
					m2.beepclip.stop();
					m2.eatclip.play();
				}
				;
			}

			// 撞到石头
			for (Rock rock : rockList) {
				if (Controlplane.hit(rock)) {
					m2.beepclip.stop();
				}
			}

			if (p.life < 0) {
				explodeList.add(new Explode(p.pX, p.pY));
				p = null;
				pnums.remove();
				m2.explodeclip.play();
			}
			;
		}
		// 附件
		if (hasAccessory) {
			accessoryList.add(new Accessory());
			hasAccessory = false;

		}
		Iterator anums = accessoryList.iterator();
		while (anums.hasNext()) {
			Accessory a = (Accessory) anums.next();
			if (a.aimage == 1)
				drawOffScreen.drawImage(a1, a.aX, a.aY, null);
			if (a.aimage == 2)
				drawOffScreen.drawImage(a2, a.aX, a.aY, null);
			if (a.aimage == 3)
				drawOffScreen.drawImage(a3, a.aX, a.aY, null);
			if (a.aimage == 4)
				drawOffScreen.drawImage(a4, a.aX, a.aY, null);
			if (a.aimage == 5)
				drawOffScreen.drawImage(a5, a.aX, a.aY, null);
			if (a.aimage == 6) {
				drawOffScreen.drawImage(a6, a.aX, a.aY, null);
			}
			a.aY += a.speed;
			if (a.aY > 900) {
				a = null;
				anums.remove();
				m2.beepclip.stop();
				continue;
				// t2.setText(Controlplane.life+"");
			}
			;
			if (Controlplane.hit(a)) {
				a = null;
				anums.remove();
				m2.beepclip.stop();
				m2.eatclip.play();
				t1.setText(Controlplane.bulletnum + "");
				t2.setText(Controlplane.life + "");
				t3.setText(Controlplane.oil + "");
				t4.setText(Controlplane.speed + "");
				t5.setText(Controlplane.pHeight + "*" + Controlplane.pWidth);
				continue;
				// t2.setText(Controlplane.life+"");
			}
			;
			// 判断是否被击中?
			Iterator bnums = bulletsList.iterator();
			while (bnums.hasNext()) {
				Bullet b = (Bullet) bnums.next();
				if (a.hit(b)) {
					b = null;
					bnums.remove();
					m2.hitclip.play();
				}
				;
			}
			if (a.life < 0) {
				explodeList.add(new Explode(a.aX, a.aY));
				a = null;
				m2.beepclip.stop();
				anums.remove();
				m2.explodeclip.play();
			}
			;
		}
		// 撞到石头
		for (Rock rock : rockList) {
			if (Controlplane.hit(rock)) {
				m2.beepclip.stop();
			}
		}

		// 子弹
		if (fire) {
			//加子弹
			bulletsList.add(new Bullet(Controlplane.pX + Controlplane.pWidth / 2 - 3, Controlplane.pY, 13, 13));
			//加额外子弹
			for (int i = 0; i < (Controlplane.fireLevel - 1) / 2; i++) {
				bulletsList.add(
						new Bullet(Controlplane.pX - Controlplane.pWidth / 2 * (i - 1) - 3, Controlplane.pY, 13, 13));
				bulletsList.add(new Bullet(Controlplane.pX + Controlplane.pWidth / 2 * i - 3, Controlplane.pY, 13, 13));
			}
			fire = false;
			t1.setText(Controlplane.bulletnum + "");
		}

		Iterator bnums = bulletsList.iterator();
		while (bnums.hasNext()) {
			Bullet b = (Bullet) bnums.next();
			drawOffScreen.drawImage(bullet, b.bX, b.bY, null);
			b.bY -= b.speed;
			if ((b.bY < 0) || (b.bY > 900)) {
				b = null;
				bnums.remove();
				continue;
			}
			if ((Controlplane.hit(b))) {
				b = null;
				bnums.remove();
				m2.hitclip.play();
				t1.setText(Controlplane.bulletnum + "");
				t2.setText(Controlplane.life + "");
				t3.setText(Controlplane.oil + "");
				t4.setText(Controlplane.speed + "");
				t5.setText(Controlplane.pHeight + "*" + Controlplane.pWidth);
			}
			;
		}
		if (gameover == 0)
			drawOffScreen.drawImage(myplane, Controlplane.pX, Controlplane.pY, null);
		if (gameover == -1)
			drawOffScreen.drawImage(gameoverimage, 500, 450, null);
		if (gameover == 1)
			drawOffScreen.drawImage(winimage, 500, 450, null);

		// 判断是否被击中?
		if ((Controlplane.life < 0) || (Controlplane.oil < 0)) {
			explodeList.add(new Explode(Controlplane.pX, Controlplane.pY));
			gameover = -1;
			Controlplane.life = 0;
			Controlplane.oil = 0;
			m2.explodeclip.play();
		}
		;
		// 判断是否胜利?
		if (planeList.size() == 0)
			gameover = 1;
		//
		if ((explodeList.size() == 0) && (gameover != 0)) {
			goon = false;
		}

		Iterator enums = explodeList.iterator();
		while (enums.hasNext()) {
			Explode e = (Explode) enums.next();
			drawOffScreen.drawImage(explode, e.eX, e.eY, null);
			e.life--;

			if (e.life < 0) {
				e = null;
				enums.remove();
			}
			;
		}
		// g.drawImage(OffScreen1,0,0,this.p2);
	}

	class MultiKeyPressListener implements KeyListener {
		// 存储按下的键
		private final Set<Integer> pressed = new HashSet<Integer>();

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public synchronized void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode());
//			key = e.getKeyCode();

			// 控制按键
//			if (key == KeyEvent.VK_RIGHT) {
			if (pressed.contains(KeyEvent.VK_RIGHT)) {
				if (Controlplane.pX < 1000 - Controlplane.pWidth)
					Controlplane.pX += Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_LEFT)) {
				if (Controlplane.pX > 0)
					Controlplane.pX -= Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_UP)) {
				if (Controlplane.pY > 0)
					Controlplane.pY -= Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_DOWN)) {
				if (Controlplane.pY < 900 - Controlplane.pHeight)
					Controlplane.pY += Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_SPACE)) {
				if (Controlplane.bulletnum - Controlplane.fireLevel >= 0)
					Controlplane.bulletnum -= Controlplane.fireLevel;
					fire = true;
				m2.gunshotclip.play();
			}
			if (pressed.contains(KeyEvent.VK_1)) {
				if (Controlplane.speed < 50) {
					Controlplane.speed += 10;
					t4.setText(Controlplane.speed + "");
				}
			}
			if (pressed.contains(KeyEvent.VK_2)) {
				if (Controlplane.speed > 10) {
					Controlplane.speed -= 10;
					t4.setText(Controlplane.speed + "");
				}
			}
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			pressed.remove(e.getKeyCode());
		}

//		public void keyReleased(KeyEvent e) {
//		}
	}

	public static Font loadFont(String fontFileName, float fontSize) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName));
			font = font.deriveFont(Font.BOLD, fontSize);
			return font;
		} catch (Exception e)// 异常处理
		{
			return new java.awt.Font(Font.MONOSPACED, Font.BOLD, 14);
//			return null;
		}
	}

	// 选择初始状态
	public void showcomponent() {
		MenuBar m_MenuBar = new MenuBar();
//		Menu menuFile = new Menu("文件"); // 创建菜单
//		m_MenuBar.add(menuFile); // 将菜单加入菜单条
//		MenuItem f1 = new MenuItem("打开"); // 创建各菜单项
//		MenuItem f2 = new MenuItem("关闭");
//		menuFile.add(f1); // 加入菜单
//		menuFile.add(f2);
//		setMenuBar(m_MenuBar);
		//
		p1 = new Panel();
		add(p1, "North");
		p1.setLayout(new GridLayout(1, 10));

		p1.add(new Label("  Bullet"), 0);
		t1 = new TextField(3);
		p1.add(t1, 1);
		p1.add(new Label("  Health"), 2);
		t2 = new TextField(3);
		p1.add(t2, 3);
		p1.add(new Label("    Oil"), 4);
		t3 = new TextField(3);
		p1.add(t3, 5);
		p1.add(new Label("   Speed"), 6);
		t4 = new TextField(3);
		p1.add(t4, 7);
		p1.add(new Label("  Volumn"), 8);
		t5 = new TextField(3);
		p1.add(t5, 9);
		p1.add(new Label(""), 10);
		start = new Button("Start");
		p1.add(start, 11);
		start.addActionListener(new Startaction());
		save = new Button("Save");
		p1.add(save, 12);
//		save.addActionListener(new Startaction());
		save.addActionListener(new Saveaction());
		load = new Button("Load");
		p1.add(load, 13);
//		load.addActionListener(new Startaction());
		load.addActionListener(new Loadaction());

		//
		p2 = new Panel();

		add(p2, "Center");
		Font font = loadFont("font/girl.ttc", 15);
		if (font != null) {
			m_MenuBar.setFont(font);
			p1.setFont(font);
		}
		/*
		 * Choice ColorChooser = new Choice(); ColorChooser.add("Green");
		 * ColorChooser.add("Red"); ColorChooser.add("Blue"); p.add(ColorChooser); t1 =
		 * new TextField(3); p.add(t1); ColorChooser.addItemListener(new ItemListener(){
		 * public void itemStateChanged(ItemEvent e){ String s= e.getItem().toString();
		 * t1.setText(s);} });
		 */
	}

	public static void main(String args[]) {
		Battlefield f = new Battlefield();
		f.showcomponent();
		f.setSize(1000, 900);
		f.setVisible(true);
		f.gameperpare();
		// f.gamebegin();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class Drawer extends Thread {
		public void run() {
			while (goon) {
				flag.putf1begin();
				gameContrl(drawOffScreen1);
				flag.putf1end();
				flag.putf2begin();
				gameContrl(drawOffScreen2);
				flag.putf2end();

			}
		}
	}

	class Displayer extends Thread {
		public void run() {
			while (goon) {
				flag.getf1begin();
				g.drawImage(OffScreen1, 0, 0, Battlefield.this.p2);
				flag.getf1end();
				flag.getf2begin();
				g.drawImage(OffScreen2, 0, 0, Battlefield.this.p2);
				flag.getf2end();
			}
//			System.out.println("Game Over");
			timer.cancel();
			timer = null;
			timer2.cancel();
			timer2 = null;
			m2.beepclip.stop();
			m1.clip.stop();
			m1 = null;
			start.enable();

		}
	}

//	class Startaction implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			level=0;
//			while(level<1||level>10){
//				try{
//					level = Integer.parseInt(JOptionPane.showInputDialog( this,
//		                     "请选择难度：1-10"));
//				}catch(Exception e){
//					continue;
//				}
//			}
//			goon = true;
//			gameover = 0;
//			start.disable();
//			gamebegin();
//
//		}
//	}

	class Saveaction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			d1.suspend();
			d2.suspend();
			ObjectOutputStream oos;
			try {
				File f = new File("save/save.dat");
				if (f.exists())
					f.delete();

				oos = new ObjectOutputStream(new FileOutputStream("save/save.dat"));
				oos.writeObject(Controlplane);
				oos.writeObject(planeList);
				oos.writeObject(bulletsList);
				oos.writeObject(accessoryList);
				oos.writeObject(explodeList);
				oos.writeObject(rockList);
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			d1.resume();
			d2.resume();
		}
	}

	class Loadaction implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			ObjectInputStream ios;

			try {
				ios = new ObjectInputStream(new FileInputStream("save/save.dat"));
				Controlplane = (Airplane) ios.readObject();
				planeList = (ArrayList) ios.readObject();
				bulletsList = (ArrayList) ios.readObject();
				accessoryList = (ArrayList) ios.readObject();
				explodeList = (ArrayList) ios.readObject();
				rockList = (CopyOnWriteArrayList<Rock>) ios.readObject();
				ios.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TimerTask task = new TimerTask() {
				public void run() {
					hasAccessory = true;
					m2.beepclip.loop();
				}
			};
			timer = new Timer();
			timer.schedule(task, 0, delay);

			TimerTask task2 = new TimerTask() {
				public void run() {
					Controlplane.oil -= 5;
					t3.setText(Controlplane.oil + "");
				}
			};
			timer2 = new Timer();
			timer2.schedule(task2, 3000, 3000);
			TimerTask task3 = new TimerTask() {
				public void run() {
					addplane = true;
				}
			};
			timer3 = new Timer();
			timer3.schedule(task3, 2000, 40000);
			goon = true;
			gameover = 0;
			p2.requestFocus();

			d1 = new Drawer();
			d2 = new Displayer();
			d1.start();
			d2.start();
			m1 = new Backgroudmusic();
			m1.run();

		}
	}

	class Backgroudmusic {
		AudioClip clip;

		public void run() {
			File backmusic = new File("music/Tobu - Seven.mid");
			try {
				clip = Applet.newAudioClip(backmusic.toURL());
				clip.loop();
			} catch (Exception e) {
			}
			;
		}
	}

	class Scenemusic {
		File gunshot, explode, beep, hit, eat;
		AudioClip gunshotclip, explodeclip, beepclip, hitclip, eatclip;

		public Scenemusic() {
			super();
			gunshot = new File("music/gunshot.wav");
			explode = new File("music/explode.wav");
			beep = new File("music/beep.wav");
			hit = new File("music/hit.wav");
			eat = new File("music/eat.wav");
			try {
				gunshotclip = Applet.newAudioClip(gunshot.toURL());
				explodeclip = Applet.newAudioClip(explode.toURL());
				beepclip = Applet.newAudioClip(beep.toURL());
				hitclip = Applet.newAudioClip(hit.toURL());
				eatclip = Applet.newAudioClip(eat.toURL());

			} catch (Exception e) {
			}
			;
		}
		/*
		 * public void run() { while (true) { if (gunshot_voice>0)
		 * {gunshotclip.play();gunshot_voice--;}; if (explode_voice>0)
		 * {explodeclip.play();explode_voice--;}; if (accessory_voice>0)
		 * {beepclip.play(); accessory_voice--;}; } }
		 */
	}
}
