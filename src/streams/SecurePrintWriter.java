package streams;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Writes encrypted data to a source.
 */
public class SecurePrintWriter extends PrintWriter {

	private final Cipher encryptionCipher;

	public SecurePrintWriter(File file, Cipher encryptionCipher) throws FileNotFoundException {
		super(file);
		this.encryptionCipher = encryptionCipher;
	}

	@Override
	public void write(char buf[], int off, int len) {
		this.write(new String(buf));
	}

	@Override
	public void write(String s, int off, int len) {

		try {

			String str = Hex.encodeHexString(this.encryptionCipher.doFinal(s.getBytes()));

			super.write(str, 0, str.length());

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

	}

}
