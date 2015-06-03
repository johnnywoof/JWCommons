package sockets.udp;

import sockets.Packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class UDPReadThread extends Thread {

	private final UDPManager manager;

	public UDPReadThread(UDPManager manager) {

		this.manager = manager;

	}

	@Override
	public void run() {

		Packet packet;

		try {

			while (this.manager.shouldKeepNetworking) {

				byte[] data = new byte[this.manager.byteBuffer];

				DatagramPacket receivePacket = new DatagramPacket(data, data.length);

				this.manager.socket.receive(receivePacket);

				ByteArrayInputStream byteArrayInputStreamStream = new ByteArrayInputStream(data);
				DataInputStream in = new DataInputStream(byteArrayInputStreamStream);

				packet = this.manager.packetRegistrator.getEmptyInstanceByID(in.read());

				if (packet != null) {

					packet.read(in);

					this.manager.onReceivePacket(packet);

				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
