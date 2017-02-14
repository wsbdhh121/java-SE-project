import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	
	private int x;
	private int y;
	
	private static final int XSPEED = 5;
	private static final int YSPEED = 5;
	
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
	
	private void locateDirection(){
		if(bL && !bR && !bU && !bD) dir = Direction.L;
		if(!bL && bR && !bU && !bD) dir = Direction.R;
		if(!bL && !bR && bU && !bD) dir = Direction.U;
		if(!bL && !bR && !bU && bD) dir = Direction.D;
		if(bL && !bR && bU && !bD) dir = Direction.LU;
		if(bL && !bR && !bU && bD) dir = Direction.LD;
		if(!bL && bR && bU && !bD) dir = Direction.RU;
		if(!bL && bR && !bU && bD) dir = Direction.RD;
		
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);		
		move();
	}



}
