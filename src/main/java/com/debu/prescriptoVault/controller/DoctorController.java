package com.debu.prescriptoVault.controller;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.service.DoctorService;
import com.debu.prescriptoVault.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin
public class DoctorController{

    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    public DoctorController(DoctorService doctorService,PrescriptionService prescriptionService){
        this.doctorService=doctorService;
        this.prescriptionService=prescriptionService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id){
        Doctor d=doctorService.findById(id);
        if(d==null) return ResponseEntity.notFound().build();
        d.setPassword(null);
        return ResponseEntity.ok(d);
    }


}
