package boundary;

import java.util.Scanner;

import control.GestioneOrdini;
import exception.OperationException;

public class BoundaryPescheria {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Gestione Pescheria");
            System.out.println("1. Modifica ordine");
            System.out.println("2. Esci");

            String op = scan.nextLine();

            if (op.equals("1")) {
                modificaOrdine();
            } else if (op.equals("2")) {
                exit = true;
            } else {
                System.out.println("Operazione non disponibile");
                System.out.println();
            }
        }

        System.out.println("Arrivederci!");
    }

    private static void modificaOrdine() {
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        int idOrdine = 0;
        int quantitaAggiornata = 0;
        boolean inputValido = false;

        try {
            // Input ID ordine
            while (!inputValido) {
                try {
                    System.out.println("Inserisci l'ID dell'ordine:");
                    idOrdine = Integer.parseInt(scan.nextLine());
                    inputValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un ID valido (numero intero).");
                }
            }

            inputValido = false;

            // Input quantità aggiornata
            while (!inputValido) {
                try {
                    System.out.println("Inserisci la quantità aggiornata:");
                    quantitaAggiornata = Integer.parseInt(scan.nextLine());

                    if (quantitaAggiornata > 0) {
                        inputValido = true;
                    } else {
                        System.out.println("Errore, la quantità deve essere un numero positivo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un numero valido.");
                }
            }

            // Richiama il metodo del control
            gestioneOrdini.modificaOrdine(idOrdine, quantitaAggiornata);
            System.out.println("Quantità aggiornata con successo per l'ordine con ID: " + idOrdine);

        } catch (OperationException oE) {
            System.out.println(oE.getMessage());
        } catch (Exception e) {
            System.out.println("Errore inaspettato, riprovare.");
        }
    }
}
