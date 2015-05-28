package sockets.test;

import sockets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0Echo implements Packet {

	public String text;

	public Packet0Echo() {
	}

	public Packet0Echo(String text) {

		this.text = text;

	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.text = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(this.text);
	}

	@Override
	public int getID() {
		return 0;
	}

}
