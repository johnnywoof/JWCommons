package sockets.udp;

import sockets.Packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPWriteThread extends Thread {

	private final UDPManager manager;

	private final ConcurrentLinkedQueue<DatagramPacket> queue = new ConcurrentLinkedQueue<>();

	private boolean running = true;

	public UDPWriteThread(UDPManager manager) {

		this.manager = manager;

	}

	public void sendPacket(InetSocketAddress socketAddress, Packet packet) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);

		DatagramPacket udpPacket;

		try {

			out.write(packet.getID());

			packet.write(out);

			out.flush();

			byte[] compiledData = baos.toByteArray();

			udpPacket = new DatagramPacket(compiledData, compiledData.length, socketAddress);

		} catch (IOException e) {

			throw new IllegalStateException(e);

		}

		this.queue.offer(udpPacket);

		if (!this.running) {

			synchronized (this) {
				this.notify();
			}

		}

	}

	@Override
	public void run() {

		DatagramPacket packet;

		try {

			while (this.manager.shouldKeepNetworking) {

				this.running = true;

				while ((packet = queue.poll()) != null) {

					this.manager.socket.send(packet);

				}

				this.running = false;

				synchronized (this) {

					this.wait();

				}

			}

		} catch (IOException | InterruptedException e) {

			e.printStackTrace();

		}

	}

}
