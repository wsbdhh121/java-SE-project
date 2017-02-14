import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	public static final int TCP_PORT = 8888;

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				System.out.println("a client connect" + s.getInetAddress() + s.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
