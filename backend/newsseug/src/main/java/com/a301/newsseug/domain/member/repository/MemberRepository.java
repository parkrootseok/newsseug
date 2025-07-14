package com.a301.newsseug.domain.member.repository;

import com.a301.newsseug.domain.member.exception.NotExistMemberException;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getOrThrowById(Long id) {
        return findById(id).orElseThrow(NotExistMemberException::new);
    }

    default Member getOrThrow(String providerId) {
        return findByProviderId(providerId).orElseThrow(NotExistMemberException::new);
    }

    @Query("SELECT m "
            + "FROM Member m "
            + "WHERE m.oAuth2Details.providerId = :providerId")
    Optional<Member> findByProviderId(
            @Param("providerId") String providerId
    );

    @Query("SELECT m "
            + "FROM Member m "
            + "WHERE m.oAuth2Details.provider = :provider "
            + "AND m.oAuth2Details.providerId = :providerId")
    Optional<Member> findByProviderId(
            @Param("provider") ProviderType providerType,
            @Param("providerId") String providerId
    );

    Boolean existsByNickname(String nickname);

}
