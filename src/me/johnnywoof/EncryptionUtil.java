package me.johnnywoof;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * The EncryptionUtil class.<br/>
 * Example usage:<br/>
 * <code>
 * EncryptionUtil encryptionUtil = new EncryptionUtil();<br/>
 * <p/>
 * String text = "hello world";<br/>
 * <p/>
 * System.out.println(text);<br/>
 * <p/>
 * byte[] encrypted = encryptionUtil.encryptData(text.getBytes());<br/>
 * <p/>
 * System.out.println(new String(encrypted));<br/>
 * <p/>
 * byte[] decrypted = encryptionUtil.decryptData(encrypted);<br/>
 * <p/>
 * System.out.println(new String(decrypted));<br/>
 * </code>
 */
public class EncryptionUtil {

	/**
	 * The public key used for encryption.
	 */
	public final PublicKey publicKey;
	/**
	 * The private key used for decryption.
	 */
	public final PrivateKey privateKey;

	/**
	 * The cipher.
	 */
	private final Cipher cipher;

	/**
	 * Creates a new EncryptionUtil instance.<br/>
	 * The method does the following below:<br/>
	 * <code>
	 * new EncryptionUtil("RSA", 2048)
	 * </code>
	 *
	 * @see EncryptionUtil#EncryptionUtil(String, int)
	 */
	public EncryptionUtil() {
		this("RSA", 2048);
	}

	/**
	 * Creates a new EncryptionUtil instance.
	 *
	 * @param algorithm The encryption algorithm to use.
	 * @param bits      The amount of bits the algorithm should use.
	 * @throws IllegalStateException If the KeyPairGenerator failed for any reason.
	 */
	public EncryptionUtil(String algorithm, int bits) {

		KeyPairGenerator kpg = null;

		try {

			kpg = KeyPairGenerator.getInstance(algorithm);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (kpg != null) {

			kpg.initialize(bits);

			KeyPair keyPair = kpg.genKeyPair();

			this.publicKey = keyPair.getPublic();
			this.privateKey = keyPair.getPrivate();

			try {

				this.cipher = Cipher.getInstance(algorithm);

			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				e.printStackTrace();
				throw new IllegalStateException("Failed to create the cipher instance.");
			}

		} else {

			throw new IllegalStateException("Failed to create the KeyPairGenerator.");

		}

	}

	/**
	 * Creates a new EncryptionUtil instance based on an existing key pair.
	 *
	 * @param keyPair The key pair to use.
	 * @throws IllegalArgumentException If the keys in the key pair do not use the same algorithm.
	 */
	public EncryptionUtil(KeyPair keyPair) {

		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();

		if (this.publicKey.getAlgorithm().equals(this.privateKey.getAlgorithm())) {

			try {

				this.cipher = Cipher.getInstance(this.privateKey.getAlgorithm());

			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				e.printStackTrace();
				throw new IllegalStateException("Failed to create the cipher instance.");
			}

		} else {

			throw new IllegalArgumentException("The keys in the key pair are not using the same encryption algorithm!");

		}

	}

	/**
	 * Decrypts data.
	 *
	 * @param encryptedData The encrypted data to decrypt.
	 * @return The decrypted data, null if the decryption failed.
	 */
	public byte[] decryptData(byte[] encryptedData) {

		try {

			cipher.init(Cipher.DECRYPT_MODE, this.privateKey);

			return cipher.doFinal(encryptedData);

		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Encrypts data.
	 *
	 * @param data The data to encrypt.
	 * @return The encrypted data, null if the encryption failed.
	 */
	public byte[] encryptData(byte[] data) {

		try {

			cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);

			return cipher.doFinal(data);

		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;

	}

}
