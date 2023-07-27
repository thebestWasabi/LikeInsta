package io.khamzin.likeinsta.service;

import io.khamzin.likeinsta.dto.UserDTO;
import io.khamzin.likeinsta.entity.UserEntity;
import io.khamzin.likeinsta.entity.enums.ERole;
import io.khamzin.likeinsta.exception.UserExistException;
import io.khamzin.likeinsta.payload.request.SignupRequest;
import io.khamzin.likeinsta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserEntity createUser(SignupRequest userIn) {
        UserEntity user = new UserEntity();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            log.info("Saving User: {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error during registration, {}", e.getMessage());
            throw new UserExistException("User: " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public UserEntity updateUser(UserDTO userDTO, Principal principal) {
        var user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }

    public UserEntity getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        var username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}
