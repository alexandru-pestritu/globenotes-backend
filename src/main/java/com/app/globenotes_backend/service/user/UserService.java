package com.app.globenotes_backend.service.user;

import com.app.globenotes_backend.dto.user.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO createUser(String email, String rawPassword);

    Optional<UserDTO> findByEmail(String email);

    Optional<UserDTO> findById(Long userId);

    void updateEmail(Long userId, String newEmail);

    void deleteUser(Long userId);

    void verifyUser(Long userId);

    boolean checkPassword(Long userId, String rawPassword);

    void updatePassword(Long userId, String newRawPassword);
}
