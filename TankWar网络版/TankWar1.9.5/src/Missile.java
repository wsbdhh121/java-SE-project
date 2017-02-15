import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;


public class Missile {

	private static final int XSPEED = 7;
	private static final int YSPEED = 7;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private boolean bGood;
	private boolean live = true;
	private int x, y;
	Direction dir;
	//private boolean live = true;
	private TankClient tc;
	
//	public boolean isLive() {
//		return live;
//	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Direction dir, boolean bGood, TankClient tc) {
		this(x, y, dir);
		this.bGood = bGood;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if(bGood)	g.setColor(Color.BLACK);
		else g.setColor(Color.PINK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
			//live = false;
			tc.missiles.remove(this);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void hitTank(Tank t){
			if(this.live && this.getRect().intersects(t.getRect()) && t.live && t.isbGood() != this.bGood){
				this.live = false;
				t.setLive(false);
				Explode ex = new Explode(x, y, this.tc);
				this.tc.explodes.add(ex);
				tc.tanks.remove(t);
			}
		}
	
	
	public void hitTank(List<Tank> enemyTank){
		for(int i = 0; i < tc.tanks.size(); i++){
			Tank t = enemyTank.get(i);
			if(this.live && this.getRect().intersects(t.getRect()) && t.live && t.isbGood() != this.bGood){
				this.live = false;
				t.setLive(false);
				Explode ex = new Explode(x, y, this.tc);
				this.tc.explodes.add(ex);
				enemyTank.remove(t);
			}
		}
	}
}
