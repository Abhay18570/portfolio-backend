package com.abhay.portfolio_backend.controller;

import com.abhay.portfolio_backend.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public String sendContactMessage(@RequestBody ContactRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("abhaysonone0@gmail.com");
        message.setSubject("New Message From Portfolio");
        message.setText(
                "Name: " + request.getName() + "\n" +
                "Email: " + request.getEmail() + "\n\n" +
                "Message:\n" + request.getMessage()
        );

        mailSender.send(message);

        return "Message sent successfully";
    }
}