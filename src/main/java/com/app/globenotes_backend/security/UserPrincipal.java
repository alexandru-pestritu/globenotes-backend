package com.app.globenotes_backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPrincipal {
    private Long userId;
    private String email;
}