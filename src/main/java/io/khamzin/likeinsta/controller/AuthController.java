package io.khamzin.likeinsta.controller;

import io.khamzin.likeinsta.payload.request.LoginRequest;
import io.khamzin.likeinsta.payload.request.SignupRequest;
import io.khamzin.likeinsta.payload.response.JWTTokenSuccessResponse;
import io.khamzin.likeinsta.payload.response.MessageResponse;
import io.khamzin.likeinsta.security.JWTTokenProvider;
import io.khamzin.likeinsta.security.SecurityConstants;
import io.khamzin.likeinsta.service.UserService;
import io.khamzin.likeinsta.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@PreAuthorize("premitAll()")
@RequestMapping("/api/auth")
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Validated @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User register successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Validated @RequestBody LoginRequest loginRequest,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authenticate);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

}
