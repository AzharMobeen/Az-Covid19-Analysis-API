package com.az.covid19.analysis.controller;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(AppConstants.API_USER_AUTHENTICATE_URI)
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request){
        log.debug("authenticateUser method called with request [{}]",request);
        AuthenticationResponse response = userService.getJwtForAuthorizeUser(request);
        return ResponseEntity.ok(response);
    }
}
