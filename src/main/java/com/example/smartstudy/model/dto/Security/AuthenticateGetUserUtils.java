package com.example.smartstudy.model.dto.Security;

import com.example.smartstudy.model.dto.Security.Lgoin.AdminLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.IdNumberLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.PhoneLoginUserDetails;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticateGetUserUtils {

    public static User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getUsername().matches("^1[3-9]\\d{9}$")){
            PhoneLoginUserDetails phoneLoginUserDetails= (PhoneLoginUserDetails) userDetails;
            return phoneLoginUserDetails.getUser();
        } else if (userDetails.getUsername().matches("^\\d{18}$")) {
            IdNumberLoginUserDetails idNumberLoginUserDetails= (IdNumberLoginUserDetails) userDetails;
            return idNumberLoginUserDetails.getUser();
        } else if (userDetails.getUsername().equalsIgnoreCase("admin")) {
            AdminLoginUserDetails adminLoginUserDetails= (AdminLoginUserDetails) userDetails;
            return adminLoginUserDetails.getUser();
        }
        return null;
    }
}
