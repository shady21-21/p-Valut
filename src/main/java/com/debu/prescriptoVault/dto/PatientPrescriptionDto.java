package com.debu.prescriptoVault.dto;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientPrescriptionDto{
    private Long id;
    private String fileName;
    private LocalDateTime uploadedAt;
    private String doctorName;
    private String doctorEmail;
    private String patientName;
    private String patientEmail;
}

