package sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketWriteThread extends Thread {

	private final SocketManager socketManager;
	private final DataOutputStream outputStream;

	private final ConcurrentLinkedQueue<Packet> queue = new ConcurrentLinkedQueue<>();

	private boolean running = true;

	public SocketWriteThread(SocketManager socketManager, DataOutputStream outputStream) {

		this.socketManager = socketManager;
		this.outputStream = outputStream;
		this.start();

	}

	public void sendPacket(Packet packet) {

		this.queue.offer(packet);

		if (!this.running) {

			synchronized (this) {
				this.notify();
			}

		}

	}

	@Override
	public void run() {

		Packet packet;

		try {

			while (this.socketManager.isConnected()) {

				this.running = true;

				while ((packet = queue.poll()) != null) {

					this.outputStream.write(packet.getID());

					packet.write(this.outputStream);

					this.outputStream.flush();

				}

				this.running = false;

				synchronized (this) {

					this.wait();

				}

			}

		} catch (IOException | InterruptedException e) {

			e.printStackTrace();

		}

		try {

			this.outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
