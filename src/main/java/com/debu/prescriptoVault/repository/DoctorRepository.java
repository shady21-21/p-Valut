package com.debu.prescriptoVault.repository;
import com.debu.prescriptoVault.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long>{
    Optional<Doctor> findByEmail(String email);
}
