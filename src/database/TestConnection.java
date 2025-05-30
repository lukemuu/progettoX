package database;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TestConnection {
	
	public static void main(String[] args) {
		
		System.out.println("=== TEST CONNESSIONE DATABASE ===");
		
		try {
			// Test connessione
			Connection conn = DBManager.getConnection();
			
			if(conn != null && !conn.isClosed()) {
				System.out.println("✅ Connessione RIUSCITA!");
				
				// Informazioni sul database
				DatabaseMetaData metaData = conn.getMetaData();
				System.out.println("Database: " + metaData.getDatabaseProductName());
				System.out.println("Versione: " + metaData.getDatabaseProductVersion());
				System.out.println("URL: " + metaData.getURL());
				System.out.println("Username: " + metaData.getUserName());
				
			} else {
				System.out.println("❌ Connessione FALLITA!");
			}
			
		} catch (SQLException e) {
			System.out.println("❌ ERRORE di connessione:");
			System.out.println("Messaggio: " + e.getMessage());
			System.out.println("Codice errore: " + e.getErrorCode());
			e.printStackTrace();
		}
		
		// Chiudi connessione
		try {
			DBManager.closeConnection();
			System.out.println("🔒 Connessione chiusa correttamente");
		} catch (SQLException e) {
			System.out.println("⚠️ Errore nella chiusura: " + e.getMessage());
		}
		
		System.out.println("=== FINE TEST ===");
	}
}