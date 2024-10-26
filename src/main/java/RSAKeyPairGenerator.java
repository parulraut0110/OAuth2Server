
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAKeyPairGenerator {
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator genRSA;
		try {
			genRSA = KeyPairGenerator.getInstance("RSA");

			genRSA.initialize(2048);


			KeyPair RSAkp1 = genRSA.generateKeyPair();
			KeyPair RSAkp2 = genRSA.generateKeyPair();

			System.out.println("RSAKP1 private: " + RSAkp1.getPrivate());
			System.out.println("RSAKP1 public: " + RSAkp1.getPublic());

			System.out.println("RSAKP2 private: " + RSAkp2.getPrivate());
			System.out.println("RSAKP2 public: " + RSAkp2.getPublic());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}