package com.debu.prescriptoVault.service;

import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DoctorService{

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,PasswordEncoder passwordEncoder){
        this.doctorRepository=doctorRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Doctor register(String name,String email,String rawPassword){
        if(doctorRepository.findByEmail(email).isPresent())throw new RuntimeException("Email exists");
        Doctor doctor=new Doctor(name,email,passwordEncoder.encode(rawPassword));
        return doctorRepository.save(doctor);
    }

    public Doctor findByEmail(String email){return doctorRepository.findByEmail(email).orElse(null);}
    public Doctor findById(Long id){return doctorRepository.findById(id).orElse(null);}
}
