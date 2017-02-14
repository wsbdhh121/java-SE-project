import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {

	TankClient tc;
	private static int UDP_PORT_START = 2223;
	private int udpPort;

	public NetClient(TankClient tc) {
		udpPort = UDP_PORT_START++;
		this.tc = tc;
	}

	public void connect(String IP, int port) {
		Socket s = null;
		try {
			s = new Socket(IP, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			tc.myTank.ID = dis.readInt();
			System.out.println("connect");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
