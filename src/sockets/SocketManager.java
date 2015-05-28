package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketManager {

	public final SocketWriteThread writeThread;

	private final Socket socket;

	protected final PacketRegistrator packetRegistrator;

	private final Object packetListener;

	public SocketManager(Object packetListener, PacketRegistrator packetRegistrator, Socket socket) throws IOException {

		this.packetListener = packetListener;

		this.socket = socket;
		this.packetRegistrator = packetRegistrator;

		new SocketReadThread(this, new DataInputStream(socket.getInputStream()));
		this.writeThread = new SocketWriteThread(this, new DataOutputStream(socket.getOutputStream()));

	}

	public boolean isConnected() {
		return this.socket.isConnected();
	}

	public void onReceivePacket(Packet packet) {

		for (Method m : this.packetListener.getClass().getMethods()) {

			Class<?>[] params = m.getParameterTypes();

			if (params.length == 1) {

				//TODO Use something else than a string
				if (params[0].getCanonicalName().equals(packet.getClass().getCanonicalName())) {

					try {

						m.invoke(packetListener, packet);

					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}

				}

			}

		}

	}

	public void disconnect() {

		try {

			this.socket.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
