package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import boundary.BoundaryPescheria;
import java.sql.Connection;
import java.sql.Statement;
import database.DBManager;

public class ModificaOrdineTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent)); // Reindirizza System.out
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Creazione della tabella ORDINE
            stmt.execute("CREATE TABLE IF NOT EXISTS ORDINE (" +
                         "IDORDINE INT PRIMARY KEY, " +
                         "IDPESCHERIA INT, " +
                         "IDRISTORANTE INT, " +
                         "IDPRODOTTO INT, " +
                         "DATA DATE, " +
                         "QTA DOUBLE, " +
                         "QTAAGGIORNATA DOUBLE, " +
                         "PREZZO FLOAT, " +
                         "STATO VARCHAR(255))");

            // Pulizia della tabella
            stmt.execute("DELETE FROM ORDINE");

            // Inserimento dati iniziali nella tabella ORDINE
            stmt.execute("INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, PREZZO, STATO) " +
                         "VALUES (1, 1, 1, 101, CURRENT_DATE, 10.0, 10.0, 50.0, 'IN_TRATTATIVA')");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() throws Exception {
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Pulizia dei dati
            stmt.execute("DELETE FROM ORDINE");

            // Rimozione della tabella
            stmt.execute("DROP TABLE IF EXISTS ORDINE");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la chiusura del database: " + e.getMessage(), e);
        } finally {
            // Ripristina System.out
        	System.setIn(System.in);
            System.setOut(originalOut);
        }
    }

    /*@Test
    public void testModificaOrdineInputValidi() {
        
        String input = "1\n1\n5.0\n100.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        
        BoundaryPescheria.modificaOrdine();

        
        String output = outContent.toString();

        
        System.out.println("Output generato:\n" + output);

        
        assertTrue("L'output non contiene 'Ordine aggiornato con successo'. Output ricevuto: " + output,
                   output.contains("Ordine aggiornato con successo"));
    }*/

    /*@Test
    public void testModificaOrdineIdPescheriaNegativo() {
        String input = "-1\n1\n5.0\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, l'ID della pescheria deve essere un numero positivo."));
    }*/

    /*@Test
    public void testModificaOrdineIdPescheriaNonIntero() {
        String input = "@\n1\n5.0\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, inserire un ID valido (numero intero)."));
    }/*

    /*@Test
    public void testModificaOrdineIdOrdineNegativo() {
        String input = "1\n-1\n5.0\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, l'ID dell'ordine deve essere un numero positivo."));
    }*/

    /*@Test
    public void testModificaOrdineIdOrdineNonIntero() {
        String input = "1\n@\n5.0\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, inserire un ID valido (numero intero)."));
    }*/

    /*@Test
    public void testModificaOrdineQuantitaAggiornataNegativa() {
        String input = "1\n1\n-5.0\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, la quantità deve essere un numero positivo."));
    }/*

    /*@Test
    public void testModificaOrdineQuantitaAggiornataNonDouble() {
        String input = "1\n1\n@\n100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, inserire un numero valido."));
    }*/

    /*@Test
    public void testModificaOrdinePrezzoNegativo() {
        String input = "1\n1\n5.0\n-100.0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, il prezzo deve essere un valore positivo."));
    }*/

    /*@Test
    public void testModificaOrdinePrezzoNonFloat() {
        String input = "1\n1\n5.0\n@\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryPescheria.modificaOrdine(); 

        String output = outContent.toString();
        assertTrue(output.contains("Errore, inserire un valore valido."));
    }
}*/

	@Test
	public void testModificaOrdineNessunOrdineTrovato() {
	    
	    String input = "999\n1\n5.0\n100.0\n"; 
	    System.setIn(new ByteArrayInputStream(input.getBytes()));
	
	    
	    BoundaryPescheria.modificaOrdine();
	
	    
	    String output = outContent.toString();
	
	    
	    assertTrue(output.contains("Nessun ordine trovato con l'ID specificato in stato 'In trattativa' per la pescheria indicata."));
	}

}