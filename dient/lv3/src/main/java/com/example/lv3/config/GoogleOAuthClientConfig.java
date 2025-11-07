package com.example.lv3.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(GoogleOAuthProperties.class)
public class GoogleOAuthClientConfig {

    @Bean
    @Conditional(GoogleCredentialsProvidedCondition.class)
    public ClientRegistrationRepository googleClientRegistrationRepository(GoogleOAuthProperties properties) {
        ClientRegistration registration = CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .scope(properties.getScope())
                .redirectUri(properties.getRedirectUri())
                .build();

        return new InMemoryClientRegistrationRepository(registration);
    }

    static class GoogleCredentialsProvidedCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment environment = context.getEnvironment();
            String clientId = environment.getProperty("app.security.oauth2.google.client-id");
            String clientSecret = environment.getProperty("app.security.oauth2.google.client-secret");
            return StringUtils.hasText(clientId) && StringUtils.hasText(clientSecret);
        }
    }
}
