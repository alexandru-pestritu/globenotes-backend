package com.app.globenotes_backend.service.email;

import java.util.Map;

public interface EmailService {

    void sendTemplatedEmail(String to, String templateName, Map<String, String> templateData);

    void sendTextEmail(String to, String subject, String body);

    void sendHtmlEmail(String to, String subject, String htmlContent);
}