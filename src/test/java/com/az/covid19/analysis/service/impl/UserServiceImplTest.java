package com.az.covid19.analysis.service.impl;

import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.entity.User;
import com.az.covid19.analysis.repository.UserRepository;
import com.az.covid19.analysis.util.JwtUtil;
import com.az.covid19.analysis.validator.AuthenticationRequestValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("UserService Test cases")
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationRequestValidator authenticationRequestValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Success Scenario")
    @Test
    void testGetJwtForAuthorizeUser() {
        AuthenticationRequest request = AuthenticationRequest.builder().userName("test").password("test").build();
        Mockito.doNothing().when(authenticationRequestValidator).validateRequest(request);
        Optional<User> user = Optional.empty();
        Mockito.when(userRepository.findByUserName(any())).thenReturn(user);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("asdfasdfa@#asdfa");
        user = Optional.of(new User());
        Mockito.when(userRepository.save(new User())).thenReturn(new User());
        Mockito.when(userRepository.findByUserName(any())).thenReturn(user);
        Mockito.when(jwtUtil.generateToken(any())).thenReturn("token");
        AuthenticationResponse response = userService.getJwtForAuthorizeUser(request);
        assertNotNull(response.getJwt());
    }

    @DisplayName("Negative Scenario")
    @Test
    void testGetJwtForAuthorizeUser_Exception() {
        AuthenticationRequest request = AuthenticationRequest.builder().userName("test").password("test").build();
        Mockito.doNothing().when(authenticationRequestValidator).validateRequest(request);
        Optional<User> user = Optional.empty();
        Mockito.when(userRepository.findByUserName("test")).thenReturn(user);
        Mockito.when(passwordEncoder.encode(any())).thenReturn(any());
        Mockito.when(userRepository.save(new User())).thenReturn(new User());
        User newUser = new User();
        newUser.setUserName(request.getUserName()); newUser.setPassword(request.getPassword());;
        Mockito.when(jwtUtil.generateToken(any())).thenReturn("token");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getJwtForAuthorizeUser(request),
                "Bad Credentials");
    }

    @Test
    void fetchAllUsers() {
        User user = new User();
        user.setUserName("Test");
        user.setPassword("Password");
        user.setJwt("sadfasdfadfa.asdfasdfasdfadsf.asdfasdfadsfasdf");
        List<User> userList = Arrays.asList(user);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<User> response = userService.fetchAllUsers();
        assertTrue(response.size() > 0);
    }

    @Test
    void testLoadUserByUsername() {
        Optional<User> user = Optional.empty();
        Mockito.when(userRepository.findByUserName(any())).thenReturn(user);
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(any(String.class)),
                "Bad Credentials");
    }
}