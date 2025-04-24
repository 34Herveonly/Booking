package com.example.Booking.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// src/main/java/com/yourpackage/config/PesaPalConfig.java
@Configuration
@ConfigurationProperties(prefix = "pesapal")
@Data // Lombok
public class PesaPalConfig {
    private String consumerKey;
    private String consumerSecret;
    private String baseUrl;
    private String callbackUrl;
    private String ipnId;

    // Sandbox vs Production URLs
    public String getAuthUrl() {
        return baseUrl + "/api/Auth/RequestToken";
    }
    public String getOrderUrl() {
        return baseUrl + "/api/Transactions/SubmitOrderRequest";
    }
    public String getStatusUrl() {
        return baseUrl + "/api/Transactions/GetTransactionStatus";
    }
}
