package com.a301.newsseug.external.oauth.service;

import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties;
import com.a301.newsseug.external.oauth.config.OAuth2ProviderProperties.ProviderInfo;
import com.a301.newsseug.external.oauth.exception.OAuth2UnlinkException;
import com.a301.newsseug.external.oauth.factory.OAuthRevocationCheckFactory;
import com.a301.newsseug.external.oauth.model.entity.CustomOAuth2User;
import com.a301.newsseug.external.oauth.model.entity.GoogleUserDetails;
import com.a301.newsseug.external.oauth.model.entity.KakaoUserDetails;
import com.a301.newsseug.external.oauth.model.entity.OAuth2UserDetails;
import com.a301.newsseug.domain.member.exception.InvalidProviderTypeException;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.domain.member.repository.MemberRepository;
import java.util.Optional;
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
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        ProviderType provider =  ProviderType.convertToEnum(userRequest.getClientRegistration().getRegistrationId());
        String accessToken = userRequest.getAccessToken().getTokenValue();
        ProviderInfo providerInfo = getProviderInfo(provider);

        if (oAuthRevocationCheckFactory.getStrategy(provider).isRevoked(accessToken, providerInfo)) {
            throw new OAuth2UnlinkException();
        }

        OAuth2UserDetails oAuth2UserDetails = getOAuth2UserDetails(provider.getValue(), oAuth2User);
        String providerId = oAuth2UserDetails.getProviderId();

        Optional<Member> member = memberRepository.findByProviderId(oAuth2UserDetails.getProviderId());

        return CustomOAuth2User.of(
                member.orElseGet(() ->
                        memberRepository.save(
                                Member.builder()
                                        .provider(ProviderType.convertToEnum(provider.getValue()))
                                        .providerId(providerId)
                                        .role(RoleType.ROLE_MEMBER)
                                        .build()
                )),
                oAuth2User.getAttributes()
        );

    }

    private OAuth2UserDetails getOAuth2UserDetails(String provider, OAuth2User oAuth2User) throws InvalidProviderTypeException {
        switch (ProviderType.convertToEnum(provider)) {
            case KAKAO -> {
                log.info("카카오 로그인");
                return new KakaoUserDetails(oAuth2User.getAttributes());
            }
            case GOOGLE -> {
                log.info("구글 로그인");
                return  new GoogleUserDetails(oAuth2User.getAttributes());
            }
        }

        throw new InvalidProviderTypeException();
    }

    private ProviderInfo getProviderInfo(ProviderType provider) {
        switch (provider) {
            case KAKAO -> {
                return oAuth2ProviderProperties.kakao();
            }
            case GOOGLE -> {
                return oAuth2ProviderProperties.google();
            }
            default -> throw new InvalidProviderTypeException();
        }
    }

}
