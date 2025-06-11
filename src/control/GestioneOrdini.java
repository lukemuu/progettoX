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
	        List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();
	        if (listaPescherie == null || listaPescherie.isEmpty()) {
	            System.err.println("Errore: Nessuna pescheria trovata. Il report non può essere inviato.");
	            return;
	        }
	
	        List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniReport();
	        EmailService emailService = new EmailService();
	
	        for (EntityPescheria pescheria : listaPescherie) {
	            String email = pescheria.getEmail();
	            String nomePescheria = pescheria.getNome();
	            int idPescheria = pescheria.getIdPescheria();
	
	            System.out.println("Pescheria: " + nomePescheria + " (ID: " + idPescheria + ")");
	
	            List<EntityOrdine> ordiniPescheria = new ArrayList<>();
	            for (EntityOrdine ordine : listaOrdini) {
	                if (ordine.getIdPescheria() == idPescheria) {
	                    ordiniPescheria.add(ordine);
	                }
	            }
	
	            System.out.println("Ordini trovati per " + nomePescheria + ": " + ordiniPescheria.size());
	
	            try {
	                String corpoEmail;
	                String oggetto = "Report ordini - " + nomePescheria;
	                if (ordiniPescheria.isEmpty()) {
	                    corpoEmail = creaReportVuoto(nomePescheria);
	                    System.out.println("Report vuoto inviato a: " + email);
	                } else {
	                    corpoEmail = creaReportOrdini(nomePescheria, ordiniPescheria);
	                    System.out.println("Report dettagliato inviato a: " + email);
	                }
	                emailService.inviaEmail(email, oggetto, corpoEmail);
	            } catch (MessagingException e) {
	                System.err.println("Errore nell'invio dell'email a " + email + ": " + e.getMessage());
	            }
	        }
	    } catch (DAOException | DBConnectionException e) {
	        System.err.println("Errore durante l'elaborazione del report: " + e.getMessage());
	    }
	}


    private String creaReportOrdini(String nomePescheria, List<EntityOrdine> ordini) {
        StringBuilder corpoEmail = new StringBuilder();
        corpoEmail.append("<html><body>");
        corpoEmail.append("<h3>Gentile ").append(nomePescheria).append(",</h3>");
        corpoEmail.append("<p>Ecco il report degli ordini:</p>");
        corpoEmail.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        corpoEmail.append("<tr>")
                  .append("<th>ID Ordine</th>")
                  .append("<th>Data</th>")
                  .append("<th>ID Prodotto</th>")
                  .append("<th>Quantità</th>")
                  .append("<th>Quantità Aggiornata</th>")
                  .append("</tr>");

        for (EntityOrdine ordine : ordini) {
            corpoEmail.append("<tr>")
                      .append("<td>").append(ordine.getIdOrdine()).append("</td>")
                      .append("<td>").append(ordine.getData()).append("</td>")
                      .append("<td>").append(ordine.getIdProdotto()).append("</td>")
                      .append("<td>").append(ordine.getQta()).append("</td>")
                      .append("<td>").append(ordine.getQtaAggiornata()).append("</td>")
                      .append("</tr>");
        }

        corpoEmail.append("</table>");
        corpoEmail.append("<p>Cordiali saluti,<br>Il Team</p>");
        corpoEmail.append("</body></html>");
        return corpoEmail.toString();
    }

    private String creaReportVuoto(String nomePescheria) {
        return "<html><body>" +
                "<h3>Gentile " + nomePescheria + ",</h3>" +
                "<p>Non sono stati registrati ordini negli ultimi 7 giorni.</p>" +
                "<p>Cordiali saluti,<br>Il Team</p>" +
                "</body></html>";
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
 
       
        // Richiama la funzione per creare la consegna
        boolean successo = ConsegnaDAO.creaConsegna(idOrdine, idFattorino, new Date(System.currentTimeMillis()));
 
        if (!successo) {
            throw new OperationException("Errore durante l'assegnazione della consegna.");
        }
 
        // Stampa un riepilogo delle scelte selezionate
        stampaScelteSelezionate(idOrdine, idFattorino);
    }
    
   

 
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
        returnList.add("null"); 
        returnList.add("0"); // ID ordine
 
        try {
            
            prodotto = ProdottoDAO.readProdotto(idPescheria, idProdotto);
 
            if (prodotto == null) {
                throw new OperationException("Prodotto non trovato");
            }
 
            
            prezzoTotale = calcolaPrezzo(prodotto.getPrezzo(), quantita);
            returnList.set(0, String.valueOf(prezzoTotale));
            
            
             
            EntityOrdine nuovoOrdine = new EntityOrdine(
            	    idRistorante,          
            	    idPescheria,
            	    idProdotto, 
            	    new Date(System.currentTimeMillis()),
            	    quantita,
            	    prezzoTotale
            	);
 
            // Salva l'ordine in stato "In trattativa" nel database
            OrdineDAO.createOrdine(nuovoOrdine);

            
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
            
            EntityPescheria pescheria = PescheriaDAO.readPescheria(String.valueOf(idPescheria));
            EmailService emailService = new EmailService();
            if (pescheria == null) {
                throw new OperationException("Pescheria non trovata con l'ID specificato.");
            }

            
            String emailPescheria = pescheria.getEmail();
            
            String oggetto = "Nuovo Ordine";
            String corpoEmail = creaCorpoEmail(codProdotto, quantita, idOrdine, prezzo);


            try {
                emailService.inviaEmail(emailPescheria, oggetto, corpoEmail);
            } catch (MessagingException e) {
                throw new OperationException("Errore durante l'invio dell'e-mail: " + e.getMessage());
            }

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante la selezione della pescheria: " + e.getMessage());
        }
    }
    

	public void confermaOrdine(int idOrdine) throws OperationException {
	    try {
	        
	        EntityOrdine.StatoOrdine statoConfermato = EntityOrdine.StatoOrdine.valueOf("CONFERMATO");
	
	        
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
	
	private String creaCorpoEmail(int codProdotto, double quantita, int idOrdine, float prezzo) {
	    StringBuilder corpoEmail = new StringBuilder();
	    corpoEmail.append("<html><body>");
	    corpoEmail.append("<p>Gentile Pescheria,</p>");
	    corpoEmail.append("<p>È stato effettuato un ordine con i seguenti dettagli:</p>");
	    corpoEmail.append("<ul>");
	    corpoEmail.append("<li><b>ID Ordine:</b> ").append(idOrdine).append("</li>");
	    corpoEmail.append("<li><b>Codice Prodotto:</b> ").append(codProdotto).append("</li>");
	    corpoEmail.append("<li><b>Quantità:</b> ").append(quantita).append("</li>");
	    corpoEmail.append("<li><b>Prezzo:</b> €").append(String.format("%.2f", prezzo)).append("</li>");
	    corpoEmail.append("</ul>");
	    corpoEmail.append("<p>Cordiali saluti,<br>Il Team</p>");
	    corpoEmail.append("</body></html>");
	    return corpoEmail.toString();
	}

}