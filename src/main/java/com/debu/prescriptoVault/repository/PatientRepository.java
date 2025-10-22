package com.debu.prescriptoVault.repository;
import com.debu.prescriptoVault.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long>{
    Optional<Patient> findByEmail(String email);
}
