
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.math.BigInteger;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

public class ECKeyPair {
    public static void main(String[] args) {
        try {
            // Add BouncyCastle as the Security Provider
            Security.addProvider(new BouncyCastleProvider());

            // Generate the EC Key Pair using secp256k1 curve
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Print the private and public keys in Base64 encoded format
            System.out.println("Private Key (Base64): " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            System.out.println("Public Key (Base64): " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

            // Extract raw private key value
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            BigInteger privateKeyValue = ecPrivateKey.getD();
            System.out.println("Private Key (Hex): " + privateKeyValue.toString(16));

            // Extract public key coordinates
            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
            BigInteger xCoord = ecPublicKey.getQ().getAffineXCoord().toBigInteger();
            BigInteger yCoord = ecPublicKey.getQ().getAffineYCoord().toBigInteger();
            System.out.println("Public Key X (Hex): " + xCoord.toString(16));
            System.out.println("Public Key Y (Hex): " + yCoord.toString(16));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}