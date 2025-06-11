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
 

public void inviaEmail(String destinatario, String oggetto, String corpoEmail) throws MessagingException {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(tlsEnabled));
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", String.valueOf(port));

    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject(oggetto);
    message.setContent(corpoEmail, "text/html; charset=utf-8");

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