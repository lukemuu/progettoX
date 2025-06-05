package boundary;

import java.util.Scanner;
import java.util.ArrayList;
import control.GestioneOrdini;
import database.OrdineDAO;
import database.ProdottoDAO;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;
import entity.EntityProdotto;
import java.util.concurrent.TimeUnit;

public class BoundaryRistorante {
	
	 static Scanner scan = new Scanner(System.in);

	    public static void main(String[] args) { 
	        boolean exit = false;

	        
	        while (!exit) {
	            System.out.println("Gestione Ristorante");
	            System.out.println("1. Acquista prodotto");
	            System.out.println("2. Esci");

	            String op = scan.nextLine();

	            if (op.equals("1")) {
	                acquistaProdotto();
	            } else if (op.equals("2")) {
	                exit = true;
	            } else {
	                System.out.println("Operazione non disponibile");
	                System.out.println();
	            }
	        }

	        System.out.println("Arrivederci!");
	    }
	 

	    public static void acquistaProdotto() {
	        try {
	            Scanner scan = new Scanner(System.in);

	            int idPescheria = 0;
	            int idProdotto = 0;
	            double quantita = 0;
	            int idRistorante = 1; // Aggiunto per identificare il ristorante
	            boolean inputValido = false;

	            
	            while (!inputValido) {
	                try {
	                    System.out.println("Inserisci l'ID della pescheria:");
	                    idPescheria = Integer.parseInt(scan.nextLine());

	                    if (idPescheria > 0) {
	                        inputValido = true;
	                    } else {
	                        System.out.println("Errore: l'ID della pescheria deve essere un intero positivo. Riprovare.");
	                    }
	                } catch (NumberFormatException nE) {
	                    System.out.println("Errore: inserire un numero valido per l'ID della pescheria. Riprovare.");
	                }
	            }

	            inputValido = false;
	            while (!inputValido) {
	                try {
	                    System.out.println("Inserisci l'ID del prodotto:");
	                    idProdotto = Integer.parseInt(scan.nextLine());

	                    if (idProdotto > 0) {
	                        inputValido = true;
	                    } else {
	                        System.out.println("Errore: l'ID del prodotto deve essere un intero positivo. Riprovare.");
	                    }
	                } catch (NumberFormatException nE) {
	                    System.out.println("Errore: inserire un numero valido per l'ID del prodotto. Riprovare.");
	                }
	            }

	            inputValido = false;
	            while (!inputValido) {
	                try {
	                    System.out.println("Inserisci la quantità del prodotto:");
	                    quantita = Integer.parseInt(scan.nextLine());

	                    if (quantita > 0) {
	                        inputValido = true;
	                    } else {
	                        System.out.println("Errore: la quantità deve essere un intero positivo. Riprovare.");
	                    }
	                } catch (NumberFormatException nE) {
	                    System.out.println("Errore: inserire un numero valido per la quantità. Riprovare.");
	                }
	            }

	            ArrayList<String> results = GestioneOrdini.getInstance().acquistaProdotto(idPescheria, idProdotto, quantita, idRistorante);
	            
	            float prezzo = Float.parseFloat(results.get(0));
	            System.out.println("Prezzo totale: " + prezzo + " euro");
	            System.out.println("Digita 'S' per confermare o qualunque altro carattere per annullare..");
	            String conferma = scan.nextLine();

	            if (!conferma.equals("S") && !conferma.equals("s")) {
	                System.out.println("Operazione annullata..");
	                System.out.println();
	                return;
	            }

	            System.out.println("Ordine confermato con successo!");
	            System.out.println("Prezzo totale: " + prezzo + " euro");
	            
	            int idOrdine = Integer.parseInt(results.get(2));
	           
	            
	            
	            
	            GestioneOrdini.getInstance().inviaOrdine(idPescheria, idProdotto, quantita,idOrdine, prezzo);

	            
	            //Assumo sempre vera la conferma da parte della pescheria
	            
	            if (true) { 
	                inputValido = false;
	                while (!inputValido) {
	                    System.out.println("Inserire il numero di carta:");

	                    String numeroCarta = scan.nextLine();

	                    try {
	                        Long.parseLong(numeroCarta);

	                        if (numeroCarta.length() == 16) {
	                            inputValido = true;
	                        } else {
	                            System.out.println("Errore inserimento carta, deve essere di 16 cifre..");
	                        }
	                    } catch (NumberFormatException e) {
	                        System.out.println("Errore inserimento carta, deve contenere solo numeri..");
	                    }
	                }

	                System.out.println();
	                System.out.println("Pagamento in corso..");
	                TimeUnit.SECONDS.sleep(3);
	                System.out.println("Pagamento effettuato!");

	                if (true) { 
	                    System.out.println("Conferma del pagamento ricevuta.");
	                    
	                   
	                    GestioneOrdini.getInstance().confermaOrdine(idOrdine);
	                    

		                System.out.println("Acquisto completato!");
		                System.out.println();
		                System.out.println();
	                    
	                } 
	                //else {
	                    //System.out.println("Errore nella conferma del pagamento.");
	                    //GestioneOrdini.getInstance().annullaOrdine(idPescheria, idProdotto, quantita);
	                }

	            //else {
	                
					// conferma della pescheria== false quindi verrà effettuata la modifica dell'ordine
				
	            //}
	            
	            

	        } catch (OperationException oE) {
	            System.out.println(oE.getMessage());
	            System.out.println("Riprovare..\n");
	        } catch (Exception e) {
	            System.out.println("Unexpected exception, riprovare..");
	            System.out.println();
	        }
	    } 
}  
	    

	   