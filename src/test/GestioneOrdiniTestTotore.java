package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import boundary.BoundaryRistorante;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import database.DBManager;

public class GestioneOrdiniTestTotore {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent)); // Reindirizza System.out
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Creazione della tabella PESCHERIA
            stmt.execute("CREATE TABLE IF NOT EXISTS PESCHERIA (" +
                         "IDPESCHERIA INT PRIMARY KEY, " +
                         "NOME VARCHAR(255), " +
                         "INDIRIZZO VARCHAR(255), " +
                         "EMAIL VARCHAR(255), " +
                         "USERNAME VARCHAR(255), " +
                         "PASSWORD VARCHAR(255))");

            // Creazione della tabella PRODOTTO
            stmt.execute("CREATE TABLE IF NOT EXISTS PRODOTTO (" +
                         "IDPRODOTTO INT PRIMARY KEY, " +
                         "CATEGORIA VARCHAR(255), " +
                         "TIPOLOGIA VARCHAR(255), " +
                         "DESCRIZIONE VARCHAR(255), " +
                         "PREZZO FLOAT, " +
                         "CODICEPAESE VARCHAR(255), " +
                         "IDPESCHERIA INT, " +
                         "FOREIGN KEY (IDPESCHERIA) REFERENCES PESCHERIA(IDPESCHERIA))");

            // Pulizia delle tabelle
            stmt.execute("DELETE FROM PRODOTTO");
            stmt.execute("DELETE FROM PESCHERIA");
            stmt.execute("DELETE FROM ORDINE");

            // Inserimento dati iniziali nella tabella PESCHERIA
            stmt.execute("INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) " +
                         "VALUES (1, 'Pescheria1', 'Via Roma 1', 'salvatoref140@gmail.com', 'user1', 'pass1')");
            stmt.execute("INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) " +
                         "VALUES (2, 'Pescheria2', 'Via Roma 2', 'sasferraro@libero.it', 'user2', 'pass2')");

            // Inserimento dati iniziali nella tabella PRODOTTO
            stmt.execute("INSERT INTO PRODOTTO (IDPRODOTTO, CATEGORIA, TIPOLOGIA, DESCRIZIONE, PREZZO, CODICEPAESE, IDPESCHERIA) " +
                         "VALUES (1, 'Categoria1', 'Tipologia1', 'Descrizione1', 10.0, 'IT', 1)");
            stmt.execute("INSERT INTO PRODOTTO (IDPRODOTTO, CATEGORIA, TIPOLOGIA, DESCRIZIONE, PREZZO, CODICEPAESE, IDPESCHERIA) " +
                         "VALUES (2, 'Categoria2', 'Tipologia2', 'Descrizione2', 20.0, 'US', 2)");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() throws Exception {
        try (Connection connection = DBManager.getConnection();
             Statement stmt = connection.createStatement()) {
            // Pulizia dei dati
            stmt.execute("DELETE FROM PRODOTTO");
            stmt.execute("DELETE FROM PESCHERIA");

            // Rimozione delle tabelle
            stmt.execute("DROP TABLE IF EXISTS PRODOTTO");
            stmt.execute("DROP TABLE IF EXISTS PESCHERIA");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la chiusura del database: " + e.getMessage(), e);
        } finally {
            // Ripristina System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testAcquistaProdottoInputValidi() {
        // Simula input validi
        String input = "1\n1\n5.0\nS\n1234567812345678\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Esegui il metodo del Boundary
        BoundaryRistorante.acquistaProdotto();

        // Cattura l'output generato
        String output = outContent.toString();

        // Stampa l'output per il debug
        System.out.println("Output generato:\n" + output);

        // Verifica che l'output contenga "Acquisto completato!"
        assertTrue("L'output non contiene 'Acquisto completato!'. Output ricevuto: " + output,
                   output.contains("Acquisto completato!"));
    }



    @Test
    public void testAcquistaProdottoIdPescheriaNonValido() {
        String input = "-1\n1\n5.0\n"; // Simula un ID pescheria non valido
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: l'ID della pescheria deve essere un intero positivo.")); // Verifica il messaggio di errore
    }

    @Test
    public void testAcquistaProdottoIdProdottoNonValido() {
        String input = "1\n-1\n5.0\n"; // Simula un ID prodotto non valido
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: l'ID del prodotto deve essere un intero positivo.")); // Verifica il messaggio di errore
    }

    @Test
    public void testAcquistaProdottoQuantitaNonValida() {
        String input = "1\n1\n-5.0\n"; // Simula una quantità non valida
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: la quantità deve essere un intero positivo.")); // Verifica il messaggio di errore
    }

    @Test
    public void testAcquistaProdottoIdPescheriaNonNumerico() {
        String input = "abc\n1\n5.0\n"; // Simula un ID pescheria non numerico
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: inserire un numero valido per l'ID della pescheria.")); // Verifica il messaggio di errore
    }

    @Test
    public void testAcquistaProdottoIdProdottoNonNumerico() {
        String input = "1\n@!\n5.0\n"; // Simula un ID prodotto non numerico
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: inserire un numero valido per l'ID del prodotto.")); // Verifica il messaggio di errore
    }

    @Test
    public void testAcquistaProdottoQuantitaNonNumerica() {
        String input = "1\n1\nxyz\n"; // Simula una quantità non numerica
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BoundaryRistorante.acquistaProdotto(); // Esegui il metodo del Boundary

        String output = outContent.toString();
        assertTrue(output.contains("Errore: inserire un numero valido per la quantità.")); // Verifica il messaggio di errore
    }
}