package com.debu.prescriptoVault.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="patients_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Patient{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    @Column(unique=true)
    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String currentOtp;

    private LocalDateTime otpExpiry;

    public enum Gender{
        MALE,
        FEMALE,
        OTHER
    }

    public enum BloodGroup{
        A_POS,
        B_POS,
        O_POS,
        AB_POS,
        A_NEG,
        B_NEG,
        O_NEG,
        AB_NEG
    }
}
