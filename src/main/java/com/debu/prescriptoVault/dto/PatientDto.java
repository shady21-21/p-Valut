package com.debu.prescriptoVault.dto;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientDto{
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private Integer age;
    private String bloodGroup;
}
