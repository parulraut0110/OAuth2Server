import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;

public class ECStringToKey {
	public static void main(String[] args) {
		try {
			// Add Bouncy Castle as the Security Provider
			Security.addProvider(new BouncyCastleProvider());

			// Example X and Y values
			String xHex = "102930c7ace27441b8ea9cb808f54ea7bbbfe2a2aac51b5c0ad4bb01130676bb";
			String yHex = "305416d85a1905a31b2e551f45692ceb092d04cd1926a31eb786aac08451f5f5";
			String privateKeyHex = "a4d6b5f8afa9be1dfeee0f0f68d544113f72a4";

			// Convert X and Y hex values to BigIntegers
			BigInteger xCoord = new BigInteger(xHex, 16);
			BigInteger yCoord = new BigInteger(yHex, 16);

			// Convert private key hex value to BigInteger
			BigInteger privateKeyValue = new BigInteger(privateKeyHex, 16);

			// Get the EC parameter spec for secp256k1
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

			// Create the public key point from X and Y coordinates
			ECPoint ecPoint = ecSpec.getCurve().createPoint(xCoord, yCoord);

			// Create the public key spec
			ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecSpec);
			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);

			// Create the private key spec
			ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(privateKeyValue, ecSpec);
			PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);

			// Print out the keys
			System.out.println("Public Key: " + publicKey);
			System.out.println("Private Key: " + privateKey);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
