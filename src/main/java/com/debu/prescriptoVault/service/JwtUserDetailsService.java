package com.debu.prescriptoVault.service;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService{

    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Doctor doctor=doctorRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new User(doctor.getEmail(),doctor.getPassword(),new ArrayList<>());
    }
}
