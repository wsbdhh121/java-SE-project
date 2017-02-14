/*	
 * 1.9.1初步修改为纯净版，enum单独Direction，去掉敌方坦克
 * 1.9.2建立server和client端，初步连接
*/
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	
	Image offScreenImage = null;
	Tank myTank = new Tank(50, 50, true, Direction.STOP, this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> enemyTank = new ArrayList<Tank>();
	Explode ex = new Explode(80, 80, this);
	
	NetClient nc = new NetClient();
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}

	public void launchFrame(){
		setLocation(400, 300);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setVisible(true);
		this.setTitle("TankWar");
		setResizable(false); //不能改变大小
		this.setBackground(Color.GRAY);
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		this.addKeyListener(new KeyMonitor());
		new Thread(new PaintThread()).start();
		nc.connect("192.168.1.114", TankServer.TCP_PORT);
	}

	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 60, 60);
		g.drawString("explodes count: " +explodes.size(), 60, 80);
		g.drawString("enemytank count: " + enemyTank.size(), 60, 100);
		 myTank.draw(g);
		 for(int i = 0; i < explodes.size(); i++){
			 Explode ex = explodes.get(i);
			 ex.draw(g);
		 }
		 for(int i = 0; i < enemyTank.size(); i++){
			 Tank ene = enemyTank.get(i);
			 ene.draw(g);
		 }
		 for(int i = 0; i < missiles.size(); i++){
			 Missile m = missiles.get(i);
			 m.hitTank(enemyTank);
			 m.hitTank(myTank);
			/* if(!m.isLive()) missiles.remove(i);
			 else */
			 m.draw(g);
		 }
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	private class PaintThread implements Runnable{

		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}
