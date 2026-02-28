package com.bookmyshow.service;

import com.bookmyshow.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private static final String RESEND_API = "https://api.resend.com/emails";
    private final AppProperties appProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public EmailService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void sendOtpEmail(String to, String name, String otp) {
        if (appProperties.getResendApiKey() == null || appProperties.getResendApiKey().isBlank()) {
            log.warn("Resend API key not set; skipping OTP email to {}", to);
            return;
        }
        try {
            String html = loadTemplate("otp.html")
                    .replace("#{name}", name != null ? name : "")
                    .replace("#{otp}", otp != null ? otp : "");
            send(to, "Mail from BookMyShow", "Hi " + name + " this is your reset password otp " + otp, html);
        } catch (Exception e) {
            log.error("Failed to send OTP email", e);
        }
    }

    public void sendTicketEmail(String to, String name, String movie, String theatre,
                                Object date, String time, Object seats, Object amount, String transactionId) {
        if (appProperties.getResendApiKey() == null || appProperties.getResendApiKey().isBlank()) {
            log.warn("Resend API key not set; skipping ticket email to {}", to);
            return;
        }
        try {
            String html = loadTemplate("ticket.html")
                    .replace("#{name}", nullToEmpty(name))
                    .replace("#{movie}", nullToEmpty(movie))
                    .replace("#{theatre}", nullToEmpty(theatre))
                    .replace("#{date}", String.valueOf(date))
                    .replace("#{time}", nullToEmpty(time))
                    .replace("#{seats}", String.valueOf(seats))
                    .replace("#{amount}", String.valueOf(amount))
                    .replace("#{transactionId}", nullToEmpty(transactionId));
            send(to, "Mail from BookMyShow - Ticket", "Your ticket details", html);
        } catch (Exception e) {
            log.error("Failed to send ticket email", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void send(String to, String subject, String text, String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(appProperties.getResendApiKey());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(Map.of(
                "from", "onboarding@resend.dev",
                "to", to, //for testing
                "subject", subject,
                "text", text,
                "html", html
        ), headers);
        restTemplate.exchange(RESEND_API, HttpMethod.POST, entity, Void.class);
    }

    private String loadTemplate(String name) {
        try {
            var in = getClass().getClassLoader().getResourceAsStream("templates/" + name);
            return in != null ? new String(in.readAllBytes()) : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String nullToEmpty(String s) {
        return s != null ? s : "";
    }
}
