package control;

import database.OrdineDAO;
import database.ConsegnaDAO;
import database.PescheriaDAO;
import database.FattorinoDAO;
import entity.EntityOrdine;
import entity.EntityPescheria;
import entity.EntityFattorino;
import boundary.BoundaryCooperativa;

import exception.DAOException;
import exception.DBConnectionException;

import java.util.ArrayList;
import java.util.List;
import exception.OperationException;
import java.sql.Date;

public class GestioneOrdini {
	
	private static GestioneOrdini gO = null;
	
	protected GestioneOrdini(){

	}

	public static GestioneOrdini getInstance() 
	{ 
		if (gO == null) 
			gO = new GestioneOrdini(); 

		return gO; 
	}
	
	

    public void inviaReport() {
        try {
            // Ottieni la lista degli ordini degli ultimi 7 giorni
            List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniUltimaSettimana();

            // Ottieni la lista delle pescherie
            List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();

            // Per ogni pescheria, filtra gli ordini corrispondenti e invia il report
            for (EntityPescheria pescheria : listaPescherie) {
                String email = pescheria.getEmail();
                int IdPescheria = pescheria.getIdPescheria();

                List<EntityOrdine> ordiniPescheria = new ArrayList<>();

                // Filtra gli ordini per questa pescheria
                for (EntityOrdine ordine : listaOrdini) {
                    if (ordine.getIdPescheria() == IdPescheria) {
                        ordiniPescheria.add(ordine);
                    }
                }

                // Logica per inviare il report (da implementare)
                System.out.println("Report inviato a: " + email);
            }
        } catch (DAOException | DBConnectionException e) {
            System.err.println("Errore durante l'elaborazione del report: " + e.getMessage());
        }
    }
    public void modificaOrdine(int idOrdine, int quantitaAggiornata) throws OperationException, DAOException, DBConnectionException {

        if (quantitaAggiornata <= 0) {
            throw new OperationException("La quantità aggiornata deve essere un numero positivo.");
        }

        // Recupera la lista degli ordini dal database
        List<EntityOrdine> listaOrdini = OrdineDAO.readOrdini();

        // Cerca l'ordine con l'ID specificato
        EntityOrdine ordineDaModificare = null;
        for (EntityOrdine ordine : listaOrdini) {
            if (ordine.getIdOrdine() == idOrdine) {
                ordineDaModificare = ordine;
                break;
            }
        }

        if (ordineDaModificare == null) {
            throw new OperationException("Nessun ordine trovato con l'ID specificato.");
        }

        // Aggiorna la quantità aggiornata nell'oggetto ordine
        ordineDaModificare.setQtaAggiornata(quantitaAggiornata);

        // Aggiorna l'ordine nel database
        boolean successo = OrdineDAO.updateOrdine(ordineDaModificare);

        if (!successo) {
            throw new OperationException("Errore durante l'aggiornamento della quantità dell'ordine.");
        }

        System.out.println("Quantità aggiornata con successo per l'ordine con ID: " + idOrdine);
    }
    
////ANCORA DA IMPLEMENTARE QUI
    public void assegnaConsegna() throws OperationException, DAOException, DBConnectionException {
        // Recupera gli ordini e i fattorini
        List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniUltimoGiorno();
        List<EntityFattorino> listaFattorini = FattorinoDAO.readAllFattorini();

        // Controlla se le liste sono vuote
        if (listaOrdini.isEmpty()) {
            throw new OperationException("Nessun ordine disponibile per l'assegnazione.");
        }
        if (listaFattorini.isEmpty()) {
            throw new OperationException("Nessun fattorino disponibile per l'assegnazione.");
        }

        // Mostra le liste utilizzando i metodi della classe GestioneOrdini
        mostraListaOrdini(listaOrdini);
        mostraListaFattorini(listaFattorini);

        // Richiama la funzione per creare le consegne
        boolean successo = ConsegnaDAO.creaConsegna();
        if (!successo) {
            throw new OperationException("Errore durante l'assegnazione delle consegne.");
        }
    }
    
    public static void mostraListaOrdini(List<EntityOrdine> listaOrdini) {
        System.out.println("Lista degli ordini disponibili:");
        for (EntityOrdine ordine : listaOrdini) {
            System.out.println("ID Ordine: " + ordine.getIdOrdine() + ", Data: " + ordine.getData() + ", Quantità: " + ordine.getQta());
        }
    }
//FINO A QUI
    public static void mostraListaFattorini(List<EntityFattorino> listaFattorini) {
        System.out.println("Lista dei fattorini disponibili:");
        for (EntityFattorino fattorino : listaFattorini) {
            System.out.println("ID Fattorino: " + fattorino.getIdFattorino() + ", Nome: " + fattorino.getNome());
        }
    }
    
    // Metodi per salvare le scelte
    private static EntityOrdine ordineSelezionato;
    private static EntityFattorino fattorinoSelezionato;

    public static void setOrdineSelezionato(EntityOrdine ordine) {
        ordineSelezionato = ordine;
    }

    public static EntityOrdine getOrdineSelezionato() {
        return ordineSelezionato;
    }

    public static void setFattorinoSelezionato(EntityFattorino fattorino) {
        fattorinoSelezionato = fattorino;
    }

    public static EntityFattorino getFattorinoSelezionato() {
        return fattorinoSelezionato;
    }
	   

}
