import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame{
	
	TextField txFd = new TextField();
	TextArea txAr = new TextArea();
	
	Socket s = null;
	DataOutputStream dos = null;

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
				disconnect();
				System.exit(0);
			}
			
		});
		txFd.addActionListener(new TFMonitor());
		setVisible(true);
		this.connect();
	}
	
	public void connect(){
		try {
			s = new Socket("192.168.1.114", 8888);
			dos = new DataOutputStream(s.getOutputStream());
//System.out.println("connect!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private class TFMonitor implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String str = txFd.getText().trim();
			txAr.setText(str);
			txFd.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}
