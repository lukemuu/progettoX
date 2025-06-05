
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import entity.EntityPescheria;
import database.PescheriaDAO;
import database.DBManager;
import control.GestioneOrdini;

public class GestioneOrdiniTest {

    private Connection connection;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;


	@Before
	public void setUp() throws Exception {
	    try {
	        connection = DBManager.getConnection();
	        try (Statement stmt = connection.createStatement()) {
	            stmt.execute("CREATE TABLE IF NOT EXISTS PESCHERIA (id INT PRIMARY KEY, nome VARCHAR(255), email VARCHAR(255))");
	            stmt.execute("CREATE TABLE IF NOT EXISTS ORDINE (id INT PRIMARY KEY, pescheria_id INT, descrizione VARCHAR(255))");
	            stmt.execute("INSERT INTO ORDINE (id, pescheria_id, descrizione) VALUES (1, 1, 'Ordine1')");
	        }
	        System.setErr(new PrintStream(errContent));
	    } catch (Exception e) {
	        throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
	    }
	}
	
	@After
	public void tearDown() throws Exception {
	    try {
	        if (connection != null && !connection.isClosed()) {
	            try (Statement stmt = connection.createStatement()) {
	                stmt.execute("DROP TABLE IF EXISTS ORDINE");
	                stmt.execute("DROP TABLE IF EXISTS PESCHERIA");
	            }
	            connection.close();
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Errore durante la chiusura del database: " + e.getMessage(), e);
	    } finally {
	        System.setErr(originalErr);
	    }
	}


    @Test
    public void testIntegrazioneNessunaPescheria() throws Exception {
        // Simula il caso in cui non ci siano pescherie
        List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
        assertTrue("La lista delle pescherie dovrebbe essere vuota", listaPescherie.isEmpty());

        // Esegui il metodo inviaReport
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        gestioneOrdini.inviaReport();

        // Verifica l'output di System.err
        String expectedError = "Errore: Nessuna pescheria trovata. Il report non può essere inviato.";
        assertTrue(errContent.toString().contains(expectedError));
    }
}


