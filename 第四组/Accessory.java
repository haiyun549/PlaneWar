import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class Accessory implements Serializable {
	int aX, aY;
	int aWidth, aHeight;
	int speed = 1;
	int typeint;
	int life = 50;
	int Xoffset = 0;
	int intervel;
	int count = 0;
	int aimage;
	static Image aimage1, aimage2, aimage3, aimage4, aimage5, aimage6;
	//初始化，坐标、大小、随机道具
	public Accessory() {
		aX = getRandomIntNum(50, 950);
		aY = 30;
		aWidth = 32;
		aHeight = 32;
		typeint = getRandomIntNum(0, 6);
		if (typeint == 1)
			aimage = 1;
		if (typeint == 2)
			aimage = 2;
		if (typeint == 3)
			aimage = 3;
		if (typeint == 4)
			aimage = 4;
		if (typeint == 5)
			aimage = 5;
		if (typeint == 6)
			aimage = 6;
	}

	public boolean hit(Bullet b) {
		if ((aX < b.bX) && (b.bX < aX + aWidth) && (aY < b.bY) && (b.bY < aY + aHeight)) {
			life -= 20;
			return true;
		} else
			return false;

	}

	public boolean hit(Airplane p) {
		if ((aX - +aWidth < p.pX) && (p.pX < aX + aWidth) && (aY < p.pY) && (p.pY < aY + aHeight)) {
			life -= 30;
			p.life -= 30;
			return true;
		} else
			return false;

	}

	public int getRandomIntNum(int a, int b) {
		Random random = new Random();
		int c = random.nextInt();
//	这里用到了Random里的nextInt()方法，这个方法会随机产生一个 int 型的数；
		if (c < 0) {
			c = -c;
		}
		int d = ((c % (b - a)) + a + 1);
//	这里是让变量d变成a和b之中的数， % 是取余运算，请认真的读者自己算一下；
		return d;

	}
}
