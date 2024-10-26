import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientDataBank {

	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try { 

			con =  DriverManager.getConnection("jdbc:mysql://localhost/Clients", "root", "Raut#0110");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			/*
			String sql = "Create Database if not exists Clients";
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully... ");
			 */

			/*
			String sql = "Create table if not exists Clients("
					+ "clientID varchar(100) not null," 
					+ "name text not null,"
					+ "redirectURI text not null,"
					+ "primary key (clientID)"
					+ ")";
			stmt.executeUpdate(sql);
			System.out.println("Client Table created successfully... ");			
			 */

			/*
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Simran', 's123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Imran', 'i123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Kimran', 'k123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Rimran', 'r123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Zimran', 'z123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('pimran', 'p123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('wimran', 'w123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('bimran', 'b123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Timran', 't123')");
			stmt.executeUpdate("Insert into users2(uname, pwd)"
					+ "values('Himran', 'h123')");

			System.out.println("Table values updated");
			 */

			/*
            ResultSet rs = stmt.executeQuery("select * from users2");

			while(rs.next())
		       System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));

			String username = request.getParameter("User");
			//System.out.println("uname is " + username);

			//ResultSet rs = stmt.executeQuery("select * from users2 where uname = '"+username+"'");
			ResultSet rs = stmt.executeQuery("select count(*) as count from users2 where uname = '"+username+"'");
			while(rs.next())
			  System.out.println("reached " + rs.getInt("count"));

			 */

		}catch (SQLException e) { 
			e.printStackTrace();	
		}


	}

}
