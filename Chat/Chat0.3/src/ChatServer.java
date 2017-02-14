import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
public class ChatServer {

	public static void main(String[] args) {
		boolean bStarted = false;
		ServerSocket ss = null;
		Socket s = null;
		DataInputStream dis = null;
		try {
			ss = new ServerSocket(8888);
		}catch(BindException e){
			System.out.println("server has been used");
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			bStarted = true;
			while(bStarted){
				boolean bConnected = false;
				s = ss.accept();
				System.out.println("a client has been connected!");
				bConnected = true;
				dis = new DataInputStream(s.getInputStream());
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
				}
				//dis.close();
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
