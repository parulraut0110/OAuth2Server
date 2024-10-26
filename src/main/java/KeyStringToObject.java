import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class KeyStringToObject {

	public static PrivateKey convertToPrivateKey(RSAPrivateKeySpec privateKeySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(privateKeySpec);
	}

	public static PublicKey convertToPublicKey(RSAPublicKeySpec publicKeySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(publicKeySpec);
	}


	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {

		String modulusString = "29089680980515828067554822054386538950516764278230538708041994815727484483215908216909406893478891574377740566639463804691195744617854651362541725656779579279074256882532699859622095859958412189428041382068157689683447213754816372096078867373783213229867607133570169757784015770954640811513725746787906966543266836073056529238781982282602692766487134157656896934665346033414352623013804467894848186272233859233755665400758880387373813798419582963973453830537071514596318381576396289799493506741359337875855663549311897909729645854132331130476754902605071017546375635678071676414761593419365140232726965439443201632859";
		String publicExponentString = "65537";

		// Convert modulus and public exponent strings to BigInteger
		BigInteger modulus = new BigInteger(modulusString, 16);
		BigInteger publicExponent = new BigInteger(publicExponentString, 16);

		// Create RSAPublicKeySpec object
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);

		// Convert to RSA key objects
		//PrivateKey privateKey = convertToPrivateKey(privateKeySpec);
		PublicKey publicKey = convertToPublicKey(publicKeySpec);
		System.out.println("Public key generated : " + publicKey);

		// Now you can use privateKey and publicKey in your code
	}
}
