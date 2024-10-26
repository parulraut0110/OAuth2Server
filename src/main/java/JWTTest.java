import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {

	public static void main(String[] args) throws Exception {
		// Generate KeyPair (Public/Private Key Pair)		        
		KeyPairGenerator genRSA = KeyPairGenerator.getInstance("RSA");
		genRSA.initialize(2048);
		KeyPair keyPair = genRSA.generateKeyPair();

		// Create JWT header
		Map<String, Object> header = new HashMap<>();
		header.put("alg", "RS256"); // RSA with SHA-256
		header.put("typ", "JWT"); // Type of token

		// Convert header to JSON string
		ObjectMapper objectMapper = new ObjectMapper();
		String headerJson = objectMapper.writeValueAsString(header);

		// Encode the header to Base64
		String base64Header = Base64.getEncoder().encodeToString(headerJson.getBytes());

		// Create JWT payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("sub", "1234567890"); // Subject
		payload.put("name", "John Doe");
		payload.put("admin", true);

		// Convert payload to JSON string
		String payloadJson = objectMapper.writeValueAsString(payload);

		// Concatenate header and payload with a dot
		String encodedJwt = base64Header + "." + Base64.getEncoder().encodeToString(payloadJson.getBytes());

		// Sign the JWT using the private key
		byte[] signature = sign(encodedJwt.getBytes(), keyPair.getPrivate());

		// Encode the signature to Base64
		String base64Signature = Base64.getEncoder().encodeToString(signature);

		// Final JWT
		String jwt = encodedJwt + "." + base64Signature;

		System.out.println("JWT: " + jwt);

		// Verify the JWT signature using the public key
		boolean verified = verify(jwt, keyPair.getPublic());
		System.out.println("Signature Verified: " + verified);
	}

	// Generate a KeyPair
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.generateKeyPair();
	}

	// Sign data using private key
	public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}

	// Verify signature using public key
	public static boolean verify(String jwt, PublicKey publicKey) throws Exception {
		String[] parts = jwt.split("\\.");
		byte[] payloadBytes = parts[0].getBytes();
		byte[] signatureBytes = Base64.getDecoder().decode(parts[1]);

		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(payloadBytes);
		return signature.verify(signatureBytes);
	}

}


