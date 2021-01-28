import java.awt.Image;
import java.io.Serializable;

public class Rock implements Serializable {
	int x, y;
	int width = 63, height = 63;
	int speed = 1;
	transient Image image;

	public Rock(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

}
