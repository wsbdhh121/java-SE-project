import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {

	public void connect(String IP, int port) {
		Socket s = null;
		try {
			s = new Socket(IP, port);
			System.out.println("connect");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
