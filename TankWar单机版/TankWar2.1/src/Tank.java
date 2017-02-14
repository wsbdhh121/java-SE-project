import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	
	private int x;
	private int y;
	
	private int oldX;
	private int oldY;
	
	TankClient tc = null; // very important
	
	private boolean bGood;

	public boolean live = true;

	private static final int XSPEED = 5;
	private static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	
	
	enum Direction{
		L, R, U, D, LU, LD, RU, RD, STOP;
	}
	
	private Direction dir;
	private Direction ptdir = Direction.D;
	
	private boolean bL = false, bR = false, 
			bU = false, bD = false;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;	
		this.bGood = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc){
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;//important
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	private void move(){
		oldX = x;
		oldY = y;
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
		if(dir != Tank.Direction.STOP){
			ptdir = dir;
		}
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!bGood){
			Direction[] dirs = Direction.values();
			if(step == 0){
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40) > 38) fire();
		}
	}
	
	private void stay(){
		x = oldX;
		y = oldY;
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
		this.locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();
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
		this.locateDirection();
	}
	
	private Missile fire() {
		if(!live) return null;
		int x1 = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y1 = this.y + Tank.HEIGHT/2 - Missile.WIDTH/2;
		Missile m;
		if(this.bGood){
			m = new Missile(x1, y1, ptdir, true, this.tc);
		}else{
			m = new Missile(x1, y1, ptdir, false, this.tc);
		}
		tc.missiles.add(m);
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
		if(!live) return;
		Color c = g.getColor();
		if(bGood == true)	g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);		
		switch(ptdir){
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case STOP:
			break;
		}
		move();
	}

	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isbGood() {
		return bGood;
	}
	
	public void collidesWithWall(List<Wall> walls){
		for(int i = 0; i < walls.size(); i++){
			Wall w = walls.get(i);
			if(this.live && this.getRect().intersects(w.getRect())){
				this.stay();
			}
		}
		
	}
	
	public boolean collidesWithTanks(java.util.List<Tank> tanks){
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
}
