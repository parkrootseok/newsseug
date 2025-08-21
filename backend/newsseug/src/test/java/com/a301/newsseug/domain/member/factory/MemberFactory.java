package com.a301.newsseug.domain.member.factory;

import com.a301.newsseug.domain.member.builder.MemberTestBuilder;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;

public final class MemberFactory {
    private MemberFactory() {}

    public static Member guest(ProviderType provider, String providerId) {
        return MemberTestBuilder.aMember()
                .provider(provider)
                .providerId(providerId)
                .buildGuest();
    }

    public static Member member(ProviderType provider, String providerId) {
        return MemberTestBuilder.aMember()
                .provider(provider)
                .providerId(providerId)
                .role(RoleType.ROLE_MEMBER)
                .buildMember();
    }
}
