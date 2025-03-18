package com.a301.newsseug.external.oauth.factory;

import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.external.oauth.strategy.GoogleRevocationCheckStrategy;
import com.a301.newsseug.external.oauth.strategy.KakaoRevocationCheckStrategy;
import com.a301.newsseug.external.oauth.strategy.OAuthRevocationCheckStrategy;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OAuthRevocationCheckFactory {

    private final Map<ProviderType, OAuthRevocationCheckStrategy> strategyMap;

    public OAuthRevocationCheckFactory(KakaoRevocationCheckStrategy kakaoRevocationCheckStrategy, GoogleRevocationCheckStrategy googleRevocationCheckStrategy) {
        strategyMap = Map.of(
                ProviderType.KAKAO, kakaoRevocationCheckStrategy,
                ProviderType.GOOGLE, googleRevocationCheckStrategy
        );
    }

    public OAuthRevocationCheckStrategy getStrategy(ProviderType providerType) {
        return strategyMap.getOrDefault(providerType, (type, info) -> true);
    }
}
