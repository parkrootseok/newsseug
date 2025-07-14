package com.a301.newsseug.domain.member.factory.dto;

import static com.a301.newsseug.domain.member.factory.fixtures.MemberFixtures.BIRTH;
import static com.a301.newsseug.domain.member.factory.fixtures.MemberFixtures.GENDER;
import static com.a301.newsseug.domain.member.factory.fixtures.MemberFixtures.NICKNAME;
import static com.a301.newsseug.domain.member.factory.fixtures.MemberFixtures.PROFILE_IMAGE_URL;

import com.a301.newsseug.domain.member.model.dto.request.SignUpRequest;

public class MemberRequestFactory {

    public static SignUpRequest updateMemberRequest() {
        return new SignUpRequest(
                NICKNAME,
                GENDER,
                BIRTH,
                PROFILE_IMAGE_URL
        );
    }

}
