package control;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import entity.EntityOrdine;
 
public class Main {
 
    public static void main(String[] args) {
        // Disabilita i warning di JavaMail
        java.util.logging.Logger.getLogger("javax.mail").setLevel(java.util.logging.Level.SEVERE);
        java.util.logging.Logger.getLogger("com.sun.mail").setLevel(java.util.logging.Level.SEVERE);
 
        // Creazione del servizio email
        EmailService emailService = new EmailService();
 
        // Lettura del destinatario da tastiera
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci l'indirizzo email del destinatario: ");
        String destinatario = scanner.nextLine();
 
        // Validazione dell'email
        if (!isValidEmail(destinatario)) {
            System.err.println("Errore: l'indirizzo email inserito non è valido.");
            scanner.close();
            return;
        }
 
        // Generazione di ordini casuali
        List<EntityOrdine> ordini = generaOrdiniCasuali(5); // Genera 5 ordini casuali
 
        // Nome della pescheria
        String nomePescheria = "Pescheria Test";
 
        try {
            // Invio dell'email con il report degli ordini
            emailService.inviaReportOrdini(destinatario, nomePescheria, ordini);
            System.out.println("✅ Email inviata con successo!");
        } catch (MessagingException e) {
            System.err.println("❌ Errore durante l'invio dell'email: " + e.getMessage());
            e.printStackTrace();
        }

        scanner.close();
    }
 
 
	private static List<EntityOrdine> generaOrdiniCasuali(int numeroOrdini) {
	    List<EntityOrdine> ordini = new ArrayList<>();
	    Random random = new Random();
	    for (int i = 0; i < numeroOrdini; i++) {
	        EntityOrdine ordine = new EntityOrdine();
	        ordine.setIdOrdine(i + 1); // ID univoco
	        ordine.setIdRistorante(random.nextInt(100) + 1); // ID ristorante casuale
	        ordine.setIdPescheria(random.nextInt(50) + 1); // ID pescheria casuale
	        ordine.setData(new Date()); // Data corrente
	        ordine.setIdProdotto(random.nextInt(200) + 1); // ID prodotto casuale
	        // Quantità casuale con massimo 2 cifre decimali
	        double qta = Math.round((random.nextDouble() * 10) * 100.0) / 100.0;
	        ordine.setQta(qta);
	        // Quantità aggiornata casuale con massimo 2 cifre decimali
	        double qtaAggiornata = Math.round((qta - random.nextDouble() * 2) * 100.0) / 100.0;
	        ordine.setQtaAggiornata(qtaAggiornata);
	        ordini.add(ordine);
	    }
	    return ordini;
	}
 
 
    // Metodo per validare l'email
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}