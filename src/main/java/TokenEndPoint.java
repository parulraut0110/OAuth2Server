import java.io.FileWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mongoclientutil.MongoClientUtil;


@WebServlet("/Token")
public class TokenEndPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TokenEndPoint.class.getName());
	HashMap<String, PrivateKey> keys;

	public TokenEndPoint() {
		super();
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

	private String base64UrlEncode(String input) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes());
	}

	private String generateToken(String user, String scope) {
		Random random = new Random();
		String signedJWT = null;

		Map<String, Object> payload = new HashMap<>();
		payload.put("user", user);
		payload.put("scope", scope);

		ObjectMapper objectMapper = new ObjectMapper();
		String payloadJson = null;
		try {
			payloadJson = objectMapper.writeValueAsString(payload);

			// Create the header
			Map<String, Object> header = new HashMap<>();
			List<Map.Entry<String, PrivateKey>> keyList = new ArrayList<>(keys.entrySet());
			int index = Math.abs(random.nextInt() % keyList.size());
			logger.log(Level.INFO, "index : " + index);
			if (keyList.get(index).getValue() instanceof RSAPrivateKey) {
				header.put("alg", "RS256");
			} else if (keyList.get(index).getValue() instanceof ECPrivateKey) {
				header.put("alg", "ES256");
			} else {
				throw new IllegalArgumentException("Unsupported key type");
			}
			header.put("typ", "JWT");
			header.put("kid", keyList.get(index).getKey());


			signedJWT = Jwts.builder()
					.setPayload(payloadJson)
					.setHeaderParams(header)
					.signWith(keyList.get(index).getValue(),
							header.get("alg").equals("RS256") ? SignatureAlgorithm.RS256 : SignatureAlgorithm.ES256)
					.compact();  // Get the signature part from the compact JWT



		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		return signedJWT;
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileHandler fileHandler = new FileHandler("C:\\logs\\Token.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.INFO);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());

		PrintWriter writer = response.getWriter();

		BigInteger mod1 = new BigInteger("23792242185458147150539252185969783888674374917548181799931181738010401377148011688620755994864929963207292528429863639335214897519338684582356518652792527760844633041080806049149679206451939577016554340033481979396792113489802900347483555232577046882403022106032282844982262147751612957438142858513374031368703331059585790073483514119942325452127487223640354053331360621838432542502139057249207770189149806206080614350816099577216549993712725325875020940751932213533825819245216629469595745801436107824701624918651189691310226617497992510710552100903407331112423462276276993315066933083522555421947868197375402164261");
		BigInteger privateExp1 = new BigInteger("1253641445768300104083036077479142254631583682266811016270810885853496268266412790180685847628747727452549186788585467491474949350076253750019171940549509726107712437730256481413421900872483957516953938375609005324681565960449117529706614267213367241334756658297140577992760197021822073543223322363681163178218822155896812139476354083350935416639063529583210255582680648855946242173835369823162484393886109219031107857633166310451929740316726858422333280114537615753128548088566205711368112218114119099678289029884034322218622964559791467879846485144743371175400974406226826700002887782711785977441297828042437764085");

		BigInteger mod2 = new BigInteger("30719171435768289842412976949084533350169897050899835257167815547408465194402510364348399793418507857287524369335443280002012089153628297451282748735948559600066623302119948071748532200015550898162148471824832892001011554847833003934417689076331760968864126752054483337296107562009788231620633775305076168806093953738049409723241162017696354366035088811066879450083429716458820666512117994579048899605760562822507396504127139239604173320581698154039700514523357510802135152536707175778690045623192308273017040099988869293779446253425244453934834899702096726855730891697389728580873239157642479257513678400219501970797");
		BigInteger privateExp2 = new BigInteger("1214948690991522457108723869753377640777581719577221615065977659930767990354933958899416394777618328060321088321367608858586986512751644826490759185247702312943416506692325028639885796762749407632883544241725542152778155090492136445031213666872940849158426790077745713265354087015416804192451328952969428407532947390111592248794144290799706954943463552294270089661404953271197593320731380352138947498552659150007931852533567511921047305719695472223255894108026000971865776189126495522720591885457058933374342501247735671534114350565499208520124490159350692217053531580869148358326669371003091911488782772479305399169");

		String privateKeyHex1 = "9ec74ceb51d2f71526f844cd3258cbc6ccd2347951af240da473eddb66e446d4";

		String privateKeyHex2 = "e2fef74227eadf96d481f0df511ccc176bfd389e688578559f55eb81099041b9";

		keys = new HashMap<>();
		keys.put("b2e1d173-4ca2-468c-bd15-ed801a060ec9", getRSAPrivate(mod1, privateExp1));
		keys.put("b3609202-4350-494d-bce9-2bb563d47207", getRSAPrivate(mod2, privateExp2));
		keys.put("a878351a-a503-42e3-bcb5-3a9d65f70b07", getECPrivate(privateKeyHex1));
		keys.put("f535bd02-85bf-4c95-9541-79be85c980a2", getECPrivate(privateKeyHex2));

		String client_id = null;
		String secret = null;
		String code = null; 
		String grantTypes = null;

		String query = request.getQueryString();
		String[] splitQuery = query.split("&", -1);
		for(String s: splitQuery) {
			if(s.startsWith("client")) 
				client_id = s.split("=")[1];
			if(s.startsWith("secret")) 
				secret = s.split("=")[1];
			if(s.startsWith("code")) 
				code = s.split("=")[1];
			if(s.startsWith("grant")) 
				grantTypes = s.split("=")[1];
		}

		logger.log(Level.INFO, "grantTypes: " + grantTypes);

		MongoCollection<Document> clientUser = MongoClientUtil.getDatabase().getCollection("UserDetails");
		Document clientUserDoc = clientUser.find(new Document("code", code)).first();
		String scope = clientUserDoc.getString("scope");
		String user = clientUserDoc.getString("user");

		MongoCollection<Document> clientRegDetails = MongoClientUtil.getDatabase().getCollection("clientRegDetails");
		Document clientRegDoc = clientRegDetails.find(new Document("client_id", clientUserDoc.getString("client_id"))).first();
		String clientSecret = clientRegDoc.getString("client_secret");
		List<String> grant_types = clientRegDoc.getList("grant_types", String.class);
		logger.log(Level.INFO, "grant_types: " + grant_types);


		Calendar cal = Calendar.getInstance();
		Long issuedAt = clientUserDoc.getLong("issued_at");
		logger.log(Level.INFO, "issued_at: " + issuedAt);

		if((issuedAt + 3*60*1000) < cal.getTimeInMillis()) {
			writer.println("Code Expired ");
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			logger.log(Level.INFO, "Code Expired");
			return;
		}

		if(!(clientSecret.equals(secret))) {
			writer.println("Possible Unauthorized user detected");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.log(Level.WARNING, "Possible Unauthorized user detected. secret did not match.");
			return;
		}

		String token;
		if(grant_types.contains(grantTypes)) {
			token = generateToken(user, scope);
			logger.log(Level.INFO, token.toString());
			response.setHeader("Authorization", "Bearer" + token);
			//String encodedToken = URLEncoder.encode("Bearer " + token, StandardCharsets.UTF_8.toString());
			PrintWriter out = response.getWriter();

			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<body onload='document.forms[0].submit()'>");
			response.getWriter().println("<form action='https://oauth2client:8543/OAuth2Client/GetCode' method='POST'>");
			response.getWriter().println("<input type='hidden' name='token' value='" + token + "'/>");
			response.getWriter().println("</form>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");

			// Set the token in a cookie
			Cookie tokenCookie = new Cookie("Authorization", token);
			tokenCookie.setHttpOnly(true); // Optional: Make the cookie HTTP-only for security
			tokenCookie.setSecure(true);   // Optional: Ensure the cookie is only sent over HTTPS
			tokenCookie.setPath("/");
			response.addCookie(tokenCookie);


			response.setHeader("Access-Control-Allow-Origin", "https://oauth2client:8543");
			response.setHeader("Access-Control-Allow-Method", "GET, PUT, OPTION, DELETE");
			response.setHeader("Access-Control-Allow-Header", "Authorization, Content-Type");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Max-Age", "3600"); 


			//response.sendRedirect("https://oauth2server:8643/OAuth2Server/TokenTest");

		}
		else {
			writer.println("unathorized request for token");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			logger.log(Level.INFO, "lack proper grant types");
			return;
		}
	}



	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "https://oauth2client:8543");
		response.setHeader("Access-Control-Allow-Method", "GET, PUT, OPTION, DELETE");
		response.setHeader("Access-Control-Allow-Header", "Authorization, Content-Type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600"); 
		response.setStatus(HttpServletResponse.SC_OK);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "https://oauth2client:8543");
		response.setHeader("Access-Control-Allow-Method", "GET, PUT, OPTION, DELETE");
		response.setHeader("Access-Control-Allow-Header", "Authorization, Content-Type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600");

		doGet(request, response);


	}

}