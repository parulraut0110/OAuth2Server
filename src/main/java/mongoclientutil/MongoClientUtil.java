package mongoclientutil;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoClientUtil {
	private static final ThreadLocal<MongoDatabase> db = new ThreadLocal<>();

	public static MongoDatabase getDatabase() {
		if(db.get() == null) {
			MongoClient mongoClient = MongoClients.create("mongodb://oauth2serverAdmin:Raut0110@oauth2server:27017/?authSource=clientdb&authMechanism=SCRAM-SHA-256");
			db.set(mongoClient.getDatabase("clientdb"));
		}
		return db.get();
	}
}
