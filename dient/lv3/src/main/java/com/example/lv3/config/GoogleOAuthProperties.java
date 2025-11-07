package com.example.lv3.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "app.security.oauth2.google")
public class GoogleOAuthProperties {

    private String clientId;
    private String clientSecret;
    private List<String> scope = new ArrayList<>(Arrays.asList("openid", "profile", "email"));
    private String redirectUri = "{baseUrl}/login/oauth2/code/{registrationId}";

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

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        if (scope == null || scope.isEmpty()) {
            this.scope = new ArrayList<>(Arrays.asList("openid", "profile", "email"));
        } else {
            this.scope = scope;
        }
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        if (StringUtils.hasText(redirectUri)) {
            this.redirectUri = redirectUri;
        }
    }

    public boolean hasClientCredentials() {
        return StringUtils.hasText(clientId) && StringUtils.hasText(clientSecret);
    }
}
