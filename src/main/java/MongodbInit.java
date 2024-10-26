
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;

import mongoclientutil.MongoClientUtil;
public class MongodbInit { 

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		try (MongoClient mongoClient = MongoClients.create("mongodb://oauth2clientAdmin:Raut0110@oauth2client:27017/?authSource=ClientRepository&authMechanism=SCRAM-SHA-256")) {
			//MongoDatabase database = MongoClientUtil.getDatabase();

			MongoDatabase db = mongoClient.getDatabase("ClientRepository");
			MongoCollection collection = db.getCollection("ClientDetails"); 

			Document doc = (Document)collection.find().first();
			System.out.println(doc.toJson());

			/*
            String resp = "{\"redirect_uris\" : [ \"https://client.example.org/callback\","
            		+ " \"https://client.example.org/callback2\" ],"
            		+ " \"client_id\" : \"s6BhdRkqt3\","
            		+ " \"client_secret\" : \"cf136dc3c1fc93f31185e5885805d\","
            		+ "  \"scope\" : \"read write dolphin\","
            		+ " \"client_name\" : \"My Example Client\"}";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(resp);

            // Extract individual field values
            JsonNode clientName = jsonNode.get("client_name");
            JsonNode clientID = jsonNode.get("client_id");
            JsonNode secret = jsonNode.get("client_secret");
            JsonNode redirectUri = jsonNode.get("redirect_uris");
            JsonNode scope = jsonNode.get("scope");

            System.out.println("name : " + clientName.asText());

            List<String> uris = new ArrayList<>();
            if(redirectUri.isArray()) {
            	for(JsonNode node : redirectUri) 
            	   uris.add(node.asText());  
            }

            for(String s : uris)
            	System.out.println(s);

            System.out.println(scope.asText() + " " + clientID.asText() + " " + secret.asText());

            MongoCollection<Document> collection = database.getCollection("clientRegDetails");
            Document document = new Document("name", clientName.asText())
                    .append("ID", clientID.asText())
                    .append("uris", uris)
                    .append("scope", scope.asText())
                    .append("secret", secret.asText());

            //Document document = new Document("uris", uris);
			 */    


			//Document foundDocument = collection.find(new Document("name", "John Doe")).first();
			//System.out.println("Found document: " + foundDocument.toJson());

		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}
}

