import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
public class ChatServer {

	public static void main(String[] args) {
		boolean bStarted = false;
		try {
			ServerSocket ss = new ServerSocket(8888);
			bStarted = true;
			while(bStarted){
				boolean bConnected = false;
				Socket s = ss.accept();
				System.out.println("a client has been connected!");
				bConnected = true;
				DataInputStream dis = new DataInputStream(s.getInputStream());
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
				}
				dis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
