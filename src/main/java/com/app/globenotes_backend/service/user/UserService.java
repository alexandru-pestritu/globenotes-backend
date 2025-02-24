package com.app.globenotes_backend.service.user;

import com.app.globenotes_backend.entity.User;

import java.util.Optional;

public interface UserService {

    User createUser(String name, String email, String rawPassword);

    Optional<User> findByEmail(String email);

    void verifyUser(User user);

    boolean checkPassword(User user, String rawPassword);

    void updatePassword(User user, String newRawPassword);

}
