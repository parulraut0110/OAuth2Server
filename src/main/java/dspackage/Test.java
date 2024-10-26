package dspackage;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import com.nimbusds.jose.jwk.Curve;

public class Test {


	private static PrivateKey getECPrivate(String privateKeyHex) {
		PrivateKey privateKey = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			BigInteger privateKeyValue = new BigInteger(privateKeyHex, 16);

			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");

			ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(privateKeyValue, ecSpec);
			privateKey = keyFactory.generatePrivate(privKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		String privateKeyHex = "d5c2b0928b2d6f62a7f224867a1619d61ad9e08e7d9ae6ac6106e1121eea5db";
		/*
		KeyPairGenerator genEC = KeyPairGenerator.getInstance("EC", "BC");
        genEC.initialize(Curve.SECP256K1.toECParameterSpec());

        // Generate the EC key pair
        KeyPair ECkp = genEC.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) ECkp.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) ECkp.getPrivate();

        // Print the private key in hex format
        String privateKeyHex = privateKey.getS().toString(16);
        System.out.println("EC Private Key: " + privateKeyHex);

        // Print the public key in hex format (X and Y coordinates)
        String publicKeyXHex = publicKey.getW().getAffineX().toString(16);
        String publicKeyYHex = publicKey.getW().getAffineY().toString(16);
        System.out.println("EC Public Key X: " + publicKeyXHex);
        System.out.println("EC Public Key Y: " + publicKeyYHex);
		 */
		System.out.println("private : " + getECPrivate(privateKeyHex));
	}
}
