import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.Binary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Identifier;

import mongoclientutil.*;

@WebServlet("/RegstrEndPoint")
public class RegstrEndPoint extends HttpServlet {
	private static final Logger logger = Logger.getLogger(RegstrEndPoint.class.getName());

	public RegstrEndPoint() {
		super();
	}

	//This is for resolving CORS issue
	@Override      
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setStatus(HttpServletResponse.SC_OK);
	}    

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Handling CORS
		response.addHeader("Access-Control-Allow-Origin", "*");
		//response.addHeader("Access-Control-Allow-Origin", "http://localhost:8081"); // Replace with your React app's origin   	
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");


		//Convert the request body to a single string
		StringBuilder requestBody = new StringBuilder();     
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) 
				requestBody.append(line);           
		} catch (IOException e) {

			e.printStackTrace();
		}

		FileHandler fileHandler = new FileHandler("C:\\logs\\oauthserver.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.INFO);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
		
		logger.log(Level.INFO, requestBody.toString());
		logger.log(Level.INFO, "Authorization : " + request.getHeader("Authorization"));

		//Convert the requestBody into a json tree where a node in the tree reflects a (map) key 
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(requestBody.toString());

		// Extract individual field values
		JsonNode clientName = jsonNode.get("client_name");
		JsonNode grantType = jsonNode.get("grant_types");
		JsonNode redirectUri = jsonNode.get("redirect_uris");


		logger.log(Level.INFO, redirectUri.asText());

		Identifier id = new Identifier();
		ClientID clientID = new ClientID(id);

		Secret secret = new Secret(clientName.asText());
		String secretAsHexString = "";
		for(byte b : secret.getSHA256()) {
			String hex = Integer.toHexString(0xFF & b);
			if(hex.length() == 1) {
				hex += "0";
			}
			secretAsHexString += hex;
		}
		// The encrypted secret from getSHA256() is base64 url encoded, so should be decoded at the client side 
		String secretUrlEncoded = Base64.getUrlEncoder().encodeToString(secret.getSHA256());

		logger.log(Level.INFO, "secret as hex string : " + secretAsHexString);      
		logger.log(Level.INFO, "secret as hex string : " + secretAsHexString);
		logger.log(Level.INFO, "ClientID : " + clientID);

		Calendar cal = Calendar.getInstance();
		long clientIDIssuedAt = cal.getTimeInMillis() / 1000;       //time in seconds
		cal.add(Calendar.HOUR_OF_DAY, 1);
		long secretExpiresAt = cal.getTimeInMillis() / 1000;

		List<String> uris = new ArrayList<>();
		if(redirectUri.isArray()) 
			for(JsonNode node : redirectUri) 
				uris.add(node.asText());

		List<String> grantTypes = new ArrayList<>();
		if(grantType.isArray()) 
			for(JsonNode node : grantType) 
				grantTypes.add(node.asText());

		
		MongoDatabase database = MongoClientUtil.getDatabase();
		logger.log(Level.INFO, "database : " + database.getName());

		MongoCollection<Document> collection = database.getCollection("clientRegDetails");
		Document secretDoc = new Document("secret", secret.getSHA256());

		logger.log(Level.INFO, "client ID : " + clientID.getValue());
		logger.log(Level.INFO, "client name : " + clientName.asText());
		logger.log(Level.INFO, "client secret : " + secretAsHexString);
		logger.log(Level.INFO, "client ID issued at : " + clientIDIssuedAt);
		logger.log(Level.INFO, "client secret expires at : " + secretExpiresAt);
		logger.log(Level.INFO, "grant types : " + grantTypes);
		logger.log(Level.INFO, "redirect uris : " + uris);
		logger.log(Level.INFO, "registration token : " + request.getHeader("Authorization"));
		logger.log(Level.INFO, "jwks_uri : " + "https://oauth2server:8643/OAuth2Server/WebKeySet");

		Document clientInfo = new Document("clientID", clientID.getValue())
				.append("client_secret", secretAsHexString)
				.append("client_secret_as_document", secretDoc)
				.append("client_name", clientName.asText())
				.append("client_id_issued_at", clientIDIssuedAt)
				.append("client_secret_expires_at", secretExpiresAt)
				.append("grant_types", grantTypes)
				.append("redirect_uris", uris)
				.append("registration_access_token", request.getHeader("Authorization"))
				.append("jwks_uri", "https://oauth2server:8643/OAuth2Server/WebKeySet");

		collection.insertOne(clientInfo);

		String jwksUri = "https://oauth2server:8643/OAuth2Server/WebKeySet"; 

		//A client response object is populated
		ClientResponse clientResponse = new ClientResponse(clientID.getValue(), clientName.asText(), secretAsHexString, clientIDIssuedAt, secretExpiresAt, jwksUri);
		ObjectMapper objectMapper = new ObjectMapper();
		String resp = "";
		resp = objectMapper.writeValueAsString(clientResponse);
		logger.log(Level.INFO, resp); 

		writer.println(resp);
		writer.flush();
		writer.close();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}
}