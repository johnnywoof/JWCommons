package random;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

/**
 * The TrueRandom class uses the random.org API in order to generate true randomness.
 */
public class TrueRandom extends Random {

	private final String userAgent;

	/**
	 * Creates a new TrueRandom instance.
	 *
	 * @see TrueRandom#TrueRandom(String)
	 */
	public TrueRandom() {
		this.userAgent = "";
	}

	/**
	 * Creates a new TrueRandom instance with an email address in it.<br/>
	 * The email is recommended so that the guys at random.org may contact you in case their guidelines are violated.<br/>
	 * See more: https://www.random.org/clients/
	 *
	 * @param email The email that will be sent to random.org
	 */
	public TrueRandom(String email) {

		this.userAgent = email;

	}

	@Override
	protected int next(int bits) {

		//Meh. Could be better.
		return this.nextInt(1000000000);

	}

	@Override
	public int nextInt() {
		return this.nextInt(32);
	}

	@Override
	public boolean nextBoolean() {
		return this.nextInt(1) == 0;
	}

	/**
	 * Returns a random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 */
	public int nextInt(int min, int max) {

		return this.nextInt((max - min) + 1) + min;
	}

	/**
	 * Generates a random number between 0 and the defined parameter.
	 *
	 * @param n The max number.
	 * @return The randomized number.
	 */
	@Override
	public int nextInt(int n) {

		if (n <= 0) {
			throw new IllegalArgumentException("n must be positive");
		} else if (n > 1000000000) {
			throw new IllegalArgumentException("Highest number that random.org supports is 1000000000.");
		}

		String response = null;

		try {

			response = this.sendGet("https://www.random.org/integers/?num=1&min=0&max=" + n + "&col=1&base=10&format=plain&rnd=new");

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response != null) {

			return Integer.parseInt(response);

		} else {

			throw new IllegalStateException("Could not obtain a response from the random.org server.");

		}

	}

	@Override
	@Deprecated
	/**
	 * @deprecated Seeds are pointless when using random.org
	 */
	synchronized public void setSeed(long seed) {
		super.setSeed(seed);
	}

	private String sendGet(String url) throws IOException {

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", this.userAgent);

		con.connect();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));

		String response = in.readLine();

		in.close();

		con.disconnect();

		return response;

	}

}
