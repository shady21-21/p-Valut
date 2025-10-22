package com.debu.prescriptoVault.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileStorageService{

    private final Path uploadDir;

    public FileStorageService(@Value("${file.upload-dir}")String uploadDir) throws IOException{
        this.uploadDir=Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String storeFile(MultipartFile file,String filename) throws IOException{
        String clean=StringUtils.cleanPath(filename);
        Path target=this.uploadDir.resolve(clean);
        Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public Resource loadFileAsResource(String filepath) throws MalformedURLException{
        Path p=Paths.get(filepath);Resource resource=new UrlResource(p.toUri());
        if(resource.exists())return resource;return null;
    }
}
