package com.az.covid19.analysis.controller;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.dto.AuthenticationRequest;
import com.az.covid19.analysis.dto.AuthenticationResponse;
import com.az.covid19.analysis.entity.User;
import com.az.covid19.analysis.exception.ErrorMessage;
import com.az.covid19.analysis.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(AppConstants.URI_FOR_USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",  content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
        }
    )
    @PostMapping(AppConstants.URI_FOR_USERS_AUTHENTICATE)
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody(required = true) AuthenticationRequest request){
        log.info("authenticateUser method called with request [{}]",request);
        AuthenticationResponse response = userService.getJwtForAuthorizeUser(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
        }
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllActiveUsers() {
        log.info("getAllActiveUsers method called");
        List<User> users = userService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }
}
