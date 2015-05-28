package sockets.test;

import sockets.PacketRegistrator;
import sockets.SocketManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SocketTest {

	public static void main(String[] args) {

		PacketRegistrator registrator = new PacketRegistrator();

		try {

			registrator.registerPacket(new Packet0Echo());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		SocketPacketListener packetListener = new SocketPacketListener();

		try {

			SocketManager socketManager = new SocketManager(packetListener, registrator, new Socket(InetAddress.getLocalHost(), 42834));

			socketManager.writeThread.sendPacket(new Packet0Echo("Hi there!"));

			try {

				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			socketManager.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
