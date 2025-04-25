package com.yanmar;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class emailsending {
    public static void main(String[] args) {
        // Sender’s email & app password
        final String fromEmail = "20cs086@kcgcollege.com";
        final String password = "kcg@1234"; // Use app password for Gmail

        // Recipient email
        String toEmail = "mvel3987@gmail.com";

        // SMTP server config
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587");            // TLS Port
        props.put("mail.smtp.auth", "true");           // Enable authentication
        props.put("mail.smtp.starttls.enable", "true");// Enable STARTTLS

        // Create session
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });


        try {
            // Compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Hello from Java!");
            message.setText("This is a test email sent from Java code.");

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
