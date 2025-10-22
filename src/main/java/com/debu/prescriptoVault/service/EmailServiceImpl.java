package com.debu.prescriptoVault.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String from;

    public EmailServiceImpl(JavaMailSender mailSender,
                            TemplateEngine templateEngine,
                            @Value("${spring.mail.from:no-reply@example.com}") String from) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = from;
    }

    @Override
    public void sendOtpEmail(String to, String patientName, String otp, int expiryMinutes) {
        try {
            Context context = new Context();
            context.setVariable("patientName", patientName);
            context.setVariable("otp", otp);
            context.setVariable("expiryMinutes", expiryMinutes);

            String html = templateEngine.process("otp-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Your OTP for Patient Management System");
            helper.setText(html, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
