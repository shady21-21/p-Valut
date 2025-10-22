package com.debu.prescriptoVault.dto;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionDto{

    private Long id;
    private String fileName;
    private LocalDateTime uploadedAt;
    private DoctorDto doctor;
    private PatientDto patient;
}

