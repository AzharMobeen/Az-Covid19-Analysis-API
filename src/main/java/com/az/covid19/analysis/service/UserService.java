package com.az.covid19.analysis.service;

import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {

    AuthenticationResponse getJwtForAuthorizeUser(AuthenticationRequest request);
    List<User> fetchAllUsers();
}