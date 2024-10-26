package mongoclientutil;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.bson.types.Binary;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.client.ClientInformation;
import com.nimbusds.oauth2.sdk.client.ClientMetadata;
import com.nimbusds.oauth2.sdk.id.ClientID;

public class ClientResponse {

	private String client_id;
	private String client_name;
	private String client_secret;
	private long client_id_issued_at;
	private long client_secret_expires_at;
	private String jwks_uri;


	public ClientResponse(String client_id, String client_name, String client_secret, long client_id_issued_at, long client_secret_expires_at, String jwks_uri) {
		this.client_id = client_id;
		this.client_name = client_name;
		this.client_secret = client_secret;
		this.client_id_issued_at = client_id_issued_at;
		this.client_secret_expires_at = client_secret_expires_at;
		this.jwks_uri = jwks_uri;
	}

	public String getClient_id() {
		return client_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public long getClient_id_issued_at() {
		return client_id_issued_at;
	}

	public long getClient_secret_expires_at() {
		return client_secret_expires_at;
	}

	public String getJwks_uri() {
		return jwks_uri;
	}
}

