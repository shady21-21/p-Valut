package com.debu.prescriptoVault.service;

import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.entity.Prescription;
import com.debu.prescriptoVault.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService{
    private final PrescriptionRepository prescriptionRepository;
    public PrescriptionService(PrescriptionRepository prescriptionRepository){this.prescriptionRepository=prescriptionRepository;}
    public Prescription savePrescription(Doctor doctor,String patientId,String patientPhone,String fileName,String filePath){
        Prescription p=new Prescription();
        p.setDoctor(doctor);
        p.setPatientId(patientId);
        p.setPatientPhone(patientPhone);
        p.setFileName(fileName);
        p.setFilePath(filePath);
        p.setUploadedAt(LocalDateTime.now());
        p.setAccessKey(generateAccessKey());
        return prescriptionRepository.save(p);
    }
    public List<Prescription> findForPatient(String patientId,String patientPhone,String accessKey){
        return prescriptionRepository.findByPatientIdAndPatientPhoneAndAccessKey(patientId,patientPhone,accessKey);
    }
    public List<Prescription> findByDoctorId(Long doctorId){return prescriptionRepository.findByDoctorId(doctorId);}
    private String generateAccessKey(){return UUID.randomUUID().toString().replace("-","").substring(0,8);}
}
