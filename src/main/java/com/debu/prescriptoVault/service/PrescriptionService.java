package com.debu.prescriptoVault.service;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.entity.Patient;
import com.debu.prescriptoVault.entity.Prescription;
import com.debu.prescriptoVault.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescriptionService{

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository){
        this.prescriptionRepository=prescriptionRepository;
    }

    public Prescription savePrescription(Doctor doctor,Patient patient,String fileName,String filePath){
        Prescription p=new Prescription();
        p.setDoctor(doctor);
        p.setPatient(patient);
        p.setFileName(fileName);
        p.setFilePath(filePath);
        p.setUploadedAt(LocalDateTime.now());
        return prescriptionRepository.save(p);
    }
    public List<Prescription> findByPatientId(Long patientId){
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> findByDoctorId(Long doctorId){
        return prescriptionRepository.findByDoctorId(doctorId);
    }
    public Prescription findById(Long id){
        return prescriptionRepository.findById(id)
                .orElse(null);
    }
}
