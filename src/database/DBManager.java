package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	
	private static Connection conn = null;
	
	private DBManager() {}
	
	public static Connection getConnection() throws SQLException {
		
		if(conn == null || conn.isClosed()) {
			// URL pubblico Railway per JDBC MySQL
			String url = "jdbc:mysql://mainline.proxy.rlwy.net:25471/railway?useSSL=false&allowPublicKeyRetrieval=true";
			String username = "root";
			String password = "QzPrYKWhlruHabkPdGBULMjsnvOeBAWn";
			
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