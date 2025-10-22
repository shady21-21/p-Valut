package com.debu.prescriptoVault.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="doctors_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Doctor{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique=true)
    private String email;

    private String password;

    public Doctor(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
    }
}
