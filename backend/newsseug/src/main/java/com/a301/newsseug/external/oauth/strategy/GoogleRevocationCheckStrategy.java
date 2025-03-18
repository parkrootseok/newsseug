package com.a301.newsseug.external.oauth.strategy;

import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties.ProviderInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleRevocationCheckStrategy implements OAuthRevocationCheckStrategy {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean isRevoked(String accessToken, ProviderInfo providerInfo) {
        try {
            String requestUrl = providerInfo.userInfoUri() + "?access_token=" + accessToken;
            ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
            return !response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return true;
        }
    }

}
