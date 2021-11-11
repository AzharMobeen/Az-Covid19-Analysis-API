package com.az.covid19.analysis.validator;

import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class AuthenticationRequestValidator {

    public void validateRequest(AuthenticationRequest request) {
        log.debug("validateRequest request {}", request);
        if(!StringUtils.hasText(request.getUserName()))
            throw new CustomRuntimeException("Username is missing", "Provided username is not valid");
        if(!StringUtils.hasText(request.getPassword()))
            throw new CustomRuntimeException("Password is missing", "Provided password is not valid");
    }
}
