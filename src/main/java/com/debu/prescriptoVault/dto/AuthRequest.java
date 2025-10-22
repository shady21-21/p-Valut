package com.debu.prescriptoVault.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequest{

    private String name;
    private String email;
    private String password;
}
