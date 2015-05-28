package authenticiation;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Objects;

public class AuthManager {

	//Credit to http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash/18143616#18143616

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private static final SecretKeyFactory SFK;
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;

	static {

		try {

			SFK = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

			throw new RuntimeException(e);

		}

	}

	private final AuthManagerDatabase database;

	public AuthManager(AuthManagerDatabase database) {

		this.database = database;

	}

	/**
	 * Determines if the entered username and password matches the one in the database.
	 * This will return false if the account does not exist.
	 *
	 * @param username The username.
	 * @param password The password. This variable will be cleared.
	 * @return If the correct password for the username has been typed.
	 */
	public boolean checkCredentials(String username, char[] password) {

		if (!this.database.accountExists(username)) {

			return false;

		}

		byte[] pwdHash = hash(password, this.database.getSalt(username));

		if (password.length == 0 && pwdHash.length == 0) {

			return false;

		}

		Arrays.fill(password, Character.MIN_VALUE);

		return Objects.deepEquals(pwdHash, this.database.getPasswordHash(username));

	}

	/**
	 * Deletes an account.
	 *
	 * @param username The username of the account to delete.
	 */
	public void deleteAccount(String username) {

		this.database.deleteAccount(username);

	}

	/**
	 * Creates a new account and saves it to the database.
	 *
	 * @param username  The username.
	 * @param password  The password. This variable will be cleared.
	 * @param overWrite If to over-write the existing account for the username, if one exists.
	 */
	public void createAccount(String username, char[] password, boolean overWrite) {

		if (username == null || password == null) {

			throw new IllegalArgumentException("The username and password cannot be null!");

		}

		if (!overWrite && this.database.accountExists(username)) {

			throw new IllegalStateException("Cannot create the new account. An account already exists.");

		}

		byte[] salt = getNextSalt();
		byte[] pwdHash = hash(password, salt);

		this.database.storeAccount(username, pwdHash, salt);

		Arrays.fill(password, Character.MIN_VALUE);
		Arrays.fill(salt, (byte) 0);
		Arrays.fill(pwdHash, (byte) 0);

	}

	/**
	 * Generates a random password of a given length, using letters and digits.
	 *
	 * @param length the length of the password
	 * @return a random password
	 */
	public static String generateRandomPassword(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int c = SECURE_RANDOM.nextInt(62);
			if (c <= 9) {
				sb.append(String.valueOf(c));
			} else if (c < 36) {
				sb.append((char) ('a' + c - 10));
			} else {
				sb.append((char) ('A' + c - 36));
			}
		}
		return sb.toString();
	}

	private static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		SECURE_RANDOM.nextBytes(salt);
		return salt;
	}

	private static byte[] hash(char[] password, byte[] salt) {

		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

		Arrays.fill(password, Character.MIN_VALUE);

		try {

			return SFK.generateSecret(spec).getEncoded();

		} catch (InvalidKeySpecException e) {

			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);

		} finally {

			spec.clearPassword();

		}
	}

}
