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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitcoinj.core.Base58;

@WebServlet("/SigninForm")
public class SigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public SigninServlet() {
        super();
       }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		Connection con;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con =  DriverManager.getConnection("jdbc:mysql://localhost/Users", "root", "Raut#0110");
		    pstmt = con.prepareStatement("insert into Users(uname, pwd) values(?,?)");
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		} 
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String base58PWD = "";
		try {
			byte[] shaDigest = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
			base58PWD = Base58.encode(shaDigest);
			pstmt.setString(1, username);
			pstmt.setString(2, base58PWD);
			pstmt.executeUpdate();
			
			
		} catch (NoSuchAlgorithmException | SQLException e) {
			e.printStackTrace();
		}
		
		writer.print("<html><body><h1>Registered successfully</h1></body></html>");
		doGet(request, response);
	}

}