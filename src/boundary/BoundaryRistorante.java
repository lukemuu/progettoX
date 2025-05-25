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
	            boolean inputValido = false;

	            while (!inputValido) {
	                try {
	                    System.out.println("Inserisci l'ID della pescheria");
	                    idPescheria = Integer.parseInt(scan.nextLine());

	                    System.out.println("Inserisci l'ID del prodotto");
	                    idProdotto = Integer.parseInt(scan.nextLine());

	                    System.out.println("Inserisci la quantità del prodotto");
	                    quantita = Integer.parseInt(scan.nextLine());

	                    if (idPescheria <= 0 || idProdotto <= 0 || quantita <= 0) {
	                        throw new IllegalArgumentException("I valori devono essere positivi.");
	                    }

	                    inputValido = true;
	                } catch (IllegalArgumentException e) {
	                    System.out.println("Errore nell'acquisizione dei dati, riprovare..");
	                    System.out.println();
	                }
	            }

	            ArrayList<String> results = GestioneOrdini.getInstance().acquistaProdotto(idPescheria, idProdotto, quantita);

	            System.out.println("Prezzo totale: " + results.get(0) + " euro");
	            System.out.println("Digita 'S' per confermare o qualunque altro carattere per annullare..");
	            String conferma = scan.nextLine();

	            if (!conferma.equals("S") && !conferma.equals("s")) {
	                System.out.println("Operazione annullata..");
	                System.out.println();
	                return;
	            }

	            System.out.println("Ordine confermato con successo!");
	            System.out.println("Prezzo totale: " + results.get(0) + " euro");
	            
	            
	            GestioneOrdini.getInstance().inviaOrdine(idPescheria, idProdotto, quantita);

	            
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
	                    GestioneOrdini.getInstance().emettiOrdine();
	                    

		                System.out.println("Acquisto completato!");
		                System.out.println();
		                System.out.println();
	                    
	                } 
	                //else {
	                    //System.out.println("Errore nella conferma del pagamento.");
	                    //GestioneOrdini.getInstance().annullaOrdine(idPescheria, idProdotto, quantita);
	                }

	                //System.out.println("Invio conferma ordine in corso..");
	                //TimeUnit.SECONDS.sleep(3);
	               // System.out.println("Conferma ordine inviata sulla mail indicata..");

	               // System.out.println("Acquisto completato!");
	               // System.out.println();
	               // System.out.println();
	            
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
	    

	   