import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
public class ChatServer {
	boolean bStarted = false;
	ServerSocket ss = null;
	ArrayList<Client> clients = new ArrayList<Client>();
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
				clients.add(c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(ss != null) ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis = null;	
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		public Client(Socket s){
			this.s = s;
			try{
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public void send(String str){
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
					for(int i = 0; i < clients.size(); i++){
						Client c = clients.get(i);
						c.send(str);
					}
				}
			}catch(EOFException e){
				System.out.println("client closed");
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
				try{
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) s.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
		
	}
}
