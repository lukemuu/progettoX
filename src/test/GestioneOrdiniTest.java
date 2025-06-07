
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
	public void testInviaReportConPiuPescherieEUnSoloOrdine() throws Exception {
	    // Inserisci due pescherie nel database
	    try (Statement stmt = connection.createStatement()) {
	        stmt.execute("INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) " +
	                     "VALUES (1, 'Pescheria1', 'Via Roma 1', 'lukeesposito03@gmail.com', 'user1', 'pass1')");
	        stmt.execute("INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) " +
	                     "VALUES (2, 'Pescheria2', 'Via Roma 2', 'luca.pesacane7@gmail.com', 'user2', 'pass2')");
	    }
	
	    // Inserisci un ordine associato solo alla prima pescheria
	    try (Statement stmt = connection.createStatement()) {
	        stmt.execute("INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
	                     "VALUES (1, 1, 1, 101, CURRENT_DATE, 10.0, 10.0, 50.0, 'CONFERMATO')");
	    }
	
	    // Verifica che ci siano due pescherie nel database
	    List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
	    assertEquals("Dovrebbero esserci due pescherie", 2, listaPescherie.size());
	
	    // Verifica che ci sia un solo ordine nel database
	    List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	    assertEquals("Dovrebbe esserci un ordine", 1, listaOrdini.size());
	
	    // Esegui il metodo inviaReport
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Verifica l'output di System.out
	    String expectedOutputPescheria1 = "Report inviato a: lukeesposito03@gmail.com (Pescheria1)";
	    String expectedOutputPescheria2 = "Report inviato a: luca.pesacane7@gmail.com (Pescheria2)";
	    String output = outContent.toString();
	
	    assertTrue("Il report per Pescheria1 non è stato inviato correttamente", output.contains(expectedOutputPescheria1));
	    assertTrue("Il report vuoto per Pescheria2 non è stato inviato correttamente", output.contains(expectedOutputPescheria2));
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


