package com.example.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    private String token;
    private String status;
    private List<String> authorities;
}
