import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	
	private int x;
	private int y;
	
	TankClient tc = null; // very important
	
	private static final int XSPEED = 5;
	private static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	
	enum Direction{
		L, R, U, D, LU, LD, RU, RD, STOP;
	}
	
	private Direction dir = Direction.STOP;
	
	private boolean bL = false, bR = false, 
			bU = false, bD = false;

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;	
	}
	
	public Tank(int x, int y, TankClient tc){
		this(x, y);
		this.tc = tc;//important
	}
	
	private void move(){
		this.locateDirection();
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
	}
	
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			tc.m = fire();//important
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
	}
	
	private Missile fire() {
		int x1 = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y1 = this.y + Tank.HEIGHT/2 - Missile.WIDTH/2;
		Missile m = new Missile(x1, y1, dir);
		return m;
	}

	private void locateDirection(){
		if(bL && !bR && !bU && !bD) dir = Direction.L;
		if(!bL && bR && !bU && !bD) dir = Direction.R;
		if(!bL && !bR && bU && !bD) dir = Direction.U;
		if(!bL && !bR && !bU && bD) dir = Direction.D;
		if(bL && !bR && bU && !bD) dir = Direction.LU;
		if(bL && !bR && !bU && bD) dir = Direction.LD;
		if(!bL && bR && bU && !bD) dir = Direction.RU;
		if(!bL && bR && !bU && bD) dir = Direction.RD;
		if(!bL && !bR && !bU && !bD) dir = Direction.STOP;
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);		
		move();
		
	}

	



}
