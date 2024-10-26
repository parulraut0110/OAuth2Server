import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/TokenTest")
public class TokenTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TokenTest() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		String token = null;
		//token = request.getHeader("Authorization");
		//logger.log(Level.INFO, "Token: " + token);
		//response.getWriter().println("Token : " + token);

		String authToken = request.getParameter("token");
		response.getWriter().println("Auth Token : " + authToken);

		if (cookies != null) 
			for (Cookie cookie : cookies) 
				if ("Authorization".equals(cookie.getName())) {
					token = cookie.getValue();
					if (token != null) 
						response.getWriter().println("Token: " + token);
					else 
						response.getWriter().write("Token not found in cookie.");
					break;
				}
				else
					response.getWriter().write("Cookie not found");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String authToken = request.getParameter("token");
		response.getWriter().println("Token: " + authToken);
	}

}