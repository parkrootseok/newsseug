package com.a301.newsseug.external.oauth.service;

import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties;
import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties.ProviderInfo;
import com.a301.newsseug.external.oauth.exception.OAuth2UnlinkException;
import com.a301.newsseug.external.oauth.factory.OAuthRevocationCheckFactory;
import com.a301.newsseug.external.oauth.model.entity.CustomOAuth2User;
import com.a301.newsseug.external.oauth.model.entity.GoogleUserDetails;
import com.a301.newsseug.external.oauth.model.entity.KakaoUserDetails;
import com.a301.newsseug.external.oauth.model.entity.OAuth2UserDetails;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final OAuthRevocationCheckFactory oAuthRevocationCheckFactory;
    private final OAuth2ProviderProperties oAuth2ProviderProperties;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ProviderType type = ProviderType.from(userRequest.getClientRegistration().getRegistrationId());
        String accessToken = userRequest.getAccessToken().getTokenValue();
        if (oAuthRevocationCheckFactory.getStrategy(type).isRevoked(accessToken, getProviderInfo(type))) {
            throw new OAuth2UnlinkException();
        }

        OAuth2UserDetails oAuth2UserDetails = getOAuth2UserDetails(type, oAuth2User);
        String providerId = oAuth2UserDetails.getProviderId();
        Member member = memberRepository.findByProviderId(type, providerId)
                .orElseGet(() -> memberRepository.save(Member.createGuest(type, providerId)));
        return CustomOAuth2User.of(member, oAuth2User.getAttributes());
    }

    private ProviderInfo getProviderInfo(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> oAuth2ProviderProperties.kakao();
            case GOOGLE -> oAuth2ProviderProperties.google();
        };
    }

    private OAuth2UserDetails getOAuth2UserDetails(ProviderType provider, OAuth2User oAuth2User) {
        return switch (provider) {
            case KAKAO -> new KakaoUserDetails(oAuth2User.getAttributes());
            case GOOGLE -> new GoogleUserDetails(oAuth2User.getAttributes());
        };
    }

}
