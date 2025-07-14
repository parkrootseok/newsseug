package com.a301.newsseug.domain.member.model.entity;

import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Details {

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(nullable = false, updatable = false)
    private String providerId;

    private OAuth2Details(ProviderType provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    public static OAuth2Details of(ProviderType provider, String providerId) {
        return new OAuth2Details(provider, providerId);
    }

}
