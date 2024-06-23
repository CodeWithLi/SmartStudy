package com.example.smartstudy.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto extends UserSecurity{
    private String userId;
    private String name;

    private String sex;
    private String username;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private byte[] headImage;
    private String phone;
    private String idNumber;

    private String belongStatus;

    private String accountType;

    @Builder(toBuilder = true)
    public UserDto(String account, String password, String code, String purpose, String userId, String name, String sex, String username, String address, Date birthday, byte[] headImage, String phone, String idNumber, String accountType) {
        super(account, password, code, purpose);
        this.userId = userId;
        this.name = name;
        this.sex = sex;
        this.username = username;
        this.address = address;
        this.birthday = birthday;
        this.headImage = headImage;
        this.phone = phone;
        this.idNumber = idNumber;
        this.accountType = accountType;
    }
}
