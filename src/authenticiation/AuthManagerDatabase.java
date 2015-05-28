package authenticiation;

public interface AuthManagerDatabase {

	void storeAccount(String username, String hashedPassword);

	String getPasswordHash(String username);

}
