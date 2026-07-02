package com.abhay.portfolio_backend.controller;

import com.abhay.portfolio_backend.dto.TestimonialRequest;
import com.abhay.portfolio_backend.entity.Testimonial;
import com.abhay.portfolio_backend.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
@CrossOrigin(origins = "*")
public class TestimonialController {

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public String submitTestimonial(@RequestBody TestimonialRequest request) {

        Testimonial testimonial = new Testimonial();
        testimonial.setName(request.getName());
        testimonial.setMessage(request.getMessage());
        testimonial.setStatus("PENDING");

        testimonialRepository.save(testimonial);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("abhaysonone0@gmail.com");
        mail.setSubject("New Testimonial Pending Approval");
        mail.setText(
                "New testimonial received:\n\n" +
                "Name: " + request.getName() + "\n\n" +
                "Message:\n" + request.getMessage() + "\n\n" +
                "Status: PENDING\n\n" +
                "Approve using Postman:\n" +
                "PUT http://localhost:8080/api/testimonials/approve/" + testimonial.getId()
        );

        mailSender.send(mail);

        return "Testimonial submitted successfully. Waiting for approval.";
    }

    @GetMapping("/approved")
    public List<Testimonial> getApprovedTestimonials() {
        return testimonialRepository.findByStatus("APPROVED");
    }

    @GetMapping("/pending")
    public List<Testimonial> getPendingTestimonials() {
        return testimonialRepository.findByStatus("PENDING");
    }

    @PutMapping("/approve/{id}")
    public String approveTestimonial(@PathVariable Long id) {

        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Testimonial not found"));

        testimonial.setStatus("APPROVED");
        testimonialRepository.save(testimonial);

        return "Testimonial approved successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteTestimonial(@PathVariable Long id) {
        testimonialRepository.deleteById(id);
        return "Testimonial deleted successfully";
    }
}