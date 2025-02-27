package com.app.globenotes_backend.service.user;

import com.app.globenotes_backend.dto.user.UserDTO;
import com.app.globenotes_backend.entity.User;

import java.util.Optional;

public interface UserService {

    UserDTO createUser(String email, String rawPassword);

    Optional<UserDTO> findByEmail(String email);

    void verifyUser(Long userId);

    boolean checkPassword(Long userId, String rawPassword);

    void updatePassword(Long userId, String newRawPassword);
}
