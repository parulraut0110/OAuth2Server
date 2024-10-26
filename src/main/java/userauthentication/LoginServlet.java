package userauthentication;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bitcoinj.core.Base58;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.Session;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//import userrequest.UserRequest;

@WebServlet("/LoginForm")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	private static UUID lastUserid;
	private static Logger logger = Logger.getLogger(LoginServlet.class.getClass().getName());

	public LoginServlet() {
		super();
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("C:\\logs\\Login.log");
			fileHandler.setLevel(Level.INFO);
			logger.addHandler(fileHandler);
			logger.setLevel(Level.INFO);
			fileHandler.setFormatter(new java.util.logging.SimpleFormatter());

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String generateSessionToken(String uname, String Jsessionid, Logger logger) {
		HashMap<String, String> payload = new HashMap<>();
		payload.put("user", uname);
		payload.put("JSESSIONID", Jsessionid);
		ObjectMapper mapper = new ObjectMapper();
		String payloadJson = null;
		try {
			payloadJson = mapper.writeValueAsString(payload);
			logger.log(Level.INFO, "payload : " + payloadJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jwt = Jwts.builder()
				.setPayload(payloadJson)
				.compact();
		
		logger.log(Level.INFO, "jwt : " + jwt);

		return jwt;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.log(Level.INFO, "in post method");
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.log(Level.INFO, "in get method");
		String uname = request.getParameter("uname");
		String password = request.getParameter("pwd");
		Connection conn = null;
		Statement stmt = null;
		MessageDigest sha = null;


		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Users", "root","Raut#0110");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.print("<!doctype html><\n>");
		writer.print("<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Login Credentails</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "<p>The username is " + request.getParameter("uname")+ "</p><br/>"
				+ "<p>The password is " + request.getParameter("pwd") + "</p><br>"
				+ "<body>\r\n"
				+ "</html>\r\n"
				);

		try { 

			PreparedStatement pstmt = conn.prepareStatement("select * from Users where uname=?");
			pstmt.setString(1, uname);
			ResultSet rs = pstmt.executeQuery();
			boolean userFound = false;
			if(rs.next()) {                        //username exists
				String pwd = rs.getString(3);
				//System.out.println(pwd)
				byte[] shaDigest = sha.digest(password.getBytes());
				String shaBase58 = Base58.encode(shaDigest);
				if(shaBase58.equals(pwd)) {
					//HttpSession session = request.getSession(false);          //Check if session exists for the user
					Cookie[] cookies = request.getCookies();
					String loginToken = request.getHeader("Authorization");
					logger.log(Level.INFO, "LoginToken : " + loginToken);
					logger.log(Level.INFO, "reached1");
					boolean found = false;
					if(cookies != null) {
						String logToken = request.getHeader("Authorization");
						Enumeration<String> headerNames = request.getHeaderNames();
					    while (headerNames.hasMoreElements()) {
					        String headerName = headerNames.nextElement();
					        String headerValue = request.getHeader(headerName);
					        logger.log(Level.INFO, "Header: " + headerName + " = " + headerValue);
					    }
						logger.log(Level.INFO, "log Token : " + logToken);
						for (Cookie c : cookies) {
							if(c.getName().equals("JSESSIONID")) {
                               /*  
								Cookie cookie = new Cookie("JSESSIONID", c.getValue());
								response.addCookie(cookie);          //cookie 1 
								*/
								found = true;
								String session = c.getValue();
								System.out.println("sessionid : " + session + " username " + uname);
								writer.println("<html><body><p><span style = 'font-family:Tahoma;'><em>You are already logged in with session id: " + session  + "</em>.</span></p></body></html>");
								return;	
							}
						}

						if(!found) {
							HttpSession session = request.getSession(true);   	//creates a new session
							session.setAttribute("user", uname);
							logger.log(Level.INFO, " sessionAttribute : " + session.getAttribute("user"));
							writer.println("<html><body><p><span style='font-family:Tahoma;'><em>Welcome " + uname + "</em>.</span> You have been registered with session id " + session.getId() + "</p></body></html>"); 

						} 
					}

					else {
						String logintoken = request.getHeader("Authorization");
						logger.log(Level.INFO, "loginToken : " + logintoken);
						HttpSession session = request.getSession(true);                       //Create a new user session
						session.setAttribute("user", uname);
						logger.log(Level.INFO, "Reached2 " + uname + " " + session.getId());
						String token = generateSessionToken(uname, session.getId(), logger);
						logger.log(Level.INFO, " sesionID : " + session.getId() + " Token : " + token);
						response.setHeader("Authorization", "Bearer " + token);
						writer.println("<html><body><p><span style='font-family:Tahoma;'><em>Welcome " + uname + "</em>.</span> You have been registered with session id " + session.getId() + "</p></body></html>");

					}
				}
				else {
					writer.println("<html><body><p><span style ='color:red;'> Wrong Credentials</span></p></body></html>");
					return;
				}
			}else {
				writer.println("<html><body><p><span style ='color:red;'> New User. Register First</span></p></body></html>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		writer.flush();
	}

}
