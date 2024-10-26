import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.StandardCharset;
import com.nimbusds.oauth2.sdk.AuthorizationCode;

import mongoclientutil.MongoClientUtil;


@WebServlet("/Auth")
public class AuthEndPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AuthEndPoint.class.getName());
	String state;
	String redirect_uri;
	String response_type;
	String client_id;
	String scope;
	MongoDatabase database;
	MongoCollection<Document> collection;

	public AuthEndPoint() {
		super();
		database = MongoClientUtil.getDatabase();
		collection = database.getCollection("clientRegDetails");
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Credentials", "True");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Credentials", "True");

		PrintWriter out = response.getWriter();


		FileHandler fileHandler = new FileHandler("C:\\logs\\AuthEndPoint.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.INFO);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());


		String query = (String) request.getQueryString();
		logger.log(Level.INFO, "query " + query);
		String[] splitQuery = query.split("&",-1);


		for(String s : splitQuery) {
			s = s.trim();
			if(s.startsWith("state")) {
				String[] stateComponent = s.split("=");
				state = stateComponent[1].trim();
				logger.log(Level.INFO, "state: " + state);
			}
			if(s.startsWith("redirect")) {
				String[] redirectComponent = s.split("=");
				redirect_uri = redirectComponent[1].trim();
				redirect_uri = URLDecoder.decode(redirect_uri, StandardCharsets.UTF_8);


				logger.log(Level.INFO, "redirect_uri: " + redirect_uri);
			}
			if(s.startsWith("response")) {
				String[] responseComponent = s.split("=");
				response_type = responseComponent[1].trim();
				logger.log(Level.INFO, "response_type: " + response_type);
			}
			if(s.startsWith("client")) {
				String[] clientComponent = s.split("=");
				client_id = clientComponent[1].trim();
				logger.log(Level.INFO, "client_id: " + client_id);
			}
			if(s.startsWith("scope")) {
				String[] scopeComponent = s.split("=");
				scope = scopeComponent[1].trim();
				logger.log(Level.INFO, "scope: " + scope);
			}
		}
		logger.log(Level.INFO, "client_id : " + client_id);
		Document clientDetails = collection.find(new Document("clientID", client_id)).first();
		logger.log(Level.INFO, clientDetails.toJson());
		if(clientDetails == null) {
			return;
		}

		//logger.log(Level.INFO, "session id: " + request.getSession(false).getId());    
		//logger.log(Level.INFO, "session id from cookies: " + request.getCookies()[0].getValue());

		if(request.getSession(false) == null) {
			out.println("<html>");
			out.println("<body>");
			out.println("<p>Login first</p>");  
			out.println("</body>");
			out.println("</html>");
			return;
		}

		else {
			out.println("session id: " + request.getSession(false).getId());
			logger.log(Level.INFO, "session id: " + request.getSession(false).getId());
			response.setContentType("text/html");

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Authorization Consent</title>");
			out.println("<style>");
			out.println("body {margin: 10px;}");
			out.println("#popup {");
			out.println("position: fixed; top: 50%; left: 50%; width: 350px; height: 150px; transform: translate(-50%, -50%);");
			out.println("padding: 10px; background-color: lightgray; border: 1px solid black;");
			out.println("}");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"popup\" style=\"display: block;\">");
			out.println("<p>Client has asked for authorization on your behalf. Would you like to grant the same?</p>");  
			out.println("<form id=\"authForm\" action=\"Auth\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"response\" id=\"response\">");
			out.println("<button id=\"btn1\" type=\"button\">Yes</button>");
			out.println("<button id=\"btn2\" type=\"button\">No</button>");
			out.println("</form>");
			out.println("</div>");
			out.println("<script>");
			out.println("document.getElementById('btn1').onclick = function() {");
			out.println("  document.getElementById('response').value = 'yes';");
			out.println("  document.getElementById('authForm').submit();");
			out.println("};");
			out.println("document.getElementById('btn2').onclick = function() {");
			out.println("  document.getElementById('response').value = 'no';");
			out.println("  document.getElementById('authForm').submit();");
			out.println("};");
			out.println("</script>");
			out.println("</body>");
			out.println("</html>");
		}

		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Credentials", "True");
		/*
        StringBuilder requestBody = new StringBuilder();
		String line = null;
		BufferedReader br = request.getReader();
		while((line = br.readLine()) != null )
			requestBody.append(line);	
		logger.log(Level.INFO, "line: " + line);
		 */
		String responseValue = request.getParameter("response");
		logger.log(Level.INFO, "Reached");

		if ("yes".equals(responseValue)) {
			logger.log(Level.INFO, "yes");
			AuthorizationCode code = new AuthorizationCode();
			Calendar cal = Calendar.getInstance();
			String user = (String)request.getSession(false).getAttribute("user");
			redirect_uri += "?code=" + code.getValue() + "&state=" + state + "&sessionid=" + request.getSession(false).getId() + "&user=" + user;

			if(request.getSession(false) != null)  {
				logger.log(Level.INFO, "session_id: " + request.getSession(false).getId());
				collection = database.getCollection("UserDetails");
				collection.insertOne(new Document("clientID", client_id).append("code", code.getValue()).append("issued_at", cal.getTimeInMillis()).append("session_id", request.getSession(false).getId()).append("scope", scope).append("user", user));
				logger.log(Level.INFO, "auth_state: " + state + " session_id: " + request.getSession(false).getId());
			}

			response.sendRedirect(redirect_uri); 

		} else if ("no".equals(responseValue)) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>User response</title>");
			out.println("<style>");
			out.println("body {margin: 10px;}");
			out.println("#popup {");
			out.println("position: fixed; top: 50%; left: 50%; width: 350px; height: 150px; transform: translate(-50%, -50%);");
			out.println("padding: 10px; background-color: lightgray; border: 1px solid black;");
			out.println("}");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"popup\" style=\"display: block;\">");
			out.println("<p>Client has declined your request</p>");  
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");

			out.flush();
		}

	}
}