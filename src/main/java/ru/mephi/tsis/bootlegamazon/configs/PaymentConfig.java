package ru.mephi.tsis.bootlegamazon.configs;


import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentConfig {
    @Value("AadxMZIF7-FYSt1LCAjKH-nMFgnbzNewOoBPMr6ociETyhCLyTzKp2UVL-XRBAQy3lzD-N25uDp6qbwV")
    private String clientId;
    @Value("EKRb84OjZ8YFplbBQq0YTVfvA74XKJB9Nt--mCpe9n2uTZbI3ulIw3cYAT0MnO-3p2D5IZuKaJLVlcq3")
    private String clientSecret;
    @Value("sandbox")
    private String mode;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> map = new HashMap<>();
        map.put("mode", mode);
        return map;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() {
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }
}
