package com.app.globenotes_backend.util;

import com.app.globenotes_backend.dto.social.FacebookUserInfo;
import com.app.globenotes_backend.dto.social.GoogleUserInfo;
import com.app.globenotes_backend.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SocialAuthUtil {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public GoogleUserInfo validateGoogleToken(String idToken) {
        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiException("Invalid Google token");
            }

            JsonNode root = objectMapper.readTree(response.body());
            GoogleUserInfo info = new GoogleUserInfo();
            info.setSub(root.path("sub").asText(null));
            info.setEmail(root.path("email").asText(null));
            info.setName(root.path("name").asText(null));
            info.setPicture(root.path("picture").asText(null));

            if (info.getSub() == null || info.getEmail() == null) {
                throw new ApiException("Invalid Google token data");
            }

            return info;
        } catch (Exception e) {
            throw new ApiException("Error validating Google token: " + e.getMessage());
        }
    }


    public FacebookUserInfo validateFacebookToken(String accessToken) {
        try {
            String url = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + accessToken;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiException("Invalid Facebook token");
            }

            JsonNode root = objectMapper.readTree(response.body());
            FacebookUserInfo info = new FacebookUserInfo();
            info.setId(root.path("id").asText(null));
            info.setName(root.path("name").asText(null));
            info.setEmail(root.path("email").asText(null));
            JsonNode picNode = root.path("picture").path("data").path("url");
            info.setPicture(picNode.asText(null));

            if (info.getId() == null || info.getEmail() == null) {
                throw new ApiException("Invalid Facebook token data");
            }

            return info;
        } catch (Exception e) {
            throw new ApiException("Error validating Facebook token: " + e.getMessage());
        }
    }
}
