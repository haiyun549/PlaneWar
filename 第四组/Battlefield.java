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

import MultiKeyPressListener.Backgroudmusic;
import MultiKeyPressListener.Displayer;
import MultiKeyPressListener.Drawer;
import MultiKeyPressListener.Scenemusic;

class Flag {
	int f1 = 0, f2 = 0;
	public Flag() { }
	public synchronized void putf1begin() {
		while (f1 == 1)
			try { wait(); } catch (Exception e) { }
	}
	public synchronized void putf1end() {
		f1 = 1;
		notifyAll();
		
	}
	public synchronized void getf1begin() {
		while (f1 == 0)
			try { wait(); } catch (Exception e) { }
	}
	public synchronized void getf1end() {
		f1 = 0;
		notifyAll();
	}
	
	
	public synchronized void putf2begin() {
		while (f2 == 1)
			try { wait(); } catch (Exception e) { }
	}
	public synchronized void putf2end() {
		f2 = 1;
		notifyAll();
	}
	
	
	public synchronized void getf2begin() {
		while (f2 == 0)
			try { wait(); } catch (Exception e) { }
	}
	public synchronized void getf2end() {
		f2 = 0;
		notifyAll();
	}
}

public class Battlefield extends JFrame {
	int level;
	Image OffScreen1, OffScreen2, O2;
	Graphics2D drawOffScreen1, drawOffScreen2, g;
	Image myplane, eplane1, eplane2, bullet, explode, backgroud, a1, a2, a3,
			a4, a5, a6, gameoverimage, winimage, rockImage, 
			box1,box2,box3,storyline
			,floor;
	
	int key;
	Airplane Controlplane;
	
	ArrayList bulletsList;
	ArrayList planeList;
	ArrayList explodeList;
	ArrayList accessoryList;
	ArrayList acccessoryBag;
	
	
	static CopyOnWriteArrayList<Rock> rockList;
	
	TextField t1, t2, t3, t4, t5;
	Panel bag1;
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
	boolean story = true;
	int gameover = 0;
	
	boolean hasAccessory = false;
	boolean addplane = false;

	Flag flag;
	private Graphics2D g2;
	private JLabel acc1;
	private JLabel acc2;
	private JLabel acc3;
	private JLabel acc4;
	private JLabel acc5;
	private JLabel acc6;
	private LinkedList bagImage = new LinkedList();
	private ArrayList<JLabel> accList = new ArrayList();
	private Image storyImage;
	private BufferedImage OffScreenPanel1;
	private Graphics2D drawOffScreenPanel1;
	private BufferedImage OffScreenPanel2;
	private Graphics2D drawOffScreenPanel2;

	////////////////////////////����λ��
	 class storyaction implements ActionListener{
		 public void storyRead() {	
		 	}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		
				try {
					JFrame storyline = new JFrame("Story Setting......");
					storyline.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
					storyline.setSize(900, 650);
					
					Container c=storyline.getContentPane(); 
					c.add(new JLabel("Story Setting"));
					
					
					JLabel preStory = new JLabel("    This is about a story:");
					preStory.setBounds(20,50,50,25);
					preStory.setBackground(Color.black);
					c.add(preStory,"North");
					c.setBackground(Color.white);
					
					
					JLabel story = new JLabel();
					story.setIcon(new ImageIcon(storyImage));
					c.add(story);
					
					//JPanel nextpage = new JPanel();
					JButton nextbutton = new JButton("next");
					nextbutton.setBounds(400,170, 80, 200);
					nextbutton.setBackground(Color.BLUE);
					nextbutton.addActionListener(new Startaction());				
					c.add(nextbutton,"South");
					
					storyline.setVisible(true);
					
					
				}catch(Exception e){
//					continue;
				}

		
		}
	 }
	 
	class Startaction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			level=0;
//			while(level<1||level>10){
			
			try{
				JFrame frame = new JFrame("Level Choose");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //�رմ���
				level = Integer.parseInt(JOptionPane.showInputDialog(frame, "Please choose the game level��1-10"));
				goon = true;
				gameover = 0;
				start.disable();
				
				gamebegin();
			}catch(Exception e){
//					continue;
			}
			
		
			
