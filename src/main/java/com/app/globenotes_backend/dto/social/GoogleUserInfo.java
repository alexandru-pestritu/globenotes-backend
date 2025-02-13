package com.app.globenotes_backend.dto.social;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;
    private String picture;
}
