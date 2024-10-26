import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



public class SignedJWT {

	static Map<String, PrivateKey> keys = new HashMap<>();

	private static PrivateKey getRSAPrivate(BigInteger modulus, BigInteger privateExponent) {
		KeyFactory keyFactory;
		PrivateKey privateKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
			e1.printStackTrace();
		}
		return privateKey;
	}

	private static PrivateKey getECPrivate(String privateKeyHex) {
		PrivateKey privateKey = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			BigInteger privateKeyValue = new BigInteger(privateKeyHex, 16);

			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(privateKeyValue, ecSpec);
			privateKey = keyFactory.generatePrivate(privKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	private static String base64UrlEncode(String input) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes());
	}


	public static void main(String[] args) {

		BigInteger mod1 = new BigInteger("21524042386791887791697816150884167157248539989854090726878315684903182798928175357607346730274157909017027854911087967730381682811618063507167426481893089600056181150457261822744335893580625488897536188098565177904043808165020766594361973000339283342103373229715197815654879236329929297134919747708696667780474207626403929116782760683461841958403185689448731299018862417557974448925825329640074999230735961895221594158714811970961637425080038626467764863663204233133703488349624385985077411127823260242069195786805040800359488498069253289952365473541684801344039419687090203243830685889960454751455708216104397101679");
		BigInteger privateExp1 = new BigInteger("3671964781810682537422790690372773103767601528244558972364664365397562221087575943142483484410794246025373146952613318022026220374373190091732691636492449887444163348226000821050598096619896902186839546378931107184585223571250052350706685370558514387390127170832213393623897604433934640075331038028244855487988431829657773407538930531403156569975028699563457308702719459746544842155742413556136455838451268017783793541651935544992854489886389152925735180789808037704312219303658848256129392013654450580158003967614235371908495767607281692215004203106343517535422350436328212608157163965857775875446601452326330436549");

		String privateKeyHex1 = "88bddbf10373614f6507914d36406d040f496aa32dc76d734003ad45fc5e0971";

		Map<String, Object> payload = new HashMap<>();
		payload.put("user", "parul");
		payload.put("scope", "read+write");


		ObjectMapper objectMapper = new ObjectMapper();
		String payloadJson = null;


		try {

			payloadJson = objectMapper.writeValueAsString(payload);
			/*
			// Encode payload to Base64Url
			String encodedPayload = base64UrlEncode(payloadJson);
			 */

			// Create the header
			Map<String, Object> header = new HashMap<>();

			header.put("alg", "RS256");
			header.put("typ", "JWT");

			// Encode header to Base64Url
			objectMapper = new ObjectMapper();
			String headerJson = objectMapper.writeValueAsString(header);
			//String encodedHeader = base64UrlEncode(headerJson);

			// Create the JWT string to be signed (header.payload)
			//String jwtWithoutSignature = encodedHeader + "." + encodedPayload;
			//System.out.println("JWT without signature " + jwtWithoutSignature);
			// Sign the JWT using the private key

			String signedJWT = Jwts.builder()
					.setPayload(payloadJson)
					.setHeaderParams(header)
					.signWith(getRSAPrivate(mod1, privateExp1), SignatureAlgorithm.RS256)
					.compact(); 
			System.out.println("Signed JWT " + signedJWT);

			/*
			String[] split = signature.split("\\.", -1);
            System.out.println("[2] : " + split[2]);
			System.out.println("signature " + signature);
			String jwt = jwtWithoutSignature + "." + signature;
			System.out.println("JWT with signature " + jwt);
			 */
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
	}

}
