package com.bookstor.pocicsfile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentReminderService appointmentReminderService;

    @PostMapping("/sendReminder")
    public String sendAppointmentReminder(@RequestParam String email) {
        appointmentReminderService.sendAppointmentReminder(email);
        return "Appointment reminder sent to " + email;
    }
}
