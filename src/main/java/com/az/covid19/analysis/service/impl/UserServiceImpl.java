package com.az.covid19.analysis.service.impl;

import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.dto.MyUserDetails;
import com.az.covid19.analysis.entity.User;
import com.az.covid19.analysis.repository.UserRepository;
import com.az.covid19.analysis.service.UserService;
import com.az.covid19.analysis.util.JwtUtil;
import com.az.covid19.analysis.validator.AuthenticationRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationRequestValidator authenticationRequestValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse getJwtForAuthorizeUser(AuthenticationRequest request) {
        log.debug("getJwtForAuthorizeUser method called!");
        authenticationRequestValidator.validateRequest(request);
        User user = createNewUser(request.getUserName(), request.getPassword());
        final UserDetails userDetails = loadUserByUsername(request.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);
        user.setJwt(jwt);
        updateUserJwt(user);
        return new AuthenticationResponse(jwt);
    }

    // It will create user only if user doesn't exist with provided username
    @Override
    public User createNewUser(String userName, String password) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        User newUser = null;
        if(!userOptional.isPresent()) {
            newUser = new User();
            newUser.setUserName(userName);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser = userRepository.save(newUser);
        } else {
            newUser = userOptional.get();
        }
        return newUser;
    }

    @Override
    public void updateUserJwt(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    // As per requirement we need to generate token for any username/password
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("loadUserByUsername method called! [{}]", username);
        Optional<User> user = userRepository.findByUserName(username);
        user.orElseThrow( () -> new UsernameNotFoundException("Bad Credentials"));
        log.debug("UserName is belongs to valid user {},{}", username, user.get());
        return user.map(MyUserDetails::new).get();
    }
}