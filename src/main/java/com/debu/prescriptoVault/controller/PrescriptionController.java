package com.debu.prescriptoVault.controller;

import com.debu.prescriptoVault.dto.PatientPrescriptionDto;
import com.debu.prescriptoVault.dto.UploadResponse;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.entity.Patient;
import com.debu.prescriptoVault.entity.Prescription;
import com.debu.prescriptoVault.service.DoctorService;
import com.debu.prescriptoVault.service.EmailService;
import com.debu.prescriptoVault.service.FileStorageService;
import com.debu.prescriptoVault.service.PatientService;
import com.debu.prescriptoVault.service.PrescriptionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PrescriptionController {

    private final FileStorageService fileStorageService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final PatientService patientService;
    private final EmailService emailService;

    public PrescriptionController(FileStorageService fileStorageService,
                                  DoctorService doctorService,
                                  PrescriptionService prescriptionService,
                                  PatientService patientService,
                                  EmailService emailService) {
        this.fileStorageService = fileStorageService;
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
        this.patientService = patientService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/doctor/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPrescription(@RequestParam("patientName") String patientName,
                                                @RequestParam("patientPhone") String patientPhone,
                                                @RequestParam("patientEmail") String patientEmail,
                                                @RequestParam(value = "patientAddress", required = false) String patientAddress,
                                                @RequestParam(value = "gender", required = false) String gender,
                                                @RequestParam(value = "age", required = false) Integer age,
                                                @RequestParam(value = "bloodGroup", required = false) String bloodGroup,
                                                @RequestParam("file") MultipartFile file,
                                                Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated())
                return ResponseEntity.status(401).body("Unauthorized");

            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Doctor doctor = doctorService.findByEmail(username);
            if (doctor == null)
                return ResponseEntity.status(404).body("Doctor not found");

            Patient.Gender g = null;
            Patient.BloodGroup bg = null;
            try {
                if (gender != null) g = Patient.Gender.valueOf(gender.toUpperCase());
            } catch (Exception ignored) {
            }
            try {
                if (bloodGroup != null)
                    bg = Patient.BloodGroup.valueOf(bloodGroup.toUpperCase().replace("+", "_POS").replace("-", "_NEG"));
            } catch (Exception ignored) {
            }

            Patient patient = patientService.findOrCreate(patientName, patientPhone, patientEmail, patientAddress, g, age, bg);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String path = fileStorageService.storeFile(file, fileName);
            Prescription pres = prescriptionService.savePrescription(doctor, patient, fileName, path);
            return ResponseEntity.status(201).body(new UploadResponse(pres.getId(), pres.getFileName(), "Uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/patient/download")
    public ResponseEntity<?> downloadPrescriptionById(@RequestParam("id") Long id) {
        try {
            Prescription p = prescriptionService.findById(id);
            if (p == null)
                return ResponseEntity.status(404).body("Not found");

            Resource resource = fileStorageService.loadFileAsResource(p.getFilePath());
            if (resource == null)
                return ResponseEntity.status(404).body("File not found");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + p.getFileName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body("File error");
        }
    }

    @PostMapping("/patient/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam("email") String email) {
        try {
            Patient patient = patientService.findByEmail(email);
            if (patient == null)
                return ResponseEntity.status(404).body("Patient not found");

            // Generate, persist, and send OTP via PatientService which internally calls EmailService
            patientService.sendOtp(email);

            return ResponseEntity.ok("OTP sent to email");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send OTP");
        }
    }

    @PostMapping("/patient/verify-otp")
    public ResponseEntity<?> verifyOtpAndGetPrescriptions(@RequestParam("email") String email,
                                                          @RequestParam("otp") String otp) {
        try {
            boolean ok = patientService.verifyOtp(email, otp);
            if (!ok) return ResponseEntity.status(401).body("Invalid or expired OTP");

            Patient patient = patientService.findByEmail(email);
            if (patient == null) return ResponseEntity.status(404).body("Patient not found");

            List<Prescription> list = prescriptionService.findByPatientId(patient.getId());
            List<PatientPrescriptionDto> resp = list.stream().map(x -> new PatientPrescriptionDto(
                    x.getId(),
                    x.getFileName(),
                    x.getUploadedAt(),
                    x.getDoctor() != null ? x.getDoctor().getName() : null,
                    x.getDoctor() != null ? x.getDoctor().getEmail() : null,
                    patient.getName(),
                    patient.getEmail()
            )).collect(Collectors.toList());

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error");
        }
    }
}
