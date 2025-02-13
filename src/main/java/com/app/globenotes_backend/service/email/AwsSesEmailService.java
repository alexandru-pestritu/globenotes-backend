package com.app.globenotes_backend.service.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsSesEmailService implements EmailService {

    private final SesClient sesClient;

    @Value("${aws.ses.from-address}")
    private String fromAddress;

    @Override
    public void sendTemplatedEmail(String to, String templateName, Map<String, String> templateData) {
        String templateDataJson = toJson(templateData);

        SendTemplatedEmailRequest request = SendTemplatedEmailRequest.builder()
                .destination(Destination.builder()
                        .toAddresses(to)
                        .build())
                .template(templateName)
                .templateData(templateDataJson)
                .source(fromAddress)
                .build();

        try {
            SendTemplatedEmailResponse response = sesClient.sendTemplatedEmail(request);
        } catch (SesException e) {
            throw new RuntimeException("Failed to send templated email", e);
        }
    }

    @Override
    public void sendTextEmail(String to, String subject, String body) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(to).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder()
                                .text(Content.builder().data(body).build())
                                .build())
                        .build())
                .source(fromAddress)
                .build();
        try {
            SendEmailResponse response = sesClient.sendEmail(request);
        } catch (SesException e) {
            throw new RuntimeException("Failed to send text email", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(to).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder()
                                .html(Content.builder().data(htmlContent).build())
                                .build())
                        .build())
                .source(fromAddress)
                .build();
        try {
            SendEmailResponse response = sesClient.sendEmail(request);
        } catch (SesException e) {
            throw new RuntimeException("Failed to send html email", e);
        }
    }

    private String toJson(Map<String, String> placeholders) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(placeholders);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert templateData to JSON", e);
        }
    }
}