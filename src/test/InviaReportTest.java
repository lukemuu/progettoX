package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import database.DBManager;
import database.OrdineDAO;
import database.PescheriaDAO;
import entity.EntityOrdine;
import entity.EntityPescheria;
import control.GestioneOrdini;
import java.util.List;

public class InviaReportTest {

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

	            // Elimina le tabelle esistenti
	            stmt.execute("DROP TABLE IF EXISTS ORDINE");
	            stmt.execute("DROP TABLE IF EXISTS PESCHERIA");
	
	            // Ricrea le tabelle
	            stmt.execute("CREATE TABLE PESCHERIA (IDPESCHERIA INT PRIMARY KEY, NOME VARCHAR(255), INDIRIZZO VARCHAR(255), EMAIL VARCHAR(255), USERNAME VARCHAR(255), PASSWORD VARCHAR(255))");
	            stmt.execute("CREATE TABLE ORDINE (IDORDINE INT PRIMARY KEY, IDPESCHERIA INT, IDRISTORANTE INT, IDPRODOTTO INT, DATA DATE, QTA DOUBLE, QTAAGGIORNATA DOUBLE, STATO VARCHAR(255), PREZZO FLOAT)");

	        }
	        System.setOut(new PrintStream(outContent));
	        System.setErr(new PrintStream(errContent));
	    } catch (Exception e) {
	        throw new RuntimeException("Errore durante la configurazione del database: " + e.getMessage(), e);
	    }
	    outContent.reset();
	    errContent.reset();
	}

    // Metodo helper per inserire pescheria tramite query
    private void insertPescheria(int id, String nome, String indirizzo, String email, String username, String password) throws Exception {
        String sql = "INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, nome);
            pstmt.setString(3, indirizzo);
            pstmt.setString(4, email);
            pstmt.setString(5, username);
            pstmt.setString(6, password);
            pstmt.executeUpdate();
        }
    }

    // Metodo helper per inserire ordine tramite query
    private void insertOrdine(int idOrdine, int idPescheria, int idRistorante, int idProdotto, String data, double qta, double qtaAggiornata, String stato, float prezzo) throws Exception {
        String sql = "INSERT INTO ORDINE (IDORDINE, IDPESCHERIA, IDRISTORANTE, IDPRODOTTO, DATA, QTA, QTAAGGIORNATA, STATO, PREZZO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idOrdine);
            pstmt.setInt(2, idPescheria);
            pstmt.setInt(3, idRistorante);
            pstmt.setInt(4, idProdotto);
            pstmt.setDate(5, java.sql.Date.valueOf(data));
            pstmt.setDouble(6, qta);
            pstmt.setDouble(7, qtaAggiornata);
            pstmt.setString(8, stato);
            pstmt.setFloat(9, prezzo);
            pstmt.executeUpdate();
        }
    }
    
    // TEST CASE 1: Nessuna pescheria registrata nel sistema
    @Test
    public void testCase1_NessunaPescheriaRegistrata() throws Exception {
        // Pre-condizioni: Database vuoto (nessuna pescheria registrata)
        List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
        assertTrue("Non dovrebbero esserci pescherie", listaPescherie.isEmpty());

        List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
        assertTrue("Non dovrebbero esserci ordini", listaOrdini.isEmpty());

        // Act
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        gestioneOrdini.inviaReport();

        // Output Attesi: "Errore: Nessuna pescheria trovata. Il report non può essere inviato."
        String errorOutput = errContent.toString().trim();
        assertTrue("Dovrebbe stampare messaggio di errore per nessuna pescheria", 
                   errorOutput.contains("Errore: Nessuna pescheria trovata"));

        // Post-condizioni Attese: Il sistema termina senza elaborare nessun report
        String standardOutput = outContent.toString();
        assertFalse("Non dovrebbe elaborare nessun report", 
                    standardOutput.contains("Report"));
    }
    
     // TEST CASE 2: Una sola pescheria registrata nel sistema, nessun ordine presente
	@Test
	public void testCase2_UnaPescheriaNessunOrdine() throws Exception {
	    // Pre-condizioni
	    insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");

	    // Verifica che non ci siano ordini
	    List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	    assertTrue("Non dovrebbero esserci ordini", listaOrdini.isEmpty());

	    // Act
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();

	    // Output generato
	    String output = outContent.toString();

	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente", 
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));

	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini", 
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 0"));

	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto", 
	               output.contains("Report vuoto inviato a: pescheriasolemio@gmail.com"));
	}

    // TEST CASE 3: Una sola pescheria registrata nel sistema, un ordine presente
    @Test
    public void testCase3_UnaPescheriaUnOrdine() throws Exception {
        // Pre-condizioni: Database con una pescheria e un ordine corrispondente
        // Setup pescheria
        insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");

        // Setup ordine
        insertOrdine(1, 1, 1, 1, "2025-06-05", 2.5, 1.0, "CONFERMATO", 10.5f);

        // Act
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        gestioneOrdini.inviaReport();

        // Output generato
	    String output = outContent.toString();

	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente", 
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));

	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini", 
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 1"));

	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto", 
	               output.contains("Report dettagliato inviato a: pescheriasolemio@gmail.com"));
    }

    // TEST CASE 4: Una sola pescheria registrata nel sistema, più ordini presenti
    @Test
    public void testCase4_UnaPescheriaPiuOrdini() throws Exception {
        // Pre-condizioni: Database con una pescheria e più ordini corrispondenti
        // Setup pescheria
        insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");

        // Setup multipli ordini
        insertOrdine(1, 1, 15, 34, "2025-06-05", 2.5, 1.0, "CONFERMATO", 10.5f);
        insertOrdine(2, 1, 1, 22, "2025-06-06", 4.5, 2.0, "CONFERMATO", 20.0f);
        insertOrdine(3, 1, 42, 26, "2025-06-08", 5.0, 2.0, "CONFERMATO", 10.0f);

        // Act
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        gestioneOrdini.inviaReport();

        
        // Output generato
	    String output = outContent.toString();

	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente", 
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));

	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini", 
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 3"));

	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto", 
	               output.contains("Report dettagliato inviato a: pescheriasolemio@gmail.com"));
    }

    // TEST CASE 5: Più pescherie registrate nel sistema, nessun ordine presente
	@Test
	public void testCase5_PiuPescherieNessunOrdine() throws Exception {
	    // Pre-condizioni
	    insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");
	    insertPescheria(2, "Pescheria La Paranza", "via Roma, 100", "pescheriaparanza@libero.it", "pescheriaparanza6", "Ciro2009");
	
	    // Act
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Output generato
	    String output = outContent.toString();
	    System.out.println("Output generato:\n" + output);
	
	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini",
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 0"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto",
	               output.contains("Report vuoto inviato a: pescheriasolemio@gmail.com"));
	
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria La Paranza (ID: 2)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini",
	               output.contains("Ordini trovati per Pescheria La Paranza: 0"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto",
	               output.contains("Report vuoto inviato a: pescheriaparanza@libero.it"));
	}


    // TEST CASE 6: Più pescherie registrate nel sistema, un solo ordine presente
	@Test
	public void testCase6_PiuPescherieUnOrdine() throws Exception {
	    // Pre-condizioni
	    insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");
	    insertPescheria(2, "Pescheria La Paranza", "via Roma, 100", "pescheriaparanza@libero.it", "pescheriaparanza6", "Ciro2009");
	    insertOrdine(1, 1, 1, 1, "2025-06-05", 2.5, 1.0, "CONFERMATO", 10.5f);
	
	    // Act
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Output generato
	    String output = outContent.toString();
	    System.out.println("Output generato:\n" + output);
	
	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 1 ordine",
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 1"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report dettagliato",
	               output.contains("Report dettagliato inviato a: pescheriasolemio@gmail.com"));
	
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria La Paranza (ID: 2)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 0 ordini",
	               output.contains("Ordini trovati per Pescheria La Paranza: 0"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report vuoto",
	               output.contains("Report vuoto inviato a: pescheriaparanza@libero.it"));
	}


    // TEST CASE 7: Più pescherie registrate nel sistema, più ordini presenti tra di esse
	@Test
	public void testCase7_PiuPescheriePiuOrdini() throws Exception {
	    // Pre-condizioni
	    insertPescheria(1, "Pescheria O'Sole Mio", "via dei mille, 39", "pescheriasolemio@gmail.com", "pescheriasolemio10", "Maradona10");
	    insertPescheria(2, "Pescheria La Paranza", "via Roma, 100", "pescheriaparanza@libero.it", "pescheriaparanza6", "Ciro2009");
	    insertOrdine(1, 2, 15, 34, "2025-06-05", 2.5, 1.0, "CONFERMATO", 10.5f);
        insertOrdine(2, 1, 12, 22, "2025-06-06", 4.5, 2.0, "CONFERMATO", 20.0f);
        insertOrdine(3, 1, 42, 26, "2025-06-08", 5.0, 1.0, "CONFERMATO", 10.0f);
	
	    // Act
	    GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
	    gestioneOrdini.inviaReport();
	
	    // Output generato
	    String output = outContent.toString();
	    System.out.println("Output generato:\n" + output);
	
	    // Verifiche
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria O'Sole Mio (ID: 1)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 2 ordine",
	               output.contains("Ordini trovati per Pescheria O'Sole Mio: 2"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report dettagliato",
	               output.contains("Report dettagliato inviato a: pescheriasolemio@gmail.com"));
	
	    assertTrue("Dovrebbe indicare la pescheria correttamente",
	               output.contains("Pescheria: Pescheria La Paranza (ID: 2)"));
	    assertTrue("Dovrebbe indicare che sono stati trovati 1 ordini",
	               output.contains("Ordini trovati per Pescheria La Paranza: 1"));
	    assertTrue("Dovrebbe indicare che è stato inviato un report dettagliato",
	               output.contains("Report dettagliato inviato a: pescheriaparanza@libero.it"));
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
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}

