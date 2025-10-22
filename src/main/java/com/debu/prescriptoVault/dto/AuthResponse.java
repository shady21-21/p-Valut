package com.debu.prescriptoVault.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse{
    private String message;
    private String token;
}
