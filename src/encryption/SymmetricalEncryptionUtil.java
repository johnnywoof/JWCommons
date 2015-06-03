package encryption;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricalEncryptionUtil {

	public final SecretKey secretKey;

	/**
	 * The ciphers.
	 */
	public Cipher encryptionCipher;
	public Cipher decryptionCipher;

	public SymmetricalEncryptionUtil() {
		this("AES", 128);
	}

	public SymmetricalEncryptionUtil(String algorithm, int bits) {

		KeyGenerator keyGenerator;

		try {

			keyGenerator = KeyGenerator.getInstance(algorithm);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

		keyGenerator.init(bits);

		this.secretKey = keyGenerator.generateKey();

		try {

			this.encryptionCipher = Cipher.getInstance(algorithm);
			this.encryptionCipher.init(Cipher.ENCRYPT_MODE, this.secretKey);

			this.decryptionCipher = Cipher.getInstance(algorithm);
			this.decryptionCipher.init(Cipher.DECRYPT_MODE, this.secretKey);

		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}

	}

	public byte[] encryptData(byte[] data) {

		try {

			return this.encryptionCipher.doFinal(data);

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}

	}

	public byte[] decryptData(byte[] data) {

		try {

			return this.decryptionCipher.doFinal(data);

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}

	}

}
