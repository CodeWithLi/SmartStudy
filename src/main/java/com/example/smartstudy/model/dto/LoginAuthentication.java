package com.example.smartstudy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAuthentication {
    private String account;
    private String password;
    private String code;
    private String purpose;
}
