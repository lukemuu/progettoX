
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
	public void testInviaReportConUnaPescheriaEVariOrdini() throws Exception {
	    // Inserisci una pescheria nel database
	    try (Statement stmt = connection.createStatement()) {
	        stmt.execute("INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) " +
	                     "VALUES (1, 'Pescheria Test', 'Via Test, 123', 'lukeesposito03@gmail.com', 'testuser', 'testpassword')");
	    }
	
	    // Inserisci vari ordini per la pescheria
	    try (Statement stmt = connection.createStatement()) {
	        stmt.execute("INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
	                     "VALUES (1, 1, 101, 201, '2025-06-06', 10.0, 10.0, 100.0, 'CONFERMATO')");
	        stmt.execute("INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
	                     "VALUES (2, 1, 102, 202, '2025-06-07', 5.0, 5.0, 50.0, 'CONFERMATO')");
	        stmt.execute("INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
	                     "VALUES (3, 1, 103, 203, '2025-06-08', 20.0, 20.0, 200.0, 'CONFERMATO')");
	    }
	
	    // Assicurati che ci sia una pescheria e tre ordini
	    List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
	    assertEquals("Dovrebbe esserci una pescheria", 1, listaPescherie.size());
	
	    List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	    assertEquals("Dovrebbero esserci tre ordini", 3, listaOrdini.size());
	
	    // Esegui il metodo inviaReport
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Verifica che l'output su System.out contenga il messaggio di report con gli ordini
	    String output = outContent.toString().trim();
	    assertTrue(output.contains("Report inviato a: lukeesposito03@gmail.com (Pescheria Test)"));
	    assertTrue(output.contains("Ordini trovati per Pescheria Test: 3"));
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


