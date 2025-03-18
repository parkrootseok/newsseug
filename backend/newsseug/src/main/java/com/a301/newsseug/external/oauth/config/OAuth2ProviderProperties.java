package com.a301.newsseug.external.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider")
public record OAuth2ProviderProperties(
        ProviderInfo kakao,
        ProviderInfo google
) {
    public record ProviderInfo (String authorizationUri,String tokenUri, String userInfoUri) {}
}
