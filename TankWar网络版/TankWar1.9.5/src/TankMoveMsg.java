import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg {
	int id;
	Direction dir;
	int msgType = Msg.TANK_MOVE_MSG;
	TankClient tc;

	public TankMoveMsg(int id, Direction dir) {
		this.id = id;
		this.dir = dir;
	}

	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (tc.myTank.ID == id)
				return;
			Direction dir = Direction.values()[dis.readInt()];
			
			boolean exit = false;
			for(int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(id == t.ID) {
					t.dir = dir;
					exit = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
