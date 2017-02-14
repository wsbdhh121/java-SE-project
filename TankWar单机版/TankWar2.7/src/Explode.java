import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}


	private int x, y;
	private TankClient tc;
	private boolean live = true;
	int step = 0;
	int[] diameter = {4, 7, 10, 13, 19, 28, 35, 23, 15, 6};
	
	
	public void draw(Graphics g){
		if(!live){
			tc.explodes.remove(this);
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		if(step == diameter.length){
			live = false;
			step = 0;
			return;
		}
		g.fillOval(x, y, diameter[step], diameter[step]);
		step++;
		g.setColor(c);
	}
}
