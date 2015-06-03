package streams;

//Credit ode to apache commons.
public class Hex {

	private static final char[] DIGITS_LOWER;
	private static final char[] DIGITS_UPPER;

	static {
		DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	}

	public static byte[] decodeHex(char[] data) {
		int len = data.length;
		if ((len & 1) != 0) {
			throw new IllegalStateException("Odd number of characters.");
		} else {
			byte[] out = new byte[len >> 1];
			int i = 0;

			for (int j = 0; j < len; ++i) {
				int f = toDigit(data[j], j) << 4;
				++j;
				f |= toDigit(data[j], j);
				++j;
				out[i] = (byte) (f & 255);
			}

			return out;
		}
	}

	protected static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
		} else {
			return digit;
		}
	}

	public static String encodeHexString(byte[] data) {
		return new String(encodeHex(data));
	}

	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}

	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		int i = 0;

		for (int j = 0; i < l; ++i) {
			out[j++] = toDigits[(240 & data[i]) >>> 4];
			out[j++] = toDigits[15 & data[i]];
		}

		return out;
	}

}
