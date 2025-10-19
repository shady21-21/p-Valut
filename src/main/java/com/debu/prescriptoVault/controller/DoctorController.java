package com.debu.prescriptoVault.controller;

import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController{

    private final DoctorService doctorService;
    public DoctorController(DoctorService doctorService){
        this.doctorService=doctorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id){
        Doctor d=doctorService.findById(id);
        if(d==null)return ResponseEntity.notFound().build();
        d.setPassword(null);
        return ResponseEntity.ok(d);
    }

}
