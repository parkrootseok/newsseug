package com.a301.newsseug.external.oauth.strategy;

import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties.ProviderInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoRevocationCheckStrategy implements OAuthRevocationCheckStrategy {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean isRevoked(String accessToken, ProviderInfo providerInfo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    providerInfo.userInfoUri(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            return !response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return true;
        }
    }

}
