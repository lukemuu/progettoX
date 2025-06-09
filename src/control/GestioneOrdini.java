package control;
 
import database.OrdineDAO;
import database.ConsegnaDAO;
import database.PescheriaDAO;
import database.FattorinoDAO;
import database.ProdottoDAO;
import entity.EntityOrdine;
import entity.EntityPescheria;
import entity.EntityProdotto;
import entity.EntityFattorino;
import boundary.BoundaryCooperativa;

 
import exception.DAOException;
import exception.DBConnectionException;
 
import java.util.ArrayList;
import java.util.List;
 
import javax.mail.MessagingException;
 
import exception.OperationException;
import java.sql.Date;
 
public class GestioneOrdini {
	
	private static GestioneOrdini gO = null;

	public GestioneOrdini(){
 
	}
 
	public static GestioneOrdini getInstance(){ 
		if (gO == null) 
			gO = new GestioneOrdini();
 
		return gO; 
	}

	public void inviaReport() {
	    try {
	        // Ottieni la lista delle pescherie
	        List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
	
	        // Controlla se non ci sono pescherie
	        if (listaPescherie == null || listaPescherie.isEmpty()) {
	            System.err.println("Errore: Nessuna pescheria trovata. Il report non può essere inviato.");
	            return;
	        }
	
	        // Ottieni la lista degli ordini degli ultimi 7 giorni
	        List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	
	        // Configura il servizio email
	        EmailService emailService = new EmailService();
	

			for (EntityPescheria pescheria : listaPescherie) {
			    String email = pescheria.getEmail();
			    String nomePescheria = pescheria.getNome();
			    int idPescheria = pescheria.getIdPescheria();
			
			    System.out.println("Pescheria: " + nomePescheria + " (ID: " + idPescheria + ")");
			
			    // Filtra gli ordini per questa pescheria
			    List<EntityOrdine> ordiniPescheria = new ArrayList<>();
			    for (EntityOrdine ordine : listaOrdini) {
					if (ordine.getIdPescheria() == pescheria.getIdPescheria()) {
					    ordiniPescheria.add(ordine);
					}
			    }
			
			    System.out.println("Ordini trovati per " + nomePescheria + ": " + ordiniPescheria.size());
			
			    try {
			        if (ordiniPescheria.isEmpty()) {
			            emailService.inviaReportVuoto(email, nomePescheria);
			            System.out.println("Report vuoto inviato a: " + email + " (" + nomePescheria + ")");
			        } else {
			            emailService.inviaReportOrdini(email, nomePescheria, ordiniPescheria);
			            System.out.println("Report dettagliato inviato a: " + email + " (" + nomePescheria + ")");
			        }
			    } catch (MessagingException e) {
			        System.err.println("Errore nell'invio dell'email a " + email + ": " + e.getMessage());
			    }

	        }
	    } catch (DAOException | DBConnectionException e) {
	        System.err.println("Errore durante l'elaborazione del report: " + e.getMessage());
	    }
	}


 
    



public void modificaOrdine(int idPescheria, int idOrdine, Double quantitaAggiornata, float nuovoPrezzo) throws OperationException, DAOException, DBConnectionException {

    // Recupera la lista degli ordini in trattativa per la pescheria specificata
    List<EntityOrdine> listaOrdini = OrdineDAO.readOrdinibyPescheria_inTrattativa(idPescheria);

    // Cerca l'ordine con l'ID specificato
    EntityOrdine ordineDaModificare = null;
    for (EntityOrdine ordine : listaOrdini) {
        if (ordine.getIdOrdine() == idOrdine) {
            ordineDaModificare = ordine;
            break;
        }
    }

    if (ordineDaModificare == null) {
        throw new OperationException("Nessun ordine trovato con l'ID specificato in stato 'In trattativa' per la pescheria indicata.");
    }

    // Aggiorna la quantità e il prezzo nell'oggetto ordine
    ordineDaModificare.setQtaAggiornata(quantitaAggiornata);
    ordineDaModificare.setPrezzo(nuovoPrezzo);

    // Aggiorna l'ordine nel database
    boolean successo = OrdineDAO.updateOrdine(ordineDaModificare);

    if (!successo) {
        throw new OperationException("Errore durante l'aggiornamento dell'ordine.");
    }

    System.out.println("Ordine aggiornato con successo: ID Ordine = " + idOrdine + ", Nuova Quantità = " + quantitaAggiornata + ", Nuovo Prezzo = " + nuovoPrezzo);
}


	
	public static List<EntityFattorino> stampaListaFattorini() {
		
	    List<EntityFattorino> fattorini = new ArrayList<>();
	    
	    try {
	        // Recupera la lista di tutti i fattorini
	        fattorini = FattorinoDAO.readAllFattorini();
	        
	        System.out.println("========================================");
	        
	        if (fattorini.isEmpty()) {
	            System.out.println("Nessun fattorino disponibile.");
	        }
	        
	        System.out.println("========================================");
	        
	    } catch (DBConnectionException e) {
	        System.out.println("Errore di connessione al database: " + e.getMessage());
	        fattorini = new ArrayList<>(); // Ritorna lista vuota in caso di errore
	    } catch (DAOException e) {
	        System.out.println("Errore durante il recupero dei dati: " + e.getMessage());
	        fattorini = new ArrayList<>(); // Ritorna lista vuota in caso di errore
	    }
	    
	    return fattorini;
	}
	
