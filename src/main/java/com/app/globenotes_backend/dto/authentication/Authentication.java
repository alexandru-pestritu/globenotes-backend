package com.app.globenotes_backend.dto.authentication;

import com.app.globenotes_backend.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
