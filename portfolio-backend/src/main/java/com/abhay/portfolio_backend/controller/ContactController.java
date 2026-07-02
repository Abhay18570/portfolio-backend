package com.abhay.portfolio_backend.controller;

import com.abhay.portfolio_backend.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactRequest request) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("abhaysonone0@gmail.com");
            message.setTo("abhaysonone0@gmail.com");
            message.setSubject("New Portfolio Message from " + request.getName());

            message.setText(
                    "Name: " + request.getName() + "\n" +
                    "Email: " + request.getEmail() + "\n\n" +
                    "Message:\n" + request.getMessage()
            );

            mailSender.send(message);

            return ResponseEntity.ok("Message sent successfully");

        } catch (MailException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Email sending failed: " + e.getMessage());
        }
    }
}