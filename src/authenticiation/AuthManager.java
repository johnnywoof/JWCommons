package authenticiation;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

public class AuthManager {

	private final AuthManagerDatabase database;
	private final MessageDigest md;
	private final Cipher encryptionCipher;

	public AuthManager(PublicKey publicKey, AuthManagerDatabase database) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

		this.database = database;

		this.encryptionCipher = Cipher.getInstance(publicKey.getAlgorithm());
		this.encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		this.md = MessageDigest.getInstance("SHA-256");

	}

	public AuthManager(PublicKey publicKey, AuthManagerDatabase database, MessageDigest messageDigest) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

		this.database = database;
		this.md = messageDigest;

		this.encryptionCipher = Cipher.getInstance(publicKey.getAlgorithm());
		this.encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);

	}

	public void createAccount(String username, byte[] password) throws BadPaddingException, IllegalBlockSizeException {

		if (username == null || password == null) {

			throw new IllegalArgumentException("The username and password cannot be null!");

		}

		this.database.storeAccount(username, this.hashData(this.encryptionCipher.doFinal(password)));

		Arrays.fill(password, (byte) 0);

	}

	private String hashData(byte[] data) {

		md.update(data);

		byte byteData[] = md.digest();

		StringBuilder sb = new StringBuilder();

		for (byte aByteData : byteData) {

			sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));

		}

		md.update((byte) 0);

		Arrays.fill(data, (byte) 0);

		return sb.toString();

	}

	public boolean checkCredentials(String username, byte[] password) throws BadPaddingException, IllegalBlockSizeException {

		if (username == null || password == null) {

			return false;

		}

		String hashedPassword = this.hashData(this.encryptionCipher.doFinal(password));

		String databaseHashedPassword = this.database.getPasswordHash(username);

		Arrays.fill(password, (byte) 0);

		return hashedPassword.equals(databaseHashedPassword);

	}

}
