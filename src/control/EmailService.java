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
 
    public EmailService(String host, int port, String username, String password, boolean tlsEnabled) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.tlsEnabled = tlsEnabled;
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
 
        // Corpo dell'email
        StringBuilder corpoEmail = new StringBuilder();
        corpoEmail.append("Gentile ").append(nomePescheria).append(",\n\n");
        corpoEmail.append("Ecco il report degli ordini:\n\n");
 
        if (ordini.isEmpty()) {
            corpoEmail.append("Nessun ordine registrato negli ultimi 7 giorni.\n");
        } else {
            for (EntityOrdine ordine : ordini) {
                corpoEmail.append("ID Ordine: ").append(ordine.getIdOrdine())
                          .append(", Data: ").append(ordine.getData())
                          .append(", Quantità: ").append(ordine.getQta())
                          .append("\n");
            }
        }
 
        corpoEmail.append("\nCordiali saluti,\nIl Team");
 
        message.setText(corpoEmail.toString());
 
        // Invio dell'email
        Transport.send(message);
    }
}