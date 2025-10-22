package com.debu.prescriptoVault.controller;
import com.debu.prescriptoVault.config.JwtUtil;
import com.debu.prescriptoVault.dto.AuthRequest;
import com.debu.prescriptoVault.dto.AuthResponse;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController{

    private final DoctorService doctorService;

    private final AuthenticationManager authManager;

    private final JwtUtil jwtUtil;

    public AuthController(DoctorService doctorService,AuthenticationManager authManager,JwtUtil jwtUtil){
        this.doctorService=doctorService;
        this.authManager=authManager;
        this.jwtUtil=jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req){
        try{
            Doctor d=doctorService.register(req.getName(),req.getEmail(),req.getPassword());
            String token=jwtUtil.generateToken(d.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse("Doctor registered successfully",token));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Registration failed: "+e.getMessage(),null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req){
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword()));
            String token=jwtUtil.generateToken(req.getEmail());
            return ResponseEntity.ok(new AuthResponse("Login successful",token));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid credentials",null));
        }
    }
}
