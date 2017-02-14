import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {

	private static final int XSPEED = 7;
	private static final int YSPEED = 7;

	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] missileImgs = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	static{
		missileImgs = new Image[]{
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/missileRD.gif")),
		};
		
		imgs.put("L", missileImgs[0]);
		imgs.put("R", missileImgs[1]);
		imgs.put("U", missileImgs[2]);
		imgs.put("D", missileImgs[3]);
		imgs.put("LU", missileImgs[4]);
		imgs.put("LD", missileImgs[5]);
		imgs.put("RU", missileImgs[6]);
		imgs.put("RD", missileImgs[7]);
	}

	private boolean bGood;
	private boolean live = true;
	private int x, y;
	Direction dir;
	// private boolean live = true;
	private TankClient tc;

	// public boolean isLive() {
	// return live;
	// }

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

	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
		switch(dir){
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case STOP:
			break;
		}
		move();
	}

	private void move() {
		switch (dir) {
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

		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			// live = false;
			tc.missiles.remove(this);
		}
		hitWall(tc.walls);
	}

	public void hitWall(List<Wall> walls) {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (this.live && this.getRect().intersects(w.getRect())) {
				this.live = false;
			}
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void hitTank(Tank t) {
		if (this.live && this.getRect().intersects(t.getRect()) && t.live && t.isbGood() != this.bGood) {
			if(t.isbGood()){
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0) t.setLive(false);
			}else{
				t.setLive(false);
			}
			this.live = false;
			Explode ex = new Explode(x, y, this.tc);
			this.tc.explodes.add(ex);
			tc.enemyTank.remove(t);
		}
	}

	public void hitTank(List<Tank> enemyTank) {
		for (int i = 0; i < tc.enemyTank.size(); i++) {
			Tank t = enemyTank.get(i);
			if (this.live && this.getRect().intersects(t.getRect()) && t.live && t.isbGood() != this.bGood) {
				this.live = false;
				t.setLive(false);
				Explode ex = new Explode(x, y, this.tc);
				this.tc.explodes.add(ex);
				enemyTank.remove(t);
			}
		}
	}
}
