/*	
 * 0.1建立一个合适的窗口
 * 0.2增加窗口关闭功能，使其不能改变大小，设置标题
 * 0.3画出坦克，重写paint
 * 0.4将位置改为实例变量，启动线程不断重画
 * 0.41创建双缓冲消除闪屏现象（虽然好像并没有出现闪屏）
 * 0.5代码重构，把容易多处改变的量定义为常量
 * 0.6加入KeyMonitor，使坦克能够根据键盘上下左右移动
 * 0.7将坦克包装成单独的类
 * 0.8让坦克能向八个方向走
 * 0.9修复0.8移动的bug
 * 1.0新增子弹类，增加发射子弹功能，待修改完善
 * 1.1根据主战坦克的方向和位置，引进发炮功能，应用了面向对象中持有对象类的引用
 * 1.2增添炮筒方向，修复子弹发射遗留bug
 * 1.3将子弹添加入ArrayList，增加发射多枚子弹功能,并解决子弹出界问题
 * 1.4修复坦克出界问题
 * 1.5加入敌方坦克，引入bGood布尔类型量判断好坏
 * 1.6将坦克击毙，引用Rectangle碰撞检测
 * 1.7模拟坦克爆炸，并定位爆炸
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
	Tank myTank = new Tank(50, 50, true, this);
	Tank enemyTank = new Tank(100, 100, false, this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	
	Explode ex = new Explode(80, 80, this);
	
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
	}

	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 60, 60);
		g.drawString("explodes count: " +explodes.size(), 60, 80);
		 myTank.draw(g);
		 enemyTank.draw(g);
		 for(int i = 0; i < explodes.size(); i++){
			 Explode ex = explodes.get(i);
			 ex.draw(g);
		 }
		 for(int i = 0; i < missiles.size(); i++){
			 Missile m = missiles.get(i);
			 m.hitTank(enemyTank);
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
