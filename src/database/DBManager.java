package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	
	private static Connection conn = null;
	
	private DBManager() {}
	
	public static Connection getConnection() throws SQLException {
		
		if(conn == null || conn.isClosed()) {
			
			String url = "jdbc:mysql://localhost:3306/progettox";
			String username = "root";
			String password = ""; // XAMPP default = password vuota
			
			conn = DriverManager.getConnection(url, username, password);
		}
		
		return conn;
	}
	
	public static void closeConnection() throws SQLException {
		if(conn != null && !conn.isClosed()) {
			conn.close();
		}
	}
}