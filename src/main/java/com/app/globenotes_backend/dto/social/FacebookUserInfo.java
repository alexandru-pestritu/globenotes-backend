package com.app.globenotes_backend.dto.social;

import lombok.Data;

@Data
public class FacebookUserInfo {
    private String id;
    private String name;
    private String email;
    private String picture;
}
