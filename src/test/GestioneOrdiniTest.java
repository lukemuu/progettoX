
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

import entity.EntityOrdine;
import entity.EntityPescheria;
import database.PescheriaDAO;
import database.DBManager;
import database.OrdineDAO;
import control.GestioneOrdini;

public class GestioneOrdiniTest {

    private Connection connection;
    
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Before
	public void setUp() throws Exception {
		
	    try {
	        connection = DBManager.getConnection();
	        try (Statement stmt = connection.createStatement()) {
	        	
	            stmt.execute("CREATE TABLE IF NOT EXISTS PESCHERIA (IDPESCHERIA INT PRIMARY KEY, NOME VARCHAR(255), INDIRIZZO VARCHAR(255), EMAIL VARCHAR(255), USERNAME VARCHAR(255), PASSWORD VARCHAR(255))");
	            stmt.execute("CREATE TABLE IF NOT EXISTS ORDINE (IDORDINE INT PRIMARY KEY, IDPESCHERIA INT, IDRISTORANTE INT, IDPRODOTTO INT, DATA DATE, QTA DOUBLE, QTAAGGIORNATA DOUBLE, PREZZO FLOAT, STATO VARCHAR(255))");
	        }
	        System.setOut(new PrintStream(outContent)); // Reindirizza System.out
	        System.setErr(new PrintStream(errContent)); // Reindirizza System.err
	    } catch (Exception e) {
	        throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
	    }
	}
		
	@Test
	public void testInviaReportConZeroPescherieEZeroOrdini() throws Exception {
	    // Assicurati che non ci siano pescherie né ordini nel database
	    List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
	    assertTrue("Non dovrebbero esserci pescherie", listaPescherie.isEmpty());
	
	    List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	    assertTrue("Non dovrebbero esserci ordini", listaOrdini.isEmpty());
	
	    // Esegui il metodo inviaReport
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Verifica che l'output su System.err contenga il messaggio di errore
	    String errorOutput = errContent.toString().trim();
	    assertTrue(errorOutput.contains("Errore: Nessuna pescheria trovata. Il report non può essere inviato."));
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
	        System.setOut(originalOut); // Ripristina System.out
	        System.setErr(originalErr); // Ripristina System.err
	    }
	}



}


