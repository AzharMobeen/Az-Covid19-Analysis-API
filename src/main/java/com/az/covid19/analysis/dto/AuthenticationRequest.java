package com.az.covid19.analysis.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String userName;
    private String password;
}