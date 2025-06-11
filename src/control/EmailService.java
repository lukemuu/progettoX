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

 
 
}