package control;
 
import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;
import entity.EntityOrdine;
 
public class EmailService {
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean tlsEnabled;
 
 
	public EmailService() {
	    this.host = "smtp.gmail.com";
	    this.port = 587;
	    this.username = System.getenv("EMAIL_USERNAME"); // Variabile d'ambiente
	    this.password = System.getenv("EMAIL_PASSWORD"); // Variabile d'ambiente
	    this.tlsEnabled = true;
	}
 
 
 

public void inviaReportOrdini(String destinatario, String nomePescheria, List<EntityOrdine> ordini) throws MessagingException {
    // Configurazione delle proprietà per il server SMTP
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(tlsEnabled));
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", String.valueOf(port));

    // Creazione della sessione con autenticazione
    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    // Creazione del messaggio email
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject("Report ordini - " + nomePescheria);

    // Corpo dell'email in formato HTML
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

    // Imposta il contenuto HTML nel messaggio
    message.setContent(corpoEmail.toString(), "text/html; charset=utf-8");

    // Invio dell'email
    Transport.send(message);
}



public void inviaReportVuoto(String destinatario, String nomePescheria) throws MessagingException {
    // Configurazione delle proprietà per il server SMTP
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(tlsEnabled));
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", String.valueOf(port));

    // Creazione della sessione con autenticazione
    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    // Creazione del messaggio email
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject("Report ordini - " + nomePescheria);

    // Corpo dell'email
    String corpoEmail = "<html><body>" +
            "<h3>Gentile " + nomePescheria + ",</h3>" +
            "<p>Non sono stati registrati ordini negli ultimi 7 giorni.</p>" +
            "<p>Cordiali saluti,<br>Il Team</p>" +
            "</body></html>";

    // Imposta il contenuto HTML nel messaggio
    message.setContent(corpoEmail, "text/html; charset=utf-8");

    // Invio dell'email
    Transport.send(message);
}


public void inviaMail(String emailPescheria, int codProdotto, double quantita,int idOrdine,float prezzo) throws MessagingException {
    // Configurazione delle proprietà per il server SMTP
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(tlsEnabled));
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", String.valueOf(port));

    // Creazione della sessione con autenticazione
    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    // Creazione del messaggio email
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailPescheria));
    message.setSubject("Nuovo Ordine");

    // Corpo dell'email
    StringBuilder corpoEmail = new StringBuilder();
    corpoEmail.append("Gentile Pescheria,\n\n");
    corpoEmail.append("È stato effettuato un ordine con i seguenti dettagli:\n");
    corpoEmail.append("- ID Ordine: ").append(idOrdine).append("\n");
    corpoEmail.append("- Codice Prodotto: ").append(codProdotto).append("\n");
    corpoEmail.append("- Quantità: ").append(quantita).append("\n");
    corpoEmail.append("- Prezzo: €").append(String.format("%.2f", prezzo)).append("\n\n");
    corpoEmail.append("Cordiali saluti,\nIl Team");
    message.setText(corpoEmail.toString());


    // Invio dell'email
    Transport.send(message);

    System.out.println("E-mail inviata con successo a: " + emailPescheria);
}
 
}