import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
public class Bullet implements Serializable {
	int bX,bY;
	int bWidth,bHeight;
	int speed=5;
	transient Image bimage;
	public Bullet(int x,int y,int w,int h){
		super();
		  bX=x;
		  bY=y;
		  bWidth=w;
		  bHeight=h;
	}
	public void hit(){
		
	}
	public void moveToTop(){
		
	}
	public void moveToBottom(){
		
	}
	public void moveToleft(){
		
	}
	public void moveToRihgt(){
		
	}
}
