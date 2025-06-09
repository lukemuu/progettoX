package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import control.GestioneOrdini;

import java.util.List;

import static org.junit.Assert.*;

import entity.EntityFattorino;
import entity.EntityOrdine;
import database.FattorinoDAO;
import database.OrdineDAO;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;

public class GestioneOrdiniTestPesa {

    @Before
    public void setUp() throws Exception {
        // Configurazione iniziale: inserimento dati di test nel database
        EntityFattorino fattorino = new EntityFattorino("Test Fattorino", "testuser", "testpassword");
        FattorinoDAO.createFattorino(fattorino);

        EntityOrdine ordine = new EntityOrdine(1, 1, 1, new java.util.Date(), 10.0, 100.0f);
        OrdineDAO.createOrdine(ordine);
    }

    @After
    public void tearDown() throws Exception {
        // Pulizia finale: rimozione dati di test dal database
        List<EntityFattorino> fattorini = FattorinoDAO.readAllFattorini();
        for (EntityFattorino fattorino : fattorini) {
            FattorinoDAO.updateFattorino(fattorino); // Simula la rimozione
        }

        List<EntityOrdine> ordini = OrdineDAO.readOrdini();
        for (EntityOrdine ordine : ordini) {
            OrdineDAO.deleteOrdine(ordine.getIdOrdine());
        }
    }

    @Test
    public void testAssegnaConsegna() throws OperationException {
        try {
            // Recupera i fattorini e gli ordini disponibili
            List<EntityFattorino> fattorini = FattorinoDAO.readAllFattorini();
            List<EntityOrdine> ordini = OrdineDAO.readOrdiniUltimoGiorno();

            assertFalse("Nessun fattorino disponibile", fattorini.isEmpty());
            assertFalse("Nessun ordine disponibile", ordini.isEmpty());

            // Seleziona il primo fattorino e il primo ordine
            EntityFattorino fattorinoSelezionato = fattorini.get(0);
            EntityOrdine ordineSelezionato = ordini.get(0);

            // Esegui l'assegnazione della consegna
            GestioneOrdini.assegnaConsegna(ordineSelezionato, fattorinoSelezionato);

            // Verifica che lo stato dell'ordine sia stato aggiornato
            assertEquals("Lo stato dell'ordine non è stato aggiornato correttamente",
                    EntityOrdine.StatoOrdine.ASSEGNATO, ordineSelezionato.getStato());

        } catch (DAOException | DBConnectionException e) {
            fail("Errore durante il test: " + e.getMessage());
        }
    }
}
