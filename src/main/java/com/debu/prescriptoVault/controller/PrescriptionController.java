package com.debu.prescriptoVault.controller;

import com.debu.prescriptoVault.dto.UploadResponse;
import com.debu.prescriptoVault.entity.Doctor;
import com.debu.prescriptoVault.entity.Prescription;
import com.debu.prescriptoVault.service.DoctorService;
import com.debu.prescriptoVault.service.FileStorageService;
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

@RestController
@RequestMapping("/api")
public class PrescriptionController{

    private final FileStorageService fileStorageService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;

    public PrescriptionController(FileStorageService fileStorageService,DoctorService doctorService,PrescriptionService prescriptionService){
        this.fileStorageService=fileStorageService;
        this.doctorService=doctorService;
        this.prescriptionService=prescriptionService;
    }

    @PostMapping(value="/doctor/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPrescription(@RequestParam("patientId")String patientId,
                                                @RequestParam("patientPhone")String patientPhone,
                                                @RequestParam("file")MultipartFile file,Authentication authentication){
        try{
            if(authentication==null||!authentication.isAuthenticated())return ResponseEntity.status(401).body("Unauthorized");
            String username=((UserDetails)authentication.getPrincipal()).getUsername();
            Doctor doctor=doctorService.findByEmail(username);
            if(doctor==null)return ResponseEntity.status(404).body("Doctor not found");
            String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
            String path=fileStorageService.storeFile(file,fileName);
            Prescription pres=prescriptionService.savePrescription(doctor,patientId,patientPhone,fileName,path);
            return ResponseEntity.status(201).body(new UploadResponse(pres.getId(),pres.getFileName(),"Uploaded successfully. Give accessKey:"+pres.getAccessKey()+" and doctorId:"+doctor.getId()));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Upload failed:"+e.getMessage());
        }
    }

    @GetMapping("/patient/download")
    public ResponseEntity<?> downloadPrescription(@RequestParam("doctorId")Long doctorId,
                                                  @RequestParam("patientId")String patientId,
                                                  @RequestParam("patientPhone")String patientPhone,
                                                  @RequestParam("accessKey")String accessKey){
        try{
            List<Prescription> list=prescriptionService.findForPatient(patientId,patientPhone,accessKey);
            if(list.isEmpty())return ResponseEntity.status(404).body("No matching prescription found");
            Prescription p=list.stream().filter(x->x.getDoctor()!=null&&x.getDoctor().getId().equals(doctorId)).findFirst().orElse(null);
            if(p==null)return ResponseEntity.status(404).body("No matching prescription for given doctor");
            Resource resource=fileStorageService.loadFileAsResource(p.getFilePath());
            if(resource==null) return ResponseEntity.status(404).body("File not found");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+p.getFileName()+"\"").body(resource);
        }catch(MalformedURLException e){
            return ResponseEntity.status(500).body("File error");
        }
    }
}