//			}
//			goon = true;
//			gameover = 0;
//			start.disable();
//			gamebegin();

		}
	}

	//////////////////////////////////////////////////////////////////ս������
	public Battlefield() {
		setTitle("������������������������ ��ӭ�տ�����������������ҵ������ս����ǿ�棡����������������������������������������");
//		setSize( 1000, 1900 );
		Graphics2D g2;
		
		OffScreen1 = new BufferedImage(1000, 900, BufferedImage.TYPE_INT_RGB);
		OffScreenPanel1 = new BufferedImage(1000, 500, BufferedImage.TYPE_INT_RGB);
		drawOffScreen1 = (Graphics2D) OffScreen1.getGraphics();
		drawOffScreenPanel1 = (Graphics2D) OffScreenPanel1.getGraphics();

		OffScreen2 = new BufferedImage(1000, 900, BufferedImage.TYPE_INT_RGB);
		OffScreenPanel2 = new BufferedImage(1000, 500, BufferedImage.TYPE_INT_RGB);
		drawOffScreen2 = (Graphics2D) OffScreen2.getGraphics();
		drawOffScreenPanel2 = (Graphics2D) OffScreenPanel2.getGraphics();
		
		
		flag = new Flag();
		
		myplane = getToolkit().getImage("Airplanes/tank.gif");
		eplane1 = getToolkit().getImage("Airplanes/airplane3-1.gif");
		eplane2 = getToolkit().getImage("Airplanes/airplane4.gif");
		
		a1 = getToolkit().getImage("accessory/lives.gif");
		a2 = getToolkit().getImage("accessory/box1.gif");
		a3 = getToolkit().getImage("accessory/oil.gif");
		a4 = getToolkit().getImage("accessory/Invincible.gif");
		a5 = getToolkit().getImage("accessory/fireLevel.gif");
		a6 = getToolkit().getImage("accessory/cleanscreen.gif");
		box1 = getToolkit().getImage("accessory/box.png");
		storyImage = getToolkit().getImage("Backgrounds/storyline.jpeg");
		floor = getToolkit().getImage("Backgrounds/floor.jpg");
		
		rockImage = getToolkit().getImage("rock/Small_Rock_Icon.png");
		Accessory.aimage1 = a1;
		Accessory.aimage2 = a2;
		Accessory.aimage3 = a3;
		Accessory.aimage4 = a4;
		Accessory.aimage5 = a5;
		Accessory.aimage6 = a6;
		Airplane.eplane1 = eplane1;
		Airplane.eplane2 = eplane2;

		bullet = getToolkit().getImage("Bullets/Bullet2.gif");
		explode = getToolkit().getImage("Bullets/explode.gif");
		//backgroud = getToolkit().getImage("Backgrounds/sandroad.jpg");
		gameoverimage = getToolkit().getImage("accessory/gameover.gif");
		winimage = getToolkit().getImage("accessory/win.gif");

		planeList = new ArrayList();
		bulletsList = new ArrayList();
		explodeList = new ArrayList();
		accessoryList = new ArrayList();
		acccessoryBag = new  ArrayList();
		rockList = new CopyOnWriteArrayList<Rock>();
		 acc1 = new JLabel();
		 acc1.setIcon(new ImageIcon( box1));
		 acc2 = new JLabel();
		
		 acc3 = new JLabel();
		 
		 acc4 = new JLabel();
	
		 acc5 = new JLabel();
		
		 acc6= new JLabel();
		 
		  acc2.setIcon(new ImageIcon( box1));
				 
				 acc3.setIcon(new ImageIcon( box1));
				 	 acc4.setIcon(new ImageIcon( box1));
				 	  acc5.setIcon(new ImageIcon( box1));
				 	   acc6.setIcon(new ImageIcon( box1));
		
				 	

		
	}

	public void gameperpare() {
		Controlplane = new Airplane(500, 750, 80, 66);
		////////////////////////////////////////////////�ɻ��ٶ�
		p2.addKeyListener(new MultiKeyPressListener());
		bag1.addKeyListener(new MultiKeyPressListener());
		
		m2 = new Scenemusic();
	}

	public void gamebegin() {
		// ��ʼ��
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
				Controlplane.oil -= 1;
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

		//////////���Ƴ�ʼ״̬��ѡ��level�Ĺ�ϵ
		Controlplane.pX = 480;
		Controlplane.pY = 700;
		Controlplane.life = 100 * level;
		Controlplane.bulletnum = 100 * level;
		Controlplane.oil = 100 * level;
		Controlplane.speed = 15;

		switch(level){
			case 1:backgroud = getToolkit().getImage("Backgrounds/sea2.jpg");break;
			case 2:backgroud = getToolkit().getImage("Backgrounds/sandroad.jpg");break;
			case 3:backgroud = getToolkit().getImage("Backgrounds/color.jpg");break;
			case 4:backgroud = getToolkit().getImage("Backgrounds/nightsky.jpg");break;
			case 5:backgroud = getToolkit().getImage("Backgrounds/beach.jpg");break;
			case 6:backgroud = getToolkit().getImage("Backgrounds/sky.jpg");break;
			case 7:backgroud = getToolkit().getImage("Backgrounds/city.jpg");break;
			case 8:backgroud = getToolkit().getImage("Backgrounds/sea.jpg");break;
			case 9:backgroud = getToolkit().getImage("Backgrounds/universe.jpg");break;
			case 10:backgroud = getToolkit().getImage("Backgrounds/color.jpg");break;
			default:backgroud = getToolkit().getImage("Backgrounds/sea2.jpg");break;
		}

		g = (Graphics2D) this.p2.getGraphics();
		g2 = (Graphics2D) this.bag1.getGraphics();
		planeList.clear();
		bulletsList.clear();
		explodeList.clear();
		accessoryList.clear();
		for (int i = 1; i <= 8; i++) {
			Airplane p1 = new Airplane(90 * i, 50, 78, 68);
			planeList.add(p1);
			p1.intervel = p1.getRandomIntNum(0, 6);
			p1.eplane = 1;
		}
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
	public void panelContrl(Graphics2D drawOffScreenPanel) {
		drawOffScreenPanel.drawImage(floor, 0, 0, null);
		
		
		//面板的控制
		//to draw lines onto bag
				
				for (int i =0; i<bag1.getSize().width; i+=55) {
				Shape line = new Line2D.Float(i, 0, i, bag1.getSize().height);
				drawOffScreenPanel.draw(line);
				}
				drawOffScreenPanel.setPaint(Color.white);
				for (int i =0; i<bag1.getSize().height; i+=55) {
					Shape line = new Line2D.Float(0, i,- bag1.getSize().width,i);
					drawOffScreenPanel.draw(line);
					}
				
		//将道具绘制在面板上
		Iterator gnums = acccessoryBag.iterator();
		int count =0;
		
		
		while (gnums.hasNext() &count <6) {
			Accessory aa = (Accessory) gnums.next();
			//JLabel acc =new JLabel();
			Image aaIcon = null;
		
			if (aa.aimage == 1)
				aaIcon = a1;
			if (aa.aimage == 2)
				aaIcon = a2;
			if (aa.aimage == 3)
				aaIcon = a3;
			if (aa.aimage == 4)
				aaIcon = a4;
			if (aa.aimage == 5)
				aaIcon = a5;
			if (aa.aimage == 6) 
				aaIcon = a6;
			
			 //画面板的位置
			
		 //背包道具看这里	
			if(count ==0) drawOffScreenPanel.drawImage(aaIcon,10,100,null);
			if(count ==1) drawOffScreenPanel.drawImage(aaIcon,65,100,null);
			if(count ==2)  drawOffScreenPanel.drawImage(aaIcon,10,160,null);
			if(count ==3)  drawOffScreenPanel.drawImage(aaIcon,65,160,null);
			if(count ==4)  drawOffScreenPanel.drawImage(aaIcon,10,220,null);
			if(count ==5)  drawOffScreenPanel.drawImage(aaIcon,65,220,null);
			
			count++;
			}
		
	
	
	}

	public void gameContrl(Graphics2D drawOffScreen) {
		// ����

		// drawOffScreen.fillRect(0, 0, 1000, 900);
		drawOffScreen.drawImage(backgroud, 0, 0, 1000, 900, 0, (int) backy,
				360, 320 + (int) backy, null);
				
		
		

		
		for (Rock rock : rockList) {
			rock.y+=rock.speed;
			if(rock.y>900){//����rock��ʧ
				rockList.remove(rock);
			}
//			rockList.add(new Rock(new Random().nextInt(1000), 0, 100, 100));
		}
		if(rockList.size()==0){
			for(int i=0;i<4;i++){
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
		Iterator pnums = planeList.iterator();
		while (pnums.hasNext()) {
			Airplane p = (Airplane) pnums.next();
			p.fly();
			if (p.eplane == 1)
				drawOffScreen.drawImage(p.eplane1, p.pX, p.pY, null);
			if (p.eplane == 2)
				drawOffScreen.drawImage(p.eplane2, p.pX, p.pY, null);

			// �����ӵ�
			if ((p.getRandomIntNum(0, 300)) == 2) {
				Bullet b2 = new Bullet(p.pX + p.pWidth / 2 - 3, p.pY
						+ p.pHeight, 13, 13);
				b2.speed = -3;
				bulletsList.add(b2);
			}
			// �ж��Ƿ񱻻���?
			Iterator bnums = bulletsList.iterator();
			while (bnums.hasNext()) {
				Bullet b = (Bullet) bnums.next();
				if (p.hit(b)) {
					b = null;
					bnums.remove();
					m2.hitclip.play();
				}
				;
				// �ж��Ƿ�ײ�����Ʒɻ�
				if (p.hit(Controlplane))
					m2.explodeclip.play();
			}
			// �ж��Ƿ�ײ������
			Iterator anums = accessoryList.iterator();
			while (anums.hasNext()) {
				Accessory a = (Accessory) anums.next();
				if (p.hit(a)) {
					acccessoryBag.add(a);
					a = null;
					anums.remove();
					
					m2.beepclip.stop();
					m2.eatclip.play();
				}
				;
			}
			
			// ײ��ʯͷ
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
		// ����
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
			// �ж��Ƿ񱻻���?
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
		 //ײ��ʯͷ
        for(Rock rock:rockList){
   		   if(Controlplane.hit(rock)){
   			 m2.beepclip.stop();
   		   }
   	   } 

		// �ӵ�
		if (fire) {
			bulletsList.add(new Bullet(Controlplane.pX + Controlplane.pWidth
					/ 2 - 3, Controlplane.pY, 13, 13));
			for (int i = 0; i < (Controlplane.fireLevel - 1) / 2; i++) {
				bulletsList.add(new Bullet(Controlplane.pX
						- Controlplane.pWidth/2 * (i - 1) - 3, Controlplane.pY,
						13, 13));
				bulletsList.add(new Bullet(Controlplane.pX + Controlplane.pWidth/2
								* i - 3, Controlplane.pY, 13, 13));
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
			if ((Controlplane.hit(b))) { //更改状态表的数值
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
		

		
					
					
		
		
		
		
		
		
		
		if (gameover == 0) {
			drawOffScreen.drawImage(myplane, Controlplane.pX, Controlplane.pY,
					null);
			//long ratio = (Controlplane.life)/300;
			//int life = (int) (ratio*700);
			Rectangle rtg ;
			
			drawOffScreen.setColor(Color.white);
			drawOffScreen.drawRect(40,20,600,10);

			
			
			drawOffScreen.setColor(Color.RED);
			rtg = new Rectangle(40,20,Controlplane.life,10);
			drawOffScreen.fill(rtg);
			
			 
			//Rectangle fr = new Rectangle(40,20,700,10);
			//rtg.setFrame(fr);
			//drawOffScreen.draw(fr);		
			
		
			
			}
		if (gameover == -1)
			drawOffScreen.drawImage(gameoverimage, 500,
					450, null);
		if (gameover == 1)
			drawOffScreen.drawImage(winimage, 500, 450,
					null);

		// �ж��Ƿ񱻻���?
		if ((Controlplane.life < 0) || (Controlplane.oil < 0)) {
			explodeList.add(new Explode(Controlplane.pX, Controlplane.pY));
			gameover = -1;
			Controlplane.life = 0;
			Controlplane.oil = 0;
			m2.explodeclip.play();
		}
		;
		// �ж��Ƿ�ʤ��?
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
		// �洢���µļ�
		private final Set<Integer> pressed = new HashSet<Integer>();
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public synchronized void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode());
//			key = e.getKeyCode();

			//���ư���
//			if (key == KeyEvent.VK_RIGHT) {
			if (pressed.contains(KeyEvent.VK_RIGHT)) {
				if(Controlplane.pX < 915)
					Controlplane.pX += Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_LEFT)) {
				if(Controlplane.pX > 5)
					Controlplane.pX -= Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_UP)) {
				if(Controlplane.pY > -5)
					Controlplane.pY -= Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_DOWN)) {
				if(Controlplane.pY < 710)
					Controlplane.pY += Controlplane.speed;
			}
			if (pressed.contains(KeyEvent.VK_SPACE)) {
				if (Controlplane.bulletnum - Controlplane.fireLevel >= 0)
					Controlplane.bulletnum-=Controlplane.fireLevel;
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


	public static Font loadFont(String fontFileName, float fontSize){
		try
		{
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName));
			font = font.deriveFont(Font.BOLD, fontSize);
			return font;
		}
		catch(Exception e)//�쳣����
		{
			return new java.awt.Font(Font.MONOSPACED, Font.BOLD, 14);
//			return null;
		}
	}


	//ѡ���ʼ״̬
	public void showcomponent() {
		MenuBar m_MenuBar = new MenuBar();
//		Menu menuFile = new Menu("�ļ�"); // �����˵�
//		m_MenuBar.add(menuFile); // ���˵�����˵���
//		MenuItem f1 = new MenuItem("��"); // �������˵���
//		MenuItem f2 = new MenuItem("�ر�");
//		menuFile.add(f1); // ����˵�
//		menuFile.add(f2);
//		setMenuBar(m_MenuBar);
		//
		JFrame window1 = new JFrame("道具背包面板");
		window1.setBounds(0, 3, 10, 20);
		window1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		//window1.setBounds();
		bag1 = new Panel(); 
		//bag1.setFont();
		
		bag1.setBackground(Color.gray);  //背包的背景可以调整
		bag1.setBounds(new Rectangle(0,0,4000,4000));
		
		add(bag1,"West");
		bag1.setLayout(new GridLayout(5, 6));
		
		bag1.setVisible(true);
		bag1.add(new Label("背包 "),0);
		bag1.add(new Label("    道具"),0);
		
		
		//bag1.add(new Label(" 1"),1);
		//bag1.add(new Label(" 2"),1);
		//bag1.add(new Label(" 5"),1);
		bag1.add(acc1,2);
		bag1.add(acc2,3);
		bag1.add(acc3,4);
		bag1.add(acc4,5);
		bag1.add(acc5,6);
		bag1.add(acc6,7);
		
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
		start.setBackground(Color.RED);
		p1.add(start, 11);
		start.addActionListener(new storyaction());
		save = new Button("Save");
		save.setBackground(Color.GRAY);
		p1.add(save, 12);
//		save.addActionListener(new Startaction());
		save.addActionListener(new Saveaction());
		load = new Button("Load");
		load.setBackground(Color.GRAY);
		p1.add(load, 13);
//		load.addActionListener(new Startaction());
		load.addActionListener(new Loadaction());

		//
		p2 = new Panel();

		add(p2, "Center");
		
		Font font = loadFont("font/Hiragino Sans GB.ttc",13);
		if(font != null) {
			m_MenuBar.setFont(font);
			p1.setFont(font);
			bag1.setFont(font);
		}
		/*
		 * Choice ColorChooser = new Choice(); ColorChooser.add("Green");
		 * ColorChooser.add("Red"); ColorChooser.add("Blue");
		 * p.add(ColorChooser); t1 = new TextField(3); p.add(t1);
		 * ColorChooser.addItemListener(new ItemListener(){ public void
		 * itemStateChanged(ItemEvent e){ String s= e.getItem().toString();
		 * t1.setText(s);} });
		 */
	}
//
	
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
				panelContrl(drawOffScreenPanel1);
				flag.putf1end();
				flag.putf2begin();
				gameContrl(drawOffScreen2);
				panelContrl(drawOffScreenPanel2);
				flag.putf2end();

			}
		}
	}

	class Displayer extends Thread {
		public void run() {
			while (goon) {
				flag.getf1begin();
				g.drawImage(OffScreen1, 10, 10, Battlefield.this.p2);
				g2.drawImage(OffScreenPanel1, 0, 70, Battlefield.this.bag1);
				flag.getf1end();
				flag.getf2begin();
				g.drawImage(OffScreen2, 10, 10, Battlefield.this.p2);
				g2.drawImage(OffScreenPanel2, 0, 70, Battlefield.this.bag1);
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
//		                     "��ѡ���Ѷȣ�1-10"));
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

				oos = new ObjectOutputStream(new FileOutputStream(
						"save/save.dat"));
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
				ios = new ObjectInputStream(
						new FileInputStream("save/save.dat"));
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
			bag1.requestFocus();

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
