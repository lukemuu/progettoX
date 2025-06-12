package boundary;
 
import java.util.List;
import java.sql.Connection;
 
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
 
import control.GestioneOrdini;
import exception.OperationException;
import exception.DAOException;
 
import entity.EntityFattorino;
import entity.EntityOrdine;
import database.FattorinoDAO;
import database.OrdineDAO;
import database.ConsegnaDAO;
import database.PescheriaDAO;
import database.ProdottoDAO;
import entity.EntityPescheria;
import entity.EntityProdotto;
import exception.DBConnectionException;

public class BoundaryCooperativa {
 
	static Scanner scan = new Scanner(System.in);
 
	public static void main(String[] args) {		
		boolean exit = false;
		
		while(!exit) {
			System.out.println("Benvenuto nel sistema di gestione della cooperativa!");
			System.out.println("1. Registra pescheria");
			System.out.println("2. Assegna consegna");
			System.out.println("3. Registra fattorino");
			System.out.println("4. Esci");
			
			String op = scan.nextLine();
 
			if(op.equals("2")) {
				assegnaConsegna();
			} else if(op.equals("4")){
				exit = true;
			}else{
				System.out.println("Operazione non disponibile");
				System.out.println();
			}
		}	
		
		System.out.println("Arrivederci!");
	}
	

	public static void assegnaConsegna() {
	    // RIMUOVI questo scanner locale - usa solo quello statico
	    // Scanner scanner = new Scanner(System.in);
	
	    try {
	    	//Preleva i fattorini tramite il control, li salva in una lista e li stampa a video
	        List<EntityFattorino> fattorini = GestioneOrdini.stampaListaFattorini();
	
	        System.out.println("Seleziona un fattorino dall'elenco:");
	        for (int i = 0; i < fattorini.size(); i++) {
	            EntityFattorino fattorino = fattorini.get(i);
	            System.out.println((i + 1) + ". Nome: " + fattorino.getNome() + ", ID Fattorino: " + fattorino.getIdFattorino());
	        }
	
	        // Chiede all'utente di inserire l'ID del fattorino
	        int sceltaFattorino = -1;
	        boolean inputValidoFattorino = false;
	        
	        while (!inputValidoFattorino) {
	            try {
	                System.out.println("Inserisci l'ID del fattorino:");
	                sceltaFattorino = Integer.parseInt(scan.nextLine()); // Usa scan statico

	                if (sceltaFattorino > 0) {
	                    inputValidoFattorino = true;
	                } else {
	                    System.out.println("Errore, l'ID del fattorino deve essere un numero positivo.");
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Errore, inserire un ID valido (numero intero).");
	            }
	        }
	        
	        //Preleva gli ordini tramite il control, li salva in una lista e li stampa a video
	        List<EntityOrdine> ordini = GestioneOrdini.stampaListaOrdini();
	
	        System.out.println("Seleziona un ordine dall'elenco:");
	        for (int i = 0; i < ordini.size(); i++) {
	            EntityOrdine ordine = ordini.get(i);
	            System.out.println((i + 1) + ". ID Ordine: " + ordine.getIdOrdine() + ", Data: " + ordine.getData() + ", Quantità: " + ordine.getQta());
	        }
	        
	        // Chiede all'utente di inserire l'ID dell'ordine
	        boolean inputValidoOrdine = false;
	        int sceltaOrdine = 0;

	        while (!inputValidoOrdine) {
	            try {
	                System.out.println("Inserisci l'ID dell'ordine:");
	                sceltaOrdine = Integer.parseInt(scan.nextLine()); // Usa scan statico

	                if (sceltaOrdine > 0) {
	                    inputValidoOrdine = true;
	                } else {
	                    System.out.println("Errore, l'ID dell'ordine deve essere un numero positivo.");
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Errore, inserire un ID valido (numero intero).");
	            }
	        }

	        // Passa le scelte a GestioneOrdini
	        GestioneOrdini.assegnaConsegna(sceltaOrdine, sceltaFattorino);

	        System.out.println("Consegna assegnata con successo! ... Preso a carico " + sceltaOrdine + ". La consegna verrà effettuata da " + sceltaFattorino + " il prima possibile.");

	    } catch (DAOException e) {
	        System.err.println("Errore durante l'assegnazione della consegna: " + e.getMessage());
	    } catch (OperationException e) {
	        System.err.println("Errore nell'operazione: " + e.getMessage());
	    } catch (DBConnectionException e) {
	        System.err.println("Errore di connessione al database: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Errore imprevisto: " + e.getMessage());
	    }
	    // RIMUOVI il finally che chiude scanner
	}
}
 
