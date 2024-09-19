package com.a301.newsseug.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2Details {

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(updatable = false, nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static OAuth2Details of(ProviderType provider, String providerId, Role role) {
        return OAuth2Details.builder()
                .provider(provider)
                .providerId(providerId)
                .role(role)
                .build();
    }

}