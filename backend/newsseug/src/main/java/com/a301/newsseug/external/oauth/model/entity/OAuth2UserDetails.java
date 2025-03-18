package com.a301.newsseug.external.oauth.model.entity;

import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;

public interface OAuth2UserDetails {

    ProviderType getProvider();
    String getProviderId();

}
