package com.debu.prescriptoVault.repository;

import com.debu.prescriptoVault.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription>findByPatientIdAndPatientPhoneAndAccessKey(String patientId,
                                                                 String patientPhone, String accessKey);
    List<Prescription>findByDoctorId(Long doctorId);
}
