package com.az.covid19.analysis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    private String userName;
    private String password;
}