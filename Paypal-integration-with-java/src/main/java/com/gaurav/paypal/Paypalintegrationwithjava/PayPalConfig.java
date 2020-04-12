package com.gaurav.paypal.Paypalintegrationwithjava;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    @Value(value = "${paypal.mode}")
    private String mode;

    @Value(value = "${paypal.client.id}")
    private String clientId;

    @Value(value = "${paypal.client.secret}")
    private String clientSecret;


    @Bean
    public Map<String, String> paypalSDK() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", getMode());
        return configMap;
    }


    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(getClientId(), getClientSecret(), paypalSDK());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext apiContext = new APIContext(oAuthTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalSDK());
        return apiContext;
    }






    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
