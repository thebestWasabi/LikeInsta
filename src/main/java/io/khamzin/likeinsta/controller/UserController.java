package io.khamzin.likeinsta.controller;

import io.khamzin.likeinsta.dto.UserDTO;
import io.khamzin.likeinsta.mapper.UserMapper;
import io.khamzin.likeinsta.service.UserService;
import io.khamzin.likeinsta.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        var user = userService.getCurrentUser(principal);
        var userDTO = userMapper.toDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        var user = userService.getUserById(Long.parseLong(userId));
        var userDTO = userMapper.toDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Object> updateUser(@RequestBody @Validated UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        var errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var user = userService.updateUser(userDTO, principal);
        var userUpdated = userMapper.toDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
