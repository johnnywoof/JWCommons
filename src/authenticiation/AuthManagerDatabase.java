package authenticiation;

public interface AuthManagerDatabase {

	void storeAccount(String username, byte[] hash, byte[] salt);

	void deleteAccount(String username);

	boolean accountExists(String username);

	byte[] getSalt(String username);

	byte[] getPasswordHash(String username);

}
