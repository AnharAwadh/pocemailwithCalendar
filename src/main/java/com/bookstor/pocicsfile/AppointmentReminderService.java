package com.bookstor.pocicsfile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class AppointmentReminderService {

    @Autowired
    private EmailService emailService;

    public void sendAppointmentReminder(String toEmail) {
        try {
            // Event details
            String summary = "Appointment Reminder";
            String description = "This is a reminder for your upcoming appointment.";
            ZonedDateTime startDateTime = ZonedDateTime.now(ZoneId.systemDefault()).plusDays(1); // Appointment is tomorrow
            ZonedDateTime endDateTime = startDateTime.plusHours(1); // 1-hour duration
            String organizerEmail = "anhar9617@gmail.com"; // Your email address

            // Generate the calendar event content
            String calendarContent = createCalendarEvent(summary, description, startDateTime, endDateTime, organizerEmail, toEmail);

            // Email details
            String subject = "Your Appointment Reminder";
            String body = "<p>Dear Customer,</p>"
                    + "<p>Please see below for your appointment details:</p>"
                    + "<p><strong>" + summary + "</strong><br>"
                    + "Date and Time: " + startDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm a")) + "</p>"
                    + "<p>Best regards,<br>Your Company</p>";

            // Send the email with the calendar event embedded
            emailService.sendEmailWithCalendarEvent(toEmail, subject, body, calendarContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createCalendarEvent(String summary, String description, ZonedDateTime startDateTime, ZonedDateTime endDateTime, String organizerEmail, String attendeeEmail) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\n")
                .append("METHOD:REQUEST\n")
                .append("PRODID:-//Your Company//Your App//EN\n")
                .append("VERSION:2.0\n")
                .append("CALSCALE:GREGORIAN\n")
                .append("BEGIN:VEVENT\n")
                .append("DTSTAMP:").append(formatDateTime(ZonedDateTime.now())).append("\n")
                .append("DTSTART:").append(formatDateTime(startDateTime)).append("\n")
                .append("DTEND:").append(formatDateTime(endDateTime)).append("\n")
                .append("SUMMARY:").append(summary).append("\n")
                .append("DESCRIPTION:").append(description).append("\n")
                .append("UID:").append(UUID.randomUUID().toString()).append("\n")
                .append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE;CN=").append(attendeeEmail)
                .append(";EMAIL=").append(attendeeEmail).append("\n")
                .append("ORGANIZER;CN=").append(organizerEmail).append(":MAILTO:").append(organizerEmail).append("\n")
                .append("BEGIN:VALARM\n")
                .append("TRIGGER:-PT15M\n") // 15 minutes before
                .append("ACTION:DISPLAY\n")
                .append("DESCRIPTION:Reminder\n")
                .append("END:VALARM\n")
                .append("END:VEVENT\n")
                .append("END:VCALENDAR");
        return sb.toString();
    }

    private String formatDateTime(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        return dateTime.withZoneSameInstant(ZoneOffset.UTC).format(formatter);
    }

}
