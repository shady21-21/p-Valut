package com.debu.prescriptoVault.service;

public interface EmailService {
    void sendOtpEmail(String to, String patientName, String otp, int expiryMinutes);
}
