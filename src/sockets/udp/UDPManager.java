package sockets.udp;

import sockets.Packet;
import sockets.PacketReadHandler;
import sockets.PacketRegistrator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class UDPManager {

	public final UDPWriteThread writeThread;

	public final int byteBuffer;

	protected boolean shouldKeepNetworking = true;

	protected final DatagramSocket socket;

	protected final PacketRegistrator packetRegistrator;

	private final Object packetListener;
	private final Method[] packetListenerMethods;

	public UDPManager(Object packetListener, PacketRegistrator packetRegistrator, int byteBuffer, int port) throws SocketException {

		this.byteBuffer = byteBuffer;
		this.socket = new DatagramSocket(port);

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

		this.packetRegistrator = packetRegistrator;

		this.writeThread = new UDPWriteThread(this);

		new UDPReadThread(this).start();
		this.writeThread.start();

	}

	public void stopNetworking() {

		this.shouldKeepNetworking = false;
		Arrays.fill(this.packetListenerMethods, null);
		this.socket.close();

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

}
