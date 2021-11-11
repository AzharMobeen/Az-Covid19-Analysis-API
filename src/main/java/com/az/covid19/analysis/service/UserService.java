package com.az.covid19.analysis.service;

import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AuthenticationResponse getJwtForAuthorizeUser(AuthenticationRequest request);
    User createNewUser(String userName, String password);
    void updateUserJwt(User user);
}