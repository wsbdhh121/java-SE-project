/*	
 * 1.9.1初步修改为纯净版，enum单独Direction，去掉敌方坦克
 * 1.9.2建立server和client端，初步连接
 * 1.9.3初步建立udp握手机制,并给坦克加上独一无二的id号
 * 1.9.4建立udp回路，加进第二辆坦克
 * 1.9.5加入坦克移动的东西
 * 1.9.6让坦克可以互相看见移动,加强两个窗口位置同步
 * 1.9.7让客户端自己指定udp端口
 * 1.9.8让子弹在两个客户端中可以互相看见,并可以相互对战，并让子弹消失
*/
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	List<Tank> tanks = new ArrayList<Tank>();
	Explode ex = new Explode(80, 80, this);
	
	NetClient nc = new NetClient(this);
	ConnDialog dialog = new ConnDialog();
	
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
		//nc.connect("192.168.1.114", TankServer.TCP_PORT);
	}

	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 60, 60);
		g.drawString("explodes count: " +explodes.size(), 60, 80);
		g.drawString("tank count: " + tanks.size(), 60, 100);
		 myTank.draw(g);
		 for(int i = 0; i < explodes.size(); i++){
			 Explode ex = explodes.get(i);
			 ex.draw(g);
		 }
		 for(int i = 0; i < tanks.size(); i++){
			 Tank ene = tanks.get(i);
			 ene.draw(g);
		 }
		 for(int i = 0; i < missiles.size(); i++){
			 Missile m = missiles.get(i);
//			 m.hitTank(tanks);
			 if(m.hitTank(myTank)){
				TankDeadMsg msg = new TankDeadMsg(myTank.ID);
				nc.send(msg);
				MissileDeadMsg mimsg = new MissileDeadMsg(m.id,m.tankId);
				nc.send(mimsg);
			 }
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
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_C){
				dialog.setVisible(true);
			}else{
				myTank.keyPressed(e);
			}
		}
	}
	
	class ConnDialog extends Dialog{
		Button b = new Button("确定");
		TextField tfIP = new TextField("192.168.1.114", 12);
		TextField tfPort = new TextField("" + TankServer.TCP_PORT, 4);
		TextField tfMyUDPPort = new TextField("2223", 4);
		public ConnDialog(){
			super(TankClient.this, true);
			b.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText().trim());
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, port);
					setVisible(false);
				}
				
			});
			this.setLayout(new FlowLayout());
			this.add(new Label("IP: "));
			this.add(tfIP);
			this.add(new Label("Port: "));
			this.add(tfPort);
			this.add(new Label("My UDP Port: "));
			this.add(tfMyUDPPort);
			this.add(b);
			this.pack();
			this.setLocation(300, 300);
			this.addWindowListener(new WindowAdapter(){

				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
				
			});
		}
	}
}
