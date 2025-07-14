package com.a301.newsseug.domain.member.model.entity;

import com.a301.newsseug.domain.member.model.entity.type.GenderType;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.global.model.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String nickname;

    @Setter
    private String profileImageUrl;

    @Setter
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Setter
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Embedded
    private OAuth2Details oAuth2Details;

    private Member(OAuth2Details oAuth2Details, RoleType role) {
        this.nickname = UUID.randomUUID().toString().substring(0, 6);
        this.profileImageUrl = "https://newsseug-bucket.s3.ap-northeast-2.amazonaws.com/profile/member/default.png";
        this.oAuth2Details = oAuth2Details;
        this.role = role;
    }

    public static Member createGuest(ProviderType provider, String providerId) {
        return new Member(OAuth2Details.of(provider, providerId), RoleType.ROLE_GUEST);
    }

    public void promoteToMember() {
        this.role = RoleType.ROLE_MEMBER;
    }

}
