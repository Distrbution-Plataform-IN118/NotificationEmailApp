import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Email {
	

    static final String FROM = "rabbitmq20180325@gmail.com";
    static final String FROMNAME = "RabbitMQ";

    static final String SMTP_USERNAME = "rabbitmq20180325";
    

    static final String SMTP_PASSWORD = "r7b2s7a2";

    static final String CONFIGSET = "ConfigSet";

    static final String HOST = "smtp.gmail.com";
    
    static final int PORT = 587;
    
    static final String SUBJECT = "Notification Email RabbitMQ Publish-Subscribe";
   

    public static void send(String message, String to) throws Exception {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(SUBJECT);
        msg.setContent(message,"text/html");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
       // msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }

    public static void main(String[] args)throws Exception {
		
    	new Email().send("Hello", "rabbitmq20180325@gmail.com");
    	
	}
}
