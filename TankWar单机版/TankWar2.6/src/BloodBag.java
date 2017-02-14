import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BloodBag {
	int x, y, w, h;
	TankClient tc;
	private boolean live = true;
	
	int step = 0;

	private int[][] pos = { { 350, 300 }, { 350, 310 }, { 330, 305 }, { 335, 300 }, { 310, 290 }, { 320, 295 },
			{ 350, 300 }, { 300, 290 }, { 320, 300 }, { 315, 310 }, { 330, 300 } };
	
	public BloodBag(TankClient tc){
		x = pos[0][0];
		y = pos[0][0];
		w = 15;
		h = 15;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}

	private void move() {
		step++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][0];
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
