package sockets.test;

public class SocketPacketListener {

	public void onPacket(Packet0Echo packet) {

		System.out.println("Called: " + packet.text);

	}

}
