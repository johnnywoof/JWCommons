package sockets.tcp;

import sockets.Packet;

import java.io.DataInputStream;
import java.io.IOException;

public class SocketReadThread extends Thread {

	private final DataInputStream inputStream;
	private final SocketManager socketManager;

	public SocketReadThread(SocketManager socketManager, DataInputStream inputStream) {

		this.socketManager = socketManager;
		this.inputStream = inputStream;
		this.start();

	}

	@Override
	public void run() {

		Packet packet;

		try {

			while (this.socketManager.isConnected()) {

				int id = this.inputStream.read();

				if (id == -1) {

					this.socketManager.disconnect();
					break;

				} else {

					packet = this.socketManager.packetRegistrator.getEmptyInstanceByID(id);

					if (packet != null) {

						packet.read(this.inputStream);

						this.socketManager.onReceivePacket(packet);

					}

				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

		try {

			this.inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
