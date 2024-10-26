package dspackage;

public class JNDIDataSource {
	
		 private static final long serialVersionUID = 3L;
		 String name = "jdbc/Users";
		 String auth = "Container";
		 String type = "javax.sql.DataSource";
	     public String maxActive = "50";
	     String maxIdle = "30";
	     String maxWait = "10000";
	     String username = "root";
	     public String password = "Raut#0110"; 
	     String driverClassName = "com.mysql.jdbc.Driver";
	     String url = "jdbc:mysql://localhost:3306/Clients";
}
