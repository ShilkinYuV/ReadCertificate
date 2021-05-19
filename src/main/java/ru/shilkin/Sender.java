package ru.shilkin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sender {

    private String username;
    private String password;
    private Properties props;
    String mailhost;
    String mailport;
    String mailtls;

    public Sender(String username, String password) {
        this.username = username;
        this.password = password;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Янтарь\\settingsdb.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mailhost = properties.getProperty("mailhost");
        mailport = properties.getProperty("mailport");
        mailtls = properties.getProperty("mailtls");

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", mailtls);
        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.port", mailport);
    }




    public void send(String subject, String text, String fromEmail, String toEmail){
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(text);

            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}