import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame{
	
	TextField txFd = new TextField();
	TextArea txAr = new TextArea();

	public static void main(String[] args) {
		new ChatClient().launch();
	}
	
	public void launch(){
		setLocation(400, 300);
		setSize(300, 300);
		this.add(txFd, BorderLayout.SOUTH);
		this.add(txAr, BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		txFd.addActionListener(new TFMonitor());
		setVisible(true);
		this.connect();
	}
	
	public void connect(){
		try {
			Socket s = new Socket("192.168.1.114", 8888);
System.out.println("connect!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private class TFMonitor implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String s = txFd.getText().trim();
			txAr.setText(s);
			txFd.setText("");
		}
		
	}

}
