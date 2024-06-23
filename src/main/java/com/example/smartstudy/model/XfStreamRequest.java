package com.example.smartstudy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XfStreamRequest {
    private String question;
    private boolean newRequest;
    private String uid;
}
