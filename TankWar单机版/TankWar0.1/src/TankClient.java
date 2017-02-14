//0.1建立一个合适的窗口
import java.awt.*;

public class TankClient extends Frame{

	public static void main(String[] args) {
		new TankClient().launchFrame();
	}

	public void launchFrame(){
		setLocation(400, 300);
		setSize(800, 600);
		setVisible(true);
	}
}
