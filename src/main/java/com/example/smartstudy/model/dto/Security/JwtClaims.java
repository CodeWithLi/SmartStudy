package com.example.smartstudy.model.dto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtClaims {
    private String userId;
    private String loginType;
    private List<String> authorities;
}
