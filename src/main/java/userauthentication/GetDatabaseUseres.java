package userauthentication;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetDatabaseUseres {
	static Connection con = null;
	static Statement stmt = null;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				con =  DriverManager.getConnection("jdbc:mysql://localhost/Users", "root", "Raut#0110");
				stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("MySql sever connected");
		ResultSet rs = stmt.executeQuery("select * from users");
		while(rs.next())
		  System.out.println(rs.getString(2) + " " + rs.getString(3));
	}

}
