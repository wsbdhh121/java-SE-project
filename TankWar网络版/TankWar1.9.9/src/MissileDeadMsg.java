import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileDeadMsg implements Msg {
	
	int id;
	int tankId;
	int msgType = Msg.MISSILE_DEAD_MSG;
	TankClient tc;
	
	public MissileDeadMsg(int id, int tankId){
		this.id = id;
		this.tankId = tankId;
	}
	
	public MissileDeadMsg(TankClient tc){
		this.tc = tc;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(tankId);
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
			if (tc.myTank.ID == id && tc.myTank.ID == tankId)
				return;
			
			for(int i = 0; i < tc.missiles.size(); i++){
				Missile m = tc.missiles.get(i);
				if(id == m.id && tankId == m.tankId) {
					m.setLive(false);
					tc.explodes.add(new Explode(m.x, m.y, tc));
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
