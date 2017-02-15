import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileNewMsg implements Msg {
	TankClient tc;
	Missile m;
	int msgType = Msg.MISSILE_NEW_MSG;
	
	public MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public MissileNewMsg(Missile m){
		this.m = m;
	}
	
	
	
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(m.id);
			dos.writeInt(m.tankId);
			dos.writeInt(m.x);
			dos.writeInt(m.y);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.bGood);
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
			int tankId = dis.readInt();
			if(tankId == tc.myTank.ID) return;
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean isbGood = dis.readBoolean();
			
			Missile m = new Missile(tankId, x, y, dir, isbGood, tc);
			tc.missiles.add(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
