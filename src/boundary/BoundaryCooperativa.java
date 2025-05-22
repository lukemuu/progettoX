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
        Scanner scanner = new Scanner(System.in);

        try {
            // Recupera la lista di tutti i fattorini
            List<EntityFattorino> fattorini = FattorinoDAO.readAllFattorini();
            if (fattorini.isEmpty()) {
                System.out.println("Nessun fattorino disponibile.");
                return;
            }

            System.out.println("Seleziona un fattorino dall'elenco:");
            GestioneOrdini.mostraListaFattorini(fattorini);

            System.out.print("Inserisci il numero del fattorino selezionato: ");
            int sceltaFattorino = scanner.nextInt();
            EntityFattorino fattorinoSelezionato = fattorini.get(sceltaFattorino - 1);

            // Recupera la lista degli ordini dell'ultimo giorno
            List<EntityOrdine> ordini = OrdineDAO.readOrdiniUltimoGiorno();
            if (ordini.isEmpty()) {
                System.out.println("Nessun ordine disponibile.");
                return;
            }

            System.out.println("Seleziona un ordine dall'elenco:");
            GestioneOrdini.mostraListaOrdini(ordini);

            System.out.print("Inserisci il numero dell'ordine selezionato: ");
            int sceltaOrdine = scanner.nextInt();
            EntityOrdine ordineSelezionato = ordini.get(sceltaOrdine - 1);

            // Passa le scelte a GestioneOrdini
            GestioneOrdini.assegnaConsegna(ordineSelezionato, fattorinoSelezionato);

            System.out.println("Consegna assegnata con successo!");

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Selezione non valida. Riprova.");
        } catch (DAOException | DBConnectionException e) {
            System.out.println("Errore durante il recupero dei dati: " + e.getMessage());
        } catch (OperationException e) {
            System.out.println("Errore durante l'assegnazione della consegna: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
 
}
	
	
	

