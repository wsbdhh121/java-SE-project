import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
public class ChatServer {
	boolean bStarted = false;
	ServerSocket ss = null;
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public void start(){
		try {
			ss = new ServerSocket(8888);
			bStarted = true;
		}catch(BindException e){
			System.out.println("server has been used");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			while(bStarted){
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("a client has been connected!");
				new Thread(c).start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private boolean bConnected = false;
		public Client(Socket s){
			this.s = s;
			try{
				dis = new DataInputStream(s.getInputStream());
				bConnected = true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
				}
			}catch(EOFException e){
				System.out.println("client closed1");
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
				try{
					if(dis != null)  dis.close();
					if(s != null) s.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
		
	}
}
