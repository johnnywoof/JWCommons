package sockets.udp.test;

import sockets.PacketRegistrator;
import sockets.test.Packet0Echo;
import sockets.test.SocketPacketListener;
import sockets.udp.UDPManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class UDPTest {

	public static void main(String[] args) {

		PacketRegistrator registrator = new PacketRegistrator();

		try {

			registrator.registerPacket(new Packet0Echo());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		SocketPacketListener packetListener = new SocketPacketListener();

		InetSocketAddress socketAddress = new InetSocketAddress("localhost", 4234);

		try {

			UDPManager manager = new UDPManager(packetListener, registrator, 1024, 4235);

			manager.writeThread.sendPacket(socketAddress, new Packet0Echo("Hi there!"));

			try {

				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			manager.stopNetworking();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);

	}

}
