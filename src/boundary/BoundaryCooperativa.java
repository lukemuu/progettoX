package boundary;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import control.GestioneOrdini;
import exception.OperationException;

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
		
		System.out.println("Inserisci l'ID dell'ordine da assegnare:");
		int idOrdine = Integer.parseInt(scan.nextLine());

		System.out.println("Inserisci l'ID del fattorino:");
		int idFattorino = Integer.parseInt(scan.nextLine());

		System.out.println("Inserisci la data di consegna (yyyy-MM-dd):");
		String dataConsegna = scan.nextLine();

		System.out.println("Inserisci l'orario di consegna (HH:mm:ss):");
		String orarioConsegna = scan.nextLine();

		try {
			GestioneOrdini.assegnaConsegna(idOrdine, idFattorino, dataConsegna, orarioConsegna);
			System.out.println("Consegna assegnata con successo!");
		} catch (OperationException e) {
			System.out.println(e.getMessage());
		}

		System.out.println();
	}
}

