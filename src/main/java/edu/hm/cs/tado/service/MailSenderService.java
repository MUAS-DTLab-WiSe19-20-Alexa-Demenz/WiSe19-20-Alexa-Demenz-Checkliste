package edu.hm.cs.tado.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Der MailSenderService kann Mails verschicken. Dazu muss im Ressources (bzw. in der Jar auf root Ebene)
 * eine Datei "mail.properties" liegen, in der der zu verwendende Mailserver konfiguriert wird.
 */
public class MailSenderService {

    private static final String USERNAME_PROPERTY = "mail.username";
    private static final String MAILSERVER_AUTH = "mail.password";
    private static final String PROPERTIES_PATH = "/mail.properties";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Properties properties;
    private Session session;

    public MailSenderService() throws IOException {
        properties = getServerProperties();
        session = getSession(properties);
    }

    /**
     * verschickt eine Mail
     *
     * @param subject Betreff der Mail
     * @param msg     Nachricht der Mail
     * @param toList  Liste aller Empfänger
     */
    public void sendMail(String subject, String msg, InternetAddress[] toList) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(properties.getProperty(USERNAME_PROPERTY)
                    , "Pflege TaDo"));
            message.setSubject(subject);

            message.setRecipients(Message.RecipientType.TO, toList);
            message.setContent(generateContent(msg));

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("An error occured while sending an Email", e);
        }
    }

    private Multipart generateContent(String msg) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        return multipart;
    }

    private Session getSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        properties.getProperty(USERNAME_PROPERTY)
                        , properties.getProperty(MAILSERVER_AUTH));
            }
        });
    }

    private Properties getServerProperties() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream(PROPERTIES_PATH));
        return prop;
    }
}
