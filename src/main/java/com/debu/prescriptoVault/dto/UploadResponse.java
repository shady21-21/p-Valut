package com.debu.prescriptoVault.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadResponse{
    private Long id;
    private String fileName;
    private String message;
}
