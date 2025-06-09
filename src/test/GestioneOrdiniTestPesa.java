package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import boundary.BoundaryCooperativa;
import database.DBManager;

public class GestioneOrdiniTestPesa{

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent)); // Reindirizza System.out
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
        	stmt.execute("DROP TABLE IF EXISTS ORDINE");
            stmt.execute("DROP TABLE IF EXISTS FATTORINO");
            stmt.execute("DROP TABLE IF EXISTS CONSEGNA");
            // Creazione delle tabelle necessarie
        	
            stmt.execute("CREATE TABLE IF NOT EXISTS FATTORINO (" +
                         "IDFATTORINO INT PRIMARY KEY, " +
                         "NOME VARCHAR(255) NOT NULL, " +
                         "USERNAME VARCHAR(255) NOT NULL UNIQUE, " +
                         "PASSWORD VARCHAR(255) NOT NULL, " +
                         "STATO ENUM('DISPONIBILE', 'OCCUPATO') NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS ORDINE (" +
                         "IDORDINE INT PRIMARY KEY, " +
                         "IDRISTORANTE INT NOT NULL, " +
                         "IDPESCHERIA INT NOT NULL, " +
                         "IDPRODOTTO INT NOT NULL, " +
                         "DATA DATE NOT NULL, " +
                         "QTA DOUBLE NOT NULL, " +
                         "QTAAGGIORNATA DOUBLE, " +
                         "PREZZO FLOAT NOT NULL, " +
                         "STATO ENUM('IN_TRATTATIVA', 'CONFERMATO', 'ASSEGNATO', 'CONSEGNATO') NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS CONSEGNA (" +
                         "IDCONSEGNA INT PRIMARY KEY AUTO_INCREMENT, " +
                         "IDORDINE INT NOT NULL, " +
                         "IDFATTORINO INT NOT NULL, " +
                         "DATA DATE NOT NULL, ");

            // Pulizia delle tabelle
            stmt.execute("DELETE FROM FATTORINO");
            stmt.execute("DELETE FROM ORDINE");
            stmt.execute("DELETE FROM CONSEGNA");

            // Inserimento dati iniziali
            stmt.execute("INSERT INTO FATTORINO (IDFATTORINO, NOME, USERNAME, PASSWORD, STATO) VALUES " +
                         "(1, 'Mario Rossi', 'mario.rossi', 'password123', 'DISPONIBILE')");
            stmt.execute("INSERT INTO ORDINE (IDORDINE, IDRISTORANTE, IDPESCHERIA, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) VALUES " +
                         "(1, 1, 1, 1, CURRENT_DATE, 10.0, 10.0, 50.0, 'CONFERMATO')");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() throws Exception {
    	try (Connection connection = DBManager.getConnection();
    		     Statement stmt = connection.createStatement()) {
    		    // Elimina prima i dati dalla tabella CONSEGNA
    		    stmt.execute("DELETE FROM CONSEGNA");

    		    // Poi elimina i dati dalla tabella FATTORINO
    		    stmt.execute("DELETE FROM FATTORINO");

    		    // Infine elimina i dati dalla tabella ORDINE
    		    stmt.execute("DELETE FROM ORDINE");

    		    // Rimuovi le tabelle
    		    stmt.execute("DROP TABLE IF EXISTS CONSEGNA");
    		    stmt.execute("DROP TABLE IF EXISTS FATTORINO");
    		    stmt.execute("DROP TABLE IF EXISTS ORDINE");
    		} catch (Exception e) {
    		    throw new RuntimeException("Errore durante la chiusura del database: " + e.getMessage(), e);
    		} finally {
    		    System.setOut(originalOut);
    		}
    }

    @Test
    public void testAssegnaConsegnaInputValidi() {
        // Simula input validi
        String input = "1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Esegui il metodo del Boundary
        BoundaryCooperativa.assegnaConsegna();

        // Cattura l'output generato
        String output = outContent.toString();

        // Verifica che l'output contenga "Consegna assegnata con successo"
        assertTrue("L'output non contiene 'Consegna assegnata con successo'. Output ricevuto: " + output,
                   output.contains("Consegna assegnata con successo"));

        // Verifica che la consegna sia stata registrata nel database
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CONSEGNA WHERE IDORDINE = 1 AND IDFATTORINO = 1")) {
            assertTrue("La consegna non è stata registrata nel database.", rs.next());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la verifica del database: " + e.getMessage(), e);
        }
    }
}
