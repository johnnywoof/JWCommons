package streams;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads encrypted data from a source.
 */
public class SecureBufferedReader extends BufferedReader {

	private final Cipher decryptionCipher;

	public SecureBufferedReader(Reader in, Cipher decryptionCipher) {
		super(in);
		this.decryptionCipher = decryptionCipher;
	}

	@Override
	public String readLine() throws IOException {

		String str = super.readLine();

		if (str == null || str.isEmpty()) {

			return str;

		}

		try {

			return new String(this.decryptionCipher.doFinal(Hex.decodeHex(str.toCharArray())));

		} catch (IllegalBlockSizeException | BadPaddingException e) {

			e.printStackTrace();
			throw new IOException(e);

		}

	}

}
