package sockets.test;

import sockets.PacketReadHandler;

public class SocketPacketListener {

	@PacketReadHandler
	public void onPacket(Packet0Echo packet) {

		System.out.println("Called: " + packet.text);

	}

}
