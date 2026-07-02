package com.abhay.portfolio_backend.controller;

import com.abhay.portfolio_backend.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String ownerEmail;

    public ContactController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactRequest request) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(ownerEmail);
            message.setTo(ownerEmail);
            message.setReplyTo(request.getEmail());
            message.setSubject("New Portfolio Message from " + request.getName());

            message.setText(
                    "Name: " + request.getName() + "\n" +
                    "Email: " + request.getEmail() + "\n\n" +
                    "Message:\n" + request.getMessage()
            );

            mailSender.send(message);

            return ResponseEntity.ok("Message sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Email sending failed: " + e.getMessage());
        }
    }
}