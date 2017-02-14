import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {

	TankClient tc;
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	
	DatagramSocket ds = null;

	public NetClient(TankClient tc) {
		udpPort = UDP_PORT_START++;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.tc = tc;
	}

	public void connect(String IP, int port) {
		new Thread(new UDPRecvThread()).start();
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
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
	}

	private void send(TankNewMsg msg) {
		msg.send(ds, "192.168.1.9", TankServer.UDP_PORT);
	}
	
	private class UDPRecvThread implements Runnable{
		byte[] buf = new byte[1024];
		
		public void run() {
			while(ds != null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
					System.out.println("a packet received from server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			TankNewMsg msg = new TankNewMsg(NetClient.this.tc);
			msg.parse(dis);
		}
		
	}
}
