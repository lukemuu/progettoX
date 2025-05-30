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
	private List<EntityOrdine> ordiniTemporanei = new ArrayList<>(); // Lista per ordini temporanei
	private static int nextId = 1; // Contatore statico per ID univoci

	private synchronized static int generateUniqueId() {
	    return nextId++;
	}
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
	        // Configura il servizio email (questi parametri andrebbero spostati in un file di configurazione)
	        EmailService emailService = new EmailService();
	        
	        // Per ogni pescheria, filtra gli ordini corrispondenti e invia il report
	        for (EntityPescheria pescheria : listaPescherie) {
	            String email = pescheria.getEmail();
	            String nomePescheria = pescheria.getNome(); // Assumendo che ci sia un metodo getNome()
	            int IdPescheria = pescheria.getIdPescheria();
	            List<EntityOrdine> ordiniPescheria = new ArrayList<>();
	            // Filtra gli ordini per questa pescheria
	            for (EntityOrdine ordine : listaOrdini) {
	                if (ordine.getIdPescheria() == IdPescheria) {
	                    ordiniPescheria.add(ordine);
	                }
	            }
	            // Invia il report in ogni caso, anche se non ci sono ordini
	            try {
	                emailService.inviaReportOrdini(email, nomePescheria, ordiniPescheria);
	                System.out.println("Report inviato a: " + email + " (" + nomePescheria +
	                                 (ordiniPescheria.isEmpty() ? " - Nessun ordine" : ""));
	            } catch (MessagingException e) {
	                System.err.println("Errore nell'invio dell'email a " + email + ": " + e.getMessage());
	            }
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

 
    public static void assegnaConsegna(EntityOrdine ordineSelezionato, EntityFattorino fattorinoSelezionato)
            throws OperationException, DAOException, DBConnectionException {
 
        // Controlla se ordine e fattorino sono stati selezionati
        if (ordineSelezionato == null) {
            throw new OperationException("Nessun ordine selezionato.");
        }
        if (fattorinoSelezionato == null) {
            throw new OperationException("Nessun fattorino selezionato.");
        }
 
        // Richiama la funzione per creare la consegna
        boolean successo = ConsegnaDAO.creaConsegna(new Date(System.currentTimeMillis()));
 
        if (!successo) {
            throw new OperationException("Errore durante l'assegnazione della consegna.");
        }
 
        // Stampa un riepilogo delle scelte selezionate
        stampaScelteSelezionate(ordineSelezionato, fattorinoSelezionato);
    }

	    // Metodi per mostrare le liste
    public static void mostraListaOrdini(List<EntityOrdine> listaOrdini) {
        System.out.println("Lista degli ordini disponibili:");
        for (int i = 0; i < listaOrdini.size(); i++) {
            EntityOrdine ordine = listaOrdini.get(i);
            System.out.println((i + 1) + ". ID Ordine: " + ordine.getIdOrdine() + ", Data: " + ordine.getData() + ", Quantità: " + ordine.getQta());
        }
    }
 
    public static void mostraListaFattorini(List<EntityFattorino> listaFattorini) {
        System.out.println("Lista dei fattorini disponibili:");
        for (int i = 0; i < listaFattorini.size(); i++) {
            EntityFattorino fattorino = listaFattorini.get(i);
            System.out.println((i + 1) + ". Nome: " + fattorino.getNome() + ", ID Fattorino: " + fattorino.getIdFattorino());
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

 
    public static void stampaScelteSelezionate(EntityOrdine ordineSelezionato, EntityFattorino fattorinoSelezionato) {
        System.out.println("========================================");
        System.out.println("          Riepilogo Scelte Utente       ");
        System.out.println("========================================");
 
        System.out.println("Ordine Selezionato:");
        System.out.println("  - ID Ordine: " + ordineSelezionato.getIdOrdine());
 
        System.out.println("----------------------------------------");
 
        System.out.println("Fattorino Selezionato:");
        System.out.println("  - Nome: " + fattorinoSelezionato.getNome());
        System.out.println("  - ID Fattorino: " + fattorinoSelezionato.getIdFattorino());
 
        System.out.println("========================================");
    }
 
	public ArrayList<String> acquistaProdotto(int idPescheria, int idProdotto, double quantita,int idRistorante) throws OperationException {
        EntityProdotto prodotto = null;
        float prezzoTotale = 0;
 
        ArrayList<String> returnList = new ArrayList<>();
        returnList.add("0"); // Prezzo totale
        returnList.add("null"); // Dettagli ordine temporaneo
 
        try {
            // Controllo esistenza prodotto
            prodotto = ProdottoDAO.readProdotto(idPescheria, idProdotto);
 
            if (prodotto == null) {
                throw new OperationException("Prodotto non trovato");
            }
 
            // Calcolo prezzo totale
            prezzoTotale = calcolaPrezzo(prodotto.getPrezzo(), quantita);
            returnList.set(0, String.valueOf(prezzoTotale));
            
            int idOrdine = generateUniqueId();
            // Creazione ordine temporaneo
            EntityOrdine ordineTemporaneo = new EntityOrdine(
            	    idOrdine, // Genera un ID univoco
            	    idRistorante,          // ID del ristorante (da aggiungere come parametro)
            	    idPescheria,
            	    new Date(System.currentTimeMillis()), // Data corrente
            	    idProdotto,
            	    quantita
            	);
 
            // Salva l'ordine temporaneo  in una lista temporanea
               ordiniTemporanei.add(ordineTemporaneo);
 
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
    
    public void inviaOrdine(int idPescheria, int codProdotto, double quantita) throws OperationException {
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
                emailService.inviaMail(emailPescheria, codProdotto, quantita);
            } catch (MessagingException e) {
                throw new OperationException("Errore durante l'invio dell'e-mail: " + e.getMessage());
            }

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante la selezione della pescheria: " + e.getMessage());
        }
    }
    
    public void emettiOrdine() throws OperationException {
        if (ordiniTemporanei.isEmpty()) {
            throw new OperationException("Nessun ordine temporaneo da emettere.");
        }

        try {
            // Salva tutti gli ordini temporanei utilizzando il metodo saveOrdine
            for (EntityOrdine ordine : ordiniTemporanei) {
                ordine.saveOrdine(); // Chiama il metodo saveOrdine di EntityOrdine
            }
            ordiniTemporanei.clear(); // Svuota la lista dopo il salvataggio
            
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante il salvataggio degli ordini: " + e.getMessage());
        }
        
       
    }
 
}