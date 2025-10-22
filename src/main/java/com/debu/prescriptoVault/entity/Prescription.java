package com.debu.prescriptoVault.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name="prescriptions_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prescription{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String filePath;

    private LocalDateTime uploadedAt;


    @ManyToOne
    @JoinColumn(name="patient_id",referencedColumnName="id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name="doctor_id",referencedColumnName="id")
    private Doctor doctor;


}
