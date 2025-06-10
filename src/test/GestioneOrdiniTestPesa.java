package test;

import control.GestioneOrdini;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import database.DBManager;
import boundary.BoundaryCooperativa;

public class GestioneOrdiniTestPesa {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent)); // Reindirizza System.out
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Creazione della tabella FATTORINO
            stmt.execute("CREATE TABLE IF NOT EXISTS FATTORINO (" +
                         "IDFATTORINO INT PRIMARY KEY, " +
                         "NOME VARCHAR(255), " +
                         "USERNAME VARCHAR(255), " +
                         "PASSWORD VARCHAR(255), " +
                         "STATO VARCHAR(255))");

            // Creazione della tabella ORDINE
            stmt.execute("CREATE TABLE IF NOT EXISTS ORDINE (" +
                         "IDORDINE INT PRIMARY KEY, " +
                         "IDRISTORANTE INT, " +
                         "IDPESCHERIA INT, " +
                         "IDPRODOTTO INT, " +
                         "DATA DATE, " +
                         "QTA DOUBLE, " +
                         "QTAAGGIORNATA DOUBLE, " +
                         "PREZZO FLOAT, " +
                         "STATO VARCHAR(255))");

            // Creazione della tabella CONSEGNA
            stmt.execute("CREATE TABLE IF NOT EXISTS CONSEGNA (" +
                         "IDCONSEGNA INT PRIMARY KEY AUTO_INCREMENT, " +
                         "IDORDINE INT NOT NULL, " +
                         "IDFATTORINO INT NOT NULL, " +
                         "DATA DATE NOT NULL, " +
                         "FOREIGN KEY (IDORDINE) REFERENCES ORDINE(IDORDINE), " +
                         "FOREIGN KEY (IDFATTORINO) REFERENCES FATTORINO(IDFATTORINO))");

            // Pulizia delle tabelle
            stmt.execute("DELETE FROM FATTORINO");
            stmt.execute("DELETE FROM ORDINE");
            stmt.execute("DELETE FROM CONSEGNA");

            // Inserimento dati iniziali nella tabella FATTORINO
            stmt.execute("INSERT INTO FATTORINO (IDFATTORINO, NOME, USERNAME, PASSWORD, STATO) " +
                         "VALUES (1, 'Mario Rossi', 'mario.rossi', 'password123', 'DISPONIBILE')");

            // Inserimento dati iniziali nella tabella ORDINE
            stmt.execute("INSERT INTO ORDINE (IDORDINE, IDRISTORANTE, IDPESCHERIA, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
                         "VALUES (1, 1, 1, 101, CURRENT_DATE, 10.0, 10.0, 50.0, 'CONFERMATO')");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
        }
    }
    
    @Test
    public void testAssegnaConsegnaControl() {
        try {
            System.out.println("Test del control layer");
            
            // Testa direttamente il metodo del control
            // Usa gli ID che hai inserito nel setup: fattorino ID=1, ordine ID=1
            GestioneOrdini.assegnaConsegna(1, 1);
            
            // Verifica che la consegna sia stata creata nel database
            try (Connection connection = DBManager.getConnection();
                 Statement stmt = connection.createStatement()) {
                
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM CONSEGNA WHERE IDORDINE = 1 AND IDFATTORINO = 1");
                rs.next();
                int consegneCount = rs.getInt(1);
                
                assertTrue("Dovrebbe essere stata creata una consegna", consegneCount > 0);
                System.out.println("Test superato: consegna creata correttamente");
                
            } catch (Exception dbException) {
                fail("Errore nella verifica del database: " + dbException.getMessage());
            }
            
        } catch (Exception e) {
            fail("Errore durante l'assegnazione della consegna: " + e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Pulizia dei dati
            stmt.execute("DELETE FROM CONSEGNA");
            stmt.execute("DELETE FROM ORDINE");
            stmt.execute("DELETE FROM FATTORINO");

            // Rimozione delle tabelle
            stmt.execute("DROP TABLE IF EXISTS CONSEGNA");
            stmt.execute("DROP TABLE IF EXISTS ORDINE");
            stmt.execute("DROP TABLE IF EXISTS FATTORINO");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la chiusura del database: " + e.getMessage(), e);
        } finally {
            System.setOut(originalOut); // Ripristina System.out
        }
    }
}
