import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.*;

public class JWKSGenerator {

	private static PublicKey getRSAPublic(BigInteger modulus, BigInteger publicExponent) {
		KeyFactory keyFactory;
		PublicKey	publicKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent)); 
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
			e1.printStackTrace();
		}   
		return publicKey;
	}

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

	private static PublicKey getECPublic(String xHex, String yHex) {
		PublicKey publicKey = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			BigInteger xCoord = new BigInteger(xHex, 16);
			BigInteger yCoord = new BigInteger(yHex, 16);

			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

			ECPoint ecPoint = ecSpec.getCurve().createPoint(xCoord, yCoord);

			ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecSpec);
			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			publicKey = keyFactory.generatePublic(pubKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return publicKey;
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

	public static void main(String[] args) throws NoSuchAlgorithmException, ParseException, JOSEException, InvalidAlgorithmParameterException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());

		BigInteger mod1 = new BigInteger("23792242185458147150539252185969783888674374917548181799931181738010401377148011688620755994864929963207292528429863639335214897519338684582356518652792527760844633041080806049149679206451939577016554340033481979396792113489802900347483555232577046882403022106032282844982262147751612957438142858513374031368703331059585790073483514119942325452127487223640354053331360621838432542502139057249207770189149806206080614350816099577216549993712725325875020940751932213533825819245216629469595745801436107824701624918651189691310226617497992510710552100903407331112423462276276993315066933083522555421947868197375402164261");
		BigInteger publicExp1 = new BigInteger("65537");
		//BigInteger privateExp1 = new BigInteger("3671964781810682537422790690372773103767601528244558972364664365397562221087575943142483484410794246025373146952613318022026220374373190091732691636492449887444163348226000821050598096619896902186839546378931107184585223571250052350706685370558514387390127170832213393623897604433934640075331038028244855487988431829657773407538930531403156569975028699563457308702719459746544842155742413556136455838451268017783793541651935544992854489886389152925735180789808037704312219303658848256129392013654450580158003967614235371908495767607281692215004203106343517535422350436328212608157163965857775875446601452326330436549");

		BigInteger mod2 = new BigInteger("30719171435768289842412976949084533350169897050899835257167815547408465194402510364348399793418507857287524369335443280002012089153628297451282748735948559600066623302119948071748532200015550898162148471824832892001011554847833003934417689076331760968864126752054483337296107562009788231620633775305076168806093953738049409723241162017696354366035088811066879450083429716458820666512117994579048899605760562822507396504127139239604173320581698154039700514523357510802135152536707175778690045623192308273017040099988869293779446253425244453934834899702096726855730891697389728580873239157642479257513678400219501970797");
		BigInteger publicExp2 = new BigInteger("65537");
		//BigInteger privateExp2 = new BigInteger("983410120392066673941085266174614833086767302397363670119642193477499318323844690919039596548993694399347209226742255334766800587667351261957778752655087527294460750177687351619452795338884703457025771966109204247012956545212457830560140909645211865197129680240049995313149359653628969971454979311266962554876478568908733389398961004566400502262934698106327510216271485196360135434923108932403021760625019151992239899241247518600097425776444389498512701190366456641464762575478130687000893486934283129891077504860936750968210866321918625118492460509399107095609020463147819904993516046125323413342483348060074236657");

		String xHex1 = "ae060af8e0dd07279ce54ccbbb0b645d6394dbb4976034199fa0fefd6b90d869";
		String yHex1 = "21b6e9d7daa12846cc5adf710ce673ead0009096fe115966201b77293a9e1554";
		//String privateKeyHex1 = "88bddbf10373614f6507914d36406d040f496aa32dc76d734003ad45fc5e0971";

		String xHex2 = "57ad0d9e693c353200f14418b2c7e68a7f6e0539fd2e6574f5a9b715ab961fef";
		String yHex2 = "e7d4849a6c73bab5d6db76620ce10caaa996a5be10d0e6ff26b5956614aaf9b2";
		//String privateKeyHex2 = "b34c5d3fc4e2ec0d895da8a97fb3fc093ab3cb55431bfb02377dc791f7b1a5c";

		PublicKey publicRSA1 = getRSAPublic(mod1, publicExp1);
		//PrivateKey privateRSA1 = getRSAPrivate(mod1, privateExp1);

		PublicKey publicRSA2 = getRSAPublic(mod2, publicExp2);
		//PrivateKey privateRSA2 = getRSAPrivate(mod2, privateExp2);

		PublicKey publicEC1 = getECPublic(xHex1, yHex1);
		//PrivateKey privateEC1 = getECPrivate(privateKeyHex1);

		PublicKey publicEC2 = getECPublic(xHex2, yHex2);
		//PrivateKey privateEC2 = getECPrivate(privateKeyHex2);

		JWK jwkRSA1 = new RSAKey.Builder((RSAPublicKey) publicRSA1)
				.keyUse(KeyUse.SIGNATURE)
				.keyID(UUID.randomUUID().toString())
				.build();

		JWK jwkRSA2 = new RSAKey.Builder((RSAPublicKey) publicRSA2)
				.keyUse(KeyUse.SIGNATURE)
				.keyID(UUID.randomUUID().toString())
				.build();

		JWK jwkEC1 = new ECKey.Builder(Curve.P_256, (ECPublicKey) publicEC1)
				.keyUse(KeyUse.SIGNATURE)
				.keyID(UUID.randomUUID().toString())
				.build();

		JWK jwkEC2 = new ECKey.Builder(Curve.P_256, (ECPublicKey) publicEC2)
				.keyUse(KeyUse.SIGNATURE)
				.keyID(UUID.randomUUID().toString())
				.build();




		List<JWK> jwKeySet = new ArrayList<>();
		jwKeySet.add(jwkRSA1);
		jwKeySet.add(jwkRSA2);
		jwKeySet.add(jwkEC1);
		jwKeySet.add(jwkEC2);

		JWKSet jwkset = new JWKSet(jwKeySet);
		//System.out.println("key set: " + jwkset.toJSONObject().toString());

		String json = jwkset.toString();
		System.out.println("key set: " + json);

		try {
			FileWriter fw = new FileWriter("C:/JWKS/JWKS.json");
			fw.write(jwkset.toJSONObject().toString());
			fw.close();
		} catch (IOException e) {
			System.out.println("Unable to create");
			e.printStackTrace();
		}
	}

}