package com.bookstor.pocicsfile;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithCalendarEvent(String toEmail, String subject, String body, String calendarContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("anhar9617@gmail.com");
        message.setRecipients(Message.RecipientType.TO, toEmail);
        message.setSubject(subject, "UTF-8");

        Multipart multipartMixed = new MimeMultipart("mixed");

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipartAlternative = new MimeMultipart("alternative");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Please view this email in an HTML-compatible email viewer.", "UTF-8");
        multipartAlternative.addBodyPart(textPart);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(body, "text/html; charset=UTF-8");
        multipartAlternative.addBodyPart(htmlPart);

        // Add the multipart/alternative to message body part
        messageBodyPart.setContent(multipartAlternative);
        multipartMixed.addBodyPart(messageBodyPart);


        MimeBodyPart calendarPart = new MimeBodyPart();
        calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");
        calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setHeader("Content-ID", "calendar_message");
        calendarPart.setHeader("Content-Disposition", "inline; filename=invite.ics");
        calendarPart.setHeader("Content-Transfer-Encoding", "8bit");
        multipartMixed.addBodyPart(calendarPart);

        message.setContent(multipartMixed);

        mailSender.send(message);
    }
}
