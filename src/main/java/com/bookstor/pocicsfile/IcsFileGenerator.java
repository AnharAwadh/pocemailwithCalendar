package com.bookstor.pocicsfile;


import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

@Service
public class IcsFileGenerator {

    public byte[] generateIcsFile(String summary, String description, ZonedDateTime startDateTime, ZonedDateTime endDateTime, String organizerEmail, String attendeeEmail) throws Exception {
        // Create a new calendar
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Your Company//Your App//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // Create the event
        String eventName = summary;
        String eventDescription = description;

        // Convert ZonedDateTime to DateTime
        DateTime start = new DateTime(java.util.Date.from(startDateTime.toInstant()));
        DateTime end = new DateTime(java.util.Date.from(endDateTime.toInstant()));
        VEvent event = new VEvent(start, end, eventName);

        // Set unique identifier
        Uid uid = new Uid(UUID.randomUUID().toString());
        event.getProperties().add(uid);

        // Add description
        event.getProperties().add(new Description(eventDescription));

        // Set organizer
        Organizer organizer = new Organizer(URI.create("mailto:" + organizerEmail));
        event.getProperties().add(organizer);

        // Add attendee
        Attendee attendee = new Attendee(URI.create("mailto:" + attendeeEmail));
        attendee.getParameters().add(Role.REQ_PARTICIPANT);
        attendee.getParameters().add(new Cn(attendeeEmail));
        event.getProperties().add(attendee);

//        // Add reminder (alarm)
//        Dur minus15Minutes = new Dur(false, 0, 0, 15, 0); // Negative duration of 15 minutes
//        Trigger trigger = new Trigger(minus15Minutes);
//        VAlarm alarm = new VAlarm(trigger);
//        alarm.getProperties().add(Action.DISPLAY);
//        alarm.getProperties().add(new Description("Reminder: " + eventName));
//        event.getAlarms().add(alarm);

        // Add the event to the calendar
        calendar.getComponents().add(event);

        // Output the calendar to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, outputStream);

        return outputStream.toByteArray();
    }
}
