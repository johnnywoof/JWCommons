package streams.test;

import encryption.SymmetricalEncryptionUtil;
import streams.SecureBufferedReader;
import streams.SecurePrintWriter;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class StreamTest {

	public static void main(String[] args) throws Exception {

		SymmetricalEncryptionUtil encryptionUtil = new SymmetricalEncryptionUtil();

		File file = new File("data.txt");

		if (file.exists() || file.createNewFile()) {

			Random random = new Random();

			SecurePrintWriter w = new SecurePrintWriter(file, encryptionUtil.encryptionCipher);

			w.println("Data: " + random.nextDouble());

			w.close();

			SecureBufferedReader br = new SecureBufferedReader(new FileReader(file), encryptionUtil.decryptionCipher);

			String l;

			while ((l = br.readLine()) != null) {

				System.out.println(l);

			}

			br.close();

		}

		System.exit(0);

	}

}
