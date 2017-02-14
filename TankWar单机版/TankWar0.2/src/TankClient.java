//0.1建立一个合适的窗口
//0.2增加窗口关闭功能，使其不能改变大小，设置标题
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{

	public static void main(String[] args) {
		new TankClient().launchFrame();
	}

	public void launchFrame(){
		setLocation(400, 300);
		setSize(800, 600);
		setVisible(true);
		this.setTitle("TankWar");
		setResizable(false); //不能改变大小
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
	}
}
