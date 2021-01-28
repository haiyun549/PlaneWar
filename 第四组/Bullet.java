import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;

public class Bullet implements Serializable {
	int bX, bY;
	int bWidth = 50, bHeight = 50;
	int speed = 5;
	transient Image bimage;

	public Bullet(int x, int y) {
		super();
		bX = x;
		bY = y;
	}

	public void hit() {

	}

	public void moveToTop() {

	}

	public void moveToBottom() {

	}

	public void moveToleft() {

	}

	public void moveToRihgt() {

	}
}
