package com.a301.newsseug.domain.member.builder;

import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.type.GenderType;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.domain.member.model.entity.OAuth2Details;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberTestBuilder {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    private Long id = SEQ.getAndIncrement();
    private String nickname = "user-" + SEQ.getAndIncrement();
    private String profileImageUrl = "https://example.com/default.png";
    private GenderType gender = GenderType.MALE;
    private LocalDate birth = LocalDate.of(1990, 1, 1);
    private RoleType role = RoleType.ROLE_GUEST;
    private ProviderType provider = ProviderType.GOOGLE;
    private String providerId = "pid-" + System.nanoTime();

    private MemberTestBuilder() {}

    public static MemberTestBuilder aMember() { return new MemberTestBuilder(); }


    public MemberTestBuilder id(Long id) { this.id = id; return this; }
    public MemberTestBuilder nickname(String nickname) { this.nickname = nickname; return this; }
    public MemberTestBuilder profileImageUrl(String url) { this.profileImageUrl = url; return this; }
    public MemberTestBuilder gender(GenderType gender) { this.gender = gender; return this; }
    public MemberTestBuilder birth(LocalDate birth) { this.birth = birth; return this; }
    public MemberTestBuilder role(RoleType role) { this.role = role; return this; }
    public MemberTestBuilder provider(ProviderType provider) { this.provider = provider; return this; }
    public MemberTestBuilder providerId(String providerId) { this.providerId = providerId; return this; }

    public Member buildGuest() {
        Member m = Member.createGuest(this.provider, this.providerId);
        m.setNickname(this.nickname);
        m.setProfileImageUrl(this.profileImageUrl);
        m.setGender(this.gender);
        m.setBirth(this.birth);

        if (this.role == RoleType.ROLE_MEMBER) {
            m.promoteToMember();
        }
        if (this.id != null) {
            ReflectionTestUtils.setField(m, "id", this.id);
        }
        return m;
    }

    public Member buildMember() {
        Member m = buildGuest();
        if (m.getRole() != RoleType.ROLE_MEMBER) {
            m.promoteToMember();
        }
        return m;
    }

    public Member buildRaw() {
        OAuth2Details details = OAuth2Details.of(this.provider, this.providerId);
        Member m = Member.createGuest(this.provider, this.providerId);
        m.setNickname(this.nickname);
        m.setProfileImageUrl(this.profileImageUrl);
        m.setGender(this.gender);
        m.setBirth(this.birth);
        if (this.role == RoleType.ROLE_MEMBER) {
            m.promoteToMember();
        }
        if (this.id != null) {
            ReflectionTestUtils.setField(m, "id", this.id);
        }
        return m;
    }
}
