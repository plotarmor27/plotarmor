
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendingEmail {

    private String userEmail;

    public SendingEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void sendMail(String code) {
        // Enter the email address and password for the account from which verification link will be send
        String email = "plotarmordb@gmail.com";
        String password = "pzzc ztqi ksqo aeog";

        Properties theProperties = new Properties();

        theProperties.put("mail.smtp.auth", "true");
        theProperties.put("mail.smtp.starttls.enable", "true");
        theProperties.put("mail.smtp.host", "smtp.gmail.com");
        theProperties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(theProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Your Verification Code");
            message.setText("Your Code is: " +code);

            Transport.send(message);

            System.out.println("Successfully sent Verification Link");

        } catch (Exception e) {
            System.out.println("Error at SendingEmail.java: " + e);
        }

    }

    public void sendMailForPwReset(String randomPw) {
        // Enter the email address and password for the account from which password will be send
        String email = "plotarmordb@gmail.com";
        String password = "pzzc ztqi ksqo aeog";

        Properties theProperties = new Properties();

        theProperties.put("mail.smtp.auth", "true");
        theProperties.put("mail.smtp.starttls.enable", "true");
        theProperties.put("mail.smtp.host", "smtp.gmail.com");
        theProperties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(theProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Your new Password");
            message.setText("Your new password is: " +randomPw);

            Transport.send(message);

            System.out.println("Successfully sent Password");

        } catch (Exception e) {
            System.out.println("Error at SendingEmail.java: " + e);
        }

    }

}