	public static List<EntityOrdine> stampaListaOrdini() {
		
		List<EntityOrdine> ordini = new ArrayList<>();
		
		try {
			ordini = OrdineDAO.readOrdiniUltimoGiorno();
			
			System.out.println("========================================");
			
	        if (ordini.isEmpty()) {
	            System.out.println("Nessun ordine disponibile.");
	        }
	        
	        System.out.println("========================================");
	        
		} catch (DBConnectionException e) {
			System.out.println("Errore di connessione al database: " + e.getMessage());
			ordini = new ArrayList<>();
		} catch (DAOException e) {
			System.out.println("Errore durante il recupero dei dati: " + e.getMessage());
			ordini = new ArrayList<>();
		}	
		
        return ordini;
	}
 
    public static void assegnaConsegna(int idOrdine, int idFattorino)
            throws OperationException, DAOException, DBConnectionException {
 
        // Controlla se ordine e fattorino sono stati selezionati
        /*if (ordineSelezionato == null) {
            throw new OperationException("Nessun ordine selezionato.");
        }
        if (fattorinoSelezionato == null) {
            throw new OperationException("Nessun fattorino selezionato.");
        }*/
 
        // Richiama la funzione per creare la consegna
        boolean successo = ConsegnaDAO.creaConsegna(idOrdine, idFattorino, new Date(System.currentTimeMillis()));
 
        if (!successo) {
            throw new OperationException("Errore durante l'assegnazione della consegna.");
        }
 
        // Stampa un riepilogo delle scelte selezionate
        stampaScelteSelezionate(idOrdine, idFattorino);
    }
    
    /* Metodi per salvare le scelte
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
    }*/

 
    private static void stampaScelteSelezionate(int idOrdine, int idFattorino) throws OperationException, DAOException, DBConnectionException {
        System.out.println("========================================");
        System.out.println("          Riepilogo Scelte Utente       ");
        System.out.println("========================================");
 
        System.out.println("Ordine Selezionato:");
        System.out.println("  - ID Ordine: " + idOrdine);
 
        System.out.println("----------------------------------------");
 
        System.out.println("Fattorino Selezionato:");
        System.out.println("  - ID Fattorino: " + idFattorino);
 
        System.out.println("========================================");
    }
    
    
 
	public ArrayList<String> acquistaProdotto(int idPescheria, int idProdotto, double quantita,int idRistorante) throws OperationException {
        EntityProdotto prodotto = null;
        float prezzoTotale = 0;
 
        ArrayList<String> returnList = new ArrayList<>();
        returnList.add("0"); // Prezzo totale
        returnList.add("null"); // Dettagli ordine temporaneo
        returnList.add("0"); // ID ordine
 
        try {
            // Controllo esistenza prodotto
            prodotto = ProdottoDAO.readProdotto(idPescheria, idProdotto);
 
            if (prodotto == null) {
                throw new OperationException("Prodotto non trovato");
            }
 
            // Calcolo prezzo totale
            prezzoTotale = calcolaPrezzo(prodotto.getPrezzo(), quantita);
            returnList.set(0, String.valueOf(prezzoTotale));
            
            
            // Creazione ordine temporaneo
            EntityOrdine nuovoOrdine = new EntityOrdine(
            	    idRistorante,          // ID del ristorante (da aggiungere come parametro)
            	    idPescheria,
            	    idProdotto, // Data corrente
            	    new Date(System.currentTimeMillis()),
            	    quantita,
            	    prezzoTotale
            	);
 
            // Salva l'ordine temporaneo  in una lista temporanea
            OrdineDAO.createOrdine(nuovoOrdine);

            // Aggiungi l'ID dell'ordine alla lista di ritorno
            returnList.set(2, String.valueOf(nuovoOrdine.getIdOrdine()));
 
        } catch (DBConnectionException dbEx) {
            throw new OperationException("Errore di connessione al database");
        } catch (DAOException ex) {
            throw new OperationException("Errore durante l'elaborazione dell'ordine");
        }
 
        return returnList;
    }
 
    private float calcolaPrezzo(float prezzoUnitario, double quantita) {
        return prezzoUnitario * (float)quantita;
    }  
    
    public void inviaOrdine(int idPescheria, int codProdotto, double quantita,int idOrdine,float prezzo) throws OperationException {
        try {
            // Recupera la pescheria in base all'ID
            EntityPescheria pescheria = PescheriaDAO.readPescheria(String.valueOf(idPescheria));
            EmailService emailService = new EmailService();
            if (pescheria == null) {
                throw new OperationException("Pescheria non trovata con l'ID specificato.");
            }

            // Recupera l'e-mail della pescheria
            String emailPescheria = pescheria.getEmail();

            try {
                emailService.inviaMail(emailPescheria, codProdotto, quantita,idOrdine,prezzo);
            } catch (MessagingException e) {
                throw new OperationException("Errore durante l'invio dell'e-mail: " + e.getMessage());
            }

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante la selezione della pescheria: " + e.getMessage());
        }
    }
    

public void confermaOrdine(int idOrdine) throws OperationException {
    try {
        // Converte la stringa "Confermato" nel valore dell'enum EntityOrdine.StatoOrdine
        EntityOrdine.StatoOrdine statoConfermato = EntityOrdine.StatoOrdine.valueOf("CONFERMATO");

        // Aggiorna lo stato dell'ordine a "Confermato"
        boolean statoAggiornato = OrdineDAO.updateStatoOrdine(idOrdine, statoConfermato);
        if (!statoAggiornato) {
            throw new OperationException("Errore durante l'aggiornamento dello stato dell'ordine.");
        }
    } catch (IllegalArgumentException e) {
        throw new OperationException("Valore dello stato non valido: " + e.getMessage());
    } catch (DBConnectionException dbEx) {
        throw new OperationException("Errore di connessione al database");
    } catch (DAOException ex) {
        throw new OperationException("Errore durante l'aggiornamento dello stato dell'ordine");
    }
}

 
}