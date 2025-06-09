
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

    public static void modificaOrdine() {
        GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();
        int idPescheria = 0;
        int idOrdine = 0;
        double quantitaAggiornata = 0.0;
        float nuovoPrezzo = 0.0f;
        boolean inputValido = false;

        try {

            while (!inputValido) {
                try {
                    System.out.println("Inserisci l'ID della pescheria:");
                    idPescheria = Integer.parseInt(scan.nextLine());
                    if (idPescheria > 0) {
                        inputValido = true;
                    } else {
                        System.out.println("Errore, l'ID della pescheria deve essere un numero positivo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un ID valido (numero intero).");
                }
            }

            inputValido = false;

            while (!inputValido) {
                try {
                    System.out.println("Inserisci l'ID dell'ordine:");
                    idOrdine = Integer.parseInt(scan.nextLine());
                    if (idOrdine > 0) {
                        inputValido = true;
                    } else {
                        System.out.println("Errore, l'ID dell'ordine deve essere un numero positivo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un ID valido (numero intero).");
                }
            }

            inputValido = false;

            while (!inputValido) {
                try {
                    System.out.println("Inserisci la quantità aggiornata:");
                    quantitaAggiornata = Double.parseDouble(scan.nextLine());

                    if (quantitaAggiornata > 0) {
                        inputValido = true;
                    } else {
                        System.out.println("Errore, la quantità deve essere un numero positivo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un numero valido.");
                }
            }

            inputValido = false;

            while (!inputValido) {
                try {
                    System.out.println("Inserisci il nuovo prezzo:");
                    nuovoPrezzo = Float.parseFloat(scan.nextLine());

                    if (nuovoPrezzo > 0) {
                        inputValido = true;
                    } else {
                        System.out.println("Errore, il prezzo deve essere un valore positivo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore, inserire un valore valido.");
                }
            }

            gestioneOrdini.modificaOrdine(idPescheria, idOrdine, quantitaAggiornata, nuovoPrezzo);
            System.out.println("Ordine aggiornato con successo: ID Ordine = " + idOrdine + ", Nuova Quantità = " + quantitaAggiornata + ", Nuovo Prezzo = " + nuovoPrezzo);

        } catch (OperationException oE) {
            System.out.println(oE.getMessage());
        } catch (Exception e) {
            System.out.println("Errore inaspettato, riprovare.");
        }
    }
}
