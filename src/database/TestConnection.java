
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println("=== TEST CONNESSIONE DATABASE ===");

        try {
            // Test connessione
            Connection conn = DBManager.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connessione RIUSCITA!");

                // Informazioni sul database
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Database: " + metaData.getDatabaseProductName());
                System.out.println("Versione: " + metaData.getDatabaseProductVersion());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("Username: " + metaData.getUserName());

                // Elimina le tabelle
                eliminaTabelle(conn);
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

    private static void eliminaTabelle(Connection conn) {
        System.out.println("=== ELIMINAZIONE TABELLE ===");

        String[] tables = {"PESCHERIA", "FATTORINO", "ORDINE", "CONSEGNA", "RISTORANTE", "PRODOTTO"};

        try (Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                try {
                    stmt.executeUpdate("DROP TABLE IF EXISTS " + table);
                    System.out.println("✅ Tabella " + table + " eliminata.");
                } catch (SQLException e) {
                    System.out.println("❌ Errore durante l'eliminazione della tabella " + table + ": " + e.getMessage());
                }
            }

            // Verifica se le tabelle sono state eliminate
            DatabaseMetaData metaData = conn.getMetaData();
            for (String table : tables) {
                try (var rs = metaData.getTables(null, null, table, null)) {
                    if (!rs.next()) {
                        System.out.println("✅ Tabella " + table + " non esiste più.");
                    } else {
                        System.out.println("⚠️ Tabella " + table + " esiste ancora.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ ERRORE durante l'eliminazione delle tabelle:");
            System.out.println("Messaggio: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FINE ELIMINAZIONE ===");
    }
}
