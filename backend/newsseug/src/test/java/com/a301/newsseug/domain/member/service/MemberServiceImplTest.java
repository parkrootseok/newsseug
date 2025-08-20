package com.a301.newsseug.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.factory.entity.MemberFactory;
import com.a301.newsseug.domain.member.factory.dto.MemberRequestFactory;
import com.a301.newsseug.domain.member.model.dto.request.SignUpRequest;
import com.a301.newsseug.domain.member.model.dto.response.GetMemberResponse;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.type.GenderType;
import com.a301.newsseug.domain.member.repository.MemberRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("사용자 관련 기능")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private CustomUserDetails userDetails;
//
//    @InjectMocks
//    private MemberServiceImpl memberService;
//
//    private Member loginMember;
//
//    @BeforeEach
//    void beforeEach() {
//        loginMember = MemberFactory.memberOfKakao(1L);
//        given(userDetails.getMember()).willReturn(loginMember);
//    }
//
//    @Test
//    @DisplayName("정보 조회[성공]")
//    void retrieveMemberDetails() {
//
//        // When
//        GetMemberResponse response = memberService.retrieveMemberDetails(userDetails);
//
//        // Then
//        assertThat(loginMember.getNickname()).isEqualTo(response.nickname());
//        assertThat(loginMember.getProfileImageUrl()).isEqualTo(response.profileImageUrl());
//
//    }
//
//
//    @Test
//    @DisplayName("정보 수정[성공]")
//    void signUp() {
//
//        // Given
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        SignUpRequest request = MemberRequestFactory.updateMemberRequest();
//        given(memberRepository.getOrThrow(loginMember.getOAuth2Details().getProviderId())).willReturn(loginMember);
//
//        // When
//        memberService.signUp(userDetails, request);
//
//        // Then
//        assertThat(loginMember.getNickname()).isEqualTo(request.nickname());
//        assertThat(loginMember.getGender()).isEqualTo(GenderType.from(request.gender()));
//        assertThat(loginMember.getBirth()).isEqualTo(LocalDate.parse(request.birth(), formatter));
//
//    }

}
