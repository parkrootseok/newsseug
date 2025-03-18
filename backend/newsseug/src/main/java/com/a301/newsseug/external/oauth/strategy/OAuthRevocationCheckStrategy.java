package com.a301.newsseug.external.oauth.strategy;

import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties.ProviderInfo;

public interface OAuthRevocationCheckStrategy {

    boolean isRevoked(String accessToken, ProviderInfo providerInfo);

}
