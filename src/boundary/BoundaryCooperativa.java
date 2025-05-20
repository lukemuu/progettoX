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

import entity.EntityFattorino;
import entity.EntityOrdine;

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
		
		
		
		
	    }
	    
	}
	
	
	

