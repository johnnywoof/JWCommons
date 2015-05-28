package sockets;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PacketRegistrator {

	private final HashMap<Integer, Constructor> packets = new HashMap<>();

	public void unregisterPacket(int packetID) {

		this.packets.remove(packetID);

	}

	public void registerPacket(Packet packet) throws NoSuchMethodException {

		packets.put(packet.getID(), packet.getClass().getConstructor());

	}

	public Packet getEmptyInstanceByID(int id) {

		try {

			return (Packet) this.packets.get(id).newInstance();

		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;

	}

}
