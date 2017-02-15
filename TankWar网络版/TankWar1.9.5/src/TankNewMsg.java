import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg implements Msg {
	Tank tank;
	int msgType = Msg.TANK_NEW_MSG;
	TankClient tc;
	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.ID);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
			dos.writeBoolean(tank.isbGood());
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
			if(tc.myTank.ID == id) return;
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean isbGood = dis.readBoolean();
			//System.out.println("id: " + id + "x: " + x + "y: " + y + "dir: " + dir + "isbGood: " + isbGood);
			Tank t = new Tank(x, y, isbGood, dir, tc);
			t.ID = id;
			tc.tanks.add(t);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
