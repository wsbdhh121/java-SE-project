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
*/
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	
	Image offScreenImage = null;
	Tank myTank = new Tank(50, 50);
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}

	public void launchFrame(){
		setLocation(400, 300);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setVisible(true);
		this.setTitle("TankWar");
		setResizable(false); //不能改变大小
		this.setBackground(Color.GREEN);
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		this.addKeyListener(new KeyMonitor());
		new Thread(new PaintThread()).start();
	}

	public void paint(Graphics g) {
		 myTank.draw(g);
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
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

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}
