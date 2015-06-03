package sockets.tcp;

import sockets.Packet;
import sockets.PacketReadHandler;
import sockets.PacketRegistrator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

public class SocketManager {

	public final SocketWriteThread writeThread;

	private final Socket socket;

	protected final PacketRegistrator packetRegistrator;

	private final Object packetListener;
	private final Method[] packetListenerMethods;

	public SocketManager(Object packetListener, PacketRegistrator packetRegistrator, Socket socket) throws IOException {

		this.packetListener = packetListener;

		ArrayList<Method> methods = new ArrayList<>();

		for (Method m : this.packetListener.getClass().getMethods()) {

			if (m.isAnnotationPresent(PacketReadHandler.class)) {

				Class<?>[] params = m.getParameterTypes();

				if (params.length == 1) {

					methods.add(m);

				}

			}

		}

		this.packetListenerMethods = methods.toArray(new Method[methods.size()]);

		methods.clear();

		this.socket = socket;
		this.packetRegistrator = packetRegistrator;

		new SocketReadThread(this, new DataInputStream(socket.getInputStream()));
		this.writeThread = new SocketWriteThread(this, new DataOutputStream(socket.getOutputStream()));

	}

	public boolean isConnected() {
		return this.socket.isConnected();
	}

	public void onReceivePacket(Packet packet) {

		for (Method m : this.packetListenerMethods) {

			if (m.getParameterTypes()[0].isInstance(packet)) {

				try {

					m.invoke(packetListener, packet);

				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
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
